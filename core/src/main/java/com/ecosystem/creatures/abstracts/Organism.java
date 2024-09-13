package com.ecosystem.creatures.abstracts;

import com.badlogic.gdx.math.Vector2;

public abstract class Organism extends Entity {
	protected float speed;
	protected Vector2 velocity;
	
	public Organism() {
		super();
		velocity = new Vector2();
		speed = 0f;
	}
	
	public Organism(Vector2 position) {
		super(position);
		this.velocity = new Vector2();
	}

	public Organism(Vector2 position, Vector2 velocity) {
		super(position);
		this.velocity = velocity;
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
