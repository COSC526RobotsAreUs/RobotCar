package com.example.will.robotcar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by Syam on 11/26/2015.
 */
public class EditPreferences extends PreferenceActivity{
    public static boolean lv_batteryInterval;
    public static boolean lv_defaultSpeed;
    public static String lv_timeInterval;
    public static SharedPreferences lv_sP ;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.preferences);

        lv_sP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        lv_batteryInterval = lv_sP.getBoolean("batteryInterval", false);
        lv_defaultSpeed = lv_sP.getBoolean("defaultSpeed",false);
        lv_timeInterval = lv_sP.getString("timeInterval", "1");

        System.out.println("syam"+lv_batteryInterval);
        System.out.println(lv_defaultSpeed);
        System.out.println(lv_timeInterval);
    }
}
