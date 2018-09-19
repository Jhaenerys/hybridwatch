package com.baloghf.notiap.service;

import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.baloghf.notiap.constants.NotificationAction;
import com.baloghf.notiap.constants.NotificationActions;

import java.util.ArrayList;

/**
 * Service which will gather the needed Notifications.
 */
public class NotificationReceiverService extends NotificationListenerService {

    // The list of the needed notifications
    private ArrayList<NotificationAction> NOTIFICATION_LIST;

    @Override
    public void onCreate() {
        super.onCreate();
        // Fill up the notification list
        NOTIFICATION_LIST = NotificationActions.createList();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    /**
     * Actions will happen, when a new notification is posted.
     *
     * @param sbn The caught Status Bar Notification
     */
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        // Translating the caught notification to a notification code
        int notificationCode = matchNotificationCode(sbn);
        Log.v("Package: ", sbn.getPackageName());
        Log.v("Extras: ", sbn.getNotification().extras.toString());
        Log.v("Sender", sbn.getNotification().extras.getString("android.title"));

        // Sending the notification code
        Intent intent = new Intent("baloghf.notificationservice");
        intent.putExtra("Notification Code", notificationCode);
        sendBroadcast(intent);
        Log.v("Noti: ", "Code: " + notificationCode);
    }

    /**
     * Gives back the correct notification code based on the content of the Status Bar Notification.
     *
     * @param sbn the Status Bar Notification the code-matching will be based on
     * @return the correct notification code
     */
    private int matchNotificationCode(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();
        String tag = sbn.getTag();
        int notificationCode = 0;

        // Iterates through the list of notifications
        for (NotificationAction application : NOTIFICATION_LIST) {
            if (packageName.equals(application.getPackageName())) {
                // Checks, if the caught notification is needed for us
                if (!packageName.equals("com.android.systemui") || tag.equals(application.getTag())) {
                    // Checks if the caught notification was from girlfriend with Facebook Messenger
                    if (packageName.equals(NotificationActions.MESSENGER.getPackageName()) && sbn.getNotification().extras.getString("android.title").contains(NotificationActions.GF_MESSENGER.getTag())) {
                        notificationCode = NotificationActions.GF_MESSENGER.getNotificationCode();
                    } else {
                        notificationCode = application.getNotificationCode();
                    }
                } else {
                    notificationCode = 0;
                }
            }
        }
        return notificationCode;
    }
}