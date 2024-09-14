package com.ecosystem.entities.abstracts;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ecosystem.entities.enums.EntityType;


public abstract class Entity {
	protected Vector2 position;
	protected float rotation;
	
	protected EntityType entityType;

	protected Texture texture;
	protected float entityWidth;
	protected float entityHeight;

	public Entity(float entityWidth, float entityHeight, Texture texture) {
		this.position = new Vector2();
		this.rotation = 0f;

		this.entityWidth = entityWidth;
		this.entityHeight = entityHeight;
		this.texture = texture;
		
		this.setEntityType();
	}

	public Entity(float entityWidth, float entityHeight, Texture texture, Vector2 position, float rotation) {
		this(entityWidth, entityHeight, texture);
		this.position = position;
		this.rotation = rotation;
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
	
	public float getEntityWidth() {
		return entityWidth;
	}
	
	public float getEntityHeight() {
		return entityHeight;
	}

	public abstract void update(float delta);

	public abstract void fixedUpdate(float delta);
	
	protected abstract void setEntityType();
	
	public EntityType getEntityType() {
		return entityType;
	}

	public void render(SpriteBatch batch) {
		batch.draw(texture, 
				position.x, position.y, 					// Bottom-left corner of where the entity is drawn
				entityWidth / 2f, entityHeight / 2f, 		// Rotation origin (center of the texture)
				entityWidth, entityHeight, 					// Width and height of the entity
				1f, 1f, 									// No scaling (1f means original size)
				rotation, 									// Rotation angle in degrees
				0, 0, 										// Texture region's X and Y coordinates (for the full texture)
				texture.getWidth(), 						// Width of the texture
				texture.getHeight(), 						// Height of the texture
				false, false 								// No flipping
		);
	}

	public void dispose() {
		texture.dispose();
	}
}
