package com.example.will.robotcar;

import android.app.Activity;
import android.os.Bundle;
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

/**
 * Created by Will on 11/6/15.
 */
public class DriveActivity extends Activity implements View.OnClickListener,View.OnTouchListener, SliderChanged{

    private OutputStream os = MainActivity.getOutputStream();
    private InputStream is = MainActivity.getInputStream();
    //private SeekBar m_powerSeek;
    //private SeekBar m_powerSeekC;
    CustomSlider powerSeek;
    CustomSlider motorSeek;
    private TextView m_progress;
    private TextView m_progressC;
    private int m_power = 50;
    private int m_powerC = 50;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drive);
        /*
        m_powerSeek = (SeekBar) findViewById(R.id.xv_seekBar);
        m_powerSeek.setOnSeekBarChangeListener(this);
        m_powerSeek.setProgress(50);

        m_powerSeekC = (SeekBar) findViewById(R.id.xv_MotorCseekBar);
        m_powerSeekC.setOnSeekBarChangeListener(this);
        m_powerSeekC.setProgress(50);
        */

        powerSeek = (CustomSlider) findViewById(R.id.xv_seekBar);
        motorSeek = (CustomSlider) findViewById(R.id.xv_MotorCseekBar);
        powerSeek.setOnSliderChangedListener(this);
        motorSeek.setOnSliderChangedListener(this);

        m_progress = (TextView) findViewById(R.id.xv_powertext);
        m_progressC = (TextView) findViewById(R.id.xv_MotorCpowertext);

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
