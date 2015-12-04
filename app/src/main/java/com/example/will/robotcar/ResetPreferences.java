package com.example.will.robotcar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by Syam on 11/26/2015.
 */
public class ResetPreferences extends PreferenceActivity  {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.preferences);

        SharedPreferences lv_sP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor lv_editor = lv_sP.edit();

        lv_editor.clear();
        lv_editor.commit();

        boolean lv_batteryInterval = lv_sP.getBoolean("batteryInterval", false);
        boolean lv_defaultSpeed = lv_sP.getBoolean("defaultSpeed", false);
        String lv_timeInterval = lv_sP.getString("timeInterval", "1");

        System.out.println("syam"+lv_batteryInterval);
        System.out.println(lv_defaultSpeed);
        System.out.println(lv_timeInterval);
    }
}

