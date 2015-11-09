package com.soundbytes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.soundbytes.db.DBHandlerResponse;
import com.soundbytes.db.FeedDatabaseHandler;

/**
 * Created by Olumide on 11/4/2015.
 */
public class NewsFeedFragment extends TitledFragment implements DBHandlerResponse {
    private String title;
    private View viewLayout;
    private LinearLayout linearLayout;// = (ViewGroup)findViewById(R.id.linearView);
    private ListView listView;
    private NewsFeedAdapter adapter;
    private FeedDatabaseHandler dbHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the fragment
        viewLayout = inflater.inflate(R.layout.fragment_newsfeed, container, false);
//        linearLayout = (ViewGroup)viewLayout.findViewById(R.id.linear_layout);
        listView = (ListView)viewLayout.findViewById(R.id.feed_list_view);
        dbHandler = new FeedDatabaseHandler(getContext(), this);
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

    /*private void populate(List<SoundByteFeedObject> soundByteObjects){
        ListIterator iterator = soundByteObjects.listIterator(soundByteObjects.size());
        while(iterator.hasPrevious()){
            SoundByteFeedObject soundByteObject = (SoundByteFeedObject)iterator.previous();
            SoundByteFeedView singleSoundByte = new SoundByteFeedView(getContext());
            singleSoundByte.populate(soundByteObject);
//            linearLayout.addView(singleSoundByte);
        }
    }*/

    public void onDBReady(){
        adapter = new NewsFeedAdapter(getContext(), dbHandler);
        listView.setAdapter(adapter);
        listView.invalidate();
    }
}
