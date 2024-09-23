package com.ecosystem.distribution.entityDistribution;

import java.util.List;
import java.util.Random;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ecosystem.entities.abstracts.Entity;
import com.ecosystem.entities.enums.EntityType;
import com.ecosystem.utils.Consts;
import com.ecosystem.utils.OpenSimplexNoise;
import com.ecosystem.utils.Utils;
import com.ecosystem.utils.classes.SpawnDistribution;

public class EntityDistribution {

	private static final float NOISE_THRESHOLD = 0f;

	private final Quadtree<Entity> entities;
	private final Random random;
	private final OpenSimplexNoise noiseGenerator;
	private final float noiseScale;
	private final int maxNumEntities;
	private final int maxTries;

	public EntityDistribution(long seed, float noiseScale, int maxNumEntities, int maxTries) {
		entities = new Quadtree<Entity>();
		random = new Random(seed);
		noiseGenerator = new OpenSimplexNoise(seed);
		this.noiseScale = noiseScale;
		this.maxNumEntities = maxNumEntities;
		this.maxTries = maxTries;
	}

	public void generateEntities() {
		int candidateAddTries = 0;
		int initalAddTries = 0;

		while (entities.size() < maxNumEntities && initalAddTries < maxTries) {
			if (entities.isEmpty()) {
				Entity initialEntity = generateInitialEntity();
				if (initialEntity == null) {
					initalAddTries++;
				} else {
					initalAddTries = 0;
					entities.insert(initialEntity);
//					System.out.println(initialEntity);
					System.out.println("Added Initial Entity -> " + entities.size());
				}
				continue;
			}

			Entity candidateEntity = generateCandidateEntity();
			if (candidateEntity == null) {
				candidateAddTries++;
			} else {
				candidateAddTries = 0;
				entities.insert(candidateEntity);
//				System.out.println(candidateEntity);
				System.out.println("Added Candidate Entity -> " + entities.size());
			}

			if (candidateAddTries > 1000) {
				Entity initialEntity = generateInitialEntity();
				if (initialEntity == null) {
					initalAddTries++;
				} else {
					initalAddTries = 0;
					entities.insert(initialEntity);
//					System.out.println(initialEntity);
					System.out.println("Added Initial Candidate Entity -> " + entities.size());
				}
			}
		}

		System.out.println("Entities generated: " + entities.size());
	}

	private boolean checkWorldBounds(float pointX, float pointY) {
		return (pointX > -Consts.getInstance().WORLD_SIZE_X && pointX < Consts.getInstance().WORLD_SIZE_X
				&& pointY > -Consts.getInstance().WORLD_SIZE_Y && pointY < Consts.getInstance().WORLD_SIZE_Y);
	}

	private boolean checkNoiseThreshold(float pointX, float pointY) {
		float noiseValue = (float) noiseGenerator.eval(pointX, pointY, 0f);
		return noiseValue > NOISE_THRESHOLD;
	}

	private EntityType chooseEntityType(float samplePointX, float samplePointY) {
		float noiseValue = (float) noiseGenerator.eval(samplePointX * noiseScale, samplePointY * noiseScale, 0f);
		return chooseEntityTypeBasedOnNoise(noiseValue);
	}

	private EntityType chooseEntityTypeBasedOnNoise(float noiseValue) {
		float totalProbability = 0;
		for (SpawnDistribution spawnDistribution : Consts.getInstance().SPAWN_DISTRIBUTIONS) {
			float adjustedProbability = spawnDistribution.spawnProbability * Math.abs(noiseValue);
			totalProbability += adjustedProbability;
		}

		float roll = random.nextFloat(totalProbability);
		float cumulative = 0;
		for (SpawnDistribution spawnDistribution : Consts.getInstance().SPAWN_DISTRIBUTIONS) {
			float adjustedProbability = spawnDistribution.spawnProbability * Math.abs(noiseValue);
			cumulative += adjustedProbability;
			if (roll <= cumulative) {
				return spawnDistribution.entityType;
			}
		}

		return EntityType.NONE;
	}

	private Entity generateInitialEntity() {
		float samplePointX = random.nextFloat(-Consts.getInstance().WORLD_SIZE_X, Consts.getInstance().WORLD_SIZE_X);
		float samplePointY = random.nextFloat(-Consts.getInstance().WORLD_SIZE_Y, Consts.getInstance().WORLD_SIZE_Y);

		if (!checkNoiseThreshold(samplePointX, samplePointY))
			return null;

		EntityType initialEntityType = chooseEntityType(samplePointX, samplePointY);
		if (initialEntityType == EntityType.NONE)
			return null;

		Entity initialEntity = Utils.createEntityOfType(initialEntityType, new Vector2(samplePointX, samplePointY), 0f,
				new Vector2(1f, 1f));
		return initialEntity;
	}

	private Entity generateCandidateEntity() {
		if (entities.isEmpty()) {
			return null;
		}

		Entity originEntity = entities.getRandomEntity();
		
		float angle = (float) (random.nextFloat() * 2 * Math.PI);

		float originEntityRadius = SpawnDistribution
				.getSpawnDistributionByEntityType(originEntity.getEntityType()).spawnRadius;

		float distance = random.nextFloat(originEntityRadius, originEntityRadius * 2f);

		float candidateX = originEntity.getPosition().x + (float) Math.cos(angle) * distance;
		float candidateY = originEntity.getPosition().y + (float) Math.sin(angle) * distance;

		if (!checkWorldBounds(candidateX, candidateY))
			return null;
		if (!checkNoiseThreshold(candidateX, candidateY))
			return null;

		EntityType entityType = chooseEntityType(candidateX, candidateY);
		if (entityType == EntityType.NONE)
			return null;

		Entity entity = Utils.createEntityOfType(entityType, new Vector2(candidateX, candidateY), 0f,
				new Vector2(1f, 1f));
		if (!isValid(entity))
			return null;

		return entity;
	}

	private boolean isValid(Entity candidateEntity) {
		// Define a search area based on the candidate's position and maximum spawn
		// radius
		float candidateEntityRadius = SpawnDistribution
				.getSpawnDistributionByEntityType(candidateEntity.getEntityType()).spawnRadius;
		float searchRadius = candidateEntityRadius * 2; // Example multiplier, adjust as needed
		Rectangle searchArea = new Rectangle(candidateEntity.getPosition().x - searchRadius / 2,
				candidateEntity.getPosition().y - searchRadius / 2, searchRadius, searchRadius);

		// Retrieve nearby entities from the quadtree within the search area
		List<Entity> nearbyEntities = entities.retrieve(searchArea);

		// Check if the candidate entity has valid placement with respect to nearby
		// entities
		for (Entity entity : nearbyEntities) {
			double distSq = Utils.distSq(candidateEntity.getPosition(), entity.getPosition());

			float currentEntityRadius = SpawnDistribution
					.getSpawnDistributionByEntityType(entity.getEntityType()).spawnRadius;
			double minDistance = (candidateEntityRadius + currentEntityRadius) / 2f + MathUtils.E;

			if (distSq < minDistance * minDistance) {
				return false; // The candidate entity is too close to an existing entity
			}
		}

		return true; // The placement is valid
	}

	public synchronized Quadtree<Entity> getEntities() {
		return entities;
	}

	public void dispose() {
		entities.dispose();
	}
}
