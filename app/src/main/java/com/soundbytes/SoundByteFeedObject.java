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
    private Date time;
    private File audioPath;
    private String filter;
    private float speed;

    //ID | SENT? | TO/FROM| DATE | TIME |FILENAME | FILTER | SPEED
    public SoundByteFeedObject(int id, boolean sent, String friend, Date date, Date time, File audioPath, String filter, float speed){
        this.id = id;
        this.sent = sent;
        this.friend = friend;
        this.date = date;
        this.time = time;
        this.audioPath = audioPath;
        this.filter = filter;
        this.speed = speed;
    }

    public int getId(){
        return id;
    }

    public boolean getIsSent(){
        return sent;
    }

    public String getFriend(){
        return friend;
    }

    public Date getDate(){
        return date;
    }

    public Date getTime(){
        return time;
    }

    public String getAudioPath(){
        if(audioPath == null)
            return "";
        return audioPath.getPath();
    }

    public String getFilter(){
        return filter;
    }

    public float getPlaybackSpeed(){
        return speed;
    }
}
