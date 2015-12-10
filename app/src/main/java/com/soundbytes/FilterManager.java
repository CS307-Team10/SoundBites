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
    public static final float regularPlaybackSpeed = 1.0f;
    public static final float speedPlaybackSpeed = 2.0f;
    public static final float slowPlaybackSpeed = 0.6f;
    public static final float highPlaybackSpeed = 1.2f;
    public static final float lowPlaybackSpeed = 0.8f;

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
        sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        final int soundId = sp.load(audioName, 1);
        AudioManager mgr = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        final float volume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                setupSingle(soundPool, sampleId);
                playingId = soundPool.play(soundId, volume * 2, volume * 2, 1, 0, regularPlaybackSpeed);
            }
        });
    }

    public void Speedup()
    {
        if (isPlaying)
            return;
        sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        final int soundId = sp.load(audioName, 1);
        AudioManager mgr = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        final float volume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                setupSingle(soundPool, sampleId);
                playingId = soundPool.play(soundId, volume * 2, volume * 2, 1, 0, speedPlaybackSpeed);
            }
        });

    }

    public void Slowdown()
    {
        if (isPlaying)
            return;
        sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        final int soundId = sp.load(audioName, 1);
        AudioManager mgr = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        final float volume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                setupSingle(soundPool, sampleId);
                playingId = soundPool.play(soundId, volume * 2, volume * 2, 1, 0, slowPlaybackSpeed);
            }
        });
    }

    public void HighPitch()
    {
        if (isPlaying)
            return;
        sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        final int soundId = sp.load(audioName, 1);
        AudioManager mgr = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        final float volume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                setupSingle(soundPool, sampleId);
                playingId = soundPool.play(soundId, volume * 2, volume * 2, 1, 0, highPlaybackSpeed);
            }
        });
    }

    public void LowPitch()
    {
        if (isPlaying)
            return;
        sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        final int soundId = sp.load(audioName, 1);
        AudioManager mgr = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        final float volume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                setupSingle(soundPool, sampleId);
                playingId = soundPool.play(soundId, volume * 2, volume * 2, 1, 0, lowPlaybackSpeed);
            }
        });
    }

    public void customSpeed(final float playbackSpeed){
        if (isPlaying)
            return;
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
        if(callback != null){
            long duration = (long)(getSoundDuration(audioName, callback.getContext())/callback.getPlaybackSpeed());
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
            if(duration != -1) {
                handler.postDelayed(r, duration);
                isPlaying = true;
            }else {
                callback.audioFinished();
            }
        }
    }

    public static long getSoundDuration(String audioName, Context context){
        MediaPlayer player = MediaPlayer.create(context, Uri.parse((new File(audioName)).toURI().toString()));
        if(player != null) {
            int duration = player.getDuration();
            player.release();
            return duration;
        }
        return -1;
    }

    public static void setAudioDoneCallback(OnAudioDoneCallback callback){
        FilterManager.callback = callback;
    }

    public static void stopAudio(){
        Log.v("fm", "stop audio");
        if(sp != null) {
            sp.stop(playingId);
            sp.autoResume();
        }
        if(handler != null)
            handler.removeCallbacks(null);
        if(r != null)
            r.stop();
        if(callback != null && isPlaying) {
            isPlaying = false;
            callback.audioFinished();
            callback = null;
        }
        isPlaying = false;
    }

    public interface OnAudioDoneCallback{
        void audioFinished();
        float getPlaybackSpeed();
        Context getContext();
    }

    public interface MyRunnable extends Runnable{
        void run();
        void stop();
    }
}
