package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by wietze on 11/9/2016.
 */

public class GameScene extends BaseScene {

    boolean canAddBall = true;
    float timeOneSpin = 6.7f;

    SpriteBatch batch;
    Sprite background;
    Vector3 touchPos;
    CatmullRomSpline<Vector2> myCatmull;

    float score = 0;
    float spawnSpeedMin = 2f;
    float spawnSpeedMax = 4.5f;

    float minTime = 0f;
    float wallspeed = 4;
    float spawnSpeed = 0;
    ArrayList<Ball> balls;
    ArrayList<Wall> walls;

    float counter = 0;
    Loop game;


    public GameScene(Loop g){
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
        Ball ball = new Ball(myCatmull);
        balls = new ArrayList<Ball>();
        balls.add(ball);

        walls = new ArrayList<Wall>();
    }

    public void setup(){
        int k = 500; //increase k for more fidelity to the spline
        Vector2[] points = new Vector2[k];
        Vector2 cp[] = new Vector2[]{
                new Vector2(144,480), new Vector2(145,450), new Vector2(150,419), new Vector2(157,381), new Vector2(177,325), new Vector2(197,291), new Vector2(212,272),
                new Vector2(237,248), new Vector2(285,218), new Vector2(351,210), new Vector2(392,219), new Vector2(443,248), new Vector2(499,300), new Vector2(547,355),
                new Vector2(579,395), new Vector2(625,462), new Vector2(677,536), new Vector2(732,607), new Vector2(777,659), new Vector2(810,692), new Vector2(861,729),
                new Vector2(909,748), new Vector2(963,750), new Vector2(1024,725), new Vector2(1081,667), new Vector2(1109,616), new Vector2(1124,568), new Vector2(1132,514),
                new Vector2(1132,441), new Vector2(1118,376), new Vector2(1092,312), new Vector2(1056,261), new Vector2(1000,220), new Vector2(956,210), new Vector2(890,218),
                new Vector2(808,273), new Vector2(733,353), new Vector2(668,442), new Vector2(591,548), new Vector2(517,641), new Vector2(428,723), new Vector2(358,751),
                new Vector2(274,737), new Vector2(223,700), new Vector2(185,652), new Vector2(164,599), new Vector2(148,535)

        };

        myCatmull = new CatmullRomSpline<Vector2>( cp, true);
        for(int i = 0; i < k; ++i)
        {
            points[i] = new Vector2();
            myCatmull.valueAt(points[i], ((float)i)/((float)k-1));
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        batch.setProjectionMatrix(game.camera.combined);
        batch.begin();
        background.draw(batch);
        Iterator<Ball> iter = balls.iterator();

        while(iter.hasNext()){
            Ball b = iter.next();
            b.draw(batch);
        }
        Iterator<Wall> wallIter = walls.iterator();

        while(wallIter.hasNext()){
            Wall w = wallIter.next();
            w.draw(batch);


        }
        batch.end();

        update(delta);
    }

    public void update(float delta){
        if(Gdx.input.isTouched()){
            touchPos.set(Gdx.input.getX(),Gdx.input.getY(),0);
            game.camera.unproject(touchPos);

            if(touchPos.x>game.screenWidth/2){
                spawnSpeed = MathUtils.random(spawnSpeedMin,spawnSpeedMax) * 0.5f;
                Iterator<Wall> iter = walls.iterator();

                while(iter.hasNext()){
                    Wall w = iter.next();
                    w.setSpeed(wallspeed *1.8f);
                    w.update();

                }
            }else{
                spawnSpeed = MathUtils.random(spawnSpeedMin,spawnSpeedMax) * 2f;
                Iterator<Wall> iter = walls.iterator();

                while(iter.hasNext()){
                    Wall w = iter.next();
                    w.setSpeed(wallspeed *0.5f);
                    w.update();

                }
            }

        }else {
            spawnSpeed = MathUtils.random(spawnSpeedMin,spawnSpeedMax);
            Iterator<Wall> iter = walls.iterator();

            while(iter.hasNext()){
                Wall w = iter.next();
                w.setSpeed(wallspeed);
                w.update();
            }
        }

        counter += delta;
        if(counter > spawnSpeed +minTime){ //TODO Remove wall if outside screen
            walls.add(new Wall(game));
            counter = 0;
        }

        System.out.println(balls.get(0).getPos());
        System.out.println(counter);
        Iterator<Ball> iter = balls.iterator();

        while(iter.hasNext()){
            Ball b = iter.next();
            if(b.getPos().x<(game.screenWidth /2) +3 && b.getPos().x > (game.screenWidth/2) -3&& b.canScore){
                score++;
                b.canScore = false;
            }else {
                b.canScore = true;
            }
        }


        if(score == 1){
            if(canAddBall){
                balls.add(new Ball(myCatmull));
                canAddBall = false;
            }
        }
        if(score >1 && score <3)
            canAddBall=true;

        if(score == 3){
            if(canAddBall){
                balls.add(new Ball(myCatmull));
                canAddBall = false;
            }
        }

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

