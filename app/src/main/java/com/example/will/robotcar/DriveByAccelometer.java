package com.example.will.robotcar;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by rshinde on 12/4/15.
 */
public class DriveByAccelometer extends AppCompatActivity  implements SensorEventListener {

    private long lastUpdate = 0;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private MotorMover motorMover;
    private boolean hasMovedSinceLastStopSignal = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accelometerdrive);

        motorMover = new MotorMover(MainActivity.getOutputStream());


        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor mySensor = event.sensor;
        TextView tvX= (TextView)findViewById(R.id.x_axis);
        TextView tvY= (TextView)findViewById(R.id.y_axis);
        TextView tvZ= (TextView)findViewById(R.id.z_axis);

        double THRESH = 2.0;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 500) {

                System.out.println("Initial"+curTime+"curTime - lastUpdate"+(curTime - lastUpdate));
                lastUpdate = curTime;
                System.out.println("xyz" + x + "   " + y + "  " + z);
                tvX.setText(Float.toString(x));
                tvY.setText(Float.toString(y));
                tvZ.setText(Float.toString(z));

                boolean isXBetweenThresholds = (x > -THRESH && x < THRESH);
                boolean isYBetweenThresholds = (y > -THRESH && y < THRESH);

                if (x > -THRESH && x < THRESH && y > -THRESH && y < THRESH){//at rest (i.e. no movement)
                    if (hasMovedSinceLastStopSignal){
                        motorMover.stop();
                        hasMovedSinceLastStopSignal = false;
                    }
                }else if (y < -THRESH && isXBetweenThresholds){//move forward
                    System.out.println("FORWARD");
                    motorMover.moveForward();
                    hasMovedSinceLastStopSignal = true;
                }else if (y > THRESH && isXBetweenThresholds){//move backward
                    System.out.println("BACK");
                    motorMover.moveBack();
                    hasMovedSinceLastStopSignal = true;
                }else if (x < -THRESH && isYBetweenThresholds){//move right
                    System.out.println("RIGHT");
                    motorMover.moveRight();
                    hasMovedSinceLastStopSignal = true;
                }else if (x > THRESH && isYBetweenThresholds){//move left
                    System.out.println("LEFT");
                    motorMover.moveLeft();
                    hasMovedSinceLastStopSignal = true;
                }
            }

        }

    }//onSensorChange

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }


    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
