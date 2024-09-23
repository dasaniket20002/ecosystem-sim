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

	private AtomicBoolean running = new AtomicBoolean(true);
	private ExecutorService physicsExecutor;

	@Override
	public void create() {
		batch = new SpriteBatch();
		Consts.getInstance();

		physicsExecutor = Executors.newSingleThreadExecutor();
		physicsExecutor.submit(() -> {
			long previousTime = System.nanoTime();
			float accumulator = 0;

			while (running.get()) {
				long currentTime = System.nanoTime();
				float deltaTime = (currentTime - previousTime) / 1_000_000_000.0f;
				previousTime = currentTime;

				accumulator += deltaTime;

				while (accumulator >= Consts.getInstance().TIME_STEP) {
					physicsUpdate(Consts.getInstance().TIME_STEP);
					accumulator -= Consts.getInstance().TIME_STEP;
				}

				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		});
	}

	@Override
	public void render() {
		TopDownCamera.getInstance().handleInput(Gdx.graphics.getDeltaTime());
		TopDownCamera.getInstance().update();
		
		Logic.getInstance().update(Gdx.graphics.getDeltaTime());
		
		ScreenUtils.clear(0.8f, 0.8f, 0.8f, 1f);
		
		batch.setProjectionMatrix(TopDownCamera.getInstance().combined);
		batch.begin();

		Logic.getInstance().render(batch);

		batch.end();
	}

	public void physicsUpdate(float delta) {
		Logic.getInstance().fixedUpdate(delta);
	}

	@Override
	public void resize(int width, int height) {
		TopDownCamera.getInstance().onResize(width, height);
	}

	@Override
	public void dispose() {
		running.set(false);

		Logic.getInstance().dispose();
		batch.dispose();

		physicsExecutor.shutdown();
	}
}
