package com.soundbytes;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

/**
 * Created by Joe on 10/14/2015.
 */
public class FilterManager
{
    private String audioName = null;
    private MediaCodec mCodec;
    private Context c;
    private static int playingId = -1;
    private static OnAudioDoneCallback callback;
    private static Handler handler;
    private static SoundPool sp;
    private static MyRunnable r;
    private static boolean isPlaying;

    public FilterManager(String inputAudio, Context ctx)
    {
        handler = new Handler();
        audioName = inputAudio;
        c = ctx;
    }

    public void Regular()
    {
        if (isPlaying)
            return;
        final float playbackSpeed = 1.0f;
        sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        final int soundId = sp.load(audioName, 1);
        AudioManager mgr = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        final float volume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                setupSingle(soundPool, sampleId);
                playingId = soundPool.play(soundId, volume * 2, volume * 2, 1, 0, playbackSpeed);
            }
        });
    }

    public void Speedup()
    {
        if (isPlaying)
            return;
        final float playbackSpeed = 2.0f;
        sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        final int soundId = sp.load(audioName, 1);
        AudioManager mgr = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        final float volume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                setupSingle(soundPool, sampleId);
                playingId = soundPool.play(soundId, volume * 2, volume * 2, 1, 0, playbackSpeed);
            }
        });

    }

    public void Slowdown()
    {
        if (isPlaying)
            return;
        final float playbackSpeed = 0.6f;
        sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        final int soundId = sp.load(audioName, 1);
        AudioManager mgr = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        final float volume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                setupSingle(soundPool, sampleId);
                playingId = soundPool.play(soundId, volume * 2, volume * 2, 1, 0, playbackSpeed);
            }
        });
    }

    public void HighPitch()
    {
        if (isPlaying)
            return;
        final float playbackSpeed = 1.2f;
        sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        final int soundId = sp.load(audioName, 1);
        AudioManager mgr = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        final float volume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                setupSingle(soundPool, sampleId);
                playingId = soundPool.play(soundId, volume * 2, volume * 2, 1, 0, playbackSpeed);
            }
        });
    }

    public void LowPitch()
    {
        if (isPlaying)
            return;
        final float playbackSpeed = 0.8f;
        sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        final int soundId = sp.load(audioName, 1);
        AudioManager mgr = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        final float volume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                setupSingle(soundPool, sampleId);
                playingId = soundPool.play(soundId, volume * 2, volume * 2, 1, 0, playbackSpeed);
            }
        });
    }

    private void setupSingle(SoundPool sp, int soundId){
        sp.autoPause();
        isPlaying = true;
        if(callback != null){
            long duration = getSoundDuration(soundId, callback.getContext());
            Log.v("duration", ""+duration);
            r = new MyRunnable() {
                boolean stopped = false;
                @Override
                public void run() {
                    if((callback != null) && !stopped) {
                        isPlaying = false;
                        callback.audioFinished();
                    }
                }

                public void stop(){
                    stopped = true;
                }
            };
            handler.postDelayed(r, duration);
        }
    }

    private long getSoundDuration(int rawId, Context context){
        MediaPlayer player = MediaPlayer.create(context, Uri.parse((new File(audioName)).toURI().toString()));
        int duration = player.getDuration();
        player.release();
        return duration;
    }

    public void setAudioDoneCallback(OnAudioDoneCallback callback){
        this.callback = callback;
    }

    public void stopAudio(){
        Log.v("fm", "stop audio");
        sp.stop(playingId);
        sp.autoResume();
        handler.removeCallbacks(null);
        r.stop();
        isPlaying = false;
    }

    public interface OnAudioDoneCallback{
        void audioFinished();
        Context getContext();
    }

    private interface MyRunnable extends Runnable{
        void run();
        void stop();
    }
}
