package com.baloghf.notiap.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.baloghf.notiap.constants.NotificationActions;

/**
 * Service which will gather the needed phone call related notifications.
 */
public class PhoneCallService extends Service {

    private TelephonyManager telephonyManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            /**
             * Actions will happen when the call state changed
             * @param state indicates the current state of the call
             * @param incomingNumber the number of the caller
             */
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                Intent intent = new Intent("baloghf.notificationservice");
                // Checks what kind of state change happened
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        intent.putExtra("Notification Code", NotificationActions.CALL_RINGING.getNotificationCode());
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        intent.putExtra("Notification Code", NotificationActions.CALL_HAPPENING.getNotificationCode());
                        break;
                    default:
                        intent.putExtra("Notification Code", "0");
                }
                Log.v("Phony: ", "Code: " + state);
                sendBroadcast(intent);
            }
        };

        // Set up the Telephony Manager to listen to the changes of the call state
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        // This will ensure, that the service will be restarted, if it happens to be closed
        return Service.START_STICKY;
    }
}
