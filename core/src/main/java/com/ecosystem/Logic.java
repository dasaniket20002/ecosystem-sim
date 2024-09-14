package com.ecosystem;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ecosystem.distribution.spawnner.EntitySpawnner;
import com.ecosystem.entities.abstracts.Entity;

public class Logic {

	private List<Entity> entities;

	public Logic() {
		start();
	}

	public void start() {
		entities = EntitySpawnner.getInstance().getEntities();
	}

	public void update(float delta) {
	}

	public void fixedUpdate(float delta) {
		for (Entity entity : entities) {
			entity.fixedUpdate(delta);
		}
	}

	public void render(SpriteBatch batch) {
		for (Entity entity : entities) {
			entity.render(batch);
		}
	}

	public void dispose() {
		for (Entity entity : entities) {
			entity.dispose();
		}
	}

}
