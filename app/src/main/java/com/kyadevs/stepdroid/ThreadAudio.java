package com.kyadevs.stepdroid;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.kyadevs.stepdroid.CommonGame.ParamsSong;

import java.io.File;
import java.io.IOException;


public class ThreadAudio extends Thread {

    float offset;
    private MediaPlayer mediaPlayer;


    public ThreadAudio(Context c, float offset, String path) throws IOException {
        Context c1 = c;
        this.offset = offset;
        File f=new File(path);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(f.getPath());
        mediaPlayer.prepare();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(ParamsSong.rush));// Esto será para el rush
        }
        //        mediaPlayer.prepare();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void playmusic()  {

        if (mediaPlayer!= null){
            int offsetMili=(int) (offset*1000);
        //    mediaPlayer.seekTo(offsetMili);
            mediaPlayer.start();
        }
    }

    public void stopmusic() {
        mediaPlayer.stop();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void run() {
        try {
            sleep((int) (offset * 1000));
            this.playmusic();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }




    }


} 