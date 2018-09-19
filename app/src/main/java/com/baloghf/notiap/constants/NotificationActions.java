package com.baloghf.notiap.constants;

import java.util.ArrayList;

/**
 * Basic class to collect the notifications we want to gather.
 */
public final class NotificationActions {
    // Facebook notification
    public static final NotificationAction FACEBOOK = new NotificationAction("Facebook", "com.facebook.katana", 1);
    // Facebook Messenger notification
    public static final NotificationAction MESSENGER = new NotificationAction("Messenger", "com.facebook.orca", 2);
    // Gmail notification
    public static final NotificationAction GMAIL = new NotificationAction("GMail", "com.google.android.gm", 3);
    // Incoming/declined call
    public static final NotificationAction CALL_RINGING = new NotificationAction("CallRinging", "", 4);
    // In call
    public static final NotificationAction CALL_OFFHOOK = new NotificationAction("CallOffhook", "", 5);
    // Incoming SMS
    public static final NotificationAction SMS = new NotificationAction("SMS", "com.google.android.apps.messaging", 6);
    // Battery notification
    public static final NotificationAction BATTERY = new NotificationAction("Battery", "com.android.systemui", 7, "low_battery");
    // Girlfriend Messenger
    public static final NotificationAction GF_MESSENGER = new NotificationAction("GfMessenger", "", 9, "Drágaság");

    /**
     * Creates a list from the needed NotificationActions.
     *
     * @return the created list
     */
    public static ArrayList<NotificationAction> createList() {
        ArrayList<NotificationAction> list = new ArrayList<>();
        list.add(FACEBOOK);
        list.add(MESSENGER);
        list.add(GMAIL);
        list.add(CALL_RINGING);
        list.add(CALL_OFFHOOK);
        list.add(SMS);
        list.add(BATTERY);
        list.add(GF_MESSENGER);
        return list;
    }
}
