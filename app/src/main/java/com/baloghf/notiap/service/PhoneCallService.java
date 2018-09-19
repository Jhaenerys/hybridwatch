package com.baloghf.notiap.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.baloghf.notiap.constants.NotificationActions;

public class PhoneCallService extends Service {

    private TelephonyManager telephonyManager;
    private PhoneStateListener phoneStateListener;

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
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                Intent intent = new Intent("com.baloghf.simplenotificationlogger.Service.NotificationReceiverService");
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        intent.putExtra("Notification Code", NotificationActions.CALL_RINGING.getNotificationCode());
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        intent.putExtra("Notification Code", NotificationActions.CALL_OFFHOOK.getNotificationCode());
                        break;
                    default:
                        intent.putExtra("Notification Code", 0);
                }
                Log.v("Phony: ", "Code: " + state);
                sendBroadcast(intent);
            }
        };

        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        return Service.START_STICKY;
    }
}
