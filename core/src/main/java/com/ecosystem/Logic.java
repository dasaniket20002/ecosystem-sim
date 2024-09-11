package com.ecosystem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ecosystem.creatures.Herbivore;
import com.ecosystem.creatures.abstracts.Organism;

public class Logic {
	
	private Organism organism;	
	
	public Logic() {
		start();
	}
	
	public void start() {
		organism = new Herbivore();
	}
	
	public void update(float delta) {
		
	}
	
	public void fixedUpdate(float delta) {
		organism.move(delta);
	}
	
	public void render(SpriteBatch batch) {
	organism.render(batch);
	}
	
	public void resize(float width, float height) {
		
	}
	
	public void dispose() {
		organism.dispose();
	}
	
}
