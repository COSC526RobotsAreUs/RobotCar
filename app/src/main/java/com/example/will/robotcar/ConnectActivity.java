package com.example.will.robotcar;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Will on 11/6/15.
 */
public class ConnectActivity extends AppCompatActivity implements View.OnClickListener{

    Timer cv_timer;
    MyTimerTask cv_myTimerTask;
    SharedPreferences sp;

    final String CC_ROBOTNAME = "NXT03";
    TextView cv_label, cv_conncStatusmsg,cv_connectDeviceNm;
    boolean cv_moveFlag = false;
    ListView cv_Blist;
    private int cv_batterypower;

    private BluetoothAdapter btInterface;
    private Set<BluetoothDevice> pairedDevices;
    private BluetoothDevice bd;

    private BroadcastReceiver btMonitor = null;
    private InputStream is = null;
    private OutputStream os = null;

    private Button cv_connectBtn, cv_disconnectBtn;
    private ImageView flipImg;
    private TableLayout cv_tableLayout;
    private TextView m_battery;

    private BatteryLevelView batteryLevelView;

    private boolean hasConnected = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setLogo(R.drawable.nxticon);
        ab.setTitle("Connect To Robot");
        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        cv_label = (TextView)findViewById(R.id.xv_txtLabel);
        cv_connectBtn = (Button) findViewById(R.id.xv_connectBtn);
        cv_connectBtn.setOnClickListener(this);
        cv_disconnectBtn = (Button) findViewById(R.id.xv_diconnectBtn);
        cv_disconnectBtn.setOnClickListener(this);

        cv_conncStatusmsg=(TextView)findViewById(R.id.xv_connectionStatus);
        cv_connectDeviceNm=(TextView)findViewById(R.id.xv_connectedDevice);
        flipImg=(ImageView)findViewById(R.id.BImgView);

        cv_tableLayout = (TableLayout) findViewById(R.id.xv_tLImgBtn);

        m_battery = (TextView) findViewById(R.id.xv_BatteryLevel);

        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        setupBTMonitor();
    }

   @Override
    public  boolean onCreateOptionsMenu(Menu menu){
       MenuInflater mf = getMenuInflater();
       mf.inflate(R.menu.menu_main, menu);
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.xv_menu:	startActivity(new Intent(this, EditPreferences.class));
                return(true);
            case R.id.xv_about_app:	startActivity(new Intent(this, EditPreferences.class));
                return(true);
            case R.id.xv_resetPreferences:	startActivity(new Intent(this, ResetPreferences.class));
                return(true);
        }
        return(super.onOptionsItemSelected(item));
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

        alertMsg.setTitle(Html.fromHtml("<font color='#000000'>Select NXT Device</font>"));
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.bluetoothpairlist, null);
        alertMsg.setView(convertView);
        cv_Blist = (ListView) convertView.findViewById(R.id.listView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, btNames);
        cv_Blist.setAdapter(adapter);
        final AlertDialog cv_ad = alertMsg.show();

        TextView listHeader=new TextView(this);
        listHeader.setTextSize(18);
        listHeader.setText("\n      Paired Bluetooth Devices");
        listHeader.setTextColor(Color.WHITE);
        listHeader.setBackgroundColor(Color.BLUE);

        cv_Blist.addHeaderView(listHeader);

        cv_Blist.setBackgroundColor(Color.LTGRAY);

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
                    System.out.println("bd.getName()" + bd.getName());
                    if (bd.getName().equalsIgnoreCase(lv_deviceNm[0])){
                        System.out.println("inside");
                        boolean connectionWasSuccessful = true;
                        try {
                            MainActivity.socket = bd.createRfcommSocketToServiceRecord(
                                    java.util.UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                            MainActivity.socket.connect();

                            is = MainActivity.socket.getInputStream();
                            os = MainActivity.socket.getOutputStream();
                            batteryLevelView = (BatteryLevelView)findViewById(R.id.batteryLevelView);

                            if(sp.getBoolean("batteryInterval", false)){
                                if(cv_timer != null)
                                    cv_timer.cancel();
                                else{
                                    cv_timer = new Timer();
                                    cv_myTimerTask = new MyTimerTask();
                                    cv_timer.schedule(cv_myTimerTask, 0, 60000);
                                }
                            }
                        }
                        catch (Exception e) {
                            cv_conncStatusmsg.setText("Error interacting with remote device [" +
                                    e.getMessage() + "]");
                            connectionWasSuccessful = false;
                        }
                        if(connectionWasSuccessful){
                            hasConnected = true;
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
            cv_connectDeviceNm.setText("    ");

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

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(1);
                        byte[] buffer = new byte[15];

                        buffer[0] = (byte) (15 - 2);            //length lsb
                        buffer[1] = 0;                        // length msb
                        buffer[2] = 0x00;                    // direct command (with response)
                        buffer[3] = 0x0B;                    // set output state
                        buffer[4] = 0;                    // output 1 (motor B)
                        buffer[5] = 0;                        // power
                        buffer[6] = 0;                        // motor on + brake between PWM
                        buffer[7] = 0;                        // regulation
                        buffer[8] = 0;                        // turn ration??
                        buffer[9] = 0;                    // run state
                        buffer[10] = 0;
                        buffer[11] = 0;
                        buffer[12] = 0;
                        buffer[13] = 0;
                        buffer[14] = 0;

                        if (os == null) {
                            os = MainActivity.socket.getOutputStream();
                        }
                        if (is == null) {
                            is = MainActivity.socket.getInputStream();
                        }
                        os.write(buffer);
                        os.flush();
                        byte[] mv_batteryresponse = new byte[7];
                        int batteryResponse = is.read(mv_batteryresponse);
                        for (int i = 0; i < 7; i++){
                            System.out.printf("0x%02X \n", mv_batteryresponse[i]);
                        }

                        int batteryPower = ((mv_batteryresponse[6] << 8) & 0x0000ff00) | (mv_batteryresponse[5] & 0x000000ff);
                        double m_double = (double) batteryPower;
                        cv_batterypower = ((int) ((m_double / 9000) * 100));
                        System.out.println("powerrrrr" + cv_batterypower);
                        batteryLevelView.setPercent(cv_batterypower);
                        m_battery.setText("Battery Power  " + cv_batterypower + "%");
                    } catch (Exception e) {
                        cv_batterypower = -1;
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.xv_connectBtn:
                if(hasConnected == false){
                    cfp_connectNXT();
                    cv_disconnectBtn.setTextColor(Color.BLACK);
                }
                break;
            case R.id.xv_diconnectBtn:
                if(hasConnected){
                    cfp_disconnectNXT();
                    hasConnected = false;
                }
                break;
        }
    }
}
