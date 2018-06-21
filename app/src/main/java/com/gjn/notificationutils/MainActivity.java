package com.gjn.notificationutils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.gjn.notificationlibrary.NotificationUtils;

public class MainActivity extends AppCompatActivity {

    private NotificationManager notificationManager;
    private PendingIntent pendingIntent;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;

        Log.e("-s-", "Build.VERSION.SDK_INT = " + Build.VERSION.SDK_INT);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, MainActivity.class);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (notificationManager != null) {
//                    openNotification();
//                }
                Notification.Builder builder = NotificationUtils.createNormal(activity,
                        "普通的通知", "一个消息", "收到消息", R.mipmap.ic_launcher_round);
                NotificationUtils.notifyNotification(activity, builder, 0);

                builder.setContentTitle("普通的通知2");
                NotificationUtils.notifyNotification(activity, builder, 1);
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (notificationManager != null) {
//                    zdyNotification();
//                }
                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_my);

                remoteViews.setTextViewText(R.id.textView, "我是文字");
                remoteViews.setTextViewText(R.id.button4, "我是按钮");
                remoteViews.setOnClickPendingIntent(R.id.button4, pendingIntent);

                Notification.Builder builder = NotificationUtils.createRemoteViews(activity, "收到新通知", R.mipmap.ic_launcher_round, remoteViews);
                NotificationUtils.notifyNotification(activity, builder, 2);

            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (notificationManager != null) {
//                    zdsNotification();
//                }
            }
        });
        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (notificationManager != null) {
//                    jdtNotification();
//                }
                Notification.Builder builder = NotificationUtils.createProgress(activity,
                        "进度条", "准备下载", "开始下载", R.mipmap.ic_launcher_round,
                        0, 100, false);
                NotificationUtils.notifyNotification(activity, builder, 3);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Notification.Builder builder;
                        for (int i = 0; i < 101; i++) {
                            builder = NotificationUtils.createProgress(activity,
                                    "下载中", "已下载"+i+"%", "下载中...", R.mipmap.ic_launcher_round,
                                    i, 100, false);
                            NotificationUtils.notifyNotification(activity, builder, 3);
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        builder = NotificationUtils.createNormal(activity, "下载结束" ,
                                "全部下载完成", "下载完成", R.mipmap.ic_launcher);
                        NotificationUtils.notifyNotification(activity, builder, 3);
                    }
                }).start();
            }
        });
        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (notificationManager != null) {
//                    notificationManager.cancelAll();
//                }
                NotificationUtils.cancalAll();
            }
        });
    }

    private void jdtNotification() {
        final Notification.Builder builder = new Notification.Builder(this);

        builder.setContentText("主内容")
                .setContentTitle("进度条通知")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("开始下载");

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 101; i++) {
                    builder.setContentText("下载中:"+i+"%")
                            .setTicker("下载中")
                            .setProgress(100, i, false);
                    notificationManager.notify(666, builder.build());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                builder.setContentText("下载完成").setTicker("下载完成")
                        .setAutoCancel(true)
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(pendingIntent)
                        .setProgress(0, 0, true);
                notificationManager.notify(666, builder.build());
            }
        }).start();

    }

    private void openNotification() {
        Notification.Builder builder = new Notification.Builder(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setSubText("补充内容");
        }else {
            builder.setContentInfo("补充内容");
        }

        builder.setContentText("主要内容")
                .setContentTitle("正常通知")
                .setTicker("状态栏提示文字")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                //跳转的intent
                .setContentIntent(pendingIntent)
                //跳转之后清除通知
                .setAutoCancel(true);
        notificationManager.notify(0, builder.build());
    }

    private void zdyNotification() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_my);

        remoteViews.setTextViewText(R.id.textView, "我是文字");
        remoteViews.setTextViewText(R.id.button4, "我是按钮");
        remoteViews.setOnClickPendingIntent(R.id.button4, pendingIntent);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setSmallIcon(R.mipmap.ic_launcher_round)
                .setWhen(System.currentTimeMillis())
                .setContentText("主要内容")
                .setContentTitle("自定义通知")
                .setTicker("状态栏提示文字")
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setCustomContentView(remoteViews);
        }else {
            builder.setContent(remoteViews);
        }

        notificationManager.notify(11, builder.build());
    }

    private void zdsNotification() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_my_big);

        remoteViews.setTextViewText(R.id.textView, "我是文字");

        Notification.Builder builder = new Notification.Builder(this);

        builder.setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentText("主要内容")
                .setContentTitle("折叠式通知")
                .setTicker("状态栏提示文字")
                .setWhen(System.currentTimeMillis())
                //计时
//                .setUsesChronometer(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setAutoCancel(true);


        Notification notification;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setCustomBigContentView(remoteViews);
            notification = builder.build();
        }else {
            notification = builder.build();
            notification.bigContentView = remoteViews;
        }

        notification.flags |= Notification.FLAG_NO_CLEAR;
        notificationManager.notify(111, notification);
    }
}
