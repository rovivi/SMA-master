package com.kyadevs.stepdroid.Player;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.kyadevs.stepdroid.CommonGame.Common;
import com.kyadevs.stepdroid.CommonGame.CustomSprite.SpriteReader;
import com.kyadevs.stepdroid.CommonGame.Note;
import com.kyadevs.stepdroid.CommonGame.ParamsSong;
import com.kyadevs.stepdroid.CommonGame.RowStep;
import com.kyadevs.stepdroid.CommonGame.SSC;
import com.kyadevs.stepdroid.CommonGame.TransformBitmap;
import com.kyadevs.stepdroid.PlayerBga;
import com.kyadevs.stepdroid.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;

//Este objeto contine todo lo relacionado con el gameplay (Sprites,Gameobjets,evaluador,etc)

public class GamePlay extends SurfaceView implements SurfaceHolder.Callback {
    private static double MIN_DISTANCE = (double) 1 / 192;

    float testFloatNOTUSE = 0f;
    //NUevo com
    //Objects variables
    //private ThreadAudio musicPlayer;
    public boolean isRunning = false;
    private GamePad touchPad;
    public MainThread mainTread;
    private RadarEffectsThread effectsThread;
    private LifeBar life;
    public Steps steps;
    public SSC stepData;
    public Combo ObjectCombo;
    //private Bitmap bgaBitmap;
    private PlayerBga BGA;
    private MediaPlayer mpMusic;
    private static Note pressedNote = new Note((byte) 100);
    public static Note[] pressedRow = {pressedNote, pressedNote, pressedNote, pressedNote, pressedNote, pressedNote, pressedNote, pressedNote, pressedNote, pressedNote};

    public String pathImage;
    //DrawerMarks and Steps information
    //public ArrayList<String[]> bufferSteps;
    public ArrayList<RowStep> bufferSteps;
    private ArrayList<String[]> dataBGA;
//    private ArrayList<String[]> ATTACKS;
    protected ArrayList<Float[]> BPMS;
    public ArrayList<Float[]> DELAYS;
    protected ArrayList<Float[]> STOPS;
    protected ArrayList<Float[]> SPEEDS;
    protected ArrayList<Float[]> WARPS;
    protected ArrayList<Float[]> TICKCOUNTS;
    protected ArrayList<Float[]> SCROLLS;
    protected ArrayList<Float[]> FAKES;
    public ArrayList<Float[]> COMBOS;
    private int playerSizeX = 400;
    private int playerSizeY = 500;
    public int currentTickCount = 0;
    protected int posBuffer = 0;
    protected int posBPM = 0;
    private float speed = 2300;
    private int currentSpeedMod = 1300;
    public int currentCombo = 1;

    public int posDelay = 0;
    protected int posSpeed = 1;
    protected int posWarp = 0;
    protected int posTickCount = 0;
    protected int posCombo = 0;
    protected byte fingersOnScreen = 0;

    public int posStop = 0;
    protected int posScroll = 0;
    protected int posFake = 0;
    //private int posAttack = 0;
    public int contadorTick = 0;
    public double residuoTick = 0;
    private int arrowSize;
    private int posIntX;

    public double currentBeat = 0d, metaBeatSpeed = -100;
    public float BPM, offset, beatsofSpeedMod, currentDurationFake = 0;
    //Generalplayer
    public SoundPool soundPool;
    private String pathMusic, event, tipo;
    public double fps;
    public float currentLife = 50;
    File fileBGA;
    private Paint dibujante;
    public int Combo = 0;
    private int soundPullBeat;
    public int soundPullMine;
    public int mineHideValue = 0;
    //////
    private Long ttranscurrido = 0l;
    private Long currenttiempo = 0l;
    public Long curentempobeat = 0l;
    private Long Startime = 0l;
    private Long ttranscurridobeat;//Time Stamps
    //Score
    byte[] inputs;
    int bad = 0, miss = 0, perfect = 0, great = 0, good = 0, maxCombo = 0;
    public boolean doEvaluate = true;
    public double currentDelay = 0;
    double residuoy = 0;
    double speedMod = 1;
    double lastSpeed;
    public double currentSecond = 0;
    SpriteReader items;
    double lostBeats = 0;
    public double lostBeatbyWarp = 0;
    long delayTime = 0;


