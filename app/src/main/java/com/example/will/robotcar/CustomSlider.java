package com.example.will.robotcar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Will on 11/25/15.
 */
public class CustomSlider extends View{
    float width;
    float height;
    int percent = 75;
    Paint goldPaint;
    Paint greyPaint;
    Paint iconPaint;
    float iconHalfWidth = (float)20.0;
    SliderChanged listener= null;

    public CustomSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        goldPaint = new Paint();
        goldPaint.setAntiAlias(true);
        goldPaint.setStrokeWidth((float) 15);
        goldPaint.setStyle(Paint.Style.FILL);
        goldPaint.setColor(Color.parseColor("#efc03e")); //gold

        greyPaint = new Paint();
        greyPaint.setAntiAlias(true);
        greyPaint.setStrokeWidth((float) 15);
        greyPaint.setStyle(Paint.Style.FILL);
        greyPaint.setColor(Color.parseColor("#c1bd9c")); //grey

        iconPaint = new Paint();
        iconPaint.setAntiAlias(true);
        iconPaint.setStrokeWidth((float) 15);
        iconPaint.setStyle(Paint.Style.FILL);
        iconPaint.setColor(Color.parseColor("#1fd3af")); //blueish

        width = this.getWidth();
        height = this.getHeight();
    }

    public void setOnSliderChangedListener(SliderChanged theListener){
        listener = theListener;
    }


    @Override
    protected void onDraw(Canvas canvas){
        width = this.getWidth();
        height = this.getHeight();
        float horizontalPointOfSeparation = (float)(width * (percent / 100.0));

        canvas.drawRect((float)0.0, (float)0.0, horizontalPointOfSeparation, height, goldPaint);
        canvas.drawRect(horizontalPointOfSeparation, (float) 0.0, width, height, greyPaint);
        canvas.drawRect(horizontalPointOfSeparation - iconHalfWidth, (float) 0.0,
                horizontalPointOfSeparation + iconHalfWidth, height, iconPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX();
        if (x >= 0 && x <= width){
            percent = (int)(event.getX() * 100.0 / width);
            this.invalidate();
        }
        listener.sliderChanged(this, percent);
        return true;
    }
}
