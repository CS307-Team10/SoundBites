package com.soundbytes;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.soundbytes.db.FeedDatabaseHandler;
import com.soundbytes.views.SoundByteFeedView;

/**
 * Created by Olumide on 11/8/2015.
 */
public class NewsFeedAdapter extends BaseAdapter{
    private Context mContext;
    private FeedDatabaseHandler dbHandler;

    /**
     * Constructor
     * @param context The activity context
     */
    public NewsFeedAdapter(Context context, FeedDatabaseHandler databaseHandler){
        this.mContext = context;
        this.dbHandler = databaseHandler;
    }

    @Override
    public Object getItem(int position){
        return null;
    }

    /**
     * @param position index of view whichs id is wanted
     * @return 0
     */
    @Override
    public long getItemId(int position){
        return 0;
    }

    /**
     * @return the number of items
     */
    @Override
    public int getCount() {
        return dbHandler.getCount();
    }

    /**
     * This method is used by the navigation drawer listView for view recycling
     * @param position the index of the view requested
     * @param convertView an old view which is now offscreen and can be reused, Note that this could also be null
     * @param parent the parent ViewGroup
     * @return the requested view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SoundByteFeedObject feedObject = dbHandler.getFeedObject(position);

        if ((convertView != null) && convertView instanceof SoundByteFeedView){
            ((SoundByteFeedView)convertView).populate(feedObject);
            return convertView;
        }else {
            SoundByteFeedView feedView = new SoundByteFeedView(mContext);
            feedView.populate(feedObject);
            return feedView;
        }
    }
}
