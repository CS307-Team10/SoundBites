package com.soundbytes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.andexert.expandablelayout.library.ExpandableLayoutItem;
import com.andexert.expandablelayout.library.ExpandableLayoutListView;
import com.soundbytes.db.DBHandlerResponse;
import com.soundbytes.db.FeedDatabaseHandler;
import com.soundbytes.views.AudioTrackView;
import com.soundbytes.views.SoundByteFeedView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    private int currentlyOpen = -1;
    private SwipeRefreshLayout refresher;
    private DatabaseUpdatedReceiver receiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the fragment
        viewLayout = inflater.inflate(R.layout.fragment_newsfeed, container, false);
        expListView = (ExpandableLayoutListView)viewLayout.findViewById(R.id.feed_exp_list_view);
        refresher = (SwipeRefreshLayout)viewLayout.findViewById(R.id.swipe_refresh);
        refresher.setOnRefreshListener(this);
        dbHandler = FeedDatabaseHandler.getInstance(getContext(), this);
        expListView.setOnItemClickListener(getItemClickListener());
        receiver = new DatabaseUpdatedReceiver();
        DatabaseUpdatedReceiver.frag = this;
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        getContext().registerReceiver(receiver, filter);
        setRetainInstance(true);
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

    public boolean isTrackCurrentlyOpen(int trackId){
        return currentlyOpen == trackId;
    }

    /**
     *
     * @param trackId this is currently just the id in the database
     */
    @Override
    public void playTrack(int trackId){
        pauseAllAudio();
        currentlyPlaying = trackId;
        SoundByteFeedObject feedObject = dbHandler.getFeedObject(trackId);
        setAudioPlaybackFinishedCallback(feedObject.getPlaybackSpeed());
        if(!feedObject.hasBeenOpened()) {
            SoundByteFeedView feedView = getFeedView(adapter.getCount() - 1 - trackId);
            if (feedView != null)
                feedView.imageViewAnimatedChange(R.drawable.opened);
        }
        dbHandler.markAsRead(trackId);
        FilterManager fm = new FilterManager(feedObject.getAudioPath(), getContext());
        switch(feedObject.getFilter()){
            case 0:
                fm.Regular();
                break;
            case 1:
                fm.HighPitch();
                break;
            case 2:
                fm.LowPitch();
                break;
            case 3:
                fm.Speedup();
                break;
            case 4:
                fm.Slowdown();
                break;
            default:
                fm.customSpeed(feedObject.getPlaybackSpeed());
        }
    }

    private void setAudioPlaybackFinishedCallback(final float speed){
        FilterManager.setAudioDoneCallback(new FilterManager.OnAudioDoneCallback() {
            public void audioFinished() {
                pauseAllAudio();
            }

            public float getPlaybackSpeed(){
                return speed;
            }

            public Context getContext() {
                return NewsFeedFragment.this.getContext();
            }
        });
    }

    @Override
    public void onVisible() {
        FilterManager.setAudioDoneCallback(null);
        ((MainActivity)getActivity()).clearFocus(expListView);
    }

    /**
     * This method should pause all audio playing and call resetAudioButton() on all audioTrackViews
     * currently in the layout.
     * TODO iterate through the audioTrackViews and call resetAudioButton()
     */
    @Override
    public void pauseAllAudio(){
        FilterManager.stopAudio();
        //Since only one audio can play at a time, it only has to pause one audio
        if(currentlyPlaying != -1)
            pauseTrack(currentlyPlaying);
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
        FilterManager.stopAudio();
        AudioTrackView trackView = getTrackView(adapter.getCount() - 1 - trackId);
        if(trackView != null)
            trackView.resetPlayButton();
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

    public void onRefresh() {
        refresher.setRefreshing(true);
        adapter.notifyDataSetChanged();
        //TODO make request to server to check for new messages
        refresher.setRefreshing(false);
    }

    public NewsFeedAdapter getAdapter(){
        return adapter;
    }

    public static class DatabaseUpdatedReceiver extends BroadcastReceiver{
        public static NewsFeedFragment frag;
        public void onReceive(final Context context, Intent intent) {
            if(intent.getAction().equals(SoundByteConstants.dbUpdateBroadcast)){
                frag.getAdapter().notifyDataSetChanged();
            }
        }
    }

    private AdapterView.OnItemClickListener getItemClickListener(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final SoundByteFeedObject feedObject = dbHandler.getFeedObject(dbHandler.getCount() -1 - position);
                ExpandableLayoutItem mEplItem = (ExpandableLayoutItem) view.findViewById(R.id.expandableLayout);
                if(feedObject.getIsSent()) {
                    mEplItem.hideNow();
                    currentlyOpen = -1;
                    return;
                }
                //Check if audio file is present, if not //show loading and download it
                final AudioTrackView track = (AudioTrackView)mEplItem.getContentLayout().findViewById(R.id.feed_trackview);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            track.autoUpdateRecordPreview(new File(feedObject.getAudioPath()), feedObject.getPlaybackSpeed());
                            currentlyOpen = feedObject.getId();
                            return;
                        }catch (IllegalArgumentException e){
                            //Do nothing
                        }catch (NullPointerException e){
                            //Do nothing
                            e.printStackTrace();
                        }
                    }
                }, 200);
                if(feedObject.getAudioPath() == null || feedObject.getAudioPath().equals("") || !(new File(feedObject.getAudioPath())).exists()) {
                    mEplItem.hideNow();
                    currentlyOpen = -1;
                    //make spinner visible
                    mEplItem.getHeaderLayout().findViewById(R.id.loading_progress_bar).setVisibility(View.VISIBLE);
                    mEplItem.getHeaderLayout().findViewById(R.id.feed_type_image).setVisibility(View.INVISIBLE);

                    //Start async task to download audio file
                    ServerRequests serverRequests = new ServerRequests(getContext());
                    serverRequests.fetchAudioFileInBackground(new UserLocalStore(getContext()).getLoggedInUser(),
                            feedObject.getAudioID(), new OnAudioDownloadCallback() {
                                @Override
                                public void onAudioDownloaded(String base64) {
                                    //Save the audio content to file
                                    if(base64 != null) {
                                        byte[] decoded = Base64.decode(base64, Base64.DEFAULT);
                                        try {
                                            File file = createAudioFile(feedObject.getFriend(), feedObject.getIsSent());
                                            FileOutputStream os = new FileOutputStream(file, true);
                                            os.write(decoded);
                                            os.close();
                                            dbHandler.updateFilePath(feedObject.getId(), file);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            });
                }
            }
        };
    }


    private File createAudioFile(String friend, boolean isSent){
        String suffix = ".3gp";
        String prefix = "SB_";
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName;
        if(isSent)
            imageFileName = prefix + "s" + timeStamp + friend;
        else
            imageFileName = prefix + "r" + timeStamp + friend;
        File storageDir = new File(Environment.getExternalStorageDirectory(), "SoundBytes" + File.separatorChar);
        if (!storageDir.exists())
            storageDir.mkdir();
        return new File(storageDir, imageFileName+suffix);
    }


    @Nullable
    private SoundByteFeedView getFeedView(int wantedPosition){
        int firstPosition = expListView.getFirstVisiblePosition() - expListView.getHeaderViewsCount(); // This is the same as child #0
        int wantedChild = wantedPosition - firstPosition;
        // Say, first visible position is 8, you want position 10, wantedChild will now be 2
        // So that means your view is child #2 in the ViewGroup:
        if (wantedChild < 0 || wantedChild >= expListView.getChildCount()) {
            return  null;
        }
        // Could also check if wantedPosition is between listView.getFirstVisiblePosition() and listView.getLastVisiblePosition() instead.
        return (SoundByteFeedView)expListView.getChildAt(wantedChild);
    }

    @Nullable
    private AudioTrackView getTrackView(int wantedPosition){
        SoundByteFeedView feedView = getFeedView(wantedPosition);
        if(feedView == null)
            return null;
        return feedView.getTrackView();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        getContext().unregisterReceiver(receiver);
    }
}
