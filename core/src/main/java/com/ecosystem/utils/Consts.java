package com.ecosystem.utils;

import com.badlogic.gdx.graphics.Texture;
import com.ecosystem.entities.enums.EntityType;
import com.ecosystem.utils.classes.SpawnDistribution;
import com.ecosystem.utils.classes.TextureDictionary;

public class Consts {
	private static Consts _instance = null;
	public static Consts getInstance() {
		if(_instance == null) _instance = new Consts();
		return _instance;
	}
	
	public final int TICK_RATE = 30;
	public final float TIME_STEP = 1f / TICK_RATE;
	public final int WORLD_SIZE_X = 1000;
	public final int WORLD_SIZE_Y = 1000;

	public final SpawnDistribution[] SPAWN_DISTRIBUTIONS = {
			new SpawnDistribution(EntityType.HERVIBORE, 0.4f, 12f), 
			new SpawnDistribution(EntityType.FOOD, 0.7f, 10f) 
	};
	
	protected TextureDictionary[] TEXTURES;
	
	protected Consts() {
		initializeTextures();
		System.out.println("Initialized Textures");
	}
	
	protected void initializeTextures() {
		TEXTURES = new TextureDictionary[] {
				new TextureDictionary(EntityType.HERVIBORE, new Texture("Digiton_H.png")),
				new TextureDictionary(EntityType.FOOD, new Texture("Food.png")),
		};
	}
	
	public TextureDictionary[] getTexturesDictionary() {
		return TEXTURES;
	}
}
