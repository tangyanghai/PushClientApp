package com.example.administrator.pushapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import static android.support.v4.app.NotificationCompat.PRIORITY_MAX;

/**
 * <p>@author : tangyanghai</p>
 * <p>@time : 2020/4/13</p>
 * <p>@for : </p>
 * <p></p>
 */
public class RunningService extends Service {

    public static boolean isRunning = false;

    private NotificationChannel foregroundChannel;

    //前台服务通知channel id
    String foregroundChannelId = "notification_channel_id_01";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        isRunning = true;
        super.onCreate();
        Notification notification = createForegroundNotification();
        startForeground(110, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        isRunning = false;
        super.onDestroy();
    }

    /**
     * 创建服务通知
     */
    private Notification createForegroundNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// 唯一的通知通道的id.


// Android8.0以上的系统，新建消息通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /*if (foregroundChannel == null) {*/

//用户可见的通道名称
            String channelName = "前台服务";
//通道的重要程度
            int importance = NotificationManager.IMPORTANCE_HIGH;
            foregroundChannel = new NotificationChannel(foregroundChannelId, channelName, importance);
            foregroundChannel.setDescription("Channel description");
//LED灯
            foregroundChannel.enableLights(true);
            foregroundChannel.setLightColor(Color.RED);
//震动
            foregroundChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            foregroundChannel.enableVibration(false);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(foregroundChannel);
            }
            /*}*/
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, foregroundChannelId);
        //通知小图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //通知标题
        builder.setContentTitle("前台通知");
        //通知内容
        builder.setContentText("app正在运行中......");
        builder.setAutoCancel(false);
        //设定通知显示的时间
        builder.setWhen(System.currentTimeMillis());
        builder.setPriority(PRIORITY_MAX);
        builder.setOngoing(true);
        //设定启动的内容
        Intent activityIntent = new Intent(this, PushActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        //创建通知并返回
        return builder.build();
    }


}
