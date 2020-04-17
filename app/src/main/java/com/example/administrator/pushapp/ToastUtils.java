package com.example.administrator.pushapp;

import android.widget.Toast;

/**
 * <p>@author : tangyanghai</p>
 * <p>@time : 2020/4/16</p>
 * <p>@for : </p>
 * <p></p>
 */
public class ToastUtils {
    public static void show(String msg){
        Toast.makeText(App.app,msg,Toast.LENGTH_LONG).show();
    }
}
