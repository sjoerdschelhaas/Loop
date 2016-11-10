package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.CatmullRomSpline;
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

    Vector2 turnPos = new Vector2(1136,479);
    Vector2 startPos = new Vector2(145,479);



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


        touchPos = new Vector3();

        background = new Sprite(new Texture("infin.jpg"));
        ball = new Sprite(new Texture("ball.png"));
        ball.setPosition(startPos.x-ball.getWidth()/2,startPos.y - ball.getHeight()/2);
        startPos =  new Vector2(ball.getX(),ball.getY());
        balls = new ArrayList<Sprite>();
        balls.add(ball);

        setup();


    }


    //https://github.com/libgdx/libgdx/wiki/Path-interface-&-Splines
    int k = 500; //increase k for more fidelity to the spline
    Vector2[] points = new Vector2[k];
    Vector2 cp[] = new Vector2[]{
            new Vector2(144,480),
            new Vector2(145,450),
            new Vector2(150,419),
            new Vector2(157,381),
            new Vector2(177,325),
            new Vector2(197,291),
            new Vector2(212,272),
            new Vector2(237,248),
            new Vector2(285,218),
            new Vector2(351,210),
            new Vector2(392,219),
            new Vector2(443,248),
            new Vector2(499,300),
            new Vector2(547,355),
            new Vector2(579,395),
            new Vector2(625,462),
            new Vector2(677,536),
            new Vector2(732,607),
            new Vector2(777,659),
            new Vector2(810,692),
            new Vector2(861,729),
            new Vector2(909,748),
            new Vector2(963,750),
            new Vector2(1024,725),
            new Vector2(1081,667),
            new Vector2(1109,616),
            new Vector2(1124,568),
            new Vector2(1132,514),
            new Vector2(1132,441),
            new Vector2(1118,376),
            new Vector2(1092,312),
            new Vector2(1056,261),
            new Vector2(1000,220),
            new Vector2(956,210),
            new Vector2(890,218),
            new Vector2(808,273),
            new Vector2(733,353),
            new Vector2(668,442),
            new Vector2(591,548),
            new Vector2(517,641),
            new Vector2(428,723),
            new Vector2(358,751),
            new Vector2(274,737),
            new Vector2(223,700),
            new Vector2(185,652),
            new Vector2(164,599),
            new Vector2(148,535),
            new Vector2(144,474)

    };
    ShapeRenderer rope=new ShapeRenderer();
    CatmullRomSpline<Vector2> myCatmull;
    public void setup(){
        myCatmull = new CatmullRomSpline<Vector2>( cp, true);
        for(int i = 0; i < k; ++i)
        {
            points[i] = new Vector2();
            myCatmull.valueAt(points[i], ((float)i)/((float)k-1));
        }
    }
    float v = 2f;
    float current = 0;
    Vector2 out = new Vector2();
    @Override
    public void render(float delta) {
        super.render(delta);

        batch.setProjectionMatrix(game.camera.combined);
        batch.begin();
        background.draw(batch);






        Iterator<Sprite> iter = balls.iterator();

        while(iter.hasNext()){
            Sprite b = iter.next();
            myCatmull.derivativeAt(out, current);
            current += (Gdx.graphics.getDeltaTime() * v) / out.len();
            if(current >=1){
                current -=1;
            }
            myCatmull.valueAt(out,current);
            batch.draw(b,out.x-b.getHeight()/2,out.y-b.getHeight()/2);

            b.draw(batch);
        }
        batch.end();

        update();


    }

    public void update(){
        if(Gdx.input.justTouched()){
            touchPos.set(Gdx.input.getX(),Gdx.input.getY(),0);
            game.camera.unproject(touchPos);
            System.out.println(touchPos);
        }

        Iterator<Sprite> iter = balls.iterator();

        while(iter.hasNext()){
            Sprite b = iter.next();


        }

    }

    @Override
    protected void handleBackPress() {
        super.handleBackPress();
    }
}
