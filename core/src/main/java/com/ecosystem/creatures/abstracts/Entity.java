package com.ecosystem.creatures.abstracts;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
	protected Vector2 position;

	protected Texture texture;

	public Entity() {
		position = new Vector2();
		
		initTexture();
	}

	public Entity(Vector2 position) {
		this.position = position;

		initTexture();
	}

	public Vector2 getPosition() {
		return position;
	}

	protected void setPosition(Vector2 position) {
		this.position = position;
	}

	public Texture getTexture() {
		return texture;
	}

	public abstract void initTexture();
	
	public abstract void update(float delta);
	
	public abstract void fixedUpdate(float delta);

	public abstract void render(SpriteBatch batch);

	public abstract void dispose();
}
