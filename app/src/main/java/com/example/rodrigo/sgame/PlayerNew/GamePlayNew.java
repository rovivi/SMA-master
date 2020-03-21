package com.example.rodrigo.sgame.PlayerNew;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.rodrigo.sgame.CommonGame.Common;
import com.example.rodrigo.sgame.CommonGame.ParamsSong;
import com.example.rodrigo.sgame.CommonSteps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import game.GameRow;
import game.Note;
import game.StepObject;


public class GamePlayNew extends SurfaceView implements SurfaceHolder.Callback {
    private static int[] colors = {Color.BLUE, Color.RED, Color.YELLOW, Color.RED, Color.BLUE, Color.BLUE, Color.RED, Color.YELLOW, Color.RED, Color.BLUE};
    float testFloatNOTUSE = 0f;
    public boolean isRunning = true;

    public MainThreadNew mainTread;
    private ArrayList<GameRow> steps;

    int FrameCounter = 0;


    //private Bitmap bgaBitmap;
    private MediaPlayer mpMusic;
    private int playerSizeX = 400, playerSizeY = 500;
    public int currentTickCount = 0, currentElement = 0;
    private Double currentSpeedMod = 1D, lastScroll = 1D;
    public double currentBeat = 20d;

    public Double BPM, fps;
    Handler handler1 = new Handler();

    public float currentDurationFake = 0, offset;
    //Generalplayer
    private Paint paint;
    //////

    //
    StepsDrawer stepsDrawer;


    public Long currentTempoBeat = 0L, currentTempo = 0L, startTime = 0L, timeLapsedBeat;

    public double currentSecond = 0, lostBeatByWarp = 0;
    String msj;
    ////////*-----------------SPEED-----------------*////////////////
    ArrayList<Double> currentSpeed = null;
    double initialSpeedMod = 1;

    ////////*-----------------SPEED-----------------*////////////////

