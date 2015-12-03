package com.soundbytes;

import android.graphics.Color;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    private AudioTrackView selectedTrack;
    private int trackCount = 0;


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
        r.setRecordListener(r);

        // set up MediaRecorder and outFileName for the RecordButton
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
        r.SetOutFileName(mFileName);
        r.SetAudioRecorder(mRecorder);

        // get PlayButton
        p = (PlayButton) view.findViewById(R.id.play_button);
        p.SetOutFileName(mFileName);
        p.SetMediaPlayer(mPlayer);

        Button f1 = (Button) view.findViewById(R.id.filter_1);
        Button f2 = (Button) view.findViewById(R.id.filter_2);
        Button f3 = (Button) view.findViewById(R.id.filter_3);
        Button f4 = (Button) view.findViewById(R.id.filter_4);

        final FilterManager fm = new FilterManager(mFileName,getContext());

        f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.Speedup();
            }
        });

        f2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.Slowdown();
            }
        });

        f3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.HighPitch();
            }
        });

        f4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.LowPitch();
            }
        });


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
    public boolean onContextItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.track_delete:
                selectedTrack.delete();
                selectedTrack = null;
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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
    public void pauseAllAudio()
    {

    }

    @Override
    public void deleteTrack(AudioTrackView track, int trackId)
    {
        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.track_layout);
        //unregister the long press stuff
        unregisterForContextMenu(track);
        //remove audioTrack from the layout
        layout.removeView(track);
        //Check if the layout will be empty after removing the trackView
        if(layout.getChildCount() == 0){
            //Since the layout is empty, add the empty text
            TextView empty = new TextView(getContext());
            empty.setId(R.id.empty_text);
            empty.setBackgroundColor(Color.parseColor("#dddddd"));
            empty.setText(R.string.no_audio_text);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            empty.setLayoutParams(params);
            layout.addView(empty);
        }
        //enable the record button, since the number of tracks is definitely less than the limit right now
        r.setEnabled(true);
        trackCount--;
    }

    @Override
    public void applyFilter(int trackId, int filterIndex){

    }



}
