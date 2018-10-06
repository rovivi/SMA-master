package com.example.rodrigo.sgame.ScreenSelectMusic;

import android.media.MediaPlayer;

public class MusicThread extends Thread {

    public MediaPlayer player;
    public long time=20000000;
    public  boolean isRunning = true;
    long timeStamp;


    public MusicThread() {
        timeStamp=System.currentTimeMillis();
    }
    public void changeSong(Double time,MediaPlayer mp) {
        player.setVolume(1f, 1f);
        this.time = (int) (time * 1000);
       this.player=mp;

    }


    @Override
    public void run() {
        super.run();
        while (isRunning) {
            try {
                if (time <= 3500 && time>(0)) {
                    float volume = (float) time / 3500;
                    if (player!=null && volume>=0 && volume<=1f){
                        player.setVolume(volume, volume);
                    }
                    else if (player!=null && volume<0){
                        player.setVolume(0, 0);
                    }

                }

                    long resta=System.currentTimeMillis()-timeStamp;
                    time-=resta;
                    timeStamp=System.currentTimeMillis();

                sleep(25);
            } catch (IllegalStateException ex2){

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    this.join();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
