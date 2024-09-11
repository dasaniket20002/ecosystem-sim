package com.ecosystem.utils;

public final class Consts {
	public static final int TICK_RATE = 30; //Number of updates per second
	public static final float TIME_STEP = 1f / TICK_RATE; //Seconds per tick
	public static final float MAX_FRAME_SKIP = ((float) TICK_RATE) * 0.2f; 	//If fps drops below 20% of tickrate, slow down game to
			                                                            	//avoid spiral of death
}
