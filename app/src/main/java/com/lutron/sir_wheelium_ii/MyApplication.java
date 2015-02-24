package com.lutron.sir_wheelium_ii;

import android.app.Application;

public class MyApplication extends Application {
    private BluetoothConnectionActivity.ConnectedThread connectedThread;

    public BluetoothConnectionActivity.ConnectedThread getConnectedThread(){
        return connectedThread;
    }

    public void setConnectedThread(BluetoothConnectionActivity.ConnectedThread connectedThread) {
        this.connectedThread = connectedThread;
    }
}
