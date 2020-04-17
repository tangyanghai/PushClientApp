package com.example.administrator.pushapp;

import android.app.Application;
import android.content.Context;


import org.xutils.x;

/**
 * <p>@author : tangyanghai</p>
 * <p>@time : 2020/4/16</p>
 * <p>@for : </p>
 * <p></p>
 */
public class App extends Application {
    public static Context app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        x.Ext.init(this);
    }
}
