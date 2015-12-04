package com.example.will.robotcar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Will on 11/25/15.
 */
public class PollActivity extends AppCompatActivity {
    int RES_0 = R.drawable.nxt_distance_120;
    int RES_1 = R.drawable.nxt_light_120;
    int RES_2 = R.drawable.nxt_touch_120;
    int RES_3 = R.drawable.nxt_sound_120;
    int RES_456 = R.drawable.nxt_servo_120;

    List<SensorImageData> listOfSensorImageData;
    RobotImageCellAdapter cellAdapter;
    ListView imageCellListView;

    PollActivity pollActivity = this;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll);

        //init the sensorImageData
        listOfSensorImageData = new ArrayList<>();
        listOfSensorImageData.add(new SensorImageData("1", RES_0));
        listOfSensorImageData.add(new SensorImageData("2", RES_1));
        listOfSensorImageData.add(new SensorImageData("3", RES_2));
        listOfSensorImageData.add(new SensorImageData("4", RES_3));
        listOfSensorImageData.add(new SensorImageData("A", RES_456));
        listOfSensorImageData.add(new SensorImageData("B", RES_456));
        listOfSensorImageData.add(new SensorImageData("C", RES_456));

        cellAdapter = new RobotImageCellAdapter(this, R.layout.robot_image_cell, listOfSensorImageData);

        imageCellListView = (ListView)findViewById(R.id.robotImageListView);
        imageCellListView.setAdapter(cellAdapter);
        imageCellListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position < 4){
                    Intent intent = new Intent(pollActivity, PopupActivity.class);
                    intent.putExtra("index", position);
                    startActivityForResult(intent, 1984);
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        System.out.println("@@@: " + resultCode);
        if(data != null && data.getExtras() != null){
            Bundle extras = data.getExtras();
            int indexToReplace = extras.getInt("indexToReplace");
            int resource = extras.getInt("resource");
            System.out.println("indexToReplace@@@: " + indexToReplace);
            System.out.println("resource@@@: " + resource);
            String characterDescriptorOfItemToRemove = listOfSensorImageData.get(indexToReplace).getRowCharacter();
            listOfSensorImageData.remove(indexToReplace);
            listOfSensorImageData.add(indexToReplace, new SensorImageData(characterDescriptorOfItemToRemove, resource));
            cellAdapter.notifyDataSetChanged();
        }
    }
}
