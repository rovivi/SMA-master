package com.example.rodrigo.sgame;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;

/**
 * Created by test on 13/04/2018.
 */

public class VideoBitmap extends Thread {
    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
    long timestarmilis;
    long timeInMillisec;
    public VideoBitmap(String path,Bitmap bitmap) {
        path =Environment.getExternalStorageDirectory().toString()+"/SONGMOVIES/BGA_OFF.mp4";
        mediaMetadataRetriever.setDataSource(path);
        timestarmilis=System.nanoTime();
        timeInMillisec = Long.parseLong(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
    }

    public Bitmap getfram(long timemilis){
        timemilis=timemilis%timeInMillisec;
        long dif =System.currentTimeMillis()-timestarmilis;
        if (dif>timeInMillisec){
            dif=dif-timeInMillisec;
            timestarmilis=System.currentTimeMillis();
        }

        return mediaMetadataRetriever.getFrameAtTime(dif);
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            this.getfram(System.nanoTime());
            try {
                sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
