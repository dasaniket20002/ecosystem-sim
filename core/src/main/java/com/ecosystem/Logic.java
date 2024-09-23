package com.ecosystem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ecosystem.distribution.entityDistribution.EntityDistribution;
import com.ecosystem.entities.abstracts.Entity;
import com.ecosystem.threads.EntityGeneratorThread;

public class Logic {

	private static Logic _instance = null;

	public static Logic getInstance() {
		if (_instance == null)
			_instance = new Logic();
		return _instance;
	}

	private EntityDistribution distributionGenerator;
	EntityGeneratorThread entityGeneratorThread;

	protected Logic() {
		start();
	}

	public void start() {
		distributionGenerator = new EntityDistribution(System.currentTimeMillis(), 0.1f, 10000, 30);
		entityGeneratorThread = new EntityGeneratorThread(distributionGenerator);
		entityGeneratorThread.start();
	}

	public void update(float delta) {
		distributionGenerator.getEntities().update(delta);
	}

	public void fixedUpdate(float delta) {

	}

	public void render(SpriteBatch batch) {
		for (Entity entity : distributionGenerator.getEntities().retrieveAll()) {
			entity.render(batch);
		}
	}

	public void dispose() {
		try {
			entityGeneratorThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		distributionGenerator.dispose();
	}

}
