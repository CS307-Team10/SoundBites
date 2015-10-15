package com.soundbytes;

import android.media.MediaCodec;
import android.media.MediaFormat;

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

    public FilterManager(String inputAudio)
    {
        audioName = inputAudio;

        try {
            mCodec = MediaCodec.createDecoderByType(audioName);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void Speedup()
    {
        

    }


}
