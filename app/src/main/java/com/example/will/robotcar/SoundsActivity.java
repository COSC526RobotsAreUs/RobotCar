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

    private final String hot = "Hot";
    private final String cold = "Cold";
    private final String haveANiceDay = "Have A Nice Day";
    private final String goodMorning = "Goodmorning";
    private final String thankYou = "Thank You";
    private final String goodBye = "Goodbye";
    private final String woops = "Woops";

    private OutputStream os = MainActivity.getOutputStream();
    Button hotBtn;
    Button coldBtn;
    Button haveANiceDayBtn;
    Button goodMorningBtn;
    Button thankYouBtn;
    Button goodByeBtn;
    Button woopsBtn;

    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Sounds Scene Started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sounds);
        hotBtn = (Button)findViewById(R.id.hot);
        hotBtn.setOnClickListener(this);
        coldBtn = (Button)findViewById(R.id.cold);
        coldBtn.setOnClickListener(this);
        haveANiceDayBtn = (Button)findViewById(R.id.haveANiceDay);
        haveANiceDayBtn.setOnClickListener(this);
        goodMorningBtn = (Button)findViewById(R.id.goodMorning);
        goodMorningBtn.setOnClickListener(this);
        thankYouBtn = (Button)findViewById(R.id.thankYou);
        thankYouBtn.setOnClickListener(this);
        goodByeBtn = (Button)findViewById(R.id.goodBye);
        goodByeBtn.setOnClickListener(this);
        woopsBtn = (Button)findViewById(R.id.woops);
        woopsBtn.setOnClickListener(this);
    }

    private void playSoundFile(String fileName){
        try{
            final int FIST_PART_LEN = 5;
            final int RSO_EXTENSION_LEN = 4;
            final int FILE_NAME_LEN = fileName.length();
            final int NULL_TERMINATING_CHAR_LEN = 1;
            final int FULL_LEN = FIST_PART_LEN + FILE_NAME_LEN +
                                RSO_EXTENSION_LEN + NULL_TERMINATING_CHAR_LEN;
            final String rso_extension = ".rso";
            byte[] buffer = new byte[FULL_LEN + 2];
            System.out.println("other len: " + FULL_LEN);
            buffer[0] = (byte) FULL_LEN; //message len lsb//
            buffer[1] = 0; //message len msb
            buffer[2] = (byte) 0x80; //no response
            buffer[3] = (byte) 0x02; //play sound file command
            buffer[4] = 0x0; //play only once (not in a loop)

            int index = 5;
            for(int i = 0; i < fileName.length(); i++){
                buffer[index] = (byte)fileName.charAt(i);
                index++;
            }
            for(int i = 0; i < rso_extension.length(); i++){
                buffer[index] = (byte)rso_extension.charAt(i);
                index++;
            }
            buffer[index] = 0;//null terminating character

            if (os == null) {
                os = MainActivity.socket.getOutputStream();
            }
            os.write(buffer);
            os.flush();
        }catch (Exception e) {
            System.out.println("could not play sound file");
        }
    }

    private void playHot(){
        try {
            String myHot = "Hot";
            byte[] buffer = new byte[15];


            buffer[0] = 15-2; //message len lsb//it was 15-2 for "Hot"
            //System.out.println("buffer[0] = " + (15 - 2));
            buffer[1] = 0; //message len msb
            buffer[2] = (byte) 0x80; //no response
            buffer[3] = (byte) 0x02; //play sound file command
            buffer[4] = 0x0; //play only once (not in a loop)
            buffer[5] = (byte) myHot.charAt(0); // filename starting here
            buffer[6] = (byte) myHot.charAt(1);
            buffer[7] = (byte) myHot.charAt(2);
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
        String fileName = null;
        if(v == hotBtn){
            fileName = hot;
        }else if (v == coldBtn){
            fileName = cold;
        }else if (v == haveANiceDayBtn){
            fileName = haveANiceDay;
        }else if (v == goodMorningBtn){
            fileName = goodMorning;
        }else if (v == thankYouBtn){
            fileName = thankYou;
        }else if (v == goodByeBtn){
            fileName = goodBye;
        }else if (v == woopsBtn){
            fileName = woops;
        }

        if (fileName != null){
            playSoundFile(fileName);
        }

    }
}
