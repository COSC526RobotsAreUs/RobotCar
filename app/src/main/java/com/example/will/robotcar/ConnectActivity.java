package com.example.will.robotcar;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Will on 11/6/15.
 */
public class ConnectActivity extends Activity implements View.OnClickListener{
    final String CC_ROBOTNAME = "NXT03";
    TextView cv_label, cv_conncStatusmsg,cv_connectDeviceNm;
    boolean cv_moveFlag = false;
    ListView cv_Blist;

    // BT Variables
    private BluetoothAdapter btInterface;
    private Set<BluetoothDevice> pairedDevices;
    private BluetoothDevice bd;

    private BroadcastReceiver btMonitor = null;
    private InputStream is = null;
    private OutputStream os = null;

    private Button cv_connectBtn, cv_disconnectBtn;
    private ImageView flipImg;
    private TableLayout cv_tableLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect);

        cv_label = (TextView)findViewById(R.id.xv_txtLabel);
        cv_connectBtn = (Button) findViewById(R.id.xv_connectBtn);
        cv_connectBtn.setOnClickListener(this);
        cv_disconnectBtn = (Button) findViewById(R.id.xv_diconnectBtn);
        cv_disconnectBtn.setOnClickListener(this);
        //cv_disconnectBtn.setEnabled(false);

        cv_conncStatusmsg=(TextView)findViewById(R.id.xv_connectionStatus);
        cv_connectDeviceNm=(TextView)findViewById(R.id.xv_connectedDevice);
        flipImg=(ImageView)findViewById(R.id.BImgView);

        cv_tableLayout = (TableLayout) findViewById(R.id.xv_tLImgBtn);

        setupBTMonitor();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(btMonitor, new IntentFilter("android.bluetooth.device.action.ACL_CONNECTED"));
        registerReceiver(btMonitor, new IntentFilter("android.bluetooth.device.action.ACL_DISCONNECTED"));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(btMonitor);
    }

    private void cfp_connectNXT() {

        btInterface = BluetoothAdapter.getDefaultAdapter();
        pairedDevices = btInterface.getBondedDevices();
        ArrayList<String> btNames = new ArrayList<String>();


        Iterator<BluetoothDevice> it = pairedDevices.iterator();
        while (it.hasNext()) {
            bd = it.next();
            btNames.add(bd.getName() + "\n" + bd.getAddress());
        }

        final AlertDialog.Builder alertMsg = new AlertDialog.Builder(this);
        alertMsg.setTitle("   Select NXT Device");

        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.bluetoothpairlist, null);
        alertMsg.setView(convertView);
        cv_Blist = (ListView) convertView.findViewById(R.id.listView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, btNames);
        cv_Blist.setAdapter(adapter);
        final AlertDialog cv_ad = alertMsg.show();

        TextView listHeader=new TextView(this);
        listHeader.setTextSize(18);
        listHeader.setText("\n        Paired Bluetooth Devices");
        listHeader.setTextColor(Color.BLACK);

        cv_Blist.addHeaderView(listHeader);

        cv_Blist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),
                        String.format("Click ListItem " + position, position), Toast.LENGTH_SHORT).show();
                String deviceNm = (String) parent.getItemAtPosition(position);
                System.out.println("deviceNm---------------->>>>" + deviceNm);
                String[] lv_deviceNm = deviceNm.split("\n");
                Iterator<BluetoothDevice> it = pairedDevices.iterator();

                while (it.hasNext()) {
                    bd = it.next();
                    System.out.println("bd.getName()"+bd.getName());
                    if (bd.getName().equalsIgnoreCase(lv_deviceNm[0])){
                        System.out.println("inside");
                        try {
                            MainActivity.socket = bd.createRfcommSocketToServiceRecord(
                                    java.util.UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                            MainActivity.socket.connect();

                        }
                        catch (Exception e) {
                            cv_conncStatusmsg.setText("Error interacting with remote device [" +
                                    e.getMessage() + "]");
                        }
                        break;
                    }
                }
                cv_ad.dismiss();

            }
        });
    }

    private void cfp_disconnectNXT() {
        try {
            MainActivity.socket.close();
            is.close();
            os.close();
            cv_conncStatusmsg.setText("Disconnected");
            cv_connectDeviceNm.setText("");
        } catch (Exception e) {
            cv_conncStatusmsg.setText("Error in disconnect -> " + e.getMessage());
        }
    }

    private void setupBTMonitor() {
        btMonitor = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(
                        "android.bluetooth.device.action.ACL_CONNECTED")) {
                    try {
                        is = MainActivity.socket.getInputStream();
                        os = MainActivity.socket.getOutputStream();
                        cv_conncStatusmsg.setText("Connected");
                        cv_tableLayout.setBackgroundColor(Color.parseColor("#636F57"));

                        cv_connectDeviceNm.setTextColor(Color.rgb(255, 165, 0));
                        cv_connectDeviceNm.setText(bd.getName());
                    } catch (Exception e) {
                        cfp_disconnectNXT();
                        is = null;
                        os = null;
                    }
                }
                if (intent.getAction().equals(
                        "android.bluetooth.device.action.ACL_DISCONNECTED")) {
                    cv_conncStatusmsg.setText("Connection is broken");
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.xv_connectBtn:
                cfp_connectNXT();
                //cv_connectBtn.setEnabled(false);
                //cv_disconnectBtn.setEnabled(true);
                break;
            case R.id.xv_diconnectBtn:
                cfp_disconnectNXT();
                //cv_connectBtn.setEnabled(true);
                //cv_disconnectBtn.setEnabled(false);
                break;

        }
    }
}
