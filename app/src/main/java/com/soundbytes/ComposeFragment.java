package com.soundbytes;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Olumide on 10/3/2015.
 */
public class ComposeFragment extends TitledFragment implements RecordButtonListeners, AudioTrackController{
    private String title;
    private AudioTrackView selectedTrack;

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
        ((RecordButton)view.findViewById(R.id.mic_button)).setRecordListener(this);
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
            layout.removeView(empty);
        layout.addView(trackView);
        registerForContextMenu(trackView);
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
    public void onStopRecording(){

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
    public void playTrack(int trackId){

    }

    @Override
    public void pauseTrack(int trackId){

    }

    @Override
    public void deleteTrack(AudioTrackView track, int trackId){
        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.track_layout);
        unregisterForContextMenu(track);
        layout.removeView(track);
        if(layout.getChildCount() == 0){
            TextView empty = new TextView(getContext());
            /*
            <TextView
                android:id="@+id/empty_text"
                android:background="#dddddd"
                android:layout_width="match_parent"
                android:text="No recordings create one!"
                android:layout_height="50dp" />*/
            empty.setId(R.id.empty_text);
            empty.setBackgroundColor(Color.parseColor("#dddddd"));
            empty.setText(R.string.no_audio_text);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            empty.setLayoutParams(params);
            layout.addView(empty);
        }
    }

    @Override
    public void applyFilter(int trackId){

    }
}
