package com.example.rodrigo.sgame.Player;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
    public static final int MAXFPS =144;
    private double avergeFPS;
    public SurfaceHolder sulrfaceHolder;
    private GamePlay game;
    public boolean running;
    public static Canvas canvas;

    public void setRunning(Boolean running) {
        this.running = running;
    }

    public MainThread(SurfaceHolder holder, GamePlay game) {
        super();
        this.sulrfaceHolder = holder;
        this.game = game;
    }
    @Override
    public void run() {
        long startTime, waitTime,  timeLapsed ;
        int frameCount = 0;
        waitTime = 1000/MAXFPS;
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
            try {
                if (waitTime > 0) {
                    sleep(waitTime);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            timeLapsed = System.nanoTime() - startTime;
            frameCount++;
            if (timeLapsed>1000000000){
                avergeFPS =frameCount;
                frameCount=0;
                startTime=System.nanoTime();
            }
            frameCount++;
            game.fps= avergeFPS;


        }
    }


} 