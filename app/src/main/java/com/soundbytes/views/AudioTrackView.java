package com.soundbytes.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.soundbytes.AudioTrackController;
import com.soundbytes.R;
import com.soundbytes.SoundByteConstants;

import java.io.File;
import java.util.Arrays;

/**
 * Created by Olumide on 10/5/2015.
 */
public class AudioTrackView extends RelativeLayout {
    private ImageButton playButton;
    private int trackId = -1;
    private AudioTrackMeterView meter;
    private AudioTrackController controller;
    private float previousX = 0;
    private float previousY = 0;
    private int currentFilter = 0;
    private GestureDetector mDetector;
    private Scroller scroller;
    protected float[] filterDists = {0f,0f};

    /**
     * Constructor
     * @param c the Activity context
     */
    public AudioTrackView(Context c){
        super(c);
        init();
    }

    /**
     * Constructor
     * @param c Activity context
     * @param attributeSet stuff
     */
    public AudioTrackView(Context c, AttributeSet attributeSet){
        super(c, attributeSet);
        init();
    }

    /**
     * Constructor
     * @param context the activity context
     * @param attr stuff
     * @param defStyle more stuff
     */
    public AudioTrackView(Context context, AttributeSet attr, int defStyle){
        super(context, attr, defStyle);
        init();
    }

    /**
     * Initialization function to avoid repeating stuff in the constructors
     */
    private void init(){
        //Get the inflater and inflate the layout
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_audio_track, this);

        //Find important views in the layout
        playButton = (ImageButton)view.findViewById(R.id.play_pause_button);
        meter = (AudioTrackMeterView)view.findViewById(R.id.track_meter_view);

        //attach playButton onClickListener
        playButton.setOnClickListener(createPlayOnClickListener());
        playButton.setTag(true);

        //initialize the gesture detector object
        mDetector = new GestureDetector(getContext(), new GestureListener());
        scroller = new Scroller(getContext());
    }

    public void setHeightInDP(int dp){
        Resources r = getResources();
        int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, px);
        setLayoutParams(params);
        invalidate();
    }

    public void disableSwipe(boolean disable){
        meter.disableSwipe(disable);
    }

    public boolean isSwipeDisabled(){
        return meter.isSwipeDisabled();
    }

    /**
     * This funcion is called to register the controller callback that is called
     * when actions are performed on the track
     * @param controller the controller that is being registeres
     * @param trackId the trackId for this track, it is usually assigned for the first time here
     */
    public void registerController(AudioTrackController controller, int trackId){
        this.controller = controller;
        this.trackId = trackId;
    }

    /**
     * This is the untested function that should display the wavy line thingy on the audioTackView
     * @param amplitude this is the maximum amplitude of the recorded audio since
     *                  the last time this method was called
     */
    public void updateRecordPreview(int amplitude) {
        meter.updateRecordPreview(amplitude);
    }

    public void autoUpdateRecordPreview(File soundFile) {

    }

    private boolean isPlayButton(){
        return (Boolean)playButton.getTag();
    }

    private void playButtonClicked(){
        if(controller == null){
            throw new NullPointerException("Audio controller not set");
        }
        if(isPlayButton()){
            controller.playTrack(trackId);
            playButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle_outline_black_48dp));
            playButton.setTag(false);
        }else{
            controller.pauseTrack(trackId);
            playButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_circle_outline_black_48dp));
            playButton.setTag(true);
        }
    }

    public void resetPlayButton(){
        playButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_circle_outline_black_48dp));
        playButton.setTag(true);
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Log.v("onDraw", "drawing");
    }

    @Override
    public boolean onInterceptTouchEvent (MotionEvent ev){
        return false;
    }

