package com.example.will.robotcar;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;

/**
 * Created by Will on 11/6/15.
 */
public class DriveActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener, SliderChanged{

    private OutputStream os = MainActivity.getOutputStream();
    private InputStream is = MainActivity.getInputStream();
    CustomSlider powerSeek;
    CustomSlider motorSeek;
    private TextView m_progress;
    private TextView m_progressC;
    private int m_power = 50;
    private int m_powerC = 50;
    private String motorSelected;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drive);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setLogo(R.drawable.nxticon);
        ab.setTitle("Drive the Robot");
        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        powerSeek = (CustomSlider) findViewById(R.id.xv_seekBar);
        motorSeek = (CustomSlider) findViewById(R.id.xv_MotorCseekBar);
        powerSeek.setOnSliderChangedListener(this);
        motorSeek.setOnSliderChangedListener(this);

        m_progress = (TextView) findViewById(R.id.xv_powertext);
        m_progressC = (TextView) findViewById(R.id.xv_MotorCpowertext);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if(sp.getBoolean("defaultSpeed", false)){
            powerSeek.setPercent(100);
            m_progress.setText("Power " + 100);
            motorSeek.setPercent(100);
            m_progressC.setText("Power " + 100);
        }
        else{
            m_progress.setText("Power  " + 75);
            m_progressC.setText("Power  " + 75);
        }

        motorSelected = sp.getString("selectedMotors","1");

        ImageButton top = (ImageButton)findViewById(R.id.top);
        top.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(motorSelected.equalsIgnoreCase("1")){
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        cfp_moveMotor(0, m_power, 0x20);
                        cfp_moveMotor(1, m_power, 0x20);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        cfp_moveMotor(0, m_power, 0x00);
                        cfp_moveMotor(1, m_power, 0x00);
                    }
                }
                else if(motorSelected.equalsIgnoreCase("2")){
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        cfp_moveMotor(1, m_power, 0x20);
                        cfp_moveMotor(2, m_power, 0x20);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        cfp_moveMotor(1, m_power, 0x00);
                        cfp_moveMotor(2, m_power, 0x00);
                    }
                }
                else{
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        cfp_moveMotor(0, m_power, 0x20);
                        cfp_moveMotor(2, m_power, 0x20);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        cfp_moveMotor(0, m_power, 0x00);
                        cfp_moveMotor(2, m_power, 0x00);
                    }
                }
                return false;
            }
        });
        ImageButton cv_down = (ImageButton)findViewById(R.id.xv_down);
        cv_down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(motorSelected.equalsIgnoreCase("1")){
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        cfp_moveMotor(0, -m_power, 0x20);
                        cfp_moveMotor(1, -m_power, 0x20);
                    }
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        cfp_moveMotor(0, m_power, 0x00);
                        cfp_moveMotor(1, m_power, 0x00);
                    }
                }
                else if(motorSelected.equalsIgnoreCase("2")){
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        cfp_moveMotor(1, -m_power, 0x20);
                        cfp_moveMotor(2, -m_power, 0x20);
                    }
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        cfp_moveMotor(1, m_power, 0x00);
                        cfp_moveMotor(2, m_power, 0x00);
                    }
                }
                else{
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        cfp_moveMotor(0, -m_power, 0x20);
                        cfp_moveMotor(2, -m_power, 0x20);
                    }
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        cfp_moveMotor(0, m_power, 0x00);
                        cfp_moveMotor(2, m_power, 0x00);
                    }
                }

                return false;
            }
        });
        ImageButton cv_left = (ImageButton)findViewById(R.id.xv_left);
        cv_left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(motorSelected.equalsIgnoreCase("1")){
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        cfp_moveMotor(0, -m_power, 0x20);
                        cfp_moveMotor(1, m_power, 0x20);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        cfp_moveMotor(0, -m_power, 0x00);
                        cfp_moveMotor(1, m_power, 0x00);
                    }
                }
                else if(motorSelected.equalsIgnoreCase("2")){
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        cfp_moveMotor(2, -m_power, 0x20);
                        cfp_moveMotor(1, m_power, 0x20);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        cfp_moveMotor(2, -m_power, 0x00);
                        cfp_moveMotor(1, m_power, 0x00);
                    }
                }
                else{
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        cfp_moveMotor(2, m_power, 0x20);
                        cfp_moveMotor(0, -m_power, 0x20);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        cfp_moveMotor(2, m_power, 0x00);
                        cfp_moveMotor(0, -m_power, 0x00);
                    }
                }

                return false;
            }
        });
        ImageButton cv_right = (ImageButton)findViewById(R.id.xv_right);
        cv_right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(motorSelected.equalsIgnoreCase("1")){
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        cfp_moveMotor(0, m_power, 0x20);
                        cfp_moveMotor(1, -m_power, 0x20);
                    }
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        cfp_moveMotor(0, m_power, 0x00);
                        cfp_moveMotor(1, -m_power, 0x00);
                    }
                }
                else if(motorSelected.equalsIgnoreCase("2")){
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        cfp_moveMotor(2, m_power, 0x20);
                        cfp_moveMotor(1, -m_power, 0x20);
                    }
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        cfp_moveMotor(2, m_power, 0x00);
                        cfp_moveMotor(1, -m_power, 0x00);
                    }
                }
                else{
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        cfp_moveMotor(0, m_power, 0x20);
                        cfp_moveMotor(2, -m_power, 0x20);
                    }
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        cfp_moveMotor(0, m_power, 0x00);
                        cfp_moveMotor(2, -m_power, 0x00);
                    }
                }

                return false;
            }
        });

        ImageButton cv_motorCtop = (ImageButton)findViewById(R.id.xv_MotorCUp);
        cv_motorCtop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    cfp_moveMotor(2, m_powerC, 0x20);
                    cfp_moveMotor(0, m_powerC, 0x20);
                    cfp_moveMotor(1, m_powerC, 0x20);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    cfp_moveMotor(2, m_powerC, 0x00);
                    cfp_moveMotor(0, m_powerC, 0x00);
                    cfp_moveMotor(1, m_powerC, 0x00);
                }
                return false;
            }
        });
        ImageButton cv_motorCdown = (ImageButton)findViewById(R.id.xv_MotorCDown);
        cv_motorCdown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    cfp_moveMotor(0, -m_powerC, 0x20);
                    cfp_moveMotor(1, -m_powerC, 0x20);
                    cfp_moveMotor(2, -m_powerC, 0x20);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    cfp_moveMotor(0, m_powerC, 0x00);
                    cfp_moveMotor(1, m_powerC, 0x00);
                    cfp_moveMotor(2, m_powerC, 0x00);
                }
                return false;
            }
        });
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

    private void cfp_moveMotor(int motor,int speed, int state) {
        try {
            byte[] buffer = new byte[15];

            buffer[0] = (byte) (15-2);			//length lsb
            buffer[1] = 0;						// length msb
            buffer[2] =  0;						// direct command (with response)
            buffer[3] = 0x04;					// set output state
            buffer[4] = (byte) motor;			// output 1 (motor B)
            buffer[5] = (byte) speed;			// power
            buffer[6] = 1 + 2;					// motor on + brake between PWM
            buffer[7] = 0;						// regulation
            buffer[8] = 0;						// turn ration??
            buffer[9] = (byte) state; //0x20;					// run state
            buffer[10] = 0;
            buffer[11] = 0;
            buffer[12] = 0;
            buffer[13] = 0;
            buffer[14] = 0;

            if(os == null){
                os = MainActivity.socket.getOutputStream();
            }
            os.write(buffer);
            os.flush();
        }
        catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void sliderChanged(CustomSlider slider,int newValue) {
        switch (slider.getId()) {
            case R.id.xv_seekBar:
                m_power = newValue;
                m_progress = (TextView) findViewById(R.id.xv_powertext);
                m_progress.setText("Power  " + newValue);
                break;
            case R.id.xv_MotorCseekBar:
                m_powerC = newValue;
                m_progressC = (TextView) findViewById(R.id.xv_MotorCpowertext);
                m_progressC.setText("Power  " + newValue);
                break;
        }
    }
}
