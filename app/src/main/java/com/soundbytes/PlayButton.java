package com.soundbytes;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.media.SoundPool;

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

    public PlayButton(Context ctx, AttributeSet attrs)
    {
        super(ctx, attrs);
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
        final float playbackSpeed = 1.0f;
        SoundPool sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        final int soundId = sp.load(mFileName, 1);
        AudioManager mgr = (AudioManager) this.getContext().getSystemService(Context.AUDIO_SERVICE);
        final float volume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(soundId, volume * 2, volume * 2, 1, 0, playbackSpeed);
            }
        });
        /*
        mPlayer = new MediaPlayer();
        try{
            mPlayer.setDataSource(mFileName);



            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        */
    }

    private void stopPlaying()
    {

    }
}
