package com.baloghf.notiap;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.text.TextUtils;
import android.widget.TextView;

import com.baloghf.notiap.constants.NotificationAction;
import com.baloghf.notiap.constants.NotificationActions;
import com.baloghf.notiap.service.PhoneCallService;

import java.util.ArrayList;

/**
 * The main Activity.
 */
public class MainActivity extends AppCompatActivity {
    // These are necessary for alerting the user to enable the Notification Listener for the application
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    // List of the notifications we want to gather
    public static ArrayList<NotificationAction> NOTIFICATION_LIST = new ArrayList<>();

    // The items on the main Activity tab
    private TextView tv1;

    // This will receive the code of the notifications, which the services gathered
    // Not used after BlueTooth implemented
    private NotificationBroadcastReceiver notificationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // The items on the main Activity tab
        tv1 = findViewById(R.id.textView);
        tv1.setText("Empty");

        // Fill up the notification list
        NOTIFICATION_LIST = NotificationActions.createList();

        // If the user did not turn the notification listener service on we prompt him/her to do so
        AlertDialog enableNotificationListenerAlertDialog;
        if (!isNotificationServiceEnabled()) {
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
            enableNotificationListenerAlertDialog.show();
        }


        // Start the PhoneCallService
        Intent phoneCallServiceIntent = new Intent(MainActivity.this, PhoneCallService.class);
        startService(phoneCallServiceIntent);

        // Register the receiver to tell the MainActivity when a notification has been received
        notificationBroadcastReceiver = new NotificationBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("baloghf.notificationservice");
        registerReceiver(notificationBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notificationBroadcastReceiver);
    }

    /**
     * Basic class to gather the needed notifications
     */
    public class NotificationBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int receivedNotificationCode = intent.getIntExtra("Notification Code", -1);
            if (receivedNotificationCode != 0) {
                tv1.setText("ID: " + receivedNotificationCode);
            }
        }
    }

    /**
     * Verifies if the Notification Listener Service is enabled for the application.
     *
     * @return <code>true</code> if enabled, <code>false</code> otherwise.
     */
    private boolean isNotificationServiceEnabled() {
        String packageName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name : names) {
                final ComponentName componentName = ComponentName.unflattenFromString(name);
                if (componentName != null) {
                    if (TextUtils.equals(packageName, componentName.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Builds the alert dialog that pops up if the user has not turned the Notification Listener Service on yet.
     *
     * @return asn alert dialog which leads to the notification enabling screen
     */
    private AlertDialog buildNotificationServiceAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.notification_listener_service);
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation);
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // This should never happen
                    }
                });
        return (alertDialogBuilder.create());
    }
}