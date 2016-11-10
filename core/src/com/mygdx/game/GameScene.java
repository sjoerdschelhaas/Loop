package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by wietze on 11/9/2016.
 */

public class GameScene extends BaseScene {

    SpriteBatch batch;
    Sprite background;
    Vector3 touchPos;
    Sprite ball;

    Vector2 starPos;
    ArrayList<Sprite> balls;

    double speed = 0.001f;

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

        starPos = new Vector2(145,479);

        touchPos = new Vector3();

        background = new Sprite(new Texture("infin.jpg"));
        ball = new Sprite(new Texture("ball.png"));
        ball.setPosition(starPos.x-ball.getWidth()/2,starPos.y - ball.getHeight()/2);
        balls = new ArrayList<Sprite>();
        balls.add(ball);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        batch.setProjectionMatrix(game.camera.combined);
        batch.begin();
        background.draw(batch);
        Iterator<Sprite> iter = balls.iterator();

        while(iter.hasNext()){
            Sprite b = iter.next();
            b.draw(batch);
        }
        batch.end();

        update();
    }

    public void update(){
        if(Gdx.input.justTouched()){
            touchPos.set(Gdx.input.getX(),Gdx.input.getY(),0);
            game.camera.unproject(touchPos);
        }

        Iterator<Sprite> iter = balls.iterator();

        while(iter.hasNext()){
            Sprite b = iter.next();
            double newY = b.getY();
            double newX;

            speed+=0.1;
            b.setPosition((float)Math.cos(speed) *100+starPos.x,(float)Math.sin(speed)*100+starPos.y);



        }

    }

    @Override
    protected void handleBackPress() {
        super.handleBackPress();
    }
}
