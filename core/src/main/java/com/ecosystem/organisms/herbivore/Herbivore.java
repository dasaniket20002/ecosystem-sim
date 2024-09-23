package com.ecosystem.organisms.herbivore;

import com.badlogic.gdx.math.Vector2;
import com.ecosystem.entities.enums.EntityType;
import com.ecosystem.organisms.abstracts.Organism;

public class Herbivore extends Organism {

	private static final float ENTITY_WIDTH = 12f;
	private static final float ENTITY_HEIGHT = 12f;

	public Herbivore() {
		super(ENTITY_WIDTH, ENTITY_HEIGHT);
	}

	public Herbivore(Vector2 position, float rotation, Vector2 scale) {
		super(ENTITY_WIDTH, ENTITY_HEIGHT, position, rotation, scale);
	}

	public Herbivore(Vector2 position, float rotation, Vector2 scale, Vector2 velocity, float speed) {
		super(ENTITY_WIDTH, ENTITY_HEIGHT, position, rotation, scale, velocity, speed);
	}
	
	protected void setEntityType() {
		entityType = EntityType.HERVIBORE;
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void fixedUpdate(float delta) {
		velocity.nor();
		position = position.add(velocity.scl(delta * speed));
	}

}
