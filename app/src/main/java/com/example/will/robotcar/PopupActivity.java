package com.example.will.robotcar;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Will on 11/27/15.
 */
public class PopupActivity extends AppCompatActivity implements View.OnClickListener{

    View row1;
    View row2;
    View row3;
    View row4;

    PopupActivity popupActivity = this;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);

        row1 = (View)findViewById(R.id.first);
        ImageView iv1 = (ImageView)row1.findViewById(R.id.robotImageViewInPopupIntent);
        iv1.setImageResource(R.drawable.nxt_light_120);
        TextView tv1 = (TextView)row1.findViewById(R.id.robotImageNameInPopupIntent);
        tv1.setText("Light Sensor");


        row2 = (View)findViewById(R.id.second);
        ImageView iv2 = (ImageView)row2.findViewById(R.id.robotImageViewInPopupIntent);
        iv2.setImageResource(R.drawable.nxt_sound_120);
        TextView tv2 = (TextView)row2.findViewById(R.id.robotImageNameInPopupIntent);
        tv2.setText("Sound Sensor");

        row3 = (View)findViewById(R.id.third);
        ImageView iv3 = (ImageView)row3.findViewById(R.id.robotImageViewInPopupIntent);
        iv3.setImageResource(R.drawable.nxt_touch_120);
        TextView tv3 = (TextView)row3.findViewById(R.id.robotImageNameInPopupIntent);
        tv3.setText("Touch Sensor");

        row4 = (View)findViewById(R.id.fourth);
        ImageView iv4 = (ImageView)row4.findViewById(R.id.robotImageViewInPopupIntent);
        iv4.setImageResource(R.drawable.nxt_distance_120);
        TextView tv4 = (TextView)row4.findViewById(R.id.robotImageNameInPopupIntent);
        tv4.setText("Distance Sensor");

        //set onClickListener for views
        row1.setOnClickListener(this);
        row2.setOnClickListener(this);
        row3.setOnClickListener(this);
        row4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int resource = R.drawable.nxt_light_120;//just in case
        if (v == row1){//light
            resource = R.drawable.nxt_light_120;
        }else if (v == row2){//sound
            resource = R.drawable.nxt_sound_120;
        }else if (v == row3){//touch
            resource = R.drawable.nxt_touch_120;
        }else if (v == row4){//distance
            resource = R.drawable.nxt_distance_120;
        }
        int indexToReplace = -1;
        if (getIntent() != null){
            Bundle extras = getIntent().getExtras();
            indexToReplace = extras.getInt("index");
        }

        Intent intent = new Intent(this, PollActivity.class);
        intent.putExtra("indexToReplace", indexToReplace);
        intent.putExtra("resource", resource);
        startActivity(intent);
    }
}
