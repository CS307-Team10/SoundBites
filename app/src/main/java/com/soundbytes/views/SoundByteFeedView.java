package com.soundbytes.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.andexert.expandablelayout.library.ExpandableLayoutItem;
import com.soundbytes.AudioTrackController;
import com.soundbytes.R;
import com.soundbytes.SoundByteFeedObject;

/**
 * Created by Olumide on 11/8/2015.
 */
public class SoundByteFeedView extends ExpandableLayoutItem {
    private AudioTrackView trackView;
    private boolean isExpanded = false;

    public SoundByteFeedView(Context c){
        super(c);
        init();
    }

    public SoundByteFeedView(Context c, AttributeSet attributeSet){
        super(c, attributeSet);
        init();
    }


    public SoundByteFeedView(Context context, AttributeSet attr, int defStyle){
        super(context, attr, defStyle);
        init();
    }

    private void init(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.soundbyte_feed_view, this);
        trackView = (AudioTrackView)findViewById(R.id.feed_trackview);
    }

    public void populate(SoundByteFeedObject soundByteFeedObject, AudioTrackController controller){
        trackView.registerController(controller, soundByteFeedObject.getId());
    }

    public void setPauseButton(){
        trackView.setPauseButton();
    }

    public void resetPlayButton(){
        trackView.resetPlayButton();
    }

    public void collapse(){
        if(isExpanded) {
            new RelativeLayout.LayoutParams(trackView.getWidth(), trackView.getHeight()).setMargins(0, 0, 0, trackView.getHeight());
            RelativeLayout.LayoutParams rl2 = new RelativeLayout.LayoutParams(trackView.getWidth(), trackView.getHeight());
            rl2.setMargins(0, 0, 0, -trackView.getHeight());
            isExpanded = false;
            invalidate();
        }
    }

    public void expand() {
        if (!isExpanded) {
//        AbsListView.LayoutParams rl2 = new AbsListView.LayoutParams(getWidth(),trackView.getHeight()+getHeight());
//        setLayoutParams(rl2);
//        Toast.makeText(context, "expand", Toast.LENGTH_LONG).show();
//        invalidate();
//        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(trackView.getWidth(),trackView.getHeight());
////        rl.setMargins(trackView.getHeight(), 0, 0, 0);
//        trackView.setLayoutParams(rl);
//        trackView.invalidate();
            isExpanded = true;
        }
    }
}
