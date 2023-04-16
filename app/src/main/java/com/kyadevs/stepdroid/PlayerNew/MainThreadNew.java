package com.kyadevs.stepdroid.PlayerNew;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThreadNew extends Thread {
    private double averageFPS;
    public  SurfaceHolder surfaceHolder;
    private GamePlayNew game;
    public boolean running;


    public void setRunning(Boolean running) {
        this.running = running;
    }

    MainThreadNew(SurfaceHolder holder, GamePlayNew game) {
        super();
        this.surfaceHolder = holder;
        this.game = game;
    }
    @Override
    public void run() {
        long startTime,  timeLapsed ;
        int frameCount = 0;
        startTime = System.nanoTime();
        while (running) {

            Canvas canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.game.update();
                    this.game.draw(canvas);
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
            timeLapsed = System.nanoTime() - startTime;
            frameCount++;
            if (timeLapsed>1000000000){
                averageFPS =frameCount;
                frameCount=0;
                startTime=System.nanoTime();
            }
            game.fps= averageFPS;
        }
    }
}