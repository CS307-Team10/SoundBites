package com.soundbytes.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andexert.expandablelayout.library.ExpandableLayoutItem;
import com.soundbytes.AudioTrackController;
import com.soundbytes.R;
import com.soundbytes.SoundByteFeedObject;

/**
 * Created by Olumide on 11/8/2015.
 */
public class SoundByteFeedView extends ExpandableLayoutItem {
    private AudioTrackView trackView;
    private TextView date;
    private TextView friend;
    private ProgressBar spinner;
    private ImageView imageView;

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
        trackView = (AudioTrackView)view.findViewById(R.id.feed_trackview);
        trackView.disableSwipe(true);
        friend = (TextView)view.findViewById(R.id.friend_text);
        date = (TextView)view.findViewById(R.id.date_text);
        imageView = (ImageView)view.findViewById(R.id.feed_type_image);
        spinner = (ProgressBar)view.findViewById(R.id.loading_progress_bar);
    }

    public void populate(SoundByteFeedObject soundByteFeedObject, AudioTrackController controller){
        trackView.registerController(controller, soundByteFeedObject.getId());
        //Set Image type
        imageView.setVisibility(VISIBLE);
        if(soundByteFeedObject.getIsSent())
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_send_black_18dp));
        else if(soundByteFeedObject.hasBeenOpened())
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.opened));
        else
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.unopened));
        //Set friend
        friend.setText(soundByteFeedObject.getFriend());
        //set date
        date.setText(DateUtils.getRelativeDateTimeString (getContext(), soundByteFeedObject.getDate().getTime(),
                DateUtils.MINUTE_IN_MILLIS, DateUtils.DAY_IN_MILLIS*30, DateUtils.FORMAT_ABBREV_RELATIVE));
        spinner.setVisibility(INVISIBLE);
    }

    public AudioTrackView getTrackView(){
        return trackView;
    }

    public void setPauseButton(){
        trackView.setPauseButton();
    }

    public void resetPlayButton(){
        trackView.resetPlayButton();
    }

    /*
     * Modified from Stackoverflow answer courtesy of @radhoo
     * http://stackoverflow.com/a/14183532/2057884
     */
    public void imageViewAnimatedChange(int newImageDrawableResource) {
        final Animation anim_out = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        final Drawable drawable = getResources().getDrawable(newImageDrawableResource);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                imageView.setImageDrawable(drawable);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                imageView.startAnimation(anim_in);
            }
        });
        imageView.startAnimation(anim_out);
    }
}
