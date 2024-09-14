package com.ecosystem.utils;

import com.ecosystem.entities.enums.EntityType;
import com.ecosystem.utils.classes.SpawnDistribution;

public final class Consts {
	public static final int TICK_RATE = 30; // Number of updates per second
	public static final float TIME_STEP = 1f / TICK_RATE; // Seconds per tick
	public static final int WORLD_SIZE_X = 100;
	public static final int WORLD_SIZE_Y = 100;

	public static final SpawnDistribution[] SPAWN_DISTRIBUTIONS = { new SpawnDistribution(EntityType.HERVIBORE, 0.7f, 12f) };
}
