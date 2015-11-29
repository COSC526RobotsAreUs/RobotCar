package com.example.will.robotcar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Will on 11/27/15.
 */
public class RobotImageCellAdapter extends ArrayAdapter<SensorImageData> {

    private Context context;
    private int resourse;
    private List<SensorImageData> objects;
    public RobotImageCellAdapter(Context context, int resource, List<SensorImageData> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourse = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position,
                View convertView,
                ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View row = inflater.inflate(resourse, parent, false);

        TextView rowCharacterView = (TextView)row.findViewById(R.id.rowCharacter);
        ImageView sensorImage = (ImageView)row.findViewById(R.id.sensorImage);

        rowCharacterView.setText(objects.get(position).getRowCharacter());
        sensorImage.setImageResource(objects.get(position).getSensorImageResourse());

        return row;
    }
}










