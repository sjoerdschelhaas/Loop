package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Loop extends Game {

	OrthographicCamera camera;
	public static final int screenWidth = 1280;
	public static final int screenHeight = 960;

	Viewport viewport;

	@Override
	public void create () {

		camera = new OrthographicCamera();
		camera.position.set(screenWidth/2,screenHeight/2,0);
		viewport = new FillViewport(screenWidth,screenHeight,camera);

		setScreen(new MenuScene(this));
	}

	@Override
	public void render () {
	super.render();

	}

	public void resize(int width, int height) {
		viewport.update(width, height);
	}


}
