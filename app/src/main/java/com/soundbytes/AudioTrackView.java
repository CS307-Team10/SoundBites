package com.soundbytes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * Created by Olumide on 10/5/2015.
 */
public class AudioTrackView extends LinearLayout {
    private ImageButton playButton;
    private int trackId = -1;
    private AudioTrackMeterView meter;
    private AudioTrackController controller;

    public AudioTrackView(Context c){
        super(c);
        init();
    }

    public AudioTrackView(Context c, AttributeSet attributeSet){
        super(c, attributeSet);
        init();
    }

    public AudioTrackView(Context context, AttributeSet attr, int defStyle){
        super(context, attr, defStyle);
        init();
    }

    private void init(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_audio_track, this);
        playButton = (ImageButton)view.findViewById(R.id.play_pause_button);
        meter = (AudioTrackMeterView)view.findViewById(R.id.track_meter_view);
        //attach playButton onClickListener
        playButton.setOnClickListener(createPlayOnClickListener());
        playButton.setTag(true);
    }

    public void registerController(AudioTrackController controller, int trackId){
        this.controller = controller;
        this.trackId = trackId;
    }

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

    private OnClickListener createPlayOnClickListener(){
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioTrackView.this.playButtonClicked();
            }
        };
    }
}
