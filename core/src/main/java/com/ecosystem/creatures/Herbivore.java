package com.ecosystem.creatures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ecosystem.creatures.abstracts.Organism;

public class Herbivore extends Organism {
	
	public Herbivore() {
		super();
	}
	
	public Herbivore(Vector2 position, Vector2 velocity) {
		super(position, velocity);
	}

	@Override
	public void initTexture() {
		texture = new Texture("bacteria.png");
	}
	
	@Override
	public void move(float delta) {
		velocity.nor();
		position = position.add(velocity.scl(delta));
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(texture, position.x, position.y, 16, 24.8f);
	}
	
	@Override
	public void dispose() {
		texture.dispose();
	}

}
