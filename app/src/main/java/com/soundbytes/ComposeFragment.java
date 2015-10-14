package com.soundbytes;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Olumide on 10/3/2015.
 */
public class ComposeFragment extends TitledFragment implements RecordButtonListeners, AudioTrackController{
    private String title;
    private RecordButton r;
    private MediaRecorder mRecorder = null;

    private PlayButton p = null;
    private MediaPlayer mPlayer = null;

    private static String mFileName = null;


    @Override
    public String getTitle(){
        //This is a work around since, the fragment isn't attached to the activity yet when this method is called
        if(title == null)
            title = MainActivity.getMainActivity()
                    .getResources().getString(R.string.record_fragment_title);
        return title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compose_audio, container, false);
        // get RecordButton
        r = (RecordButton) view.findViewById(R.id.mic_button);
        r.setRecordListener(this);

        // set up MediaRecorder and outFileName for the RecordButton
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
        r.SetOutFileName(mFileName);
        r.SetMediaRecorder(mRecorder);

        // get PlayButton
        p = (PlayButton) view.findViewById(R.id.play_button);
        p.SetOutFileName(mFileName);
        p.SetMediaPlayer(mPlayer);

        return view;
    }

    @Override
    public void onStartRecording(){
        AudioTrackView track = createTrack();
        addTrackToLayout(track);
        track.registerController(this, 1);
    }

    private AudioTrackView createTrack(){
        return new AudioTrackView(getContext());
    }

    private void addTrackToLayout(AudioTrackView trackView){
        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.track_layout);
        TextView empty = (TextView)layout.findViewById(R.id.empty_text);
        if(empty != null)
            layout.removeView(empty);
        layout.addView(trackView);
    }

    @Override
    public void onStopRecording(){

    }

    @Override
    public void playTrack(int trackId){

    }

    @Override
    public void pauseTrack(int trackId){

    }

    @Override
    public void deleteTrack(int trackId){

    }

    @Override
    public void applyFilter(int trackId){

    }
}
