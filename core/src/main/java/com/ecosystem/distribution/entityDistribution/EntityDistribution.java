package com.ecosystem.distribution.entityDistribution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.ecosystem.entities.abstracts.Entity;
import com.ecosystem.entities.enums.EntityType;
import com.ecosystem.utils.Consts;
import com.ecosystem.utils.OpenSimplexNoise;
import com.ecosystem.utils.Utils;
import com.ecosystem.utils.classes.SpawnDistribution;

public class EntityDistribution {
	private final List<Entity> entities;
	private final Random rand;
	private final OpenSimplexNoise noiseGenerator;
	private final float noiseScale;

	public EntityDistribution(long seed, float noiseScale) {
		this.entities = new ArrayList<>();
		this.rand = new Random(seed);
		this.noiseGenerator = new OpenSimplexNoise(seed);
		this.noiseScale = noiseScale;
	}

	public void generateDistribution(int numEntities, int tries) {
		Entity initialEntity = generateInitialEntity();
//		System.out.println("Initial " + initialEntity.getEntityType() + " placed at (" + initialEntity.getPosition().x
//				+ ", " + initialEntity.getPosition().y + ").");
		entities.add(initialEntity);

		int tryCount = 0;
		while (entities.size() < numEntities && tryCount < tries) {
			EntityType candidateEntityType = chooseEntityType();
			Entity candidateEntity = generateCandidateEntity(candidateEntityType);

			if (isValid(candidateEntity)) {
//				System.out.println("Another " + candidateEntity.getEntityType() + " placed at ("
//						+ candidateEntity.getPosition().x + ", " + candidateEntity.getPosition().y + ").");
				entities.add(candidateEntity);
				tryCount = 0;
			} else {
				tryCount++;
			}
		}

		System.out.println("Entities Generated: " + entities.size());
	}

	private Entity generateInitialEntity() {
		EntityType initialEntityType = chooseEntityType();

		float initialX = rand.nextFloat(-Consts.WORLD_SIZE_X, Consts.WORLD_SIZE_X);
		float initialY = rand.nextFloat(-Consts.WORLD_SIZE_Y, Consts.WORLD_SIZE_Y);

		Entity initialEntity = Utils.createEntityOfType(initialEntityType, new Vector2(initialX, initialY), 0f);

		return initialEntity;
	}

	private EntityType chooseEntityType() {
		float x = rand.nextFloat() * Consts.WORLD_SIZE_X;
		float y = rand.nextFloat() * Consts.WORLD_SIZE_Y;
		float noiseValue = getNoiseValue(x, y);
		return chooseEntityBasedOnNoise(noiseValue);
	}

	private EntityType chooseEntityBasedOnNoise(float noiseValue) {
		float totalProbability = 0;
		for (SpawnDistribution spawnDistribution : Consts.SPAWN_DISTRIBUTIONS) {
			float adjustedProbability = spawnDistribution.spawnProbability * noiseValue;
			totalProbability += adjustedProbability;
		}

		float roll = rand.nextFloat() * totalProbability;
		float cumulative = 0;
		for (SpawnDistribution spawnDistribution : Consts.SPAWN_DISTRIBUTIONS) {
			float adjustedProbability = spawnDistribution.spawnProbability * noiseValue;
			cumulative += adjustedProbability;
			if (roll <= cumulative) {
				return spawnDistribution.entityType;
			}
		}

		return EntityType.NONE;
	}

	private float getNoiseValue(float x, float y) {
		float noiseValue = (float) noiseGenerator.eval(x * noiseScale, y * noiseScale, 0);
		return (noiseValue + 1f) / 2f;
	}

	private Entity generateCandidateEntity(EntityType entityType) {
		Entity originEntity = entities.get(rand.nextInt(entities.size()));
		float angle = (float) (rand.nextFloat() * 2 * Math.PI);

		float entityRadius = SpawnDistribution.getSpawnDistributionByEntityType(Consts.SPAWN_DISTRIBUTIONS,
				entityType).spawnRadius;
		float originEntityRadius = SpawnDistribution.getSpawnDistributionByEntityType(Consts.SPAWN_DISTRIBUTIONS,
				originEntity.getEntityType()).spawnRadius;

		float distance = rand.nextFloat() * (entityRadius + originEntityRadius);

		float candidateX = originEntity.getPosition().x + (float) Math.cos(angle) * distance;
		float candidateY = originEntity.getPosition().y + (float) Math.sin(angle) * distance;

		// Ensure candidate is within world bounds
//		candidateX = Math.max(-Consts.WORLD_SIZE_X - entityRadius,
//				Math.min(candidateX, Consts.WORLD_SIZE_X - entityRadius));
//		candidateY = Math.max(-Consts.WORLD_SIZE_Y - entityRadius,
//				Math.min(candidateY, Consts.WORLD_SIZE_Y - entityRadius));

		return Utils.createEntityOfType(entityType, new Vector2(candidateX, candidateY), 0f);
	}

	private boolean isValid(Entity candidateEntity) {
		for (Entity entity : entities) {
			double distSq = Utils.distSq(candidateEntity.getPosition(), entity.getPosition());

			float candidateEntityRadius = SpawnDistribution.getSpawnDistributionByEntityType(Consts.SPAWN_DISTRIBUTIONS,
					candidateEntity.getEntityType()).spawnRadius;
			float currentEntityRadius = SpawnDistribution.getSpawnDistributionByEntityType(Consts.SPAWN_DISTRIBUTIONS,
					entity.getEntityType()).spawnRadius;

			double minDistance = (candidateEntityRadius + currentEntityRadius) / 2f + MathUtils.E;
			if (distSq < minDistance * minDistance) {
				return false;
			}
		}
		return true;
	}

	public List<Entity> getEntities() {
		return entities;
	}
}
