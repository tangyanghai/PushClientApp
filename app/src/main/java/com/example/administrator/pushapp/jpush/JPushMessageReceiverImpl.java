package com.example.administrator.pushapp.jpush;

import android.content.Context;

import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * <p>@author : tangyanghai</p>
 * <p>@time : 2020/4/14</p>
 * <p>@for : </p>
 * <p></p>
 */
public class JPushMessageReceiverImpl extends JPushMessageReceiver {

    @Override
    public void onMessage(Context context, CustomMessage msg) {
        super.onMessage(context, msg);
    }

}
