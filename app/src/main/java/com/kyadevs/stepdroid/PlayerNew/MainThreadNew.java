package com.kyadevs.stepdroid.PlayerNew;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThreadNew extends Thread {
    public static final int MAXFPS =1000;
    private double avergeFPS;
    public  SurfaceHolder sulrfaceHolder;
    private GamePlayNew game;
    public boolean running;
    public static Canvas canvas;


    public void setRunning(Boolean running) {
        this.running = running;
    }

    MainThreadNew(SurfaceHolder holder, GamePlayNew game) {
        super();
        this.sulrfaceHolder = holder;
        this.game = game;
    }
    @Override
    public void run() {
        long startTime, waitTime,  timeLapsed ;
        int frameCount = 0;
        waitTime = 0;
        startTime = System.nanoTime();
        while (running) {

            canvas = null;
            try {
                canvas = this.sulrfaceHolder.lockCanvas();
                synchronized (sulrfaceHolder) {
                    this.game.update();
                    this.game.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (canvas != null) {
                        sulrfaceHolder.unlockCanvasAndPost(canvas);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            timeLapsed = System.nanoTime() - startTime;
            frameCount++;
            if (timeLapsed>1000000000){
                avergeFPS =frameCount;
                frameCount=0;
                startTime=System.nanoTime();
            }
            game.fps= avergeFPS;


        }
    }


} 