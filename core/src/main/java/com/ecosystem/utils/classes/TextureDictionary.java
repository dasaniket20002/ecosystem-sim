package com.ecosystem.utils.classes;

import com.badlogic.gdx.graphics.Texture;
import com.ecosystem.entities.enums.EntityType;
import com.ecosystem.utils.Consts;

public class TextureDictionary {
	public EntityType entityType;
	public Texture texture;
	
	public TextureDictionary(EntityType entityType, Texture texture) {
		this.entityType = entityType;
		this.texture = texture;
	}
	
	public TextureDictionary() {
		this(EntityType.NONE, null);
	}
	
	public static Texture getTextureByEntityType(TextureDictionary textures[], EntityType entityType) {
		for(TextureDictionary texture: textures) {
			if(texture.entityType == entityType) return texture.texture;
		}
		return null;
	}
	
	public static Texture getTextureByEntityType(EntityType entityType) {
		return getTextureByEntityType(Consts.getInstance().getTexturesDictionary(), entityType);
	}
	
}