//    @Override
//    /**
//     * This method decides what to do when the view is touched,
//     * whether to allow the view pager intercept it of whether to let the scrollview intercept it
//     */
    public boolean onTouchEvent(MotionEvent event){
        boolean result = mDetector.onTouchEvent(event);
        if(!result) {
            result = super.onTouchEvent(event);
        }
        if((event.getAction() == MotionEvent.ACTION_UP) || (event.getAction() == MotionEvent.ACTION_CANCEL)) {
            getParent().getParent().requestDisallowInterceptTouchEvent(false);
            resetFilterDists();
        }
        else if(event.getAction() == MotionEvent.ACTION_MOVE){
    //            Log.v("touch", "move");
            float x = event.getX();
            float y = event.getY();
            if(Math.abs(x - previousX) > 4*Math.abs(y - previousY))
                getParent().getParent().requestDisallowInterceptTouchEvent(!isSwipeDisabled());
            previousX = x;
            previousY = y;
        }
        return result;
    }


    public void resetFilterDists(){
        filterDists[0] = 0;
        filterDists[1] = 0;
    }

    public void setFilterDists(float x, float y){
        filterDists[0] = x;
        filterDists[1] = y;
    }

    public float[] getFilterDists(){
        return filterDists.clone();
    }

    public void increaseFilterDists(float x, float y){
        filterDists[0] += x;
        filterDists[1] += y;
    }


    private void nextFilter(){
        currentFilter++;
        currentFilter = currentFilter % SoundByteConstants.FILTER_NAME.length;
        controller.applyFilter(trackId, currentFilter);
    }

    private void previousFilter(){
        currentFilter--;
        while(currentFilter < 0)
            currentFilter += SoundByteConstants.FILTER_NAME.length;
        controller.applyFilter(trackId, currentFilter);
    }

    /**
     * This method deletes the track
     */
    public void delete(){
        if(controller == null){
            throw new NullPointerException("Audio controller not set");
        }
        controller.deleteTrack(this, trackId);
    }

    /**
     * Creating the listener for the play/pause button
     * @return the onClick listener
     */
    private OnClickListener createPlayOnClickListener(){
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioTrackView.this.playButtonClicked();
            }
        };
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
            Log.v("Gesture", "Fling " + velocityX + " " + velocityY);
            scroller.fling((int) getFilterDists()[0], (int) getFilterDists()[1], (int)(10*Math.signum(velocityX)), 0,
                    -meter.getWidth(), meter.getWidth(), 0, 0);

            //decide where the scroller snaps to
            if(velocityX < 0) {
                scroller.setFinalX(-meter.getWidth());
            }else if(velocityX > 0) {
                scroller.setFinalX(meter.getWidth());
            }else{
                if(getFilterDists()[0] < 0)
                    scroller.setFinalX(-meter.getWidth());
                else
                    scroller.setFinalX(meter.getWidth());
            }
            //calculate time needed
            int time = Math.abs(scroller.getFinalX() - 4*scroller.getCurrX())/10;
            scroller.extendDuration(time - scroller.getDuration() > 0? time - scroller.getDuration(): 0);
            scroller.computeScrollOffset();

            //create a handler that updates the position of the filter text layer
            meter.invalidate(scroller, currentFilter, filterDists);
            final Handler updateHandler = new Handler();
            updateHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.v("Handler", "run "+scroller.isFinished());
                    if (!scroller.isFinished()) {
                        //stuff used in the visualization
                        scroller.computeScrollOffset();
                        setFilterDists(scroller.getCurrX(), scroller.getCurrY());
                        meter.invalidate(scroller, currentFilter, filterDists);
                        updateHandler.postDelayed(this, 10);
                    } else {
                        if (scroller.getFinalX() <= 0)
                            nextFilter();
                        else
                            previousFilter();
                        meter.invalidate(scroller, currentFilter, filterDists);
                        resetFilterDists();
                        scroller.setFinalX(0);
                        scroller.setFinalY(0);
                        scroller.forceFinished(true);
                        removeCallbacks(null);
                    }
                }
            }, 10);
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY){
            //"FYI This doesn't always calls on fling when the finger gets lifted"
            AudioTrackView.this.cancelLongPress();
//            increaseFilterDists(-distanceX, 0);
//            meter.invalidate(scroller, currentFilter, filterDists);
            return true;
        }
    }
}
