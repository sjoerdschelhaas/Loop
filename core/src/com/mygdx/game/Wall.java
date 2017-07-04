package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by wietze on 11/14/2016.
 */

public class Wall {

    Sprite wall;

    private Rectangle boundingRec;

    static float wallHeight;

    float speed = 5f;


    public Rectangle getBoundingRec(){
        return boundingRec;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Wall(Loop g,float x, float y){

        wall = new Sprite(g.manager.get("smallerWall.png",Texture.class));
        wallHeight = wall.getHeight();

        wall.setPosition(x,y);

        boundingRec = new Rectangle();
    }

    public void  update(boolean left){
        if (left){
            wall.setPosition(wall.getX()-speed,wall.getY());
        }else{
            wall.setPosition(wall.getX()+speed,wall.getY());
        }



        boundingRec.set(wall.getBoundingRectangle());

    }

    public void draw(Batch batch) {
        wall.draw(batch);
    }
}
