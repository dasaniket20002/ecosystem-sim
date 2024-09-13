package com.ecosystem;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ecosystem.creatures.Herbivore;
import com.ecosystem.creatures.HerbivoreSpawnner;
import com.ecosystem.creatures.abstracts.Organism;

public class Logic {

	private List<Organism> organisms;

	public Logic() {
		start();
	}

	public void start() {
		HerbivoreSpawnner herbivoreSpawnner = new HerbivoreSpawnner();
		List<Herbivore> herbivores = herbivoreSpawnner.spawnEntities();
		
		organisms = new ArrayList<>();
		organisms.addAll(herbivores);
	}

	public void update(float delta) {
	}

	public void fixedUpdate(float delta) {
		for (Organism organism : organisms) {
			organism.fixedUpdate(delta);
		}
	}

	public void render(SpriteBatch batch) {
		for (Organism organism : organisms) {
			organism.render(batch);
		}
	}

	public void dispose() {
		for (Organism organism : organisms) {
			organism.dispose();
		}
	}

}
