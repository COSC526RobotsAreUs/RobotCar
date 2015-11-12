package com.example.will.robotcar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Will on 11/11/15.
 */
public class BatteryLevelView extends View{
    int percent = 0;

    public BatteryLevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPercent(int percent){
        this.percent = percent >= 0 && percent <= 100 ? percent : 0;
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas){
        Paint redPaint = new Paint();
        redPaint.setAntiAlias(true);
        redPaint.setStrokeWidth((float) 15);
        redPaint.setStyle(Paint.Style.FILL);
        redPaint.setColor(Color.parseColor("#7E7E7E"));

        float left = this.getLeft();
        float top = this.getTop();
        float right = this.getRight();
        float bottom = this.getBottom();
        System.out.println("left" + left + ", right:" + right);
        System.out.println("top" + top + ", bottom:" + bottom);
        canvas.drawRect((float)0.0, (float)0.0, this.getWidth(), this.getHeight(), redPaint);

        Paint greenPaint = new Paint();
        greenPaint.setAntiAlias(true);
        greenPaint.setColor(Color.parseColor("#1FBC1A"));
        greenPaint.setStrokeWidth((float) 15);
        greenPaint.setStyle(Paint.Style.FILL);
        float widthOfView = this.getWidth();
        float decimalPercent = (float)(percent / 100.0);
        float widthOfGreenBatteryLevel = widthOfView * decimalPercent;
        canvas.drawRect((float) 0.0, (float) 0.0, widthOfGreenBatteryLevel, this.getHeight(), greenPaint);

    }
}
