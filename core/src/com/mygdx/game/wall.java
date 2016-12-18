package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by wietze on 11/14/2016.
 */

public class Wall {
    Sprite border;
    Sprite wall;



    private float speed = 4f;


    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Wall(Loop g){
        border = new Sprite(new Texture("border.png"));
        wall = new Sprite(new Texture("wall.png"));

        border.setPosition(g.screenWidth / 2 - border.getWidth() /2,g.screenHeight);
        wall.setPosition(g.screenWidth / 2 - wall.getWidth() /2,g.screenHeight);

    }

    public void  update(){
        border.setPosition(border.getX(),border.getY() - speed);
        wall.setPosition(wall.getX(),wall.getY() - speed);

    }

    public void draw(Batch batch) {
        wall.draw(batch);
        border.draw(batch);
    }
}
