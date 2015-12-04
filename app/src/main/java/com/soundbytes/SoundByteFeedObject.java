package com.soundbytes;

import java.io.File;
import java.util.Date;

/**
 * Created by Olumide on 11/4/2015.
 */
public class SoundByteFeedObject {
    private int id;
    private boolean sent;
    private String friend;
    private Date date;
    private File audioPath;
    private int filter;
    private float speed;
    private boolean opened;
    private String audio_id;

    //ID | SENT? | TO/FROM| DATE | TIME |FILENAME | FILTER | SPEED
    public SoundByteFeedObject(int id, boolean sent, String friend, Date date, File audioPath, int filter, float speed, boolean opened, String audio_id){
        this.id = id;
        this.sent = sent;
        this.friend = friend;
        this.date = date;
        this.audioPath = audioPath;
        this.filter = filter;
        this.speed = speed;
        this.opened = opened;
        this.audio_id = audio_id;
    }

    public int getId(){
        return id;
    }

    public boolean getIsSent(){
        return sent;
    }

    public void setFilePath(File file){
        audioPath = file;
    }

    public String getAudioID(){
        return audio_id;
    }

    public boolean hasBeenOpened(){
        return opened;
    }

    public void markAsRead(){
        opened = true;
    }

    public String getFriend(){
        return friend;
    }

    public Date getDate(){
        return date;
    }

    public String getAudioPath(){
        if(audioPath == null)
            return null;
        return audioPath.getPath();
    }

    public int getFilter(){
        return filter;
    }

    public float getPlaybackSpeed(){
        return speed;
    }
}
