package com.soundbytes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageButton;

import android.os.Handler;

/**
 * Created by Olumide on 10/7/2015.
 */
public class RecordButton extends ImageButton {
    private Paint paint;
    private RectF rect;
    private Handler updateHandler;
    private Handler stopHandler;
    private GestureDetector mDetector;
    private boolean isRecording = false;
    private float angleSweep = 0;
    private long startTime;
    private RecordButtonListeners recordListener;

    public RecordButton(Context context){
        super(context);
        init();
    }

    public RecordButton(Context context, AttributeSet attr){
        super(context, attr);
        init();
    }

    public RecordButton(Context context, AttributeSet attr, int defStyle){
        super(context, attr, defStyle);
    }

    public void setRecordListener(RecordButtonListeners listener){
        recordListener = listener;
    }
    private void init(){
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setColor(Color.parseColor("#df181f"));
        mDetector = new GestureDetector(getContext(), new GestureListener());
    }

    private void initializeHandlers(){
        cleanUpHandlers();
        startTime = System.currentTimeMillis();
        isRecording = true;
        updateHandler = new Handler();
        updateHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isRecording()) {
                    angleSweep = ((System.currentTimeMillis() - startTime)*360f)/SoundByteConstants.TIME_LIMIT;
                    updateHandler.postDelayed(this, 10);
                }else
                    angleSweep = 0;
                invalidate();
            }
        }, 10);
        stopHandler = new Handler();
        stopHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cleanUpHandlers();
            }
        }, SoundByteConstants.TIME_LIMIT);
    }

    public boolean isRecording(){
        return isRecording;
    }

    private void cleanUpHandlers(){
        if(isRecording())
            //TODO stop recording
            isRecording = false;
        try {
            updateHandler.removeCallbacks(null);
            stopHandler.removeCallbacks(null);
        }catch (NullPointerException e){
            //Do Nothing
        }
        angleSweep = 0;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        boolean result = mDetector.onTouchEvent(event);
        if(!result) {
            result = super.onTouchEvent(event);
        }
        if((!isPressed() || (event.getAction() == MotionEvent.ACTION_UP)) && isRecording()) {
            Log.v("Gesture", "stop " + event.getAction());
            isRecording = false;
            cleanUpHandlers();
            recordListener.onStopRecording();
        }
        return result;
    }

    @Override
    public void onDraw(Canvas canvas){
        if(rect == null)
            rect = new RectF(10,10, getWidth()-10, getHeight()-10);
        super.onDraw(canvas);
        canvas.drawArc(rect, 270, angleSweep, false, paint);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap (MotionEvent event){
            Log.v("Gesture", "doubleTap");
            return true;
        }

        @Override
        public void onLongPress (MotionEvent event){
            Log.v("Gesture", "longPress");
            recordListener.onStartRecording();
            isRecording = true;
            initializeHandlers();
        }

        @Override
        public boolean onSingleTapUp (MotionEvent event){
            Log.v("Gesture", "SingleTapUp");
            return false;
        }
    }
}
