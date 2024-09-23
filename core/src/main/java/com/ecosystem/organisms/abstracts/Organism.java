package com.ecosystem.organisms.abstracts;

import com.badlogic.gdx.math.Vector2;
import com.ecosystem.entities.abstracts.Entity;

public abstract class Organism extends Entity {
	protected float speed;
	protected Vector2 velocity;

	public Organism(float entityWidth, float entityHeight) {
		super(entityWidth, entityHeight);
		velocity = new Vector2();
		speed = 0f;
	}

	public Organism(float entityWidth, float entityHeight, Vector2 position,
			float rotation, Vector2 scale) {
		super(entityWidth, entityHeight, position, rotation, scale);
		this.velocity = new Vector2();
		this.speed = 0f;
	}

	public Organism(float entityWidth, float entityHeight, Vector2 position,
			float rotation, Vector2 scale, Vector2 velocity, float speed) {
		super(entityWidth, entityHeight, position, rotation, scale);
		this.velocity = velocity;
		this.speed = speed;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	protected void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public float getSpeed() {
		return speed;
	}

	protected void setSpeed(float speed) {
		this.speed = speed;
	}
}
