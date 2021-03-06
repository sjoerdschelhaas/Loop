package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by wietze on 11/14/2016.
 */

public class Ball {

    private Circle boundingCircle;
    Sprite ball;

    CatmullRomSpline<Vector2> myCatmull;

    float speed = 10f;
    float current = 0;
    Vector2 out = new Vector2();

    public Ball(CatmullRomSpline cm) {
        myCatmull = cm;
        ball = new Sprite(new Texture("ball.png"));
        boundingCircle = new Circle();
    }


    public Circle getBoundingCircle(){
        return boundingCircle;
    }

    public void draw(Batch batch){
        myCatmull.derivativeAt(out, current);
        current += (Gdx.graphics.getDeltaTime() * speed) / out.len();
        if(current >=1){
            current -=1;
        }
        myCatmull.valueAt(out,current);

        boundingCircle.set(out.x, out.y, ball.getWidth()/2-2);
        batch.draw(ball,out.x-ball.getHeight()/2,out.y-ball.getHeight()/2);
    }

    public Vector2 getPos(){
        return new Vector2(out);
    }
}
