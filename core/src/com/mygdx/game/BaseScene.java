package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;

/**
 * Created by wietze on 11/8/2016.
 */

public class BaseScene extends ScreenAdapter{

    private Main game;
    private  boolean keyHandled;

    public BaseScene(Main g) {
        game = g;
        keyHandled = false;

       Gdx.input.setCatchBackKey(true);
       Gdx.input.setCatchMenuKey(true);

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            if (keyHandled) {
                return;
            }
            handleBackPress();
            keyHandled = true;
        } else {
            keyHandled = false;
        }
    }

    protected void handleBackPress(){
        System.out.println("back");
    }
}
