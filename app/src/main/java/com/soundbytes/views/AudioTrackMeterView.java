package com.soundbytes.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.soundbytes.SoundByteConstants;

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

    /**
     * Constructor
     * @param c the activity context
     */
    public AudioTrackMeterView(Context c){
        super(c);
        init();
    }

    /**
     * Constructor
     * @param c the activity context
     * @param attributeSet stuff
     */
    public AudioTrackMeterView(Context c, AttributeSet attributeSet){
        super(c, attributeSet);
        init();
    }

    /**
     * Constructor
     * @param context the activity context
     * @param attr stuff
     * @param defStyle even more stuff
     */
    public AudioTrackMeterView(Context context, AttributeSet attr, int defStyle){
        super(context, attr, defStyle);
        init();
    }

    /**
     * Convenience method used to avoid repeating code in the constructors
     */
    private void init(){
        amplitudeList = new LinkedList<>();
        paint = new Paint();
    }


    @Override
    /*
     * This method draws the wavy line on the AudioTrack view
     */
    public void onDraw(Canvas canvas){
        //FYI it updates every 10ms
        super.onDraw(canvas);
        if(amplitudeList.size() == 0)
            return;
        Iterator<Point> it = amplitudeList.iterator();
        Point previousPoint = it.next();
        Point currentPoint;
        //while there are points
        while(it.hasNext()){
            currentPoint = it.next();
            //draw line from previous point to current point
            canvas.drawLine(previousPoint.x*stepSize, previousPoint.y,
                    currentPoint.x*stepSize, currentPoint.y, paint);
        }
    }

    @Override
    /*
     * THis method is called when the size of the screen changes for any reason
     */
    public void onSizeChanged (int w, int h, int oldw, int oldh){
        super.onSizeChanged(w,h,oldw,oldh);
        float timeInc = 0.01f; //As in updated every 10ms
        stepSize = (int)((w - getPaddingRight() - getPaddingLeft())/(SoundByteConstants.TIME_LIMIT/timeInc));
        viewHeight = h - getPaddingBottom() - getPaddingTop() - 8;//8 just cause
    }

    /**
     * This methid stores the relevan info needed by the onDraw method in order
     * to draw the wavy visualization
     * @param amplitude the maximum amplitude of the recording
     *                  since the last time this method was called
     */
    public void updateRecordPreview(int amplitude){
        //scale amplitude to view height - padding
        // According to http://stackoverflow.com/a/13683201/2057884 the max is Short.MAX_VALUE
        int scaledAmplitude = (amplitude * viewHeight)/ Short.MAX_VALUE;

        final int step = 1; //Just make it one then multiply in onDraw since it can change
        int xValue = 0;
        if(amplitudeList.size() != 0){
            xValue = amplitudeList.getLast().x + step;
        }
        //add the point to the list
        amplitudeList.add(new Point(xValue, scaledAmplitude));
        invalidate();
    }
}
