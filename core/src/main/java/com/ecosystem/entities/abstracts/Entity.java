package com.ecosystem.entities.abstracts;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ecosystem.entities.enums.EntityType;
import com.ecosystem.utils.classes.TextureDictionary;

public abstract class Entity {

	protected Vector2 position;
	protected float rotation;
	protected Vector2 scale;

	protected EntityType entityType;

	protected float entityWidth;
	protected float entityHeight;
	protected Texture texture;

	public Entity(float entityWidth, float entityHeight) {
		this.position = new Vector2();
		this.rotation = 0f;
		this.scale = new Vector2(1f, 1f);

		this.entityWidth = entityWidth;
		this.entityHeight = entityHeight;

		this.setEntityType();

		this.texture = TextureDictionary.getTextureByEntityType(this.entityType);
	}

	public Entity(float entityWidth, float entityHeight, Vector2 position, float rotation, Vector2 scale) {
		this(entityWidth, entityHeight);
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}

	public Vector2 getPosition() {
		return position;
	}

	protected void setPosition(Vector2 position) {
		this.position = position;
	}

	public float getRotation() {
		return rotation;
	}

	protected void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public Vector2 getScale() {
		return scale;
	}

	protected void setScale(Vector2 scale) {
		this.scale = scale;
	}

	public float getEntityWidth() {
		return entityWidth;
	}

	public float getEntityHeight() {
		return entityHeight;
	}

	public Texture getTexture() {
		return texture;
	}

	public abstract void update(float delta);

	public abstract void fixedUpdate(float delta);

	protected abstract void setEntityType();

	public EntityType getEntityType() {
		return entityType;
	}

	public void render(SpriteBatch batch) {
		batch.draw(texture, position.x, position.y, // Bottom-left corner of where the entity is drawn
				entityWidth / 2f, entityHeight / 2f, // Rotation origin (center of the texture)
				entityWidth, entityHeight, // Width and height of the entity
				scale.x, scale.y, // No scaling (1f means original size)
				rotation, // Rotation angle in degrees
				0, 0, // Texture region's X and Y coordinates (for the full texture)
				texture.getWidth(), // Width of the texture
				texture.getHeight(), // Height of the texture
				false, false // No flipping
		);
	}

	@Override
	public String toString() {
		String ret = "Entity : " + this.hashCode() + "\n\tPOSITION : " + position + "\n\tROTATION : " + rotation
				+ "\n\tSCALE : " + scale + "\n";
		return ret;
	}

	public Rectangle getBoundingBox() {
		return new Rectangle(position.x, position.y, entityWidth * scale.x, entityHeight * scale.y);
	}

	public void dispose() {
		texture.dispose();
	}
}
