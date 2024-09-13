package com.ecosystem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.ecosystem.io.TopDownCamera;
import com.ecosystem.utils.Consts;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends ApplicationAdapter {
	private SpriteBatch batch;
	private Logic logic;
	private TopDownCamera camera;

	private AtomicBoolean running = new AtomicBoolean(true);
	private ExecutorService physicsExecutor;

	@Override
	public void create() {
		batch = new SpriteBatch();
		logic = new Logic();
		camera = new TopDownCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		physicsExecutor = Executors.newSingleThreadExecutor();
		physicsExecutor.submit(() -> {
			long previousTime = System.nanoTime();
			float accumulator = 0;

			while (running.get()) {
				long currentTime = System.nanoTime();
				float deltaTime = (currentTime - previousTime) / 1_000_000_000.0f;
				previousTime = currentTime;

				accumulator += deltaTime;

				while (accumulator >= Consts.TIME_STEP) {
					physicsUpdate(Consts.TIME_STEP);
					accumulator -= Consts.TIME_STEP;
				}

				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		});
	}

	public void setCamera(TopDownCamera camera) {
		this.camera = (TopDownCamera) camera;
	}

	@Override
	public void render() {
		camera.handleInput(Gdx.graphics.getDeltaTime());
		logic.update(Gdx.graphics.getDeltaTime());
		update();
	}

	public void physicsUpdate(float delta) {
		logic.fixedUpdate(delta);
	}

	public void update() {
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		ScreenUtils.clear(0.305f, 0.701f, 0f, 1f);

		batch.begin();

		logic.render(batch);

		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		camera.onResize(width, height);
	}

	@Override
	public void dispose() {
		running.set(false);

		batch.dispose();
		logic.dispose();

		physicsExecutor.shutdown();
	}
}
