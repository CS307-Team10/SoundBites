package com.soundbytes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaRecorder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import android.os.Handler;

import java.io.IOException;

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
    private MediaRecorder mRecorder = null;
    private String mFileName = null;
    private static final String LOG_TAG = "AudioRecordTest";

    boolean mStartRecording = true;

    public RecordButton(Context context){
        super(context);
        init();
        setOnClickListener(clicker);
    }

    public RecordButton(Context context, AttributeSet attr){
        super(context, attr);
        init();
        setOnClickListener(clicker);
    }

    public RecordButton(Context context, AttributeSet attr, int defStyle){
        super(context, attr, defStyle);
        setOnClickListener(clicker);
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

    public void SetMediaRecorder(MediaRecorder m)
    {
        mRecorder = m;
    }

    public void SetOutFileName(String fileName)
    {
        mFileName = fileName;
    }

    OnClickListener clicker = new OnClickListener() {
        @Override
        public void onClick(View v)
        {
            onRecord(mStartRecording);
            mStartRecording = !mStartRecording;
        }
    };

    private void onRecord(boolean start)
    {
        if(start)
            startRecording();
        else
            stopRecording();
    }

    private void startRecording()
    {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try{
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording()
    {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
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
