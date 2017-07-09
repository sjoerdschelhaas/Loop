package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by wietze on 7/9/2017.
 */

public class Tutorial {

    float elapsedTime = 0;

    Loop game;
    Sprite leftTut;
    Sprite rightTut;

    Animation tap;

    BitmapFont tutFont;
    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    String leftTutStr = "Tap on the left side of the screen to make the ball go slower";
    String rightTutStr = "Tap on the right side of the screen to make the ball go faster";
    String wallStr = "The goal is to dodge the incoming walls";

    float leftWidth;
    float rightWidth;
    float wallStrWidth;
    GlyphLayout layout = new GlyphLayout(); //dont do this every frame! Store it as member
    Ball ball;
    GameScene gameScene;

    CatmullRomSpline<Vector2> catmullRomSpline;

    int touchCount = 0;

    boolean done = false;

    public Tutorial(Loop g, GameScene gameScene, CatmullRomSpline<Vector2> catmull){
        this.game = g;
        this.gameScene = gameScene;
        this.catmullRomSpline = catmull;

        Array<TextureRegion> animFrames = new Array<TextureRegion>();
        leftTut = new Sprite(g.manager.get("leftTut.png", Texture.class));
        rightTut = new Sprite(g.manager.get("rightTut.png", Texture.class));

        leftTut.setPosition(0,0);
        rightTut.setPosition(game.screenWidth/2,0);

        animFrames.add(g.uiAtlas.findRegion("tap"));
        animFrames.add(g.uiAtlas.findRegion("tapTick"));


        tap = new Animation(0.3f,animFrames, Animation.PlayMode.LOOP);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("DroidSerif-Bold.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 38;
        parameter.color = Color.WHITE;
        tutFont = generator.generateFont(parameter);


        layout.setText(tutFont,leftTutStr);
        leftWidth = layout.width;
        layout.setText(tutFont,rightTutStr);
        rightWidth = layout.width;
        layout.setText(tutFont,wallStr);
        wallStrWidth = layout.width;

        ball = new Ball(catmull,game);

    }



    public void draw(SpriteBatch batch,float delta){

        ball.draw(batch);

        if(Gdx.input.justTouched())
            touchCount++;
        elapsedTime += delta;
        if(touchCount == 0) {
            leftTut.draw(batch);
            batch.draw(tap.getKeyFrame(elapsedTime, true), game.screenWidth * 0.25f, game.screenHeight / 2);
            tutFont.draw(batch,leftTutStr,game.screenWidth/2 - (leftWidth/2)
                    ,game.screenHeight*0.4f);
            ball.speed = 3;
        }else if (touchCount == 1){
            rightTut.draw(batch);
            batch.draw(tap.getKeyFrame(elapsedTime, true), game.screenWidth * 0.75f, game.screenHeight / 2);
            tutFont.draw(batch,rightTutStr,game.screenWidth/2 - (rightWidth/2)
                    ,game.screenHeight*0.4f);
            ball.speed = 17;
        }else if(touchCount == 2){
            if(gameScene.walls.size()==0) {
                gameScene.spawnSetOfWalls(ball);
            }
            tutFont.draw(batch,wallStr,game.screenWidth/2 - (wallStrWidth/2),game.screenHeight*0.4f);
            ball.speed = 10;

            Iterator<WallSet> wallSetIter = gameScene.walls.iterator();
            while (wallSetIter.hasNext()) {
                WallSet ws = wallSetIter.next();
                Iterator<Wall> wallIter = ws.getWallSet().iterator();
                while (wallIter.hasNext()) {
                    Wall w = wallIter.next();
                    w.draw(batch);
                    w.update(gameScene.left);

                }
            }

        }else{
            done = true;
        }
    }


}
