package com.gjn.notificationlibrary;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

/**
 * Created by gjn on 2018/6/21.
 */

public class NotificationUtils {
    private static final String TAG = "NotificationUtils";
    private static NotificationManager manager;

    public static void notifyNotification(Activity activity, Notification.Builder builder, int id){
        checkManager(activity);

        manager.notify(id, builder.build());
    }

    public static void notifyNotification(Activity activity, Notification.Builder builder, int id, String tag){
        checkManager(activity);

        manager.notify(tag, id, builder.build());
    }

    public static Notification.Builder createProgress(Activity activity, String title, String content, String ticker, int icon,
                                                      int progress, int max, boolean indeterminate){
        Notification.Builder builder = createNormal(activity, title, content, ticker, icon);
        builder.setProgress(max, progress, indeterminate);
        return builder;
    }

    public static Notification.Builder createRemoteViews(Activity activity, String ticker, int icon,
                                                         RemoteViews views){
        Notification.Builder builder = createNormal(activity, "", "", ticker, icon);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setCustomContentView(views);
        }else {
            builder.setContent(views);
        }
        return builder;
    }

    public static Notification.Builder createNormal(Activity activity, String title,
                                                    String content, String ticker,
                                                    int icon){
        return createNormal(activity, title, content, ticker, icon, (Intent) null);
    }

    public static Notification.Builder createNormal(Activity activity, String title,
                                                    String content, String ticker,
                                                    int icon, PendingIntent intent){
        Notification.Builder builder = createNormal(activity, title, content, ticker, icon);
        builder.setContentIntent(intent);
        return builder;
    }

    public static Notification.Builder createNormal(Activity activity, String title,
                                                    String content, String ticker,
                                                    int icon, Intent intent){
        PendingIntent pendingIntent = null;
        if (intent != null) {
            pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        }
        return new Notification.Builder(activity)
                .setTicker(ticker)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSmallIcon(icon);
    }

    private static void checkManager(Activity activity) {
        if (manager == null) {
            manager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }

    public static void cancalAll(){
        if (manager != null) {
            manager.cancelAll();
            manager = null;
        }
    }

    public static void cancal(int id){
        if (manager != null) {
            manager.cancel(id);
        }
    }

    public static void cancal(String tag, int id){
        if (manager != null) {
            manager.cancel(tag, id);
        }
    }
}
