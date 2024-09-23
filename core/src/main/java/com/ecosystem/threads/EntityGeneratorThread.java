package com.ecosystem.threads;

import com.ecosystem.distribution.entityDistribution.EntityDistribution;

public class EntityGeneratorThread extends Thread {

	private EntityDistribution distributionGenerator;

	public EntityGeneratorThread(EntityDistribution distributionGenerator) {
		this.distributionGenerator = distributionGenerator;	
	}

	@Override
	public void run() {
		distributionGenerator.generateEntities();
	}
}
