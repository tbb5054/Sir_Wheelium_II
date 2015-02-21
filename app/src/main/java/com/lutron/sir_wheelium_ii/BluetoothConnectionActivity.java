package com.lutron.sir_wheelium_ii;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class BluetoothConnectionActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connection);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bluetooth_connection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.bluetooth_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onFindDevicesClicked(View view) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter != null)
        {
            String status;
            // Continue with bluetooth setup.
            if (bluetoothAdapter.isEnabled()) {
                // Enabled. Work with Bluetooth.
                String myDeviceAddress = bluetoothAdapter.getAddress();
                String myDeviceName = bluetoothAdapter.getName();
                int state = bluetoothAdapter.getState();
                status = myDeviceName + " : " + myDeviceAddress+ " : " + state;
            }
            else
            {
                // Disabled. Do something else.
                status = "Bluetooth is not Enabled.";
            }

            TextView t=(TextView)findViewById(R.id.deviceStatusTextView);
            t.setText(status);
        }
    }
}
