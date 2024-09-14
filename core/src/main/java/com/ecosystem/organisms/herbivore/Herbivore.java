package com.ecosystem.organisms.herbivore;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.ecosystem.entities.enums.EntityType;
import com.ecosystem.organisms.abstracts.Organism;

public class Herbivore extends Organism {

	private static final float TEXTURE_WIDTH = 16f;
	private static final float TEXTURE_HEIGHT = 16f;
	private static final Texture TEXTURE = new Texture("Digiton_H.png");

	public Herbivore() {
		super(TEXTURE_WIDTH, TEXTURE_HEIGHT, TEXTURE);
	}

	public Herbivore(Vector2 position, float rotation) {
		super(TEXTURE_WIDTH, TEXTURE_HEIGHT, TEXTURE, position, rotation);
	}

	public Herbivore(Vector2 position, float rotation, Vector2 velocity, float speed) {
		super(TEXTURE_WIDTH, TEXTURE_HEIGHT, TEXTURE, position, rotation, velocity, speed);
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
