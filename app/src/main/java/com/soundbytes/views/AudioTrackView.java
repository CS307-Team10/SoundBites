package com.soundbytes.views;

import android.content.Context;
import android.content.res.Resources;
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

import com.soundbytes.AudioTrackController;
import com.soundbytes.R;

/**
 * Created by Olumide on 10/5/2015.
 */
public class AudioTrackView extends RelativeLayout {
    private ImageButton playButton;
    private int trackId = -1;
    private AudioTrackMeterView meter;
    private AudioTrackController controller;
    private GestureDetector mDetector;
    private float previousX = 0;
    private float previousY = 0;

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
    }

    public void setHeightInDP(int dp){
        Resources r = getResources();
        int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, px);
        setLayoutParams(params);
        invalidate();
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
    /**
     * This method decides what to do when the view is touched,
     * whether to allow the view pager intercept it of whether to let the scrollview intercept it
     */
    public boolean onTouchEvent(MotionEvent event){
        boolean result = mDetector.onTouchEvent(event);
        if(!result) {
            result = super.onTouchEvent(event);
        }
        if((event.getAction() == MotionEvent.ACTION_UP) || (event.getAction() == MotionEvent.ACTION_CANCEL))
            getParent().getParent().requestDisallowInterceptTouchEvent(false);
        else if(event.getAction() == MotionEvent.ACTION_MOVE){
            Log.v("touch", "move");
            float x = event.getX();
            float y = event.getY();
            if(Math.abs(x - previousX) > 4*Math.abs(y - previousY))
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
            previousX = x;
            previousY = y;
        }
        return result;
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
            return false;
        }
    }
}
