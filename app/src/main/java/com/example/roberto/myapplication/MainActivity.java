package com.example.roberto.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.Scanner;

public class MainActivity extends AppCompatActivity {


    private final static int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter Bluetooth = BluetoothAdapter.getDefaultAdapter();

    private ArrayAdapter<String> discoveredDevicesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (Bluetooth == null) {
            Toast.makeText(this, "El Bluetooth no esta disponible!", Toast.LENGTH_SHORT).show();
            finish(); //si el bluetooth no esta disponible esta madre cierra la aplicacion
        }

        else{
            Toast.makeText(this, "El Bluetooth esta disponible", Toast.LENGTH_SHORT).show();

        }
    }

    public void connectBluetooth(View v){

        //cuando se presiona el boton btnConnect empieza revisando si el bluetooth esta activo y si no lo esta lo activa
        if (!Bluetooth.isEnabled()) {
            Intent intentActivo = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intentActivo, REQUEST_ENABLE_BT);
            Toast.makeText(this, "Activando Bluetooth...", Toast.LENGTH_SHORT).show();
        }

        else if(Bluetooth.isEnabled()){
            Toast.makeText(this, "El Bluetooth ya esta activado", Toast.LENGTH_SHORT).show();
        }


        if(Bluetooth.isDiscovering()){
            Bluetooth.cancelDiscovery();
        }

        Bluetooth.startDiscovery();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(discoveryFinishReceiver, filter);

        filter = new IntentFilter(Bluetooth.ACTION_DISCOVERY_FINISHED);
        registerReceiver(discoveryFinishReceiver, filter);

    }













     private final BroadcastReceiver discoveryFinishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (device.getBondState() != BluetoothDevice.BOND_BONDED){
                  discoveredDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            } else if (Bluetooth.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (discoveredDevicesAdapter.getCount()==0){
                    discoveredDevicesAdapter.add(getString(R.string.noDevices));
                }
            }
        }
    };

}
