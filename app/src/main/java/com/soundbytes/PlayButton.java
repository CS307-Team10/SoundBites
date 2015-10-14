package com.soundbytes;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.io.IOException;

/**
 * Created by Joe on 10/13/2015.
 */
public class PlayButton extends ImageButton
{
    boolean mStartPlaying = true;

    private MediaPlayer mPlayer = null;

    private static final String LOG_TAG = "AudioRecordTest";
    private String mFileName = null;

    public PlayButton(Context ctx)
    {
        super(ctx);
        setOnClickListener(clicker);
    }

    OnClickListener clicker = new OnClickListener() {
        public void onClick(View v)
        {
            onPlay(mStartPlaying);
            mStartPlaying = !mStartPlaying;
        }
    };

    public void SetMediaPlayer(MediaPlayer m)
    {
        mPlayer = m;
    }

    public void SetOutFileName(String fileName)
    {
        mFileName = fileName;
    }

    private void onPlay(boolean start)
    {
        if(start)
            startPlaying();
        else
            stopPlaying();
    }

    private void startPlaying()
    {
        mPlayer = new MediaPlayer();
        try{
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying()
    {
        mPlayer.release();
        mPlayer = null;
    }
}
