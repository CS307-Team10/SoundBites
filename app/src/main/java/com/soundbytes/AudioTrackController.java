package com.soundbytes;

/**
 * Created by Olumide on 10/4/2015.
 */
public interface AudioTrackController {
    void playTrack(int trackId);
    void pauseTrack(int trackId);
    void deleteTrack(AudioTrackView track, int trackId);
    void applyFilter(int trackId, int filterIndex);
    void pauseAllAudio();
}
