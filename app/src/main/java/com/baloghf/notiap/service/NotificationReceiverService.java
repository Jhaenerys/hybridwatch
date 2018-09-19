package com.baloghf.notiap.service;

import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.baloghf.notiap.constants.NotificationAction;
import com.baloghf.notiap.constants.NotificationActions;

import java.util.ArrayList;

public class NotificationReceiverService extends NotificationListenerService {

    private ArrayList<NotificationAction> NOTIFICATION_LIST;

    @Override
    public void onCreate() {
        super.onCreate();
        NOTIFICATION_LIST = NotificationActions.createList();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        int notificationCode = matchNotificationCode(sbn);

        Intent intent = new Intent("com.baloghf.simplenotificationlogger.Service.NotificationReceiverService");
        intent.putExtra("Notification Code", notificationCode);
        sendBroadcast(intent);
        Log.v("Noti: ", "Code: " + notificationCode);
    }

    private int matchNotificationCode(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();
        int notificationCode = 0;

        for (NotificationAction application : NOTIFICATION_LIST) {
            if (packageName.equals(application.getPackageName())) {
                notificationCode = application.getNotificationCode();
            }
        }
        return notificationCode;
    }
}