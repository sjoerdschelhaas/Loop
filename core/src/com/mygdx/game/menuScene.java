package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by wietze on 11/8/2016.
 */

public class MenuScene extends BaseScene {


    float textWidthHighScore;
    float textWidthScore;

    SpriteBatch batch;
    Sprite background;
    Image logo;



    BitmapFont fontHighScore;
    BitmapFont fontScore;
    GlyphLayout layout;

    float initAnim = 1.5f;
    float counter = 0;

    Stage stage;
    Main g;

    boolean canAnim = true;

    public MenuScene(Main game) {
        super(game);

        g = game;

    }

    @Override
    public void show() {
        super.show();
        batch = new SpriteBatch();


        background = new Sprite(new Texture("background.jpg"));

        stage = new Stage();
        stage.setViewport(g.viewport);


        Gdx.input.setInputProcessor(stage);



        fontHighScore = new BitmapFont(Gdx.files.internal("scoreFont.fnt"));
        fontHighScore.setColor(Color.BLACK); // TODO change color
        layout = new GlyphLayout(fontHighScore, "Highscore");
        textWidthHighScore = layout.width;
        Color c = fontHighScore.getColor();
        fontHighScore.setColor(c.r, c.g, c.b, 0);


        fontScore = new BitmapFont(Gdx.files.internal("scoreFont2.fnt"));
        fontScore.setColor(Color.DARK_GRAY); // TODO change color
        layout = new GlyphLayout(fontScore, "99");
        textWidthScore = layout.width;
        Color c2 = fontScore.getColor();
        fontScore.setColor(c2.r, c2.g, c2.b, 0);

        Texture t = new Texture("menulogo.png");
        logo = new Image();
        logo.setDrawable(new TextureRegionDrawable(new TextureRegion(t)));
        logo.setSize(t.getWidth() * 0.75f, t.getHeight() * 0.75f);
        logo.setPosition(g.screenWidth * 0.5f - (logo.getWidth() / 2), 0 - logo.getHeight());


        MoveToAction moveToLogo = new MoveToAction();
        moveToLogo.setPosition(g.screenWidth * 0.5f - (logo.getWidth() / 2), g.screenHeight * 0.73f - (logo.getWidth() / 2));
        moveToLogo.setDuration(initAnim);
        moveToLogo.setInterpolation(Interpolation.pow5);
        logo.addAction(moveToLogo);


        ImageButton.ImageButtonStyle ims = new ImageButton.ImageButtonStyle();
        ims.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("playButton.png")));
        ims.pressedOffsetY = -5;
        ims.pressedOffsetX = -5;


        ImageButton imPlay = new ImageButton(ims);
        imPlay.setPosition((g.screenWidth / 2) - imPlay.getWidth() / 2
                , 0);
        imPlay.setOrigin(imPlay.getX() + imPlay.getWidth() / 2, imPlay.getY() - imPlay.getHeight() / 2);


        MoveToAction moveTo = new MoveToAction();
        moveTo.setPosition((g.screenWidth / 2) - imPlay.getWidth() / 2,
                (g.screenHeight * 0.35f) - imPlay.getHeight() / 2);
        moveTo.setDuration(initAnim);
        moveTo.setInterpolation(Interpolation.pow5);

        imPlay.addAction(moveTo);

        imPlay.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

            }
        });

        ImageButton.ImageButtonStyle imsExit = new ImageButton.ImageButtonStyle();
        imsExit.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("exit.png")));
        imsExit.pressedOffsetY = -5;
        imsExit.pressedOffsetX = -5;

        ImageButton imExit = new ImageButton(imsExit);
        imExit.setPosition((g.screenWidth * 0.35f) - imExit.getWidth() / 2
                , 0);
        MoveToAction moveToExit = new MoveToAction();
        moveToExit.setPosition((g.screenWidth * 0.35f) - imExit.getWidth() / 2,
                (g.screenHeight * 0.20f) - imExit.getHeight() / 2);
        moveToExit.setDuration(initAnim);
        moveToExit.setInterpolation(Interpolation.pow5);

        imExit.addAction(moveToExit);

        imExit.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                System.out.println("not yet implemented");

            }
        });


        ImageButton.ImageButtonStyle imsStar = new ImageButton.ImageButtonStyle();
        imsStar.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("star.png")));
        imsStar.pressedOffsetY = -5;
        imsStar.pressedOffsetX = -5;

        ImageButton imStar = new ImageButton(imsStar);
        imStar.setPosition((g.screenWidth * 0.5f) - imStar.getWidth() / 2
                , 0);
        MoveToAction moveToStar = new MoveToAction();
        moveToStar.setPosition((g.screenWidth * 0.5f) - imStar.getWidth() / 2,
                (g.screenHeight * 0.2f) - imStar.getHeight() / 2);
        moveToStar.setDuration(initAnim);
        moveToStar.setInterpolation(Interpolation.pow5);

        imStar.addAction(moveToStar);

        imStar.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                System.out.println("not yet implemented");

            }
        });


        ImageButton.ImageButtonStyle imsSettings = new ImageButton.ImageButtonStyle();
        imsSettings.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("settings.png")));
        imsSettings.pressedOffsetY = -5;
        imsSettings.pressedOffsetX = -5;

        ImageButton imSettings = new ImageButton(imsSettings);
        imSettings.setPosition((g.screenWidth * 0.65f) - imSettings.getWidth() / 2
                , 0);
        MoveToAction moveToSettings = new MoveToAction();
        moveToSettings.setPosition((g.screenWidth * 0.65f) - imSettings.getWidth() / 2,
                (g.screenHeight * 0.2f) - imSettings.getHeight() / 2);
        moveToSettings.setDuration(initAnim);
        moveToSettings.setInterpolation(Interpolation.pow5);

        imSettings.addAction(moveToSettings);

        imSettings.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                System.out.println("not yet implemented");

            }
        });

        stage.addActor(imPlay);
        stage.addActor(imExit);
        stage.addActor(imStar);
        stage.addActor(imSettings);
        stage.addActor(logo);
    }

    @Override
    public void resume() {
        super.resume();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        batch.setProjectionMatrix(g.camera.combined);

        batch.begin();
        background.draw(batch);
        //logo.draw(batch);
        fontHighScore.draw(batch, "HighScore", g.screenWidth / 2 - (textWidthHighScore / 2), g.screenHeight * 0.58f);
        fontScore.draw(batch, "99", g.screenWidth / 2 - (textWidthScore / 2), g.screenHeight * 0.52f);

        batch.end();

        stage.draw();

        update(delta);

    }

    public boolean addTransText(BitmapFont f, float alpha) {
        Color c = f.getColor();
        if (f.getColor().a > 0.99) {
            f.setColor(c.r, c.g, c.b, 1);
            return false;
        }
        f.setColor(c.r, c.g, c.b, c.a + alpha);
        return true;

    }

    public void update(float delta) {
        if (canAnim) {
            counter += delta;
            if (counter > 1.5) {
                canAnim = addTransText(fontHighScore, 0.009f);
                canAnim = addTransText(fontScore, 0.009f);
            }
        }
    }
    @Override
    protected void handleBackPress() {
        super.handleBackPress();
        System.out.println("Not implemented yet");
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

    }
}





