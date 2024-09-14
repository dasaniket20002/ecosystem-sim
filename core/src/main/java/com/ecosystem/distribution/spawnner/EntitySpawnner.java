package com.ecosystem.distribution.spawnner;

import com.ecosystem.distribution.entityDistribution.EntityDistribution;

public class EntitySpawnner {
	private static EntityDistribution entityDistribution;

	public static EntityDistribution getInstance() {
		if (entityDistribution == null)
			initDistribution();
		return entityDistribution;
	}

	private static void initDistribution() {
		entityDistribution = new EntityDistribution(System.currentTimeMillis(), 0.1f);
		entityDistribution.generateDistribution(10000, 1000);
	}
}
