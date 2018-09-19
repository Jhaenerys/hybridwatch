package com.baloghf.notiap.constants;

/**
 * Basic class to hold information about the notifications we want to gather
 */
public final class NotificationAction {
    /**
     * The name of the notification action
     */
    private String _name;
    /**
     * The name of the package of the notification action
     */
    private String _packageName;
    /**
     * The given notification code
     */
    private int _notificationCode;
    /**
     * A given tag to the notification action
     * Can be used to distinguish between different notifications in the same package
     */
    private String _tag;

    /**
     * Constructor if no tag needed.
     *
     * @param name             the name of the notification action
     * @param packageName      the name of the package of the notification action
     * @param notificationCode the chosen notification code
     */
    NotificationAction(String name, String packageName, int notificationCode) {
        _name = name;
        _packageName = packageName;
        _notificationCode = notificationCode;
        _tag = "0";
    }

    /**
     * Constructor if a tag is needed.
     *
     * @param name             the name of the notification action
     * @param packageName      the name of the package of the notification action
     * @param notificationCode the chosen notification code
     * @param tag              the chosen tag
     */
    NotificationAction(String name, String packageName, int notificationCode, String tag) {
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

    public String getTag() {
        return _tag;
    }
}
