package com.kyadevs.stepdroid.CommonGame.CustomSprite;

import android.graphics.Canvas;



public class ThreadSprite extends Thread {


    public static final int MAX_FPS =35;
    private double avergeFPS;
    //public  SurfaceHolder surfaceHolder;
    private Sprite[] displayArea;
    public boolean running;
    public static Canvas canvas;
    int c = 0;

    public void setRunning(Boolean running) {
        this.running = running;
    }

    public ThreadSprite(Sprite... displayArea) {
        super();
        // this.surfaceHolder = holder;
        this.displayArea = displayArea;
    }

    @Override
    public void run() {
        long startTime, waitTime, timeLapsed;
        int frameCount = 0;
        waitTime = 1000 / MAX_FPS;
        startTime = System.nanoTime();
        while (running) {
            //c++;
            for (Sprite sprite : displayArea) {
                canvas = null;
                try {
                    synchronized (sprite.getHolder()) {
                        canvas = sprite.getHolder().lockCanvas();
                        if (canvas != null) {
                        sprite.update();
                        sprite.draw(canvas);}
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (canvas != null) {

                            sprite.getHolder().unlockCanvasAndPost(canvas);


                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                avergeFPS = frameCount;
                frameCount = 0;
                startTime = System.nanoTime();
            }
            frameCount++;
            // displayArea.fps= avergeFPS;


        }


    }


}
