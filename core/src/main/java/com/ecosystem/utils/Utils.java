package com.ecosystem.utils;

import com.badlogic.gdx.math.Vector2;
import com.ecosystem.entities.Food;
import com.ecosystem.entities.abstracts.Entity;
import com.ecosystem.entities.enums.EntityType;
import com.ecosystem.organisms.herbivore.Herbivore;

public class Utils {
	public static float invSqrt(float x) {
		float xhalf = 0.5f * x;
		int i = Float.floatToIntBits(x);
		i = 0x5f3759df - (i >> 1);
		x = Float.intBitsToFloat(i);
		x *= (1.5f - xhalf * x * x);
		return x;
	}

	public static double invSqrt(double x) {
		double xhalf = 0.5d * x;
		long i = Double.doubleToLongBits(x);
		i = 0x5fe6ec85e7de30daL - (i >> 1);
		x = Double.longBitsToDouble(i);
		x *= (1.5d - xhalf * x * x);
		return x;
	}

	public static float distSq(float x1, float y1, float x2, float y2) {
		float x2_x1 = x2 - x1;
		float y2_y1 = y2 - y1;
		return (x2_x1 * x2_x1) + (y2_y1 * y2_y1);
	}

	public static float distSq(Vector2 v1, Vector2 v2) {
		return distSq(v1.x, v1.y, v2.x, v2.y);
	}

	public static Entity createEntityOfType(EntityType entityType, Vector2 position, float rotation, Vector2 scale) {
		switch (entityType) {
		case HERVIBORE:
			return new Herbivore(position, rotation, scale);
		case CARNIVORE:
		case FOOD:
			return new Food(position, rotation, scale);
		default:
		}

		return null;
	}

	public static Entity createEntityOfType(EntityType entityType) {
		return createEntityOfType(entityType, new Vector2(), 0f, new Vector2(1f, 1f));
	}
}
