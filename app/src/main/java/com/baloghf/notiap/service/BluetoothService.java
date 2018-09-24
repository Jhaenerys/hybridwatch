package com.baloghf.notiap.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

import com.baloghf.notiap.R;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothService extends Service {
    // This will receive the code of the notifications, which the services gathered
    private NotificationBroadcastReceiver notificationBroadcastReceiver;

    // This will receive the address of the selected Bluetooth devices
    private AddressBroadcastReceiver addressBroadcastReceiver;

    // Bluetooth background worker thread to send data
    private ConnectedThread connectedThread;

    // Bluetooth related variables
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket = null;

    private static final UUID BLUETOOTH_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier

    private String bluetoothAddress;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Register the notification receiver to tell when a notification has been received
        notificationBroadcastReceiver = new NotificationBroadcastReceiver();
        IntentFilter notificationIntentFilter = new IntentFilter();
        notificationIntentFilter.addAction("baloghf.notificationservice");
        registerReceiver(notificationBroadcastReceiver, notificationIntentFilter);

        // Register the address receiver to gather the necessary data of the selected Bluetooth device
        addressBroadcastReceiver = new AddressBroadcastReceiver();
        IntentFilter addressIntentFilter = new IntentFilter();
        addressIntentFilter.addAction("baloghf.addresssender");
        registerReceiver(addressBroadcastReceiver, addressIntentFilter);

        // Get the default Bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    /**
     * Basic class to gather the needed notifications
     */
    public class NotificationBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String receivedNotificationCode = intent.getStringExtra("Notification Code");
            if (!receivedNotificationCode.equals("0")) {
                if (connectedThread != null) {
                    connectedThread.write(receivedNotificationCode);
                }
            }
        }
    }

    /**
     * Basic class to get the needed address of the Bluetooth device and connect to it
     */
    public class AddressBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras().containsKey("Address")) {
                bluetoothAddress = intent.getStringExtra("Address");
            }
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(bluetoothAddress);
            connectToDevice(device);
        }
    }

    /**
     * Creates a secure connection to the Bluetooth device using the UUID.
     *
     * @param device the device the service should connect to
     * @return the created socket
     * @throws IOException on error, for example Bluetooth not available, or insufficient permissions
     */
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return device.createRfcommSocketToServiceRecord(BLUETOOTH_MODULE_UUID);
    }

    /**
     * A thread which will be used to send data to a Bluetooth device
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket bluetoothSocket;
        private final OutputStream outputStream;

        ConnectedThread(BluetoothSocket socket) {
            bluetoothSocket = socket;
            OutputStream tmpOut = null;

            // Get the input stream
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            outputStream = tmpOut;
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes();           //converts entered String into bytes
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * Connects the phone to the given Bluetooth device.
     *
     * @param device the Bluetooth device the phone should connect to
     */
    void connectToDevice(BluetoothDevice device) {
        boolean fail = false;
        try {
            bluetoothSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            fail = true;
            Toast.makeText(getBaseContext(), R.string.socket_creation_failed, Toast.LENGTH_SHORT).show();
        }
        // Establish the Bluetooth socket connection.
        try {
            bluetoothSocket.connect();
        } catch (IOException e) {
            try {
                fail = true;
                bluetoothSocket.close();
            } catch (IOException e2) {
                Toast.makeText(getBaseContext(), R.string.socket_creation_failed, Toast.LENGTH_SHORT).show();
            }
        }
        if (!fail) {
            connectedThread = new ConnectedThread(bluetoothSocket);
            connectedThread.start();
        }
    }
}
