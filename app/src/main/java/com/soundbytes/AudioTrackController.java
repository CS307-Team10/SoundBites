package com.soundbytes;

/**
 * Created by Olumide on 10/4/2015.
 */
public interface AudioTrackController {
    void playTrack(int trackId);
    void pauseTrack(int trackId);
    void deleteTrack(int trackId);
    void applyFilter(int trackId);//TODO complete to show what kind of filter
}