    //metods
    private Runnable musicRun = new Runnable() {
        @Override
        public void run() {
            if (mpMusic != null)
                mpMusic.start();
        }
    };
    Handler handler1 = new Handler();

    public void reset() {
        delayTime = 0;
        currentBeat = 0d;
        metaBeatSpeed = -100;
        currentTickCount = 1;
        posBuffer = 0;
        posBPM = 0;
        speed = 2300;
        currentSpeedMod = 1300;
        posDelay = 0;
        posSpeed = 0;
        posWarp = 0;
        posTickCount = 0;
        posStop = 0;
        posScroll = 0;
        posFake = 0;
        bad = 0;
        miss = 0;
        perfect = 0;
        great = 0;
        good = 0;
        currentSecond = 0;
        currentDelay = 0;
        Combo = 0;
        contadorTick = 0;
    }

    public GamePlay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GamePlay(Context context, SSC ssc, int nchar, String path, byte[] pad, int width, int height) {
        super(context);
        try {
            build1Object(context, ssc, nchar, path, null, pad, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void build1Object(Context context, SSC stepData, int nchar, String path, PlayerBga bga, byte[] panel, int width, int height) throws IOException {

        try {
            //init object
            reset();
            this.setZOrderOnTop(true); //necessary
            getHolder().setFormat(PixelFormat.TRANSPARENT);
            BGA = bga;
            //bgaBitmap = null;
            life = new LifeBar(context, "");
            this.inputs = panel;
            soundPool = new SoundPool(30, AudioManager.STREAM_MUSIC, 65);
            soundPullBeat = soundPool.load(this.getContext(), R.raw.beat2, 3);
            soundPullMine = soundPool.load(this.getContext(), R.raw.mine, 3);
            getHolder().addCallback(this);


            //SSC parsing
            this.stepData = stepData;//se lee el archio ssc y se convierte a array y maps
            bufferSteps = stepData.createBuffer(nchar);//se crea un array para el player
            if (stepData.chartsInfo[nchar].get("SCROLLS") != null && !stepData.chartsInfo[nchar].get("SCROLLS").equals(""))
                SCROLLS = stepData.arrayListSpeed(stepData.chartsInfo[nchar].get("SCROLLS").toString());
            BPMS = setMetadata((String) stepData.chartsInfo[nchar].get("BPMS"), stepData.songInfo.get("BPMS"));
            BPM = BPMS.get(0)[1];
            FAKES = setMetadata((String) stepData.chartsInfo[nchar].get("FAKES"), stepData.songInfo.get("FAKES"));
            SPEEDS = setMetadata((String) stepData.chartsInfo[nchar].get("SPEEDS"), stepData.songInfo.get("SPEEDS"));
            if (!Common.testingRadars) {
                TICKCOUNTS = setMetadata((String) stepData.chartsInfo[nchar].get("TICKCOUNTS"), stepData.songInfo.get("TICKCOUNTS"));
                STOPS = setMetadata((String) stepData.chartsInfo[nchar].get("STOPS"), stepData.songInfo.get("STOPS"));
                DELAYS = setMetadata((String) stepData.chartsInfo[nchar].get("DELAYS"), stepData.songInfo.get("DELAYS"));
                WARPS = setMetadata((String) stepData.chartsInfo[nchar].get("WARPS"), stepData.songInfo.get("WARPS"));
                COMBOS = setMetadata((String) stepData.chartsInfo[nchar].get("COMBOS"), stepData.songInfo.get("COMBOS"));
            }


            ////////TEST

            ArrayList<Float[]> auxBPM2WARP = new ArrayList<>();
            //  effectMap.put("BPMS", BPMS);
            if (BPMS != null) {
                for (int x = 0; x < BPMS.size(); x++) {
                    if (BPMS.get(x)[1] > 100000) {
                        Float[] auxF = BPMS.get(x).clone();
                        float warpValue = BPMS.get(x + 1)[0] - BPMS.get(x)[0];
                        warpValue *= 0.9999;
                        auxF[1] = warpValue;
                        auxBPM2WARP.add(auxF);
                        BPMS.remove(x);
                        x--;
                    }
                }

            }

            if (WARPS != null) {
                WARPS.addAll(auxBPM2WARP);
                Collections.sort(WARPS, new Comparator<Float[]>() {
                    public int compare(Float[] o1, Float[] o2) {
                        return o1[0].compareTo(o2[0]);
                    }
                });
            } else
                WARPS = auxBPM2WARP;
            ///TEST AREA
            //ATTACKS= setMetadata(stepData.chartsInfo[nchar].get("ATTACKS"),stepData.songInfo.get("ATTACKS"));

            if (stepData.chartsInfo[nchar].get("OFFSET") != null) {
                String xof = stepData.chartsInfo[nchar].get("OFFSET").toString();
                offset = Float.parseFloat(xof);
            } else {
                String xof = stepData.songInfo.get("OFFSET");
                offset += Float.parseFloat(xof);
            }
            offset += ((float) Common.OFFSET / 1000);

            if (stepData.chartsInfo[nchar].get("STEPSTYPE") != null && !stepData.chartsInfo[nchar].get("STEPSTYPE").equals("")) {
                tipo = stepData.chartsInfo[nchar].get("STEPSTYPE").toString();
                if (tipo.equals("pump-double") && stepData.chartsInfo[nchar].get("DESCRIPTION") != null && stepData.chartsInfo[nchar].get("DESCRIPTION").toString().contains("DP"))
                    tipo = "pump-routine";
            } else
                tipo = "";
            //end parsing

            //init media
            mpMusic = new MediaPlayer();
            dataBGA = new ArrayList<>();
            String urlVideo = "";
            String[] infoBgas = null;
            if (stepData.songInfo.get("BGCHANGES") != null)
                infoBgas = stepData.songInfo.get("BGCHANGES").split(",");

            //end media

            //init setting


            //end setting


            if (infoBgas != null && Common.checkBGADir(path, infoBgas[0], context) != null)
                fileBGA = new File(Common.checkBGADir(path, infoBgas[0], context));
            else if (infoBgas != null) {
                for (int i = 0; i < infoBgas.length; i++) {
                    dataBGA.add(infoBgas[i].split("="));
                    for (String databgas : dataBGA.get(i)) {
                        if ((databgas.endsWith("mpg") || databgas.endsWith("mp4") || databgas.endsWith("avi"))) {
                            if (Common.checkBGADir(path, databgas, context) != null) {
                                fileBGA = new File(Common.checkBGADir(path, databgas, context));
                                BGA.bg.seekTo((int) (-offset * 1000));
                                break;
                            } else
                                urlVideo = "https://kyagamy.mx/assets/bga/" + databgas;
                        }
                    }
                }
            }


            if (fileBGA != null && BGA != null && fileBGA.exists())//Si hay reproductor y si hay bga
                BGA.setVideoPath(fileBGA.getPath());
            else if ((!urlVideo.equals("")) && Common.bgaExist(urlVideo)) {
                Uri uri = Uri.parse(urlVideo);
                BGA.bg.setVideoURI(uri);
            } else if (BGA != null) {
                String path2 = "android.resource://" + context.getPackageName() + "/" + R.raw.bgaoff;
                BGA.setVideoPath(path2);
            }
            //beat = (double) -offset / (60 / BPM);
            currentSecond = offset;
            pathMusic = path + "/" + stepData.songInfo.get("MUSIC");
            pathImage = path + "/" + stepData.songInfo.get("BACKGROUND");

            try {
                mpMusic.setDataSource(pathMusic);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    mpMusic.setPlaybackParams(mpMusic.getPlaybackParams().setSpeed(ParamsSong.rush));// Esto será para el rush
            } catch (IOException e) {
                e.printStackTrace();
                throw  e;
            }

            mpMusic.prepare();
            mpMusic.setOnCompletionListener(mp -> {
                startEvaluation();
                stop();
            });
            // bgaBitmap = BitmapFactory.decodeFile(path + "/" + stepData.songInfo.get("BACKGROUND").toString());
            mainTread = new MainThread(getHolder(), this);
            fps = 0;
            setFocusable(true);
            touchPad = new GamePad(context, tipo, inputs);

            if (ParamsSong.av == -1)
                currentSpeedMod = (int) (height * ParamsSong.speed / 4);
            else {
                //currentSpeedMod = BPM;
                float vel = (float) ParamsSong.av / (float) (height * 0.25);
                currentSpeedMod = (int) (height * vel / 2);
            }
            if (ParamsSong.gameMode > 2)
                currentSpeedMod *= 2.2f;
            //flecha
            steps = new Steps(context, tipo);

            dibujante = new Paint();
            dibujante.setTextSize(20);

            if (tipo.equals("dance-single")) {
                //  steps.makeDDR(context);
            }


            //-- Metrics of player
            playerSizeX = Common.WIDTH;
            playerSizeY = Common.HEIGHT;


            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {//horizonal
                Common.HIDE_PAD = true;
                if (tipo.equals("pump-single")) {
                    Common.START_Y = 0.105f;
                    arrowSize = (int) (playerSizeX / 10 * 0.9);
                    posIntX = (int) (playerSizeX * 0.275);
                    playerSizeY = (int) (Common.HEIGHT);

                } else if (tipo.equals("pump-double")) {
                    Common.START_Y = 0.105f;
                    playerSizeY = (int) (Common.HEIGHT);
                    arrowSize = (int) (playerSizeX / 10 * 0.9);
                    posIntX = (int) (playerSizeX * 0.125);

                }
            } else {//portriot
                Common.HIDE_PAD = false;
                //playerSizeX = c.getWidth() * (1);
                if (tipo.equals("pump-single")) {
                    switch (ParamsSong.gameMode) {
                        case 0:
                            Common.START_Y = 0.105f;
                            playerSizeY = (int) (Common.HEIGHT * (0.545));
                            arrowSize = (int) (playerSizeX / 5 * 0.75);
                            posIntX = (int) (playerSizeX * 0.125);
                            break;
                        case 1:
                            Common.START_Y = 0.095f;
                            playerSizeY = (int) (Common.HEIGHT * (0.54));
                            arrowSize = (int) (playerSizeX / 10 * 0.9);
                            posIntX = (int) (playerSizeX * 0.275);
                            break;
                        case 2:

                            Common.START_Y = 0.05f;
                            playerSizeY = (Common.HEIGHT);
                            arrowSize = (int) (playerSizeX / 5 * 0.65);
                            posIntX = (int) (playerSizeX * 0.175);
                            break;
                        case 3:
                            Common.START_Y = 0.9f;
                            playerSizeY = (Common.HEIGHT);
                            arrowSize = (int) (playerSizeX / 5);
                            posIntX = 0;
                            break;
                    }
                } else if (tipo.equals("pump-double") || tipo.equals("pump-routine")) {
                    arrowSize = (int) (playerSizeX / 10 * 0.8);
                    posIntX = (int) (playerSizeX * 0.1);
                    playerSizeY = (int) (Common.HEIGHT * (0.54));
                }
            }
            ObjectCombo = new Combo(context, playerSizeX, playerSizeY);
            ObjectCombo.start();
            mainTread.setRunning(true);
            mainTread.start();

            File bgImage = new File(pathImage);//se le asigna una mimagen al panel
            if (bgImage.exists() && bgImage.isFile()) {
                Bitmap ww = BitmapFactory.decodeFile(bgImage.getPath());
                if (ww != null) {
                    ww = TransformBitmap.makeTransparent(TransformBitmap.myblur(ww, context), 130);
                    //bga.bgPad.setImageBitmap(ww);
                }
            }
            dibujante.setColor(Color.TRANSPARENT);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    public void startGame() {
        curentempobeat = currenttiempo = Startime = System.nanoTime();
        try {
            if (mainTread.running) {
                ttranscurrido = System.nanoTime();
                effectsThread = new RadarEffectsThread(this);
                effectsThread.start();
                isRunning = true;
                currentBeat = 0;
                if (offset > 0)
                    handler1.postDelayed(musicRun, (long) (offset * 1000));
                else {
                    currentBeat = (double) offset / (60 / BPM);
                    mpMusic.start();
                }
                BGA.startVideo();
            } else
                mainTread.sulrfaceHolder = this.getHolder();
        } catch (Exception e) {
            e.printStackTrace();
            throw  e;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        stop();
    }

    public void update() {
        currentLife = (currentLife > 100) ? 100 : currentLife;
        life.updateLife(currentLife);
        ObjectCombo.update(Combo);
        steps.update();
        if (currentDelay != 0 && !Common.EVALUATE_ON_SECUNDARY_THREAD)
            evaluate();//evaluate only if
    }

    private void clearMemory() {
        soundPool = null;
        stepData = null;
        dataBGA = null;
        fileBGA = null;
        BGA = null;
        touchPad = null;
        System.gc();
    }

    public void draw(Canvas canvas) {

        super.draw(canvas);
        try {
            if (canvas != null) {
                if (isRunning) {
                    residuoy = 0;
                    Paint painty = new Paint();
                    // painty.setTextSize(20);
                    painty.setStyle(Paint.Style.FILL);
                    painty.setColor(Color.TRANSPARENT);
                    // painty.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    canvas.drawRect(new Rect(0, 0, Common.WIDTH, playerSizeY), painty);
                    drawStats(canvas);
                    if ((posBuffer + 1 < bufferSteps.size())) {
                        drawCharts(canvas, posBuffer, false);
                    } else if ((posBuffer + 1 >= bufferSteps.size())) {
                        this.startEvaluation();
                        stop();
                    }
                }
                ObjectCombo.draw(canvas);
                if ((!Common.HIDE_PAD))
                    touchPad.draw(canvas);
                life.draw(canvas);
                if (mineHideValue > 0) {
                    Paint pRect = new Paint();
                    pRect.setColor(Color.WHITE);
                    pRect.setAlpha(mineHideValue);
                    canvas.drawRect(new Rect(0, 0, Common.WIDTH, Common.HEIGHT), pRect);
                    mineHideValue -= 9;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", e.getMessage());
        }
    }

    public void stop() {
        clearMemory();
        boolean retry = true;
        if (mainTread != null)
            mainTread.setRunning(false);
        while (retry) {
            try {
                if (effectsThread != null) {
                    effectsThread.running = false;
                    effectsThread.join();
                }
                if (mainTread != null)
                    mainTread.setRunning(false);
                releaseMediaPlayer();
                retry = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        try {
            int maskedAction = event.getActionMasked();
            int fingers = event.getPointerCount();
            this.fingersOnScreen = (byte) fingers;
            int[][] inputsTouch = new int[fingers][2];
            for (int i = 0; i < fingers; i++) {
                inputsTouch[i][0] = (int) event.getX(i);
                inputsTouch[i][1] = (int) event.getY(i);
//                this.event += " " + i + ":(" + (int) event.getX(i) + "," + (int) event.getY(i) + ")";
            }
            switch (maskedAction) {
                case MotionEvent.ACTION_POINTER_UP:
                    int actionIndex = event.getPointerId(event.getActionIndex());
                    this.touchPad.unpress(event.getX(actionIndex), event.getY(actionIndex));
                    break;
                case MotionEvent.ACTION_DOWN:
                    if (event.getX() > playerSizeX / 2 && event.getY() < playerSizeY / 2)
                        currentSpeedMod += 150;
                    else if (event.getX() < playerSizeX / 2 && event.getY() < playerSizeY / 2) {
                        if (currentSpeedMod > 99)
                            currentSpeedMod -= 150;
                    } else if (event.getX() < playerSizeX / 2 && event.getY() > playerSizeY / 2 && event.getY() < playerSizeY) {
                        //ParamsSong.autoplay = !ParamsSong.autoplay;
                    } else if (event.getX() > playerSizeX / 2 && event.getY() > playerSizeY / 2 && event.getY() < playerSizeY) {
                        //    steps.doMagic = !steps.doMagic;
                    }
                    touchPad.checkInputs(inputsTouch, true);
                default:
                    this.event = "numero" + maskedAction;
                    this.touchPad.checkInputs(inputsTouch, false);
                    break;
                case MotionEvent.ACTION_UP:
                    if (fingers == 1)
                        touchPad.clearPad();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    public void calculateBeat() {
        if (lostBeatbyWarp > 0) {
            currentBeat += lostBeatbyWarp * 2;
            lostBeatbyWarp = 0;
        }
        ttranscurridobeat = System.nanoTime() - curentempobeat;
        currentBeat += ParamsSong.rush * ttranscurridobeat / ((60 / BPM) * 1000 * 1000000);
        currentDurationFake -= ttranscurridobeat / ((60 / BPM) * 1000 * 1000000);//reduce la duración de los fakes
        curentempobeat = System.nanoTime();
    }

    public void drawStats(Canvas c) {
        dibujante.setTextSize(20);
        dibujante.setStyle(Paint.Style.FILL);

//        dibujante.setColor(Color.BLACK);
        c.drawPaint(dibujante);
        //dibujante.setColor(Color.TRANSPARENT);
        speed = (int) (currentSpeedMod * speedMod);
        currentSecond += (System.nanoTime() - Startime) / 1000000000.0;//se calcula el segundo
        Startime = System.nanoTime();
        //beat

        try {
            if (isRunning && currentDelay == 0) {
                calculateBeat();
                if (posBuffer >= (bufferSteps.size() + 1)) {
                    this.startEvaluation();
                    this.stop();
                } else if (bufferSteps.get(posBuffer + 1).beat <= currentBeat && posBuffer < bufferSteps.size()) {
                    while (bufferSteps.get(posBuffer + 1).beat <= currentBeat) {
                        posBuffer++;
                        evaluate();
                        calculateBeat();
                    }
                }
            } else {
                if (currentDelay <= currentSecond) {
                    lostBeats = (currentSecond - currentDelay);
                    currentBeat += lostBeats / ((60 / BPM));
                    //reduce la duración de los fakes
                    currentDelay = 0;
                    curentempobeat = System.nanoTime();
                    currentDurationFake -= (currentSecond - currentDelay) / ((60 / BPM) * 1000 * 1000000);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Common.DRAWSTATS) {
            dibujante.setTextSize(25);
            dibujante.setColor(Color.WHITE);
            c.drawText("FPS: " + fps, 0, 250, dibujante);
            c.drawText("Log: " + currentTickCount, 0, 100, dibujante);
            c.drawText("event: " + event, 0, playerSizeY - 200, dibujante);
            //iversosn
            c.drawText("C Seg: " + String.format("%.3f", currentSecond), 00, playerSizeY - 300, dibujante);
            c.drawText("C Beat: " + String.format("%.3f", currentBeat), 00, playerSizeY - 150, dibujante);
            c.drawText("C BPM: " + this.BPM, 00, playerSizeY - 250, dibujante);
            c.drawText("C Speed: " + this.speedMod, 00, playerSizeY - 100, dibujante);
            String st = "";
            for (int j = 0; j < 10; j++)
                st += inputs[j];
//        showSomeArray(BPMS, c, posBPM);
            c.drawText("pad: " + st, playerSizeX - 250, playerSizeY - 20, dibujante);
            dibujante.setColor(Color.BLACK);
        }
        //  c.drawRect(0, playerSizeY - 55, c.getWidth(), c.getHeight(), dibujante);
        dibujante.setColor(Color.TRANSPARENT);

    }

    private void showSomeArray(ArrayList<Float[]> array, Canvas c, int poscosa) {
        if (array != null) {
            int posy = 0;
            for (int x = poscosa; x < array.size(); x++) {
                c.drawText("Speed: " + array.get(x)[0] + "=" + array.get(x)[1] + "=", playerSizeX - 300, playerSizeY - 150 - 50 * posy, dibujante);
                posy++;
            }
        }
    }


    private void drawCharts(Canvas c, int bufferPos, Boolean interpolate) {
        double currentY = (playerSizeY * Common.START_Y);
        dibujante.setTextSize(25);
        dibujante.setStyle(Paint.Style.FILL);

        if (interpolate) {
            // if we stay that mean, the game is if a intermedium frame
            if (bufferSteps.get(posBuffer).beat < currentBeat) {
                //  double scrollSize = (float) bufferSteps.get(bufferPos)[2];
                double distanciaentrebeactualyela = currentBeat - bufferSteps.get(posBuffer).beat;
                testFloatNOTUSE = (float) distanciaentrebeactualyela;
                if (testFloatNOTUSE > 1)
                    event = distanciaentrebeactualyela + "";
                double porcentajeaaumentar = (distanciaentrebeactualyela) / (MIN_DISTANCE + bufferSteps.get(posBuffer).beat);
                event = testFloatNOTUSE + "";
                double distanciarestar = speed / 192 * porcentajeaaumentar;
                currentY -= distanciarestar * MIN_DISTANCE;
            }
        }

        Stack<Object[]> stackSteps = new Stack<>();
        Stack<Object[]> stackStepstop = new Stack<>();
        if (ParamsSong.gameMode < 3) {
            pushToStackSteps(posBuffer, -50, stackStepstop, currentY, false, true);
            while (!stackStepstop.isEmpty())
                stackSteps.push(stackStepstop.pop());
            currentY = (int) (playerSizeY * Common.START_Y);
            pushToStackSteps(posBuffer, playerSizeY, stackSteps, currentY, true, true);
        } else {
            pushToStackSteps(posBuffer, (int) (playerSizeY * 1.1f), stackStepstop, currentY, false, false);
            while (!stackStepstop.isEmpty())
                stackSteps.push(stackStepstop.pop());
            currentY = (int) (playerSizeY * Common.START_Y);
            pushToStackSteps(posBuffer, 0, stackSteps, currentY, true, false);
        }
        steps.draw(c, stackSteps, speed, posIntX, arrowSize, playerSizeX, playerSizeY);
        dibujante.setColor(Color.TRANSPARENT);
    }


    private void pushToStackSteps(int bufferPos, int End, Stack<Object[]> stackSteps, double currentY, Boolean downStepType, Boolean inverter) {
        int suma = downStepType ? 1 : -1;
        for (int i = bufferPos; (i) >= 0 && i < bufferSteps.size() && (speedMod > 0.04); i += suma) {

            if (inverter) {
                if ((downStepType && End < currentY) || ((!downStepType && End > currentY)))
                    break;
            } else {
                if ((downStepType && End > currentY) || ((!downStepType && End < currentY)))
                    break;
            }
            double scrollSize = bufferSteps.get(i).scroll;
            double nextY = (currentY + (scrollSize * speed / 192));
            if (containSteps(bufferSteps.get(i).rowStep)) {
                Object[] auxString = new Object[3];
                auxString[0] = bufferSteps.get(i).rowStep;
                auxString[1] = currentY;
                auxString[2] = nextY;
                stackSteps.push(auxString);
            }
            double aumentoY = (currentSpeedMod * speedMod / 192);
            if ((inverter && downStepType) | (!inverter && !downStepType))
                currentY += aumentoY * scrollSize;
            else
                currentY -= aumentoY * scrollSize;
        }
    }

    public void playTick() {
        if (doEvaluate)
            soundPool.play(soundPullBeat, 0.7f, 0.7f, 1, 0, 1.1f);
    }

    public void evaluate() {
        if (doEvaluate) {
            if (ParamsSong.autoplay) {
                ObjectCombo.posjudge = 0;
                if (containTapNote(bufferSteps.get(posBuffer).rowStep)) {
                    playTick();
                    combopp();
                    currentLife += 0.5 * currentCombo;
                    ObjectCombo.show();
                    Note[] auxrow = bufferSteps.get(posBuffer).rowStep;
                    for (int w = 0; w < auxrow.length; w++) {//animations
                        int aux = auxrow[w].getNoteType();
                        if (aux == 1)
                            steps.noteSkins[0].explotions[w].play();
                        else if (aux == 2) {
                            steps.noteSkins[0].explotions[w].play();
                            steps.noteSkins[0].explotionTails[w].play();
                        } else if (aux == 0)
                            steps.noteSkins[0].explotionTails[w].stop();
                    }
                    bufferSteps.get(posBuffer).rowStep = pressedRow;
                } else if (containLongs(bufferSteps.get(posBuffer).rowStep)) {
                    residuoTick += ((double) currentTickCount / 48);
                    if (residuoTick >= 1) {
                        residuoTick -= 1;
                        ObjectCombo.show();
                        combopp();
                        currentLife += 0.5 * currentCombo;
                    }
                    bufferSteps.get(posBuffer).rowStep = pressedRow;
                }
            } else {//juicio normal
                double[] currentJudge = Common.JUDMENT[ParamsSong.judgment];
                int posBack;
                //   int posNext;
                int backSteps;
                int rGreat = mil2BackSpaces((float) currentJudge[3]);
                int rGood = rGreat + mil2BackSpaces((float) currentJudge[2]);
                int rBad = rGood + mil2BackSpaces((float) currentJudge[1]);
                backSteps = rBad + 1;

                posBack = -backSteps;
                if (backSteps >= posBuffer)
                    posBack = -posBuffer;
                //posNext = backSteps;

                if (containTaps(bufferSteps.get(posBuffer + posBack).rowStep)) {//evaluate miss
                    comboLess();
                    bufferSteps.get(posBuffer + posBack).rowStep = pressedRow;
                    posBack++;
                }
                if (containsMine(bufferSteps.get(posBuffer + posBack).rowStep)) {//evaluate miss
                    bufferSteps.get(posBuffer + posBack).rowStep = pressedRow;
                    posBack++;
                }
                if (containLongs(bufferSteps.get(posBuffer + posBack).rowStep)) {
                    residuoTick += ((double) currentTickCount / 48);
                    if (residuoTick >= 1) {
                        residuoTick -= 1;
                        ObjectCombo.show();
                        comboLess();
                        currentLife += 0.5 * currentCombo;
                        miss += currentCombo;
                    }
                }
            }
        }
    }

    private boolean containStartLong(Note[] row) {
        for (Note x : row)
            if (x.getNoteType() % 10 == 2)
                return true;
        return false;
    }

    private void combopp() {
        if (Combo < 0)
            Combo = 0;
        Combo += currentCombo;
        if (Combo > maxCombo)
            maxCombo = Combo;
    }

    private void comboLess() {
        miss += currentCombo;
        ObjectCombo.posjudge = 4;
        Combo = (Combo > 0) ? 0 : currentCombo;
        ObjectCombo.show();
        currentLife -= 1 - Combo;
    }


    private void addContadorTick() {
        contadorTick++;
        if (contadorTick > 48)
            contadorTick = 0;
    }

    private int mil2BackSpaces(float judgeTime) {
        int backs = 0;
        float auxJudge = 0;
        while ((posBuffer - backs) >= 0) {
            auxJudge += Common.beat2Second((double) 4 / 192, BPM) * 1000;
            backs++;
            if (auxJudge >= judgeTime + 23)
                break;
        }
        return backs;
    }

    private boolean containTaps(Note... row) {
        for (Note x : row)
            if (!x.getFake() && (x.getNoteType() % 10 == 1))
                return true;
        return false;
    }

    private boolean containsMine(Note... row) {
        for (Note x : row)
            if (!x.getFake() && (x.getNoteType() % 10 == 7))
                return true;
        return false;
    }

    private boolean containLongs(Note... row) {
        for (Note x : row)
            if (!x.getFake() && (x.getNoteType() > 0) && (x.getNoteType() % 10 == 2 || x.getNoteType() % 10 == 3 || x.getNoteType() % 10 == 4))
                return true;
        return false;
    }


    private boolean containTapNote(Note... row) {
        for (Note x : row)
            if (!x.getFake() && (x.getNoteType() % 10 == 1 || x.getNoteType() % 10 == 2))
                return true;
        return false;
    }

    private boolean containSteps(Note... row) {
        for (Note x : row)
            if ((x.getNoteType() != 0 && x.getNoteType() != 127))
                return true;
        return false;
    }


    private void startEvaluation() {
        BGA.startEvaluation(new int[]{perfect, great, good, bad, miss, maxCombo});

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
    @Nullable
    private ArrayList
    setMetadata(String dataCurrentChar, String dataGeneral) {
        if (dataCurrentChar != null)
            return stepData.arrayListSpeed(dataCurrentChar);
        else if (dataGeneral != null && !dataGeneral.equals(""))
            return stepData.arrayListSpeed(dataGeneral);
        return null;
    }
}