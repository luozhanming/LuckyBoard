package com.luozm.luckyboarddemo;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by cdc4512 on 2018/4/20.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }
}
