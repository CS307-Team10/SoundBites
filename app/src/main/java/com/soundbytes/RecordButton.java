package com.soundbytes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import android.os.Handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Joe on 10/7/2015.
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

    /**
     * Constructor
     * @param context the activity context
     */
    public RecordButton(Context context){
        super(context);
        init();
        setOnClickListener(clicker);

    }

    /**
     * Constructor
     * @param context the Activity Context
     * @param attr stuff
     */
    public RecordButton(Context context, AttributeSet attr){
        super(context, attr);
        init();
        setOnClickListener(clicker);

    }

    /**
     * Constructor
     * @param context the activity context
     * @param attr stuff
     * @param defStyle even more stuff
     */
    public RecordButton(Context context, AttributeSet attr, int defStyle){
        super(context, attr, defStyle);
        setOnClickListener(clicker);

    }

    /**
     * This function registers the recordListener object that implements the recordListener interface
     * the recordListener is called when a new audio starts recording or stops recording
     * @param listener the object that implements recordListener
     */
    public void setRecordListener(RecordButtonListeners listener){
        recordListener = listener;
    }

    private void init(){
        //Initialize the paint object
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setColor(Color.parseColor("#df181f"));

        //Initialize the gesture detector
        mDetector = new GestureDetector(getContext(), new GestureListener());
    }

    /**
     * This initializes two handlers, kind of like an alarm, one that stops the recording
     * when the time limit is reached, and the otehr that updated the visualization
     */

    private void initializeHandlers(){
        //Stop the previous handlers just in case they haven't been stopped yet
        cleanUpHandlers();
        //store the time the recording started, for updating the visualization
        startTime = System.currentTimeMillis();
        isRecording = true;

        //Creates the handler that updated the visualization
        updateHandler = new Handler();
        updateHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isRecording()) {
                    //stuff used in the visualization
                    angleSweep = ((System.currentTimeMillis() - startTime)*360f)/SoundByteConstants.TIME_LIMIT;

                    updateHandler.postDelayed(this, 10);
                } else
                    angleSweep = 0;
                invalidate();
            }
        }, 10);

        //Created the handler that stops the recording after the time limit is reached
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

    /**
     * This function stops the handlers and stops the recording if recording.
     * It also resets the visualization
     */
    private void cleanUpHandlers(){
        //Stop recording if still recording
        if(isRecording())
            recordListener.onStopRecording();
            isRecording = false;

        //Stop the handlers
        try {
            updateHandler.removeCallbacks(null);
            stopHandler.removeCallbacks(null);
        }catch (NullPointerException e){
            //Do Nothing
        }

        //reset the visualization
        angleSweep = 0;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        //call teh gesture detector and see if it consumed the touchEvent
        boolean result = mDetector.onTouchEvent(event);
        //if the touch event wasn't consumed passs it on to the super class
        if(!result) {
            result = super.onTouchEvent(event);
        }
        //Confirm that the record button is still pressed
        if((!isPressed() || (event.getAction() == MotionEvent.ACTION_UP)) && isRecording()) {
            //If record button is no longer pressed, stop the recording
            Log.v("Gesture", "stop " + event.getAction());
            isRecording = false;
            cleanUpHandlers();
            recordListener.onStopRecording();
        }
        return result;
    }

    @Override
    /**
     * Draws the red visualization arc
     */
    public void onDraw(Canvas canvas){
        if(rect == null)
            rect = new RectF(10,10, getWidth()-10, getHeight()-10);
        super.onDraw(canvas);
        canvas.drawArc(rect, 270, angleSweep, false, paint);
    }


    public void SetAudioRecorder(MediaRecorder m)
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
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
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

        //FilterManager fm = new FilterManager(mFileName);
        //fm.Speedup();

        // new code


    }



    //Class that detects gestures

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap (MotionEvent event){
            Log.v("Gesture", "doubleTap");
            return true;
        }

        @Override
        public void onLongPress (MotionEvent event){
            if(isEnabled()) {
                Log.v("Gesture", "longPress");
                recordListener.onStartRecording();
                isRecording = true;
                initializeHandlers();
            }
        }

        @Override
        public boolean onSingleTapUp (MotionEvent event){
            Log.v("Gesture", "SingleTapUp");
            return false;
        }
    }
}
