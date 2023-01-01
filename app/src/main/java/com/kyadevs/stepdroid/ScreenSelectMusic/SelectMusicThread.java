package com.kyadevs.stepdroid.ScreenSelectMusic;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class SelectMusicThread extends Thread {
    public static final int MAXFPS = 200;
    private double avergeFps;
    int errors=0;
    public SurfaceHolder surfaceHolder;
    private ThemeElements game;
    public boolean running;
    public static Canvas canvas;

    public void setRunning(Boolean running) {
        this.running = running;
    }

    public SelectMusicThread(SurfaceHolder holder, ThemeElements game) {
        super();
        this.surfaceHolder = holder;
        this.game = game;
    }

    @Override
    public void run() {
        long startTime, waitTime, timeLapsed;
        int frameCount = 0;
        waitTime = 1000 / MAXFPS;
        startTime = System.nanoTime();
        while (running) {
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    if (canvas != null) {
                        this.game.update();
                        this.game.draw(canvas);
                    } else {
                        // this.join();
                    }
                }
            } catch (IllegalArgumentException e) {
                if (errors++>5){
                    try {
                        this.join();
                    } catch (InterruptedException ex1) {
                        e.printStackTrace();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                if (waitTime > 0) {
                    sleep(waitTime);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            timeLapsed = System.nanoTime() - startTime;
            frameCount++;
            if (timeLapsed > 1000000000) {
                avergeFps = frameCount;
                frameCount = 0;
                startTime = System.nanoTime();
            }
            frameCount++;
            game.fps = (float) avergeFps;


        }
    }


}
