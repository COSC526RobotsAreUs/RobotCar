package com.example.will.robotcar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.OutputStream;

/**
 * Created by Will on 11/6/15.
 */
public class DriveActivity extends Activity implements View.OnClickListener{

    private OutputStream os = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drive);

        ImageButton top = (ImageButton)findViewById(R.id.top);
        top.setOnClickListener(this);
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
            //cv_label.setText("Error in MoveForward(" + e.getMessage() + ")");
        }
    }

    @Override
    public void onClick(View v) {
        ImageButton top = (ImageButton)findViewById(R.id.top);
        if(v == top){
            cfp_moveMotor(0, 75, 0x20);
        }
    }
}
