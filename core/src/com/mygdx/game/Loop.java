package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Loop extends Game {

	OrthographicCamera camera;

	public static final int screenWidth = 1280;
	public static final int screenHeight = 960;
    TextureAtlas uiAtlas;
    AssetManager manager = new AssetManager();

	boolean isFirstTime;

	Rate rate;
	boolean isSound = true;

	Preferences prefs;
	Viewport viewport;

	public Loop(Rate r){
		this.rate = r;
	}

	@Override
	public void create () {

		camera = new OrthographicCamera();
		camera.position.set(screenWidth/2,screenHeight/2,0);
		viewport = new FillViewport(screenWidth,screenHeight,camera);

		prefs = Gdx.app.getPreferences("Preferences");

		isFirstTime = prefs.getBoolean("first",true);

        manager.load("ui.pack",TextureAtlas.class);
		manager.load("background.jpg", Texture.class);
        manager.load("infin.jpg",Texture.class);
		manager.load("smallerWall.png",Texture.class);
		manager.load("ball.png",Texture.class);
		manager.load("hit.mp3", Sound.class);
		manager.load("win.mp3", Sound.class);

		if(isFirstTime){
			manager.load("leftTut.png",Texture.class);
			manager.load("rightTut.png",Texture.class);
		}

        manager.finishLoading();

		uiAtlas = manager.get("ui.pack",TextureAtlas.class);




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
