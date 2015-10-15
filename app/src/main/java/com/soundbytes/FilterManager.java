package com.soundbytes;

import java.io.*;

/**
 * Created by Joe on 10/14/2015.
 */
public class FilterManager
{
    private WavFile audioByte = null;

    public FilterManager(String inputAudio)
    {
        try {
            // open the wav file as input
            audioByte = WavFile.openWavFile(new File(inputAudio));
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
    }

    public WavFile Speedup()
    {
        WavFile tempFile = audioByte;

        int numChannels = tempFile.getNumChannels();

        double[] buffer = new double[100 * numChannels];

        int framesRead = 0;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        do
        {
            try
            {
                framesRead = tempFile.readFrames(buffer, 100);
            }
            catch (Exception e)
            {
                System.err.println(e);
            }

            for(int s = 0; s < framesRead * numChannels; s++)
            {
                System.out.println(buffer[s]);
            }

        }while(framesRead != 0);

        return tempFile;
    }
}
