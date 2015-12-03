package com.soundbytes.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Scroller;

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
    private boolean swipeDisabled = false;
    private int currentFilter = 0;
    private Scroller scroller = null;
    private Paint textPaint;
    protected float[] filterDists = {0f,0f};

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

    public void disableSwipe(boolean disable){
        this.swipeDisabled = disable;
    }

    public boolean isSwipeDisabled(){
        return swipeDisabled;
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

        textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(60f);
        textPaint.setAlpha(180);
        textPaint.setColor(Color.WHITE);
        textPaint.setFakeBoldText(true);
        textPaint.setShadowLayer(20f, 0, 0, Color.argb(0, 0, 0, 100));
    }

    public void invalidate(Scroller scroller, int currentFilter, float[] filterDists){
        this.scroller = scroller;
        this.currentFilter = currentFilter;
        this.filterDists = filterDists;
        invalidate();
    }

    @Override
    /*
     * This method draws the wavy line on the AudioTrack view
     */
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(amplitudeList.size() != 0) {
            Iterator<Point> it = amplitudeList.iterator();
            Point previousPoint = it.next();
            Point currentPoint;
            //while there are points
            while (it.hasNext()) {
                currentPoint = it.next();
                //draw line from previous point to current point
                canvas.drawLine(previousPoint.x * stepSize, previousPoint.y,
                        currentPoint.x * stepSize, currentPoint.y, paint);
            }
        }

        if(scroller == null && filterDists[0] == 0f){
            drawTextOffset(canvas, textPaint, SoundByteConstants.FILTER_NAME[currentFilter], 0, 0);
//            canvas.drawText(SoundByteConstants.FILTER_NAME[currentFilter], 0, getHeight()/2, textPaint);
        }else{
            //draw outgoing filter
            drawTextOffset(canvas, textPaint, SoundByteConstants.FILTER_NAME[currentFilter], filterDists[0], 0);
            //draw incoming filter
            if(filterDists[0] <= 0) {
                drawTextOffset(canvas, textPaint, SoundByteConstants.FILTER_NAME[
                                (currentFilter + 1) % SoundByteConstants.FILTER_NAME.length],
                        filterDists[0] + getWidth(), 0);
            }else {
                drawTextOffset(canvas, textPaint, SoundByteConstants.FILTER_NAME[
                        currentFilter == 0? currentFilter-1+SoundByteConstants.FILTER_NAME.length: currentFilter-1],
                        filterDists[0] - getWidth(), 0);
            }
        }
    }

    /**
     * Adapted from http://stackoverflow.com/a/32081250/2057884
     */
    protected void drawTextOffset(Canvas canvas, Paint paint, String text, float xOffset, float yOffset){
        int cHeight = canvas.getClipBounds().height();
        int cWidth = canvas.getClipBounds().width();
        Rect r = new Rect();
        paint.setTextAlign(Paint.Align.LEFT);
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text.toUpperCase(), x + xOffset, y + yOffset, paint);
    }

    @Override
    /*
     * THis method is called when the size of the screen changes for any reason
     */
    public void onSizeChanged (int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
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