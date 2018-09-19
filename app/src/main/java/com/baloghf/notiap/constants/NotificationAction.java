package com.baloghf.notiap.constants;

public final class NotificationAction {
    private String _name;
    private String _packageName;
    private int _notificationCode;
    private String _tag;

    public NotificationAction(String name, String packageName, int notificationCode) {
        _name = name;
        _packageName = packageName;
        _notificationCode = notificationCode;
        _tag = "0";
    }

    public NotificationAction(String name, String packageName, int notificationCode, String tag) {
        _name = name;
        _packageName = packageName;
        _notificationCode = notificationCode;
        _tag = tag;
    }

    public String getName() {
        return _name;
    }

    public String getPackageName() {
        return _packageName;
    }

    public int getNotificationCode() {
        return _notificationCode;
    }

    public String getTag() { return _tag;}
}
