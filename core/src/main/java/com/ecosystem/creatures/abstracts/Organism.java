package com.ecosystem.creatures.abstracts;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Organism {
	protected Vector2 position;
	protected Vector2 velocity;
	
	protected Texture texture;
	
	public Organism() {
		position = new Vector2();
		velocity = new Vector2();
		
		initTexture();
	}
	public Organism(Vector2 position, Vector2 velocity) {
		this.position = position;
		this.velocity = velocity;
		
		initTexture();
	}
	
	public abstract void initTexture();
	
	public Vector2 getPosition() {
		return position;
	}

	protected void setPosition(Vector2 position) {
		this.position = position;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	protected void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public Texture getTexture() {
		return texture;
	}
	
	public abstract void move(float delta);
	
	public abstract void render(SpriteBatch batch);
	
	public abstract void dispose();
}
