package com.ecosystem.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

public class TopDownCamera extends OrthographicCamera {
	
	private float rotationSpeed;
	private float speed;
	private float zoomSpeed;
	
	public TopDownCamera(float viewportWidth, float viewportHeight) {
		super(viewportWidth, viewportHeight);

        position.set(0f, 0f, 10f);
        lookAt(0f, 0f, 0f);
        up.set(0f, 1f, 0f);
        
        update();
		
		this.rotationSpeed = 2f;
		this.speed = 10f;
		this.zoomSpeed = 2f;
	}
	
	public TopDownCamera(float viewportWidth, float viewportHeight, float rotationSpeed) {
		this(viewportWidth, viewportHeight);
		this.rotationSpeed = rotationSpeed;
	}
	
	public TopDownCamera onResize(int width, int height) {
		viewportWidth = width;
		viewportHeight = height;
		update();
		return this;
	}
	
	public void handleInput(float delta) {
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			zoom += zoomSpeed * delta;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			zoom -= zoomSpeed * delta;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			translate(-speed * delta, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			translate(speed * delta, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			translate(0, -speed * delta, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			translate(0, speed * delta, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			rotate(-rotationSpeed * delta, 0, 0, 1);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.E)) {
			rotate(rotationSpeed * delta, 0, 0, 1);
		}

		zoom = MathUtils.clamp(zoom, 0.1f, 1000f);
	}
}
