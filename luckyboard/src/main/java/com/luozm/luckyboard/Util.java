package com.luozm.luckyboard;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by cdc4512 on 2018/3/30.
 */

public class Util {

    public static int dp2px(Context context, float dp) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) (dp * dm.density);
    }

    public static int getScreenWidth(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

}
