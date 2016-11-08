package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SizeToAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Main extends ApplicationAdapter {


	public enum GameState {
		Play, Paused,menu
	}



	SpriteBatch batch;
	Sprite background;
	Sprite logo;
	public static final int screenWidth = 1280;
	public static final int screenHeight = 960;

	FPSLogger fpsLogger;
	OrthographicCamera camera;
	Viewport viewport;


	Stage stage;



	@Override
	public void create () {
		fpsLogger = new FPSLogger();
		camera = new OrthographicCamera(screenWidth,screenHeight);
		camera.position.set(screenWidth/2,screenHeight/2,0);
		viewport = new FillViewport(screenWidth,screenHeight,camera);

		batch = new SpriteBatch();
		background = new Sprite(new Texture("background.jpg"));

		stage = new Stage();
		stage.setViewport(viewport);

		Gdx.input.setInputProcessor(stage);


		logo = new Sprite(new Texture("menulogo.png"));
		logo.setSize(logo.getWidth()*0.8f,logo.getHeight()*0.8f);
		logo.setPosition(screenWidth *0.5f - (logo.getWidth() /2), screenHeight*0.7f -(logo.getWidth() /2));



		ImageButton.ImageButtonStyle ims = new ImageButton.ImageButtonStyle();
		ims.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("playButton.png")));
		ims.pressedOffsetY=-5;
		ims.pressedOffsetX=-5;


		ImageButton imPlay = new ImageButton(ims);
		imPlay.setPosition((screenWidth / 2) - imPlay.getWidth() / 2
				, 0);


		MoveToAction moveTo = new MoveToAction();
		moveTo.setPosition((screenWidth / 2) - imPlay.getWidth() / 2,
				(screenHeight *0.43f) - imPlay.getHeight() / 2);
		moveTo.setDuration(1.5f);
		moveTo.setInterpolation(Interpolation.pow5);

		imPlay.addAction(moveTo);

		imPlay.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				System.out.println("not yet implemented");
				System.out.println("this is a git test");
			}
		});

		stage.addActor(imPlay);


	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		background.draw(batch);
		logo.draw(batch);
		batch.end();

		stage.draw();


	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public void resize(int width, int height) {
		viewport.update(width, height);
	}
}
