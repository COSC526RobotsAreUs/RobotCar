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



    private float mLastX, mLastY, mLastZ;

    private long lastUpdate = 0;
    private static final int SHAKE_THRESHOLD = 600;

    private boolean mInitialized;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private final float NOISE = (float) 2.0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accelometerdrive);

        mInitialized = false;

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
        ImageView iv = (ImageView)findViewById(R.id.image);

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 5000) {

                System.out.println("Initial"+curTime+"curTime - lastUpdate"+(curTime - lastUpdate));
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                float deltaX = Math.abs(mLastX - x);
                float deltaY = Math.abs(mLastY - y);
                float deltaZ = Math.abs(mLastZ - z);
                System.out.println("xyz"+deltaX+"   "+deltaY+ "  " +deltaZ);
                tvX.setText(Float.toString(deltaX));
                tvY.setText(Float.toString(deltaY));
                tvZ.setText(Float.toString(deltaZ));

                iv.setVisibility(View.VISIBLE);

                if (deltaX > deltaY) {

                    iv.setImageResource(R.drawable.horizontalshaker);
                }

                else if (deltaY > deltaX) {

                    iv.setImageResource(R.drawable.verticalshake);
                }

                else {
                    iv.setVisibility(View.INVISIBLE);
                }
            }






         /*  long curTime = System.currentTimeMillis();

            System.out.println("Initial"+curTime+"curTime - lastUpdate"+(curTime - lastUpdate));
            if ((curTime - lastUpdate) > 5000) {
                if (!mInitialized) {

                    mLastX = x;
                    mLastY = y;
                    mLastZ = z;

                    tvX.setText("0.0");
                    tvY.setText("0.0");
                    tvZ.setText("0.0");

                    mInitialized = true;

                    long diffTime = (curTime - lastUpdate);
                    lastUpdate = curTime;
                }



                else {

                    float deltaX = Math.abs(mLastX - x);
                    float deltaY = Math.abs(mLastY - y);
                    float deltaZ = Math.abs(mLastZ - z);

                    if (deltaX < NOISE) deltaX = (float) 0.0;
                    if (deltaY < NOISE) deltaY = (float) 0.0;
                    if (deltaZ < NOISE) deltaZ = (float) 0.0;

                    mLastX = x;
                    mLastY = y;
                    mLastZ = z;

                    tvX.setText(Float.toString(deltaX));
                    tvY.setText(Float.toString(deltaY));
                    tvZ.setText(Float.toString(deltaZ));

                    iv.setVisibility(View.VISIBLE);

                    if (deltaX > deltaY) {

                        iv.setImageResource(R.drawable.horizontalshaker);
                    }

                    else if (deltaY > deltaX) {

                        iv.setImageResource(R.drawable.verticalshake);
                    }

                    else {
                        iv.setVisibility(View.INVISIBLE);
                    }

                    long diffTime = (curTime - lastUpdate);
                    lastUpdate = curTime;
                }
            }*/

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
