package com.mygdx.game;

import java.util.ArrayList;

/**
 * Created by wietze on 6/16/2017.
 */

public class WallSet {

    boolean canSpawn = true;

    private ArrayList<Wall> wallSet;

    public WallSet(){
        wallSet = new ArrayList<Wall>();
    }
    public void add(Wall wall){
        wallSet.add(wall);
    }

    ArrayList<Wall> getWallSet(){
        return wallSet;
    }
    // We are only interested in where the set is i on the y-axis
    float getXPos(){
        return wallSet.get(0).wall.getX();
    }
}
