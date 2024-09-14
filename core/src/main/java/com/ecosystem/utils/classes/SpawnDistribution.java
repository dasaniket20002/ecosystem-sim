package com.ecosystem.utils.classes;

import com.ecosystem.entities.enums.EntityType;

public class SpawnDistribution {
	public EntityType entityType;
	public float spawnProbability;
	public float spawnRadius;
	
	public SpawnDistribution(EntityType entityType, float spawnProbability, float spawnRadius) {
		this.entityType = entityType;
		this.spawnProbability = spawnProbability;
		this.spawnRadius = spawnRadius;
	}
	
	public SpawnDistribution() {
		this(EntityType.NONE, 0f, 0f);
	}
	
	public static SpawnDistribution getSpawnDistributionByEntityType(SpawnDistribution spawnDistributions[], EntityType entityType) {
		for(SpawnDistribution spawnDistribution: spawnDistributions) {
			if(spawnDistribution.entityType == entityType) return spawnDistribution;
		}
		return null;
	}
}