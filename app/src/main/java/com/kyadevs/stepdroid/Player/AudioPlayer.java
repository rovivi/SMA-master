package com.kyadevs.stepdroid.Player;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class AudioPlayer
        extends AudioTrack
{
    private boolean isPlaying = false;

    public AudioPlayer( int bitRate, int audioFormat, int length)
    {
        super(AudioManager.STREAM_MUSIC, bitRate, audioFormat, length, AudioFormat.ENCODING_PCM_16BIT, AudioTrack.MODE_STREAM);
    }

    public void flush()
    {
        if (this.isPlaying) {
            return;
        }
        super.flush();
    }

    public int getPlaybackHeadPosition()
    {
        if (this.isPlaying) {
            return -1;
        }
        return super.getPlaybackHeadPosition();
    }

    public int getPlaybackRate()
    {
        if (this.isPlaying) {
            return -1;
        }
        return super.getPlaybackRate();
    }

    public void pause()
    {
        if (this.isPlaying) {
            return;
        }
        super.pause();
    }

    public void play()
    {
        if (this.isPlaying) {
            return;
        }
        super.play();
    }

    public void release()
    {
        if (this.isPlaying) {
            return;
        }
        this.isPlaying = true;
        super.release();
        Log.e("MyAudioTrack", "Released");
    }

    public int setPlaybackRate(int paramInt)
    {
        if (this.isPlaying) {
            return -3;
        }
        return super.setPlaybackRate(paramInt);
    }

    public void stop()
    {
        if (this.isPlaying) {
            return;
        }
        super.stop();
    }
}
