package com.example.rodrigo.sgame.Player;

import android.media.AudioTrack;
import android.util.Log;

public class AudioPlayer
        extends AudioTrack
{
    private boolean a = false;

    public AudioPlayer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
        super(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, 1);
    }

    public void flush()
    {
        if (this.a) {
            return;
        }
        super.flush();
    }

    public int getPlaybackHeadPosition()
    {
        if (this.a) {
            return -1;
        }
        return super.getPlaybackHeadPosition();
    }

    public int getPlaybackRate()
    {
        if (this.a) {
            return -1;
        }
        return super.getPlaybackRate();
    }

    public void pause()
    {
        if (this.a) {
            return;
        }
        super.pause();
    }

    public void play()
    {
        if (this.a) {
            return;
        }
        super.play();
    }

    public void release()
    {
        if (this.a) {
            return;
        }
        this.a = true;
        super.release();
        Log.e("MyAudioTrack", "Released");
    }

    public int setPlaybackRate(int paramInt)
    {
        if (this.a) {
            return -3;
        }
        return super.setPlaybackRate(paramInt);
    }

    public void stop()
    {
        if (this.a) {
            return;
        }
        super.stop();
    }
}
