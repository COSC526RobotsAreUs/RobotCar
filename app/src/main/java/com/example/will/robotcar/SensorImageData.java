package com.example.will.robotcar;

import android.widget.ImageView;

/**
 * Created by Will on 11/25/15.
 */
public class SensorImageData {
    private String rowCharacter;
    private int sensorImageResourse;

    public SensorImageData(String rowCharacter, int imageResourse){
        this.rowCharacter = rowCharacter;
        this.sensorImageResourse = imageResourse;
    }

    public String getRowCharacter() {
        return rowCharacter;
    }

    public int getSensorImageResourse() {
        return sensorImageResourse;
    }


}
