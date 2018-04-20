package com.luozm.luckyboard;

import android.graphics.Bitmap;

/**
 * Created by cdc4512 on 2018/3/30.
 */

public class LuckyAward implements Cloneable{
    private String name;
    private String img;
    private float rate;

    public LuckyAward(String name, String img, float rate) {
        this.name = name;
        this.img = img;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBitmap() {
        return img;
    }

    public void setBitmap(String bitmap) {
        this.img = bitmap;
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
