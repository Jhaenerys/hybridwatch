package com.baloghf.notiap.constants;

public final class NotificationAction {
    private String _name;
    private String _packageName;
    private int _notificationCode;

    public NotificationAction(String name, String packageName, int notificationCode) {
        _name = name;
        _packageName = packageName;
        _notificationCode = notificationCode;
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
}
