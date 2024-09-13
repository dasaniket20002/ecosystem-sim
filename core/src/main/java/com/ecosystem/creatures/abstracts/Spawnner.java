package com.ecosystem.creatures.abstracts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.ecosystem.utils.Consts;
import com.ecosystem.utils.SimplexNoise;

public abstract class Spawnner {
	private static class SpawnPosition {
		int x;
		int y;
		float noiseValue;

		public SpawnPosition(int x, int y, float noiseValue) {
			this.x = x;
			this.y = y;
			this.noiseValue = noiseValue;
		}
	}

	protected List<Vector2> generateSpawnLocations(int numEntities) {
		return generateSpawnLocations(numEntities, 0);
	}

	protected List<Vector2> generateSpawnLocations(int numEntities, long seed) {
		List<SpawnPosition> positions = new ArrayList<>();
		float scaledSeed = (seed / 1000000000000f);
		for (int x = -Consts.WORLD_SIZE_X; x < Consts.WORLD_SIZE_X; x++) {
			for (int y = -Consts.WORLD_SIZE_Y; y < Consts.WORLD_SIZE_Y; y++) {
				float sampleX = x + scaledSeed;
				float sampleY = y + scaledSeed;
				float noiseValue = (float) SimplexNoise.noise(sampleX, sampleY);
				
				noiseValue = (noiseValue + 1) / 2f;
				positions.add(new SpawnPosition(x, y, noiseValue));
			}
		}

		Collections.sort(positions, (p1, p2) -> Float.compare(p2.noiseValue, p1.noiseValue));

		List<Vector2> spawnLocations = new ArrayList<>();
		for (int i = 0; i < numEntities; i++) {
			SpawnPosition selectedLocation = positions.get(i % numEntities);
			spawnLocations.add(new Vector2(selectedLocation.x, selectedLocation.y));
		}

		System.out.println(spawnLocations);
		return spawnLocations;
	}
}
