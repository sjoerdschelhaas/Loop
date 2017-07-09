package com.mygdx.game;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by wietze on 11/9/2016.
 */

public class GameScene extends BaseScene {


    SpriteBatch batch;
    Sprite background;
    Vector3 touchPos;
    CatmullRomSpline<Vector2> myCatmull;

    boolean canDrawGameOverScore = false;
    boolean canPlayWinEffect = true;
    float winEffectCounter = 0;
    float score = 0;

    private ParticleEffect effect;

    float maxHoleSize = 250;
    float minHoleSize = 120;

    float currentWallSpeed = 4;
    // if the ball is moving left
    boolean left = true;

    Ball ball;
    ArrayList<WallSet> walls;

    Loop game;

    GameState gameState;

    float maxWallSpeed = 13;

    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    BitmapFont fontScore;
    GlyphLayout layout;
    float textWidthScore;

    private Hud hud;
    Sound win = Gdx.audio.newSound(Gdx.files.internal("win.mp3"));
    Sound hit = Gdx.audio.newSound(Gdx.files.internal("hit.mp3"));

    FPSLogger fps = new FPSLogger();

    Tutorial tut;


    public enum GameState {
        PLAYING,
        PAUSE,
        GAMEOVER,
        TUTORIAL
    }


    public GameScene(Loop g) {
        super(g);
        game = g;


        hud = new Hud(game,this);

        setup();
    }

