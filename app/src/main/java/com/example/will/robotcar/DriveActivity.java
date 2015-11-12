package com.example.will.robotcar;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Will on 11/6/15.
 */
public class DriveActivity extends Activity implements View.OnClickListener,View.OnTouchListener, SeekBar.OnSeekBarChangeListener{

    private OutputStream os = null;
    private InputStream is = null;
    private SeekBar m_powerSeek;
    private SeekBar m_powerSeekC;
    private TextView m_progress;
    private TextView m_progressC;
    private int m_power = 50;
    private int m_powerC = 50;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drive);

        m_powerSeek = (SeekBar) findViewById(R.id.xv_seekBar);
        m_powerSeek.setOnSeekBarChangeListener(this);
        m_powerSeek.setProgress(50);

        m_powerSeekC = (SeekBar) findViewById(R.id.xv_MotorCseekBar);
        m_powerSeekC.setOnSeekBarChangeListener(this);
        m_powerSeekC.setProgress(50);

        m_progress = (TextView) findViewById(R.id.xv_powertext);
        m_progressC = (TextView) findViewById(R.id.xv_MotorCpowertext);

        //int power = cfp_BatteryPower();
        //System.out.println("powerrrr" + power);

        ImageButton top = (ImageButton)findViewById(R.id.top);
        top.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    cfp_moveMotor(0, m_power, 0x20);
                    cfp_moveMotor(1, m_power, 0x20);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    cfp_moveMotor(0, m_power, 0x00);
                    cfp_moveMotor(1, m_power, 0x00);
                }
                return false;
            }
        });
        ImageButton cv_down = (ImageButton)findViewById(R.id.xv_down);
        cv_down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    cfp_moveMotor(0, -m_power, 0x20);
                    cfp_moveMotor(1, -m_power, 0x20);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    cfp_moveMotor(0, m_power, 0x00);
                    cfp_moveMotor(1, m_power, 0x00);
                }
                return false;
            }
        });
        ImageButton cv_left = (ImageButton)findViewById(R.id.xv_left);
        cv_left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    cfp_moveMotor(1, m_power, 0x20);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    cfp_moveMotor(1, m_power, 0x00);
                }
                return false;
            }
        });
        ImageButton cv_right = (ImageButton)findViewById(R.id.xv_right);
        cv_right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    cfp_moveMotor(0, m_power, 0x20);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    cfp_moveMotor(0, m_power, 0x00);
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
    /*
    private int cfp_BatteryPower() {
        try {
            byte[] buffer = new byte[15];

            buffer[0] = (byte) (15-2);			//length lsb
            buffer[1] = 0;						// length msb
            buffer[2] =  0x00;					// direct command (with response)
            buffer[3] = 0x0B;					// set output state
            buffer[4] = 0;          			// output 1 (motor B)
            buffer[5] = 0;			            // power
            buffer[6] = 0;					    // motor on + brake between PWM
            buffer[7] = 0;						// regulation
            buffer[8] = 0;						// turn ration??
            buffer[9] = 0;  					// run state
            buffer[10] = 0;
            buffer[11] = 0;
            buffer[12] = 0;
            buffer[13] = 0;
            buffer[14] = 0;

            if(os == null){
                os = MainActivity.socket.getOutputStream();
            }
            if(is == null){
                is = MainActivity.socket.getInputStream();
            }
            os.write(buffer);
            os.flush();

            byte[] mv_batteryresponse = new byte[7];
            int batteryResponse = is.read(mv_batteryresponse);
            System.out.println("batteryResponse"+batteryResponse);
            for(int i=0;i<7;i++)
                System.out.printf("0x%02X \n",mv_batteryresponse[i]);
            int batteryPower = ((mv_batteryresponse[6]<<8) & 0x0000ff00) | (mv_batteryresponse[5] & 0x000000ff);
            System.out.println(batteryPower);
            double m_double = (double) batteryPower;
            System.out.println(m_double);
            return ((int) ((m_double/9000) * 100));
        }
        catch (Exception e) {
            return -1;
        }
    }
    */
    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.xv_seekBar:
                m_power = progress;
                m_progress = (TextView) findViewById(R.id.xv_powertext);
                m_progress.setText("POWER  " + progress);
                break;
            case R.id.xv_MotorCseekBar:
                m_powerC = progress;
                m_progressC = (TextView) findViewById(R.id.xv_MotorCpowertext);
                m_progressC.setText("POWER  " + progress);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
