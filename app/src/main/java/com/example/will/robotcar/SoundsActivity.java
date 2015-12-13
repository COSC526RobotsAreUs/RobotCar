package com.example.will.robotcar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.OutputStream;

/**
 * Created by Will on 12/12/15.
 */
public class SoundsActivity extends AppCompatActivity implements View.OnClickListener{

    private OutputStream os = MainActivity.getOutputStream();
    Button soundButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sounds);
        soundButton = (Button)findViewById(R.id.soundButton);
        soundButton.setOnClickListener(this);
    }

    private void playSoundFile(String fileName){
        try {
            byte[] buffer = new byte[15];

            buffer[0] = 0; //message len lsb
            buffer[1] = 0; //message len msb
            buffer[2] = (byte) 0x80; //no response
            buffer[3] = (byte) 0x02; //play sound file command
            buffer[4] = 0x0; //play only once (not in a loop)
            buffer[5] = (byte) fileName.charAt(0); // filename starting here
            buffer[6] = (byte) fileName.charAt(1);
            buffer[7] = (byte) fileName.charAt(2);
            buffer[8] = (byte) '.';//start of .rso
            buffer[9] = (byte) 'r';
            buffer[10] = (byte) 's';
            buffer[11] = (byte) 'o';
            buffer[12] = 0;
            buffer[13] = 0;
            buffer[14] = 0;

            if (os == null) {
                os = MainActivity.socket.getOutputStream();
            }
            os.write(buffer);
            os.flush();
        }catch (Exception e) {
            System.out.println("could not play sound file");
        }
    }

    @Override
    public void onClick(View v) {
        if(v == soundButton){
            String soundFile = "Hot";
            playSoundFile(soundFile);
        }
    }
}
