package com.soundbytes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Olumide on 10/4/2015.
 */
public class AudioTrackMeterView extends LinearLayout{
    private LinkedList<Point> amplitudeList;
    private Paint paint;
    private int stepSize = 0;
    private int viewHeight = 0;

    public AudioTrackMeterView(Context c){
        super(c);
        init();
    }

    public AudioTrackMeterView(Context c, AttributeSet attributeSet){
        super(c, attributeSet);
        init();
    }

    public AudioTrackMeterView(Context context, AttributeSet attr, int defStyle){
        super(context, attr, defStyle);
        init();
    }

    private void init(){
        amplitudeList = new LinkedList<>();
        paint = new Paint();
    }


    @Override
    public void onDraw(Canvas canvas){
        //FYI it updates every 10ms
        super.onDraw(canvas);
        if(amplitudeList.size() == 0)
            return;
        Iterator<Point> it = amplitudeList.iterator();
        Point previousPoint = it.next();
        Point currentPoint;
        while(it.hasNext()){
            currentPoint = it.next();
            //draw line from previous point to current point
            canvas.drawLine(previousPoint.x*stepSize, previousPoint.y,
                    currentPoint.x*stepSize, currentPoint.y, paint);
        }
    }

    @Override
    public void onSizeChanged (int w, int h, int oldw, int oldh){
        super.onSizeChanged(w,h,oldw,oldh);
        int maxLen = 6; //as in 6 seconds
        float timeInc = 0.01f; //As in updated every 10ms
        stepSize = (int)((w - getPaddingRight() - getPaddingLeft())/(maxLen/timeInc));
        viewHeight = h - getPaddingBottom() - getPaddingTop() - 8;//8 just cause
    }

    public void updateRecordPreview(int amplitude){
        //scale amplitude to view height - padding
        // According to http://stackoverflow.com/a/13683201/2057884 the max is Short.MAX_VALUE
        int scaledAmplitude = (amplitude * viewHeight)/ Short.MAX_VALUE;

        final int step = 1; //Just make it one then multiply in onDraw since it can change
        int xValue = 0;
        if(amplitudeList.size() != 0){
            xValue = amplitudeList.getLast().x + step;
        }
        amplitudeList.add(new Point(xValue, scaledAmplitude));
        invalidate();
    }
}
