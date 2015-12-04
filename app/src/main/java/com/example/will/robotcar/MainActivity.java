package com.example.will.robotcar;

import android.app.TabActivity;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.view.Menu;
import android.widget.Toolbar;

import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends TabActivity {
    private static InputStream is;
    private static OutputStream os;

    public static InputStream getInputStream(){
        return is;
    }
    public static OutputStream getOutputStream(){
        return os;
    }

    public static BluetoothSocket socket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost = getTabHost();

        // Tab for Connect
        TabSpec connectSpec = tabHost.newTabSpec("Connect");
        // setting Title and Icon for the Tab
        Intent connectIntent = new Intent(this, ConnectActivity.class);
        connectSpec.setIndicator("Connect", getResources().getDrawable(R.drawable.icon_connect));
        connectSpec.setContent(connectIntent);

        // Tab for Drive
        TabSpec driveSpec = tabHost.newTabSpec("Drive");
        // setting Title and Icon for the Tab
        Intent driveIntent = new Intent(this, DriveActivity.class);
        driveSpec.setIndicator("Drive", getResources().getDrawable(R.drawable.icon_drive));
        driveSpec.setContent(driveIntent);

        TabSpec pollSpec = tabHost.newTabSpec("Poll");
        Intent pollIntent = new Intent(this, PollActivity.class);
        pollSpec.setIndicator("Poll");
        pollSpec.setContent(pollIntent);

        tabHost.addTab(connectSpec);
        tabHost.addTab(driveSpec);
        tabHost.addTab(pollSpec);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


}
