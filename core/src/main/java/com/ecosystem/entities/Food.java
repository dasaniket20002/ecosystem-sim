package com.ecosystem.entities;

import com.badlogic.gdx.math.Vector2;
import com.ecosystem.entities.abstracts.Entity;
import com.ecosystem.entities.enums.EntityType;

public class Food extends Entity {
	private static final float ENTITY_WIDTH = 8f;
	private static final float ENTITY_HEIGHT = 8f;

	public Food() {
		super(ENTITY_WIDTH, ENTITY_HEIGHT);
	}

	public Food(Vector2 position, float rotation, Vector2 scale) {
		super(ENTITY_WIDTH, ENTITY_HEIGHT, position, rotation, scale);
	}
	
	protected void setEntityType() {
		entityType = EntityType.FOOD;
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void fixedUpdate(float delta) {
	}
}