    @Override
    public void show() {
        super.show();
        batch = new SpriteBatch();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        touchPos = new Vector3();

        background = new Sprite(game.manager.get("infin.jpg",Texture.class));

        walls = new ArrayList<WallSet>();

        effect = new ParticleEffect();


        generator = new FreeTypeFontGenerator(Gdx.files.internal("DroidSerif-Bold.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 48;
        parameter.color = Color.BLACK;
        fontScore = generator.generateFont(parameter);
        layout = new GlyphLayout(fontScore, "Score 0");
        textWidthScore = layout.width * 0.5f;
        hud.setInputProcessor();

        if(game.isFirstTime){
            gameState = GameState.TUTORIAL;
            hud.imPause.setVisible(false);
        }else{
            gameState = GameState.PLAYING;
            ball = new Ball(myCatmull,game);
            spawnSetOfWalls(ball);

        }
    }

    public void setup() {
        int k = 500; //increase k for more fidelity to the spline
        Vector2[] points = new Vector2[k];
        Vector2 cp[] = new Vector2[]{
                new Vector2(144, 480), new Vector2(145, 450), new Vector2(150, 419), new Vector2(157, 381), new Vector2(177, 325), new Vector2(197, 291), new Vector2(212, 272),
                new Vector2(237, 248), new Vector2(285, 218), new Vector2(351, 210), new Vector2(392, 219), new Vector2(443, 248), new Vector2(499, 300), new Vector2(547, 355),
                new Vector2(579, 395), new Vector2(625, 462), new Vector2(677, 536), new Vector2(732, 607), new Vector2(777, 659), new Vector2(810, 692), new Vector2(861, 729),
                new Vector2(909, 748), new Vector2(963, 750), new Vector2(1024, 725), new Vector2(1081, 667), new Vector2(1109, 616), new Vector2(1124, 568), new Vector2(1132, 514),
                new Vector2(1132, 441), new Vector2(1118, 376), new Vector2(1092, 312), new Vector2(1056, 261), new Vector2(1000, 220), new Vector2(956, 210), new Vector2(890, 218),
                new Vector2(808, 273), new Vector2(733, 353), new Vector2(668, 442), new Vector2(591, 548), new Vector2(517, 641), new Vector2(428, 723), new Vector2(358, 751),
                new Vector2(274, 737), new Vector2(223, 700), new Vector2(185, 652), new Vector2(164, 599), new Vector2(148, 535)

        };

        myCatmull = new CatmullRomSpline<Vector2>(cp, true);
        for (int i = 0; i < k; ++i) {
            points[i] = new Vector2();
            myCatmull.valueAt(points[i], ((float) i) / ((float) k - 1));
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        fps.log();
        batch.setProjectionMatrix(game.camera.combined);

        batch.begin();
        background.draw(batch);
        if(gameState == GameState.TUTORIAL){
            if(tut != null){
                background.draw(batch);
                tut.draw(batch,delta);
            }

        }else {
            background.draw(batch);
            fontScore.draw(batch, "Score: " + (int) score, game.screenWidth * 0.7f, game.screenHeight * 0.85f);
            if (canDrawGameOverScore)
                hud.drawGameOVerFont(batch, (int) score);
            ball.draw(batch);


            Iterator<WallSet> wallSetIter = walls.iterator();
            while (wallSetIter.hasNext()) {
                WallSet ws = wallSetIter.next();
                Iterator<Wall> wallIter = ws.getWallSet().iterator();
                while (wallIter.hasNext()) {
                    Wall w = wallIter.next();
                    w.draw(batch);

                }
            }


            effect.draw(batch, delta);
        }
            batch.end();

            hud.draw(batch);

        update(delta);
    }

    public void update(float delta) {


        winEffectCounter += delta;
        if(!canPlayWinEffect && winEffectCounter > 0.5){
            canPlayWinEffect = true;
            winEffectCounter = 0;
        }
        if (hud.isPaused())
            gameState = GameState.PAUSE;
        else if(gameState != GameState.GAMEOVER && gameState != GameState.TUTORIAL)
            gameState = GameState.PLAYING;


        if (gameState == GameState.PLAYING) {

            hardiFy();

            if(walls.size()!=0){
                if(canPlayWinEffect){
                    if(ball.getPos().x > walls.get(0).getXPos() - 10 && ball.getPos().x < walls.get(0).getXPos()+10){
                        if(game.isSound){
                            win.play();
                        }
                        canPlayWinEffect = false;
                    }

                }

            }
            if (Gdx.input.isTouched(0)) {
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                game.camera.unproject(touchPos);

                if (touchPos.x > game.screenWidth / 2) {
                    ball.speed = 17;

                } else {
                    ball.speed = 3;
                }
            } else {
                ball.speed = 10;
            }

            score += 0.3f;


            Iterator<WallSet> wallSetIter = walls.iterator();
            while (wallSetIter.hasNext()) {
                WallSet ws = wallSetIter.next();

                Iterator<Wall> wallIter = ws.getWallSet().iterator();
                while (wallIter.hasNext()) {
                    Wall w = wallIter.next();
                    w.update(left);
                    //Remove wall when out of screen

                }
                if (ws.getXPos() < -10 || ws.getXPos() > game.screenWidth) {
                    wallSetIter.remove();
                }
            }


            if (walls.size() == 0) {
                spawnSetOfWalls(ball);
            }

            // TODO Make game harder by making walls go faster and reducing the holes


            //******************
            // collision
            //******************
            Iterator<WallSet> collWallSetIter = walls.iterator();
            while (collWallSetIter.hasNext()) {
                WallSet ws = collWallSetIter.next();
                Iterator<Wall> collWallIter = ws.getWallSet().iterator();
                while (collWallIter.hasNext()) {
                    Wall w = collWallIter.next();
                    if (Intersector.overlaps(ball.getBoundingCircle(), w.getBoundingRec())) {
                        // TODO restart game, show particles, reset score
                        // score = 0;


                        // second argument is the locaiton the for the particle image
                        effect.load(Gdx.files.internal("particle.p"), Gdx.files.internal(""));
                        effect.start();

                        effect.setPosition(ball.getPos().x, ball.getPos().y);
                        if(!ball.gameOver)
                            if(game.isSound){
                                if(game.prefs.getInteger("score") < score){
                                    game.prefs.putInteger("score",(int)score);
                                    game.prefs.flush();
                                    hit.play();
                                }

                            }
                        ball.gameOver = true;
                        gameState = GameState.GAMEOVER;

                    }

                }
            }


        } else if (gameState == GameState.PAUSE) {
            ball.speed = 0;

        } else if (gameState == GameState.GAMEOVER) {
            fontScore.setColor(1,1,1,0);
            hud.imPause.setVisible(false);
            if(effect.isComplete()){
                canDrawGameOverScore = true;
                if(Gdx.input.justTouched()){
                    startOver();
                }
            }
        }
        else if (gameState == GameState.TUTORIAL){
            if(tut == null) {
                tut = new Tutorial(game,this,myCatmull);
            }
            if(tut.done){
                gameState = gameState.PLAYING;
                startOver();
                hud.imPause.setVisible(true);
                game.prefs.putBoolean("first",false);
                game.prefs.flush();
            }

        }
    }


    public void spawnSetOfWalls(Ball ball) {
        WallSet wallSet;

        if (ball.getPos().x <= game.screenWidth / 2) {
            float x = game.screenWidth;
            float y = MathUtils.random(0,200);

            wallSet = new WallSet();
            for (int i = 0; i < 3; i++) {
                wallSet.add(new Wall(game, x, y));
                y = y + Wall.wallHeight + MathUtils.random(minHoleSize, maxHoleSize);
                left = true;

            }
        } else {
            float x = 0;
            float y = MathUtils.random(0,200);

            wallSet = new WallSet();
            for (int i = 0; i < 3; i++) {
                wallSet.add(new Wall(game, x, y));
                y = y + Wall.wallHeight + MathUtils.random(minHoleSize, maxHoleSize);
                left = false;
            }
        }

        walls.add(wallSet);

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        hud.resize(width, height);

    }

    @Override
    protected void handleBackPress() {
        super.handleBackPress();
    }

    public void hardiFy() {
        Iterator<WallSet> collWallSetIter = walls.iterator();
        while (collWallSetIter.hasNext()) {
            if(currentWallSpeed < maxWallSpeed){
                currentWallSpeed += 0.0022f;
                System.out.println(currentWallSpeed);
            }else if(maxHoleSize>100){
                currentWallSpeed += 0.0001;
                maxHoleSize -= 0.02f;
                System.out.println(currentWallSpeed);
                System.out.println(maxHoleSize);
            }
            WallSet ws = collWallSetIter.next();
            Iterator<Wall> collWallIter = ws.getWallSet().iterator();
            while (collWallIter.hasNext()) {
                Wall w = collWallIter.next();
                w.setSpeed(currentWallSpeed);
            }
        }

        System.out.println(maxHoleSize);


    }

    public void startOver(){
        gameState = GameState.PLAYING;
        ball = new Ball(myCatmull,game);
        currentWallSpeed = 5;
        canDrawGameOverScore = false;
        hud.imPause.setVisible(true);
        fontScore.setColor(0,0,0,1);
        score = 0;
        if(walls.size() != 0)
            walls.remove(0);

    }

}


