package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by wietze on 11/9/2016.
 */

public class GameScene extends BaseScene {

    //TODO Remove when release
    ShapeRenderer shapeRenderer = new ShapeRenderer();

    SpriteBatch batch;
    Sprite background;
    Vector3 touchPos;
    CatmullRomSpline<Vector2> myCatmull;

    float score = 0;
    private ParticleEffect effect;

    float maxHoleSize = 300;
    float minHoleSize = 80;

    // if the ball is moving left
    boolean left = true;

    Ball ball;
    ArrayList<WallSet> walls;

    Loop game;


    BitmapFont fontScore;
    GlyphLayout layout;
    float textWidthScore;


    public GameScene(Loop g) {
        super(g);
        game = g;

    }

    @Override
    public void show() {
        super.show();
        batch = new SpriteBatch();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        setup();


        touchPos = new Vector3();

        background = new Sprite(new Texture("infin.jpg"));

        walls = new ArrayList<WallSet>();

        effect = new ParticleEffect();

        ball = new Ball(myCatmull);
        spawnSetOfWalls();

        // TODO change to freetype
        fontScore = new BitmapFont(Gdx.files.internal("scoreFont3.fnt"));
        fontScore.setColor(Color.BLACK);
        fontScore.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        layout = new GlyphLayout(fontScore, "Score 0");
        textWidthScore = layout.width * 0.5f;

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

        batch.setProjectionMatrix(game.camera.combined);
        batch.begin();
        background.draw(batch);


        ball.draw(batch);


        Iterator<WallSet> wallSetIter = walls.iterator();
        while (wallSetIter.hasNext()){
            WallSet ws = wallSetIter.next();
            Iterator<Wall> wallIter = ws.getWallSet().iterator();
            while (wallIter.hasNext()) {
                Wall w = wallIter.next();
                w.draw(batch);

            }
        }

        fontScore.draw(batch, "Score: " + (int) score, game.screenWidth * 0.8f - (textWidthScore / 2), game.screenHeight * 0.9f);

        effect.draw(batch,delta);

        batch.end();

        // TODO remove later
        shapeRenderer.setProjectionMatrix(game.camera.combined);
        Ball b = ball;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(b.getBoundingCircle().x, b.getBoundingCircle().y, b.getBoundingCircle().radius);
        shapeRenderer.end();



        update(delta);
    }

    public void update(float delta) {

        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(touchPos);

            if (touchPos.x > game.screenWidth / 2) {
                ball.speed = 17;

                // TODO cant have unlimitied boost?

            }else{
                ball.speed = 3;
            }
        } else {
            ball.speed = 10;
        }




        Iterator<WallSet> wallSetIter = walls.iterator();
        while (wallSetIter.hasNext()){
            WallSet ws = wallSetIter.next();

            Iterator<Wall> wallIter = ws.getWallSet().iterator();
            while (wallIter.hasNext()) {
                Wall w = wallIter.next();
                w.update(left);
                //Remove wall when out of screen

            }
            if (ws.getXPos() < -10 || ws.getXPos() > game.screenWidth){
                wallSetIter.remove();
            }
        }


        if(walls.size() == 0){
            spawnSetOfWalls();
        }

        // TODO Make game harder by making walls go faster and reducing the holes


        //******************
        // collision
        //******************
        Iterator<WallSet> collWallSetIter = walls.iterator();
        while (collWallSetIter.hasNext()){
            WallSet ws = collWallSetIter.next();
            Iterator<Wall> collWallIter = ws.getWallSet().iterator();
            while (collWallIter.hasNext()) {
                Wall w = collWallIter.next();
                if (Intersector.overlaps(ball.getBoundingCircle(), w.getBoundingRec())) {
                    // TODO restart game, show particles, reset score
                    score = 0;
                    System.out.println("Collision");


                    // second argument is the locaiton the for the particle image
                    effect.load(Gdx.files.internal("particle.p"),Gdx.files.internal(""));
                    effect.start();

                    effect.setPosition(ball.getPos().x,ball.getPos().y);
                    System.out.println(Gdx.graphics.getWidth()/2 +", " +  Gdx.graphics.getHeight()/2);

                }

            }
        }



    }


    public void spawnSetOfWalls(){
        WallSet wallSet;

        if(ball.getPos().x <= game.screenWidth/2){
            float x = game.screenWidth;
            float y = 200;

            wallSet = new WallSet();
            for (int i = 0; i < 2; i++) {
                wallSet.add(new Wall(game,x,y));
                y = y + Wall.wallHeight + MathUtils.random(minHoleSize,maxHoleSize);
                left = true;

            }
        }else {
            float x = 0;
            float y = 200;

            wallSet = new WallSet();
            for (int i = 0; i < 2; i++) {
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

    }

    @Override
    protected void handleBackPress() {
        super.handleBackPress();
    }
}


