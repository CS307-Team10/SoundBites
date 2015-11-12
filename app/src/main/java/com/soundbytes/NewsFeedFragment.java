package com.soundbytes;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.andexert.expandablelayout.library.ExpandableLayoutListView;
import com.soundbytes.db.DBHandlerResponse;
import com.soundbytes.db.FeedDatabaseHandler;
import com.soundbytes.views.AudioTrackView;

/**
 * Created by Olumide on 11/4/2015.
 */
public class NewsFeedFragment extends TitledFragment implements DBHandlerResponse, AudioTrackController, SwipeRefreshLayout.OnRefreshListener{
    private String title;
    private View viewLayout;
    private ExpandableLayoutListView expListView;
    private NewsFeedAdapter adapter;
    private FeedDatabaseHandler dbHandler;
    private int currentlyPlaying = -1;
    private SwipeRefreshLayout refresher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the fragment
        viewLayout = inflater.inflate(R.layout.fragment_newsfeed, container, false);
        expListView = (ExpandableLayoutListView)viewLayout.findViewById(R.id.feed_exp_list_view);
        refresher = (SwipeRefreshLayout)viewLayout.findViewById(R.id.swipe_refresh);
        refresher.setOnRefreshListener(this);
        dbHandler = FeedDatabaseHandler.getInstance(getContext(), this);
        return viewLayout;
    }

    @Override
    /**
     * This returns the title of the fragment
     */
    public String getTitle(){
        //This is a work around since, the fragment isn't attached to the activity yet when this method is called
        if(title == null)
            title = MainActivity.getMainActivity()
                    .getResources().getString(R.string.newsfeed_fragment_title);
        return title;
    }

    public boolean isTrackCurrentlyPlaying(int trackId){
        return currentlyPlaying == trackId;
    }

    @Override
    public void playTrack(int trackId){
        pauseAllAudio();
        currentlyPlaying = trackId;
        //TODO play audio track here
    }

    /**
     * This method should pause all audio playing and call resetAudioButton() on all audioTrackViews
     * currently in the layout.
     * TODO iterate through the audioTrackViews and call resetAudioButton()
     */
    @Override
    public void pauseAllAudio(){
        //Since only one audio can play at a time, it only has to pause one audio
        currentlyPlaying = -1;
    }

    /**
     * This method is called when the pause button on an audioTrackView is pressed.
     * The user expects the track to stop playing
     * It might be beneficial to stop all other tracks that are playing
     * @param trackId this is the index of the track that a new filter needs to be applied to.
     *                It's the id that's assigned to an audioTrackView on controller registration.
     */
    @Override
    public void pauseTrack(int trackId){
        //TODO
    }

    @Override
    public void deleteTrack(AudioTrackView track, int trackId){
        //Not used in this fragment
    }

    @Override
    public void applyFilter(int trackId, int filterIndex){
        //Not used in this fragment
    }

    public void onDBReady(){
        adapter = new NewsFeedAdapter(getContext(), dbHandler, this);
        expListView.setAdapter(adapter);
        expListView.invalidate();
    }

    public void onRefresh(){
        Log.v("swipe", "onRefresh");
        if(expListView.getChildCount() < adapter.getCount()){
            refresher.setRefreshing(true);
            adapter.notifyDataSetChanged();
            refresher.setRefreshing(false);
        }else{
            refresher.setRefreshing(false);
        }
    }
}
