package com.soundbytes.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.soundbytes.R;
import com.soundbytes.SoundByteFeedObject;

/**
 * Created by Olumide on 11/8/2015.
 */
public class SoundByteFeedView extends RelativeLayout {
    private Context context;
    private AudioTrackView trackView;

    public SoundByteFeedView(Context c){
        super(c);
        init(c);
    }

    public SoundByteFeedView(Context c, AttributeSet attributeSet){
        super(c, attributeSet);
        init(c);
    }


    public SoundByteFeedView(Context context, AttributeSet attr, int defStyle){
        super(context, attr, defStyle);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.soundbyte_feed_view, this);
//        View view = View.inflate(this.context, R.layout.soundbyte_feed_view, false);
        trackView = (AudioTrackView)view.findViewById(R.id.feed_trackview);
    }

    public void populate(SoundByteFeedObject soundByteFeedObject){
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundByteFeedView.this.expand();
            }
        });
    }

    public void collapse(){
        new RelativeLayout.LayoutParams(trackView.getWidth(),trackView.getHeight()).setMargins(0,0,0,trackView.getHeight());
        RelativeLayout.LayoutParams rl2 = new RelativeLayout.LayoutParams(trackView.getWidth(),trackView.getHeight());
        rl2.setMargins(0, 0, 0, -trackView.getHeight());
        invalidate();
    }

    public void expand(){
//        AbsListView.LayoutParams rl2 = new AbsListView.LayoutParams(getWidth(),trackView.getHeight()+getHeight());
//        setLayoutParams(rl2);
//        Toast.makeText(context, "expand", Toast.LENGTH_LONG).show();
//        invalidate();
//        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(trackView.getWidth(),trackView.getHeight());
////        rl.setMargins(trackView.getHeight(), 0, 0, 0);
//        trackView.setLayoutParams(rl);
//        trackView.invalidate();
    }
}
