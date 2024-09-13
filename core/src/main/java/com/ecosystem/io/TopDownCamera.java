package com.ecosystem.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class TopDownCamera extends OrthographicCamera {

	private static final float LERP = 0.1f;

	private static final float SPEED = 20f;
	private static final float ROTATION_SPEED = 20f;
	private static final float ZOOM_SPEED = 2f;

	private static final float SPEED_MULTIPLIER = 2f;

	private float rotationSpeed;
	private float speed;
	private float zoomSpeed;

	private Vector2 followPosition;
	private float followZoom;

	public TopDownCamera(float viewportWidth, float viewportHeight) {
		super();

		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;

		this.followPosition = new Vector2(0f, 0f);
		this.followZoom = 1f;

		position.set(followPosition.x, followPosition.y, 10f);
		lookAt(followPosition.x, followPosition.y, 0f);
		up.set(0f, 1f, 0f);

		update();

		this.rotationSpeed = ROTATION_SPEED;
		this.speed = SPEED;
		this.zoomSpeed = ZOOM_SPEED;
	}

	public TopDownCamera onResize(int width, int height) {
		viewportWidth = width;
		viewportHeight = height;
		update();
		return this;
	}

	@Override
	public void update() {
		position.x = MathUtils.lerp(position.x, followPosition.x, LERP);
		position.y = MathUtils.lerp(position.y, followPosition.y, LERP);

		zoom = MathUtils.lerp(zoom, followZoom, LERP);

		super.update();
	}

	public void handleInput(float delta) {
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			followZoom += zoomSpeed * delta;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.E)) {
			followZoom -= zoomSpeed * delta;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			followPosition.x += speed * delta;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			followPosition.x -= speed * delta;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			followPosition.y -= speed * delta;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			followPosition.y += speed * delta;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.R)) {
			rotate(-rotationSpeed * delta, 0, 0, 1);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.T)) {
			rotate(rotationSpeed * delta, 0, 0, 1);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			this.speed = SPEED * SPEED_MULTIPLIER;
			this.rotationSpeed = ROTATION_SPEED * SPEED_MULTIPLIER;
			this.zoomSpeed = ZOOM_SPEED * SPEED_MULTIPLIER;
		}
		if (!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			this.rotationSpeed = ROTATION_SPEED;
			this.speed = SPEED;
			this.zoomSpeed = ZOOM_SPEED;
		}

		zoom = MathUtils.clamp(zoom, 0.1f, 1000f);
	}
}