    public GamePlayNew(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    private Runnable musicRun = new Runnable() {
        @Override
        public void run() {
            if (mpMusic != null) {
                mpMusic.start();
                isRunning = true;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public GamePlayNew(Context context, StepObject steps) {
        super(context);
        build1Object(context, steps);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void build1Object(Context context, StepObject stepData) {
        try {
            reset();
            this.setZOrderOnTop(true); //necessary
            getHolder().setFormat(PixelFormat.TRANSPARENT);
            getHolder().addCallback(this);

            mpMusic = new MediaPlayer();

            mainTread = new MainThreadNew(getHolder(), this);
            mainTread.setRunning(true);

            fps = 0d;
            setFocusable(true);
            paint = new Paint();
            paint.setTextSize(30);
            //-- Metrics of player
            playerSizeX = Common.WIDTH;
            playerSizeY = Common.HEIGHT;
            steps = stepData.steps;
            BPM = Objects.requireNonNull(Objects.requireNonNull(steps.get(0).getModifiers()).get("BPMS")).get(1);
            paint.setColor(Color.WHITE);

            try {
                mpMusic.setDataSource(stepData.getMusicPath());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mpMusic.setPlaybackParams(mpMusic.getPlaybackParams().setSpeed(ParamsSong.rush));// Esto será para el rush
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            //steps
            stepsDrawer = new StepsDrawer(getContext(), stepData.getStepType());

            offset = stepData.getSongOffset();
            mpMusic.prepare();
            mpMusic.setOnCompletionListener(mp -> stop());
            mpMusic.setOnPreparedListener(mp -> startGame());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reset() {
        currentBeat = 0;
        currentSecond = 20;
        currentElement = 0;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        reset();
        mainTread.setRunning(true);
        mainTread.start();
    }


    public void startGame() {
        currentTempoBeat = currentTempo = startTime = System.nanoTime();
        try {
            if (mainTread.running) {
                if (offset > 0) {
                    handler1.postDelayed(musicRun, (long) (offset * 1000));
                } else {
                    //currentBeat = (double) offset / (60 / BPM);
                    mpMusic.seekTo((int) Math.abs(offset * 1000));
                    mpMusic.setOnPreparedListener(mp -> {
                        mpMusic.start();
                        isRunning = true;
                    });
                    mpMusic.prepare();
                }
            } else
                mainTread.sulrfaceHolder = this.getHolder();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        stop();
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        //se limpia la pantalla
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        try {
            int speed = (int) (50 * ParamsSong.speed);
            double lastScrollAux = lastScroll;
            double lastBeat = this.currentBeat + 0;
            double lastPosition = 100;
            if (isRunning) {
                drawStats(canvas);
                try {
                    for (int x = 0; (currentElement + x) < steps.size(); x++) {
                        GameRow currentElemt = steps.get(currentElement + x);
                        double diffBeats = currentElemt.getCurrentBeat() - lastBeat;
                        lastPosition += diffBeats * speed * currentSpeedMod * lastScrollAux;
                        if (lastPosition >= playerSizeY / 2)
                            break;
                        if (currentElemt.getNotes() != null) {
                            stepsDrawer.draw(canvas, currentElemt.getNotes(), (int) lastPosition);
                        }
                        if (currentElemt.getModifiers() != null && currentElemt.getModifiers().get("SCROLLS") != null)
                            lastScrollAux = Objects.requireNonNull(currentElemt.getModifiers().get("SCROLLS")).get(1);
                        lastBeat = currentElemt.getCurrentBeat();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (isRunning) {
            calculateBeat();
            stepsDrawer.update();
        }

        if (currentSpeed != null)
            calculateCurrentSpeed();

    }

    void calculateCurrentSpeed() {
        double beatInicial = currentSpeed.get(0);
        double razonBeat = (initialSpeedMod - currentSpeed.get(1)) / currentSpeed.get(2);
        double metaSpeed = currentSpeed.get(1);
        double metaBeat = currentSpeed.get(0) + currentSpeed.get(2);
        currentSpeedMod = initialSpeedMod + (beatInicial - currentBeat) * razonBeat;
        if (CommonSteps.Companion.almostEqualEasy(metaSpeed, currentSpeedMod) || currentBeat >= metaBeat) {
            currentSpeedMod = metaSpeed;
            currentSpeed = null;
        }
    }

    void effects(HashMap<String, ArrayList<Double>> effects) {
        msj = "";
        for (Map.Entry<String, ArrayList<Double>> entry : effects.entrySet()) {
            switch (entry.getKey()) {
                case "BPMS":
                    double auxBPM = entry.getValue().get(1);
                    double difBetweenBeats2 = currentBeat - entry.getValue().get(0);//2.5
                    currentBeat = entry.getValue().get(0) + (difBetweenBeats2 / (BPM / auxBPM));//
                    BPM = auxBPM;
                    break;
                case "WARPS":
                    currentBeat += entry.getValue().get(1);
                    break;
                case "SPEEDS":
                    if (entry.getValue().get(2) == 0d)
                        currentSpeedMod = entry.getValue().get(1);
                    else {
                        //initialSpeedMod=(currentSpeed==null)? currentSpeedMod:initialSpeedMod;
                        initialSpeedMod = currentSpeedMod;
                        currentSpeed = entry.getValue();
                    }
                    break;
                case "SCROLLS":
                    lastScroll = entry.getValue().get(1);//==0d?1d:0d;
                    break;
                default:
            }
            msj = entry.getKey() + "__" + entry.getValue().get(0) + ":" + entry.getValue().get(1);
        }
    }


    public void calculateBeat() {
        currentSecond += (System.nanoTime() - startTime) / 10000000.0;//se calcula el segundo
        startTime = System.nanoTime();
        if (lostBeatByWarp > 0) {
            currentBeat += lostBeatByWarp * 2;
            lostBeatByWarp = 0;
        }
        timeLapsedBeat = System.nanoTime() - currentTempoBeat;
        currentBeat += 1D * timeLapsedBeat / ((60 / BPM) * 1000 * 1000000);
        currentDurationFake -= timeLapsedBeat / ((60 / BPM) * 1000 * 1000000);//reduce la duración de los fakes
        currentTempoBeat = System.nanoTime();
        while (steps.get(currentElement).getCurrentBeat() <= currentBeat) {
            checkEffects();
            currentElement++;
        }
        isRunning = !(currentElement >= steps.size());

    }

    public void drawStats(Canvas c) {
        paint.setTextSize(20);
        paint.setStyle(Paint.Style.FILL);
        c.drawPaint(paint);
        paint.setTextSize(25);
        paint.setColor(Color.WHITE);
        c.drawText("::: " + msj, 0, 20, paint);
        c.drawText("FPS: " + fps, 0, 250, paint);
        c.drawText("Log: " + currentTickCount, 0, 100, paint);
        c.drawText("event: " + testFloatNOTUSE, 0, playerSizeY - 200, paint);
        c.drawText("C Seg: " + String.format(new Locale("es"), "%.3f", currentSecond), 0, playerSizeY - 300, paint);
        c.drawText("C Beat: " + String.format(new Locale("es"), "%.3f", currentBeat), 0, playerSizeY - 150, paint);
        c.drawText("C BPM: " + this.BPM, 0, playerSizeY - 250, paint);
        c.drawText("C Speed: " + this.currentSpeedMod, 0, playerSizeY - 100, paint);
        c.drawText("Scroll: " + this.lastScroll, 0, playerSizeY - 400, paint);
        c.drawText("pad: ", playerSizeX - 250, playerSizeY - 20, paint);
        c.drawText("frame: " + FrameCounter++, 100, 100, paint);
        //  paint.setColor(Color.BLACK);
        paint.setColor(Color.TRANSPARENT);
    }

    void checkEffects() {
        if (steps.get(currentElement).getModifiers() != null)
            effects(Objects.requireNonNull(steps.get(currentElement).getModifiers()));
    }


    public void stop() {
        boolean retry = true;
        if (mainTread != null)
            mainTread.setRunning(false);
        while (retry) {
            try {

                if (mainTread != null)
                    mainTread.setRunning(false);
                releaseMediaPlayer();
                retry = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void releaseMediaPlayer() {
        try {
            if (mpMusic != null) {
                if (mpMusic.isPlaying())
                    mpMusic.stop();
                mpMusic.release();
                mpMusic = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
