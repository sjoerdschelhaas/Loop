package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by wietze on 6/22/2017.
 */

public class Hud {

    private Loop game;
    private Stage stage;

    float initAnim = 1;

    OrthographicCamera hudCam;
    Viewport viewport;

    ImageButton exitToMenu;
    ImageButton resume;
    ImageButton replay;

    private boolean isPaused;

    public Hud(Loop g){
        game = g;


        hudCam = new OrthographicCamera(game.screenWidth,game.screenHeight);
        viewport = new FillViewport(game.screenWidth,game.screenHeight,hudCam);

        stage = new Stage(viewport);

        Gdx.input.setInputProcessor(stage);

        isPaused = false;

        ImageButton.ImageButtonStyle   ims = new ImageButton.ImageButtonStyle();
        ims.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("pause.png")));
        ims.pressedOffsetY = -5;
        ims.pressedOffsetX = -5;


        ImageButton imPause = new ImageButton(ims);
        imPause.setPosition(40,g.screenHeight/2+250);


        imPause.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                isPaused = true;
                spawnPauseMenu();
            }
        });

        stage.addActor(imPause);

    }

    public void draw(){
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public boolean isPaused(){
        return isPaused;
    }

    public void spawnPauseMenu(){
        ImageButton.ImageButtonStyle resumeStyle = new ImageButton.ImageButtonStyle();
        resumeStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("resume.png")));
        resumeStyle.pressedOffsetY = -5;
        resumeStyle.pressedOffsetX = -5;

        ImageButton.ImageButtonStyle exitStyle = new ImageButton.ImageButtonStyle();
        exitStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("extToMenu.png")));
        exitStyle.pressedOffsetY = -5;
        exitStyle.pressedOffsetX = -5;

        ImageButton.ImageButtonStyle replayStyle = new ImageButton.ImageButtonStyle();
        replayStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("replay.png")));
        replayStyle.pressedOffsetY = -5;
        replayStyle.pressedOffsetX = -5;




        exitToMenu = new ImageButton(exitStyle);

        exitToMenu.setPosition((game.screenWidth *0.35f) - exitToMenu.getWidth() / 2
                , -100);
        exitToMenu.setOrigin(exitToMenu.getX() + exitToMenu.getWidth() / 2, exitToMenu.getY() - exitToMenu.getHeight() / 2);


        MoveToAction exitMoveTo = new MoveToAction();
        exitMoveTo.setPosition((game.screenWidth *0.35f) - exitToMenu.getWidth() / 2,
                (game.screenHeight / 2) - exitToMenu.getHeight() / 2);
        exitMoveTo.setDuration(initAnim);
        exitMoveTo.setInterpolation(Interpolation.pow5);

        exitToMenu.addAction(exitMoveTo);

        exitToMenu.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                game.setScreen(new MenuScene(game));

            }
        });




        resume = new ImageButton(resumeStyle);

        resume.setPosition((game.screenWidth / 2) - resume.getWidth() / 2
                , -100);
        resume.setOrigin(resume.getX() + resume.getWidth() / 2, resume.getY() - resume.getHeight() / 2);


        MoveToAction resumeMoveTo = new MoveToAction();
        resumeMoveTo.setPosition((game.screenWidth / 2) - resume.getWidth() / 2,
                (game.screenHeight /2) - resume.getHeight() / 2);
        resumeMoveTo.setDuration(initAnim);
        resumeMoveTo.setInterpolation(Interpolation.pow5);

        resume.addAction(resumeMoveTo);

        resume.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                isPaused = false;
                removePauseMenu();
            }
        });

        replay = new ImageButton(replayStyle);

        replay.setPosition((game.screenWidth *0.65f) - replay.getWidth() / 2
                , -100);
        replay.setOrigin(replay.getX() + replay.getWidth() / 2, replay.getY() - replay.getHeight() / 2);


        MoveToAction replayMoveTo = new MoveToAction();
        replayMoveTo.setPosition((game.screenWidth *0.65f) - replay.getWidth() / 2,
                (game.screenHeight /2) - replay.getHeight() / 2);
        replayMoveTo.setDuration(initAnim);
        replayMoveTo.setInterpolation(Interpolation.pow5);

        replay.addAction(replayMoveTo);

        replay.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                // TODO create replay stuff
                game.setScreen(new GameScene(game));
            }
        });


        stage.addActor(exitToMenu);
        stage.addActor(resume);
        stage.addActor(replay);
    }
    public void removePauseMenu(){
        exitToMenu.setVisible(false);
        replay.setVisible(false);
        resume.setVisible(false);

        exitToMenu = null;
        replay = null;
        resume = null;
    }

    public void resize(int width, int height){
        viewport.update(width,height,true);

    }



}