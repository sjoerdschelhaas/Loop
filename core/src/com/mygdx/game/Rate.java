package com.mygdx.game;

/**
 * Created by wietze on 7/9/2017.
 */

public class Rate  {
    static RateHandler rateHelper;
    public Rate(RateHandler r){
        rateHelper = r;
    }

    public void rate(){rateHelper.rate();}
}
