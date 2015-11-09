package com.soundbytes;


import android.os.Bundle;
import android.graphics.Color;
import android.view.ContextMenu;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.soundbytes.views.AudioTrackView;
import com.soundbytes.views.RecordButton;

/**
 * Created by Olumide on 10/3/2015.
 */
public class ComposeFragment extends TitledFragment implements RecordButtonListeners, AudioTrackController{
    private String title;
    private AudioTrackView selectedTrack;
    private RecordButton recordButton;
    private int trackCount = 0;

    @Override
    /**
     * This returns the title of the fragment
     */
    public String getTitle(){
        //This is a work around since, the fragment isn't attached to the activity yet when this method is called
        if(title == null)
            title = MainActivity.getMainActivity()
                    .getResources().getString(R.string.record_fragment_title);
        return title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the fragment
        View view = inflater.inflate(R.layout.fragment_compose_audio, container, false);
      //Find the record button and set the listener
        recordButton = ((RecordButton) view.findViewById(R.id.mic_button));
        recordButton.setRecordListener(this);
        return view;
    }

    @Override
    /**
     * This method is called when the recordButton is help down,
     * the user expects audio to begin recording, this method adds a new audioTrackView to the layout
     */
    public void onStartRecording(){
        AudioTrackView track = createTrack();
        //Add the new audioTrack to layout
        addTrackToLayout(track);
        //Register this fragment with the AudioTrack in order to receive onPlay
        //onPause e.t.c callbacks
        track.registerController(this, 1);
    }

    private AudioTrackView createTrack(){
        //Return new AudioTrack
        AudioTrackView track = new AudioTrackView(getContext());
        track.setHeightInDP(54);
        return track;
    }

    private void addTrackToLayout(AudioTrackView trackView){
        //Find the layout
        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.track_layout);
        //Find the empty TextView
        TextView empty = (TextView)layout.findViewById(R.id.empty_text);
        //Remove the empty TextView from layout
        layout.removeView(empty);
        //Add the trackView to layout
        layout.addView(trackView);
        //Setup the long press stuff
        registerForContextMenu(trackView);
        //TODO replace the counter with an actual model
        //Check if the limit has been reached then disable record button if so
        if(++trackCount >= SoundByteConstants.MAX_TRACK_COUNT)
            recordButton.setEnabled(false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        selectedTrack = (AudioTrackView)v;
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.track_floating_context_menu, menu);
    }

    @Override
    /**
     * This method is called when the user lifts their hand off from the recordButton,
     * note that this gets called even if the user lifts their hand off well after the time limit has passed
     */
    public void onStopRecording(){
        //TODO stop recording audio
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.track_delete:
                selectedTrack.delete();
                selectedTrack = null;
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    /**
     * This method is called when an audioTracks playButton is pressed.
     * When this method is called, the user expects the track to play
     * @param trackId this is the index of the track that a new filter needs to be applied to.
     *                It's the id that's assigned to an audioTrackView on controller registration.
     */
    public void playTrack(int trackId){
        //TODO play audio track here
    }

    @Override
    /**
     * This method should pause all audio playing and call resetAudioButton() on all audioTrackViews
     * currently in the layout.
     * TODO iterate through the audioTrackViews and call resetAudioButton()
     */
    public void pauseAllAudio(){

    }

    @Override
    /**
     * This method is called when the pause button on an audioTrackView is pressed.
     * The user expects the track to stop playing
     * It might be beneficial to stop all other tracks that are playing
     * @param trackId this is the index of the track that a new filter needs to be applied to.
     *                It's the id that's assigned to an audioTrackView on controller registration.
     */
    public void pauseTrack(int trackId){
        //TODO
    }

    @Override
    /**
     * This only removes the audioTrackView from layout as of right now.
     * TODO delete the audioTrack view from the model after removing from layout
     * @param track this is the AudioTrackView that should be removed from the layout
     * @param trackId this is the index of the track that a new filter needs to be applied to.
     *                It's the id that's assigned to an audioTrackView on controller registration.
     */
    public void deleteTrack(AudioTrackView track, int trackId){
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
        recordButton.setEnabled(true);
        trackCount--;
    }

    @Override
    /**
     * This is called when the track is swiped on, and the user expects to hear
     * the new filter when play is pressed
     * @param trackId this is the index of the track that a new filter needs to be applied to.
     *                It's the id that's assigned to an audioTrackView on controller registration.
     * @param filterIndex this is what would be used to identify the filter that should be applied
     *                    the name of the filer it corresponds to can be gotten from SoundByteConstants.FILTER_NAMES
     */
    public void applyFilter(int trackId, int filterIndex){
        //TODO
    }
}
