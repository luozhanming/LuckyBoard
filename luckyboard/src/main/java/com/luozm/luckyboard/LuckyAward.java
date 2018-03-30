package com.luozm.luckyboard;

import android.graphics.Bitmap;

/**
 * Created by cdc4512 on 2018/3/30.
 */

public class LuckyAward implements Cloneable{
    private String name;
    private Bitmap bitmap;
    private float rate;

    public LuckyAward(String name, Bitmap bitmap, float rate) {
        this.name = name;
        this.bitmap = bitmap;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
