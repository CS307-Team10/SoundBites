package com.soundbytes;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.SoundPool;

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

    public FilterManager(String inputAudio, Context ctx)
    {
        audioName = inputAudio;
        c = ctx;
    }

    public void Speedup()
    {
        final float playbackSpeed = 2.0f;
        SoundPool sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        final int soundId = sp.load(audioName, 1);
        AudioManager mgr = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        final float volume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(soundId, volume * 2, volume * 2, 1, 0, playbackSpeed);
            }
        });

    }

    public void Slowdown()
    {
        final float playbackSpeed = 0.6f;
        SoundPool sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        final int soundId = sp.load(audioName, 1);
        AudioManager mgr = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        final float volume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(soundId, volume * 2, volume * 2, 1, 0, playbackSpeed);
            }
        });
    }

    public void HighPitch()
    {
        final float playbackSpeed = 1.2f;
        SoundPool sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        final int soundId = sp.load(audioName, 1);
        AudioManager mgr = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        final float volume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(soundId, volume * 2, volume * 2, 1, 0, playbackSpeed);
            }
        });
    }

    public void LowPitch()
    {
        final float playbackSpeed = 0.8f;
        SoundPool sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        final int soundId = sp.load(audioName, 1);
        AudioManager mgr = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        final float volume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(soundId, volume * 2, volume * 2, 1, 0, playbackSpeed);
            }
        });
    }


}
