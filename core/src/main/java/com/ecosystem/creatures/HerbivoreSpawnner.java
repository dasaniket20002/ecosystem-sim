package com.ecosystem.creatures;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.ecosystem.creatures.abstracts.Spawnner;

public class HerbivoreSpawnner extends Spawnner {
	private static final int DEFAULT_NUM_ENTITIES = 50;

	public List<Herbivore> spawnEntities() {
		return this.spawnEntities(DEFAULT_NUM_ENTITIES);
	}

	public List<Herbivore> spawnEntities(int numEntities) {
		List<Vector2> spawnLocations = generateSpawnLocations(numEntities, System.currentTimeMillis());
		List<Herbivore> herbivores = new ArrayList<>();
		
		for(Vector2 spawnLocation: spawnLocations) {
			Herbivore herbivore = new Herbivore(spawnLocation);
			herbivores.add(herbivore);
		}
		
		return herbivores;
	}
}
