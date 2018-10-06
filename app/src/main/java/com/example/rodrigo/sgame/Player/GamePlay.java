package com.example.rodrigo.sgame.Player;

import com.example.rodrigo.sgame.CommonGame.Common;
import com.example.rodrigo.sgame.CommonGame.ParamsSong;
import com.example.rodrigo.sgame.CommonGame.SpriteReader;

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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.rodrigo.sgame.R;
import com.example.rodrigo.sgame.CommonGame.SSC;
import com.example.rodrigo.sgame.ThreadAudio;
import com.example.rodrigo.sgame.PlayerBga;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

//Este objeto contine todo lo relacionado con el gameplay (Sprites,Gameobjets,evaluador,etc)

public class GamePlay extends SurfaceView implements SurfaceHolder.Callback {
    private static double MIN_DISTANCE = (double) 1 / 192;
    float testFloatNOTUSE= 0f;

    //Objects variables
    //private ThreadAudio musicPlayer;
    private GamePad touchPad;
    public MainThread mainTread;
    private RadarEffectsThread effectsThread;
    private LifeBar life;
    private Steps steps;
    public SSC stepData;
    private Combo ObjectCombo;
    //private Bitmap bgaBitmap;
    private PlayerBga BGA;
    private Context context;
    private MediaPlayer mpMusic;
    private static byte[] preseedRow = {100, 100, 100, 100, 100, 100, 100, 100, 100, 100};

    public String pathImage;
    //DrawerMarks and Steps information
    //public ArrayList<String[]> bufferSteps;
    public ArrayList<Object[]> bufferSteps;
    private ArrayList<String[]> databga;
    private ArrayList<String[]> ATTACKS;
    protected ArrayList<Float[]> BPMS;
    protected ArrayList<Float[]> DELAYS;
    protected ArrayList<Float[]> STOPS;
    protected ArrayList<Float[]> SPEEDS;
    protected ArrayList<Float[]> WARPS;
    protected ArrayList<Float[]> TICKCOUNTS;
    protected ArrayList<Float[]> SCROLLS;
    protected ArrayList<Float[]> FAKES;
    private int playerSizeX = 400;
    private int playerSizeY = 500;
    protected int currentTickCount = 0;
    protected int posBuffer = 0;
    protected int posBPM = 0;
    private float speed = 2300;
    private int currentSpeedMod = 1300;
    protected int posDelay = 0;
    protected int posSpeed = 1;
    protected int posWarp = 0;
    protected int posTickCount = 0;
    protected int posstop = 0;
    protected int posScroll = 0;
    protected int posFake = 0;
    private int posAttack = 0;
    private int contadorTick = 0;
    private double residuoTick = 0;

    public double currentBeat = 0d, metaBeatSpeed = -100;
    public float BPM, offset, beatsofSpeedMod, currentDurationFake = 0;
    //Generalplayer
    private SoundPool soundPool;
    private String pathMusic,event, tipo;
    public double fps;
    private float currentLife = 50;
    File fileBGA;
    private Paint dibujante;
    private int Combo = 0, soundPullBeat;
    //////
    private Long ttranscurrido;
    private Long currenttiempo;
    public Long curentempobeat;
    private Long Startime;
    private Long ttranscurridobeat;//Time Stamps
    //Score
    byte[] inputs;
    int bad = 0, miss = 0, perfect = 0, great = 0, good = 0, maxCombo = 0;
    public boolean doEvaluate = true, autoplay = false;
    double currentDelay = 0, residuoy = 0, speedMod = 1, lastSpeed, currentSecond = 0;
    SpriteReader items;
    double lostBeats = 0, lostBeatbyWarp = 0;
    long delayTime = 0;


    //metods
    private Runnable musicRun = new Runnable() {
        @Override
        public void run() {
            if (mpMusic != null) {
                try {
                    mpMusic.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mpMusic.start();
            }
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
        posstop = 0;
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public GamePlay(Context context, SSC ssc, int nchar, String path, byte[] pad, int width, int height) {
        super(context);
        build1Object(context, ssc, nchar, path, null, pad, width, height);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void build1Object(Context context, SSC stepData, int nchar, String path, PlayerBga bga, byte[] panel, int width, int height) {
        this.context = context;
        reset();
        this.setZOrderOnTop(true); //necessary
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        BGA = bga;
        //bgaBitmap = null;
        life = new LifeBar(context, "");
        this.inputs = panel;
        soundPool = new SoundPool(30, AudioManager.STREAM_MUSIC, 65);
        soundPullBeat = soundPool.load(this.getContext(), R.raw.beat2, 3);
        getHolder().addCallback(this);
        this.stepData = stepData;//se lee el archio ssc y se convierte a array y maps
        //RAW

        bufferSteps = stepData.createBuffer2(nchar);//se crea un array para el player


        if (stepData.chartsInfo[nchar].get("SCROLLS") != null && !stepData.chartsInfo[nchar].get("SCROLLS").equals("")) {
            SCROLLS = stepData.arrayListSpeed(stepData.chartsInfo[nchar].get("SCROLLS").toString());
        }
        BPMS = setMetadata((String) stepData.chartsInfo[nchar].get("BPMS"), stepData.songInfo.get("BPMS"));
        BPM = BPMS.get(0)[1];
        //ATTACKS= setMetadata(stepData.chartsInfo[nchar].get("ATTACKS"),stepData.songInfo.get("ATTACKS"));
        FAKES = setMetadata((String) stepData.chartsInfo[nchar].get("FAKES"), stepData.songInfo.get("FAKES"));
        TICKCOUNTS = setMetadata((String) stepData.chartsInfo[nchar].get("TICKCOUNTS"), stepData.songInfo.get("TICKCOUNTS"));

        STOPS = setMetadata((String) stepData.chartsInfo[nchar].get("STOPS"), stepData.songInfo.get("STOPS"));
        DELAYS = setMetadata((String) stepData.chartsInfo[nchar].get("DELAYS"), stepData.songInfo.get("DELAYS"));
        SPEEDS = setMetadata((String) stepData.chartsInfo[nchar].get("SPEEDS"), stepData.songInfo.get("SPEEDS"));
        WARPS = setMetadata((String) stepData.chartsInfo[nchar].get("WARPS"), stepData.songInfo.get("WARPS"));

        if (stepData.chartsInfo[nchar].get("OFFSET") != null) {
            String xof = stepData.chartsInfo[nchar].get("OFFSET").toString();
            offset = Float.parseFloat(xof);
        } else {
            String xof = stepData.songInfo.get("OFFSET");

            offset += Float.parseFloat(xof);
        }


        if (stepData.chartsInfo[nchar].get("STEPSTYPE") != null && !stepData.chartsInfo[nchar].get("STEPSTYPE").equals("")) {
            tipo = stepData.chartsInfo[nchar].get("STEPSTYPE").toString();
        } else {
            tipo = "";
        }

        databga = new ArrayList<>();

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        boolean internetConectionBywify;
        String urlvideo = "";
        if (stepData.songInfo.get("BGCHANGES") != null) {
            String[] infoBgas = stepData.songInfo.get("BGCHANGES").split(",");
            if (infoBgas != null) {

                if (Common.checkBGADir(path, infoBgas[0]) != null) {
                    fileBGA = new File(Common.checkBGADir(path, infoBgas[0]));
                } else {
                    for (int i = 0; i < infoBgas.length; i++) {
                        databga.add(infoBgas[i].split("="));

                        for (String databgas : databga.get(i)) {

                            if ((databgas.endsWith("mpg") || databgas.endsWith("mp4") || databgas.endsWith("avi"))) {

                                if (Common.checkBGADir(path, databgas) != null) {
                                    fileBGA = new File(Common.checkBGADir(path, databgas));
                                    BGA.bg.seekTo((int) (-offset * 1000));
                                    break;
                                } else {
                                    urlvideo = "https://kyagamy.mx/assets/bga/" + databgas;
                                }

                            }


                        }

                    }
                }
            }
        }
        if (fileBGA != null && BGA != null && fileBGA.exists()) {//Si hay reproductor y si hay bga
            BGA.setVideoPath(fileBGA.getPath());
        } else if ((!urlvideo.equals("")) && Common.bgaExist(urlvideo)) {
            Uri uri = Uri.parse(urlvideo);
            BGA.bg.setVideoURI(uri);
        } else if (BGA != null) {

            String path2 = "android.resource://" + context.getPackageName() + "/" + R.raw.bgaoff;
            BGA.setVideoPath(path2);
        }


        //currentBeat = (double) -offset / (60 / BPM);
        currentSecond = offset;
        pathMusic = path + "/" + stepData.songInfo.get("MUSIC");
        pathImage = path + "/" + stepData.songInfo.get("BACKGROUND");

        //musica


        mpMusic = new MediaPlayer();
        try {
            mpMusic.setDataSource(pathMusic);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mpMusic.setPlaybackParams(mpMusic.getPlaybackParams().setSpeed(ParamsSong.rush));// Esto será para el rush
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        // bgaBitmap = BitmapFactory.decodeFile(path + "/" + stepData.songInfo.get("BACKGROUND").toString());
        mainTread = new MainThread(getHolder(), this);
        fps = 0;
        setFocusable(true);
        touchPad = new GamePad(context, tipo, inputs, width, height);
//        items = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.objectospiu), 32, 2, 2f);

        currentSpeedMod = (int) (height * ParamsSong.speed / 4);

        //flecha
        steps = new Steps(context, BGA.getresolution());

        dibujante = new Paint();
        dibujante.setTextSize(20);
        ObjectCombo = new Combo(context);
        ObjectCombo.start();

        if (tipo.equals("dance-single")) {
            steps.makeDDR(context);
        }
        dibujante.setColor(Color.TRANSPARENT);

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    public void startGame() {
        curentempobeat = currenttiempo = Startime = System.nanoTime();

        try {
            if (!mainTread.running) {
                ttranscurrido = System.nanoTime();


                effectsThread = new RadarEffectsThread(this);
                effectsThread.start();
                mainTread.setRunning(true);

                if (offset > 0) {
                    // musicPlayer = new ThreadAudio(this.getContext(), offset, pathMusic);
                    currentBeat = 0;
                    mainTread.start();
                    handler1.postDelayed(musicRun, (long) (offset * 1000));
                    //mpMusic.start();
                    //musicPlayer.start();

                    BGA.startVideo();
                } else {
                    BGA.startVideo();
                    currentBeat = (double) offset / (60 / BPM);
                    //musicPlayer = new ThreadAudio(this.getContext(), 0, pathMusic);
//                    mpMusic.setDataSource(pathMusic);
                    // musicPlayer.start();

                    mainTread.start();

                    mpMusic.prepare();
                    mpMusic.start();
                }
            } else {
                mainTread.sulrfaceHolder = this.getHolder();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        stop();
    }

    public void update() {
        if (currentLife > 100) {
            currentLife = 100;
        } else if (currentLife <= 0) {
            currentLife = 0.01f;
        }
        life.updateLife(currentLife);
        ObjectCombo.update(Combo);
        steps.update();


        if (currentDelay != 0) {
            evaluate();//evaluate only if
        }


    }

    private void clearMemory() {
        // bgaBitmap = null;
        soundPool = null;
        stepData = null;
        databga = null;
        fileBGA = null;
        BGA = null;
        touchPad = null;
        steps = null;

        //  System.gc();
    }

    public void draw(Canvas canvas) {

        super.draw(canvas);
        try {

            if (canvas != null) {
                residuoy = 0;
                Paint painty = new Paint();
                painty.setTextSize(20);
                painty.setStyle(Paint.Style.FILL);
                painty.setColor(Color.TRANSPARENT);
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                drawStats(canvas);
                if ((posBuffer + 1 < bufferSteps.size())) {
                    if ((double) bufferSteps.get(posBuffer)[1] < currentBeat) {
                        drawCharts(canvas, posBuffer, true);
                    } else if ((double) bufferSteps.get(posBuffer)[1] > currentBeat) {
                        //drawCharts(canvas, posBuffer, true);
                    }
                } else if ((posBuffer + 1 >= bufferSteps.size())) {
                    this.startEvaluation();
                    stop();
                }

                ObjectCombo.draw(canvas, playerSizeX, playerSizeY);
                touchPad.draw(canvas);
                life.draw(canvas, playerSizeX, playerSizeY);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        // clearMemory();
        boolean retry = true;

        if (mainTread != null) {
            mainTread.setRunning(false);
        }

        while (retry) {
            try {
                effectsThread.running = false;
                effectsThread.join();
                mainTread.setRunning(false);
                //mainTread.join();
                //musicPlayer.stopmusic();
                //musicPlayer.join();
                //musicPlayer.interrupt();
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
           // this.event = "f:" + fingers + " ";//Se limpia el string
            int[][] inputsTouch = new int[fingers][2];
            for (int i = 0; i < fingers; i++) {
                inputsTouch[i][0] = (int) event.getX(i);
                inputsTouch[i][1] = (int) event.getY(i);
                this.event += " " + i + ":(" + (int) event.getX(i) + "," + (int) event.getY(i) + ")";
            }
            switch (maskedAction) {


                case MotionEvent.ACTION_POINTER_UP:
                    // Get the pointer ID
                    int mActivePointerId = event.getPointerId(fingers - 1);
                    int actionIndex = event.getPointerId(event.getActionIndex());
                    // ... Many touch events later...

                    // Use the pointer ID to find the index of the active pointer
                    // and fetch its position
                    int pointerIndex = event.findPointerIndex(mActivePointerId);
                    // Get the pointer's current position
                    //    float x = event.getX(pointerIndex);
                    //   float y = event.getY(pointerIndex);
//                        this.touchPad.unpress(x, y);
                    this.event += "l:" + "" + actionIndex;

                    if (actionIndex < inputsTouch.length) {
                        //  this.touchPad.unpress(inputsTouch[actionIndex][0],inputsTouch[actionIndex][1]);
                        //inputsTouch[actionIndex][0]=-1;
                        //inputsTouch[actionIndex][1]=-1;

                    }

                    break;
                case MotionEvent.ACTION_DOWN:
                    if (event.getX() > playerSizeX / 2 && event.getY() < playerSizeY / 2) {
                        currentSpeedMod += 150;
                    } else if (event.getX() < playerSizeX / 2 && event.getY() < playerSizeY / 2) {
                        if (currentSpeedMod > 99) {
                            currentSpeedMod -= 150;
                        }
                    } else if (event.getX() < playerSizeX / 2 && event.getY() > playerSizeY / 2 && event.getY() < playerSizeY) {
                        autoplay = !autoplay;
                    } else if (event.getX() > playerSizeX / 2 && event.getY() > playerSizeY / 2 && event.getY() < playerSizeY) {

                        steps.efecto = !steps.efecto;
                    }
                    touchPad.checkInputs(inputsTouch);


                default:
                    this.touchPad.checkInputs(inputsTouch);

                    break;
                case MotionEvent.ACTION_UP:
                    if (fingers == 1) {
                        touchPad.clearPad();
                    }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        return true;
    }


    public void calculateBeat() {

        if (lostBeatbyWarp > 0) {
            currentBeat += lostBeatbyWarp;
            lostBeatbyWarp = 0;
            //calculateEffects();
        }

        ttranscurridobeat = System.nanoTime() - curentempobeat;
        currentBeat += ParamsSong.rush * ttranscurridobeat / ((60 / BPM) * 1000 * 1000000);
        currentDurationFake -= ttranscurridobeat / ((60 / BPM) * 1000 * 1000000);//reduce la duración de los fakes
        curentempobeat = System.nanoTime();


    }

    public void drawStats(Canvas c) {
//calculate stats=
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
            if (currentDelay == 0) {
                calculateBeat();
                if (posBuffer >= (bufferSteps.size() + 1)) {
                    this.startEvaluation();
                    this.stop();

                } else if ((double) bufferSteps.get(posBuffer + 1)[1] <= currentBeat && posBuffer < bufferSteps.size()) {
                    while ((double) bufferSteps.get(posBuffer + 1)[1] <= currentBeat) {
                        posBuffer++;
                        evaluate();
                        calculateBeat();
                        //calculateEffects();
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
        } catch (IndexOutOfBoundsException e) {
            /*this.stop();
            this.startEvaluation();*/
        }


        dibujante.setTextSize(25);
        dibujante.setColor(Color.WHITE);
        c.drawText("FPS: " + fps, 50, 50, dibujante);
        c.drawText("Log: " + currentTickCount, 50, 100, dibujante);
        c.drawText("event: " + testFloatNOTUSE, 0, playerSizeY - 200, dibujante);
        //iversosn
        c.drawText("C Seg: " + String.format("%.3f", currentSecond), 10, playerSizeY - 100, dibujante);
        c.drawText("C Beat: " + String.format("%.3f", currentBeat), 10, playerSizeY - 50, dibujante);
        c.drawText("C BPM: " + this.BPM, 10, playerSizeY - 150, dibujante);
        c.drawText("C Speed: " + this.speedMod, 10, playerSizeY - 0, dibujante);
        String st = "";
        for (int j = 0; j < 10; j++) {
            st += inputs[j];
        }
//        showSomeArray(BPMS, c, posBPM);
        c.drawText("pad: " + st, playerSizeX - 250, playerSizeY - 20, dibujante);
        dibujante.setColor(Color.BLACK);
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
        int posIntX = (int) (playerSizeX * 0.05);
        int wa = 0;
        double currentY = (playerSizeY * 0.085);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {//horizonal
            playerSizeX = c.getWidth() * (1);
            playerSizeY = c.getHeight() * (1);
            if (tipo.equals("pump-single")) {
                wa = (int) (playerSizeX / 10 * 0.8);
                posIntX = (int) (playerSizeX * 0.2);
            } else if (tipo.equals("pump-double")) {
                wa = (int) (playerSizeX / 10 * 0.8);
                posIntX = (int) (playerSizeX * 0.1);
                steps.receptor.draw(c, new Rect((int) (playerSizeX * 0.04), (int) currentY, (int) (playerSizeX * 0.542
                ), (int) currentY + wa));
                steps.receptor.draw(c, new Rect((int) (playerSizeX * 0.435), (int) currentY, (int) (playerSizeX * 0.94
                ), (int) currentY + wa));
            }
        } else {//portriot
            playerSizeX = c.getWidth() * (1);
            playerSizeY = (int) (c.getHeight() * (0.5));
            wa = (int) (playerSizeX / 10 * 0.5);
            posIntX = (int) (playerSizeX * 0.35);

            if (tipo.equals("pump-single")) {
                wa = (int) (playerSizeX / 10 * 0.8);
                posIntX = (int) (playerSizeX * 0.3);
                steps.receptor.draw(c, new Rect((int) (playerSizeX * 0.24), (int) currentY, (int) (playerSizeX * 0.742
                ), (int) currentY + wa));
            } else if (tipo.equals("pump-double")) {
                wa = (int) (playerSizeX / 10 * 0.8);
                posIntX = (int) (playerSizeX * 0.1);
                steps.receptor.draw(c, new Rect((int) (playerSizeX * 0.04), (int) currentY, (int) (playerSizeX * 0.542
                ), (int) currentY + wa));
                steps.receptor.draw(c, new Rect((int) (playerSizeX * 0.435), (int) currentY, (int) (playerSizeX * 0.94
                ), (int) currentY + wa));
            }

        }


        Paint dibujante = new Paint();
        dibujante.setTextSize(25);
        dibujante.setStyle(Paint.Style.FILL);

        if (interpolate) {
            // if we stay that mean, the game is if a intermedium frame
            if ((double) bufferSteps.get(posBuffer)[1] < currentBeat) {


                double scrollSize = (float) bufferSteps.get(bufferPos)[2];
                double distanciaentrebeactualyela = currentBeat - (double) bufferSteps.get(posBuffer)[1];
                testFloatNOTUSE= (float) distanciaentrebeactualyela;
                if (testFloatNOTUSE>1){
                    event = distanciaentrebeactualyela + "";


                }

                double porcentajeaaumentar = (distanciaentrebeactualyela)   / (MIN_DISTANCE + (double) bufferSteps.get(posBuffer)[1]);

                event = testFloatNOTUSE + "";

                double distanciarestar = speed / 192 * porcentajeaaumentar;

                currentY -= distanciarestar * MIN_DISTANCE;

            }
        }

        dibujante.setColor(Color.WHITE);
        Stack<Object[]> stackSteps = new Stack<>();
        for (int i = bufferPos; currentY < playerSizeY && i < bufferSteps.size() && (speedMod > 0.04); i++) {
            double scrollSize = (float) bufferSteps.get(i)[2];
            double nextY = (currentY + (scrollSize * speed / 192));
            /* if (i%16==0){ c.drawText(scrollSize+"", 50, (int)currentY, dibujante); }*/
            if (containSteps((byte[]) bufferSteps.get(i)[0])) {
                Object[] auxString = new Object[3];
                auxString[0] = bufferSteps.get(i)[0];
                auxString[1] = currentY;
                auxString[2] = nextY;
                stackSteps.push(auxString);
            }
            double aumentoY = (currentSpeedMod * speedMod / 192);
            currentY += aumentoY * scrollSize;
        }

        currentY = (int) (playerSizeY * 0.085);

        for (int i = bufferPos; currentY > -50 && (i) >= 0 && (speedMod > 0.04); i--) {
            float w = (float) bufferSteps.get(i)[2];
            int nextY = (int) (currentY + (int) (speed / 192 * w));
            if (containSteps((byte[]) bufferSteps.get(i)[0])) {
                Object[] auxString = new Object[3];
                auxString[0] = bufferSteps.get(i)[0];
                auxString[1] = (int) (currentY);
                auxString[2] = nextY;
                stackSteps.push(auxString);
            }
            Double div = ((currentSpeedMod * speedMod / 192));
            int aumentY = (int) (residuoy + (currentSpeedMod * speedMod / 192));
            residuoy += div - aumentY;
            currentY -= aumentY * w;

        }
        //  event = "Nsteps" + stackSteps.size();
        //steps.draw(c, stackSteps, currentY, speed, posIntX, wa, playerSizeX, playerSizeY);
        steps.draw(c, stackSteps, speed, posIntX, wa, playerSizeX, playerSizeY);
        dibujante.setColor(Color.TRANSPARENT);
    }

    public void playTick() {
        if (doEvaluate) {
            soundPool.play(soundPullBeat, 0.7f, 0.7f, 1, 0, 1.1f);
        }
    }

    public void evaluate() {
        if (doEvaluate) {
            double[] currentJudge = Common.juicios[ParamsSong.judgment];
            if (autoplay) {
                ObjectCombo.posjudge = 0;
                if (containTapNote((byte[]) bufferSteps.get(posBuffer)[0])) {
                    playTick();
                    combopp();
                    currentLife += 0.5;
                    ObjectCombo.show();
                    byte[] auxrow = (byte[]) bufferSteps.get(posBuffer)[0];
                    for (int w = 0; w < auxrow.length; w++) {//animations
                        int aux = auxrow[w];
                        if (aux == 1) {
                            steps.explotions[w].play();
                        } else if (aux == 2) {
                            steps.explotions[w].play();
                            steps.explotionTails[w].play();
                        } else if (aux == 0) {
                            steps.explotionTails[w].stop();
                        }
                    }
                    bufferSteps.get(posBuffer)[0] = preseedRow;


                } else if (containLongs((byte[]) bufferSteps.get(posBuffer)[0])) {


                    residuoTick += ((double) currentTickCount / 48);
                    if (residuoTick >= 1) {
                        residuoTick -= 1;
                        ObjectCombo.show();
                        combopp();
                        currentLife += 0.5;
                    }


                    bufferSteps.get(posBuffer)[0] = preseedRow;

                }

            } else {//juicio normal
                int posBack;
                int posNext;
                int backSteps;
                int rGreat = mil2BackSpaces((float) currentJudge[3]);
                int rGood = rGreat + mil2BackSpaces((float) currentJudge[2]);
                int rBad = rGood + mil2BackSpaces((float) currentJudge[1]);
                backSteps = rBad + 1;

                posBack = -backSteps;
                if (backSteps >= posBuffer) {
                    posBack = -posBuffer;
                }
                posNext = backSteps;

                if (containTaps((byte[]) bufferSteps.get(posBuffer + posBack)[0])) {
                    comboLess();
                    bufferSteps.get(posBuffer + posBack)[0] = preseedRow;
                    posBack++;
                    miss++;

                }
                if (containLongs((byte[]) bufferSteps.get(posBuffer + posBack)[0])) {
                    residuoTick += ((double) currentTickCount / 48);
                    if (residuoTick >= 1) {
                        residuoTick -= 1;
                        ObjectCombo.show();
                        comboLess();
                        currentLife += 0.5;
                        miss++;
                    }
                }

                int posEvaluate = -1;
                while ((posBuffer + posBack < bufferSteps.size()) && posBack <= posNext) {
                    if (containSteps((byte[]) bufferSteps.get(posBuffer + posBack)[0])) {
                        boolean checkLong = true;
                        //byte[] auxRow = (byte[]) bufferSteps.get(posBuffer + posBack)[0];
                        for (int w = 0; w < ((byte[]) bufferSteps.get(posBuffer + posBack)[0]).length; w++) {
                            byte currentChar = ((byte[]) bufferSteps.get(posBuffer + posBack)[0])[w];


                            if (posBack < 1 && (inputs[w] != 0) && (containLongs(currentChar))) {

                                if (containStartLong((byte[]) bufferSteps.get(posBuffer)[0])) {
                                    ObjectCombo.show();
                                    combopp();
                                    currentLife += 0.5;
                                }
                                steps.explotionTails[w].play();


                                if (checkLong) {
                                    residuoTick += ((double) currentTickCount / 48);
                                    checkLong = false;// se hace que no se sume de nuevo
                                }
                                ((byte[]) bufferSteps.get(posBuffer + posBack)[0])[w] = 100;//Se vacia el array

                                if (residuoTick >= 1) {
                                    residuoTick -= 1;
                                    ObjectCombo.posjudge = 0;
                                    ObjectCombo.show();
                                    currentLife += 0.5;
                                    combopp();

                                    perfect++;
                                }
                                inputs[w] = 2;

                            }

                            if (inputs[w] == 1 && containTaps(currentChar)) {

                                steps.explotions[w].play();
                                ((byte[]) bufferSteps.get(posBuffer + posBack)[0])[w] = 0;
                                inputs[w] = 2;
                                posEvaluate = posBuffer + posBack;

                            }
                            if (inputs[w] == 0) {
                                steps.explotionTails[w].stop();
                            }

                            //bufferSteps.get(posBuffer + posBack)[0] = auxRow;

                        }
                    }
                    if (posEvaluate != -1) {
                        if (!containSteps((byte[]) bufferSteps.get(posEvaluate)[0])) {

                            int auxRetro = Math.abs(posBack);

                            if (auxRetro < rGreat) {//perfetc
                                ObjectCombo.posjudge = 0;
                                combopp();
                                currentLife += 0.5;
                                perfect++;
                            } else if (auxRetro < rGood) {//great
                                ObjectCombo.posjudge = 1;
                                combopp();
                                great++;
                            } else if (auxRetro < rBad) {//good
                                ObjectCombo.posjudge = 2;
                                good++;
                            } else {//bad
                                ObjectCombo.posjudge = 3;
                                Combo = 0;
                                currentLife -= 0.5;
                                bad++;
                            }
                            // AQUI SE VERA SI ES GREAT O QUE ONDA
                            ObjectCombo.show();
                        }
                        posEvaluate = -1;

                    }
                    posBack++;
                }
            }
        }
    }

    private boolean containStartLong(byte[] row) {
        for (int x : row) {
            if (x % 10 == 2) {
                return true;
            }
        }
        return false;

    }

    private void combopp() {
        if (Combo < 0) {
            Combo = 0;
        }
        Combo++;
        if (Combo > maxCombo) {
            maxCombo = Combo;
        }
    }

    private void comboLess() {
        miss++;
        ObjectCombo.posjudge = 4;
        if (Combo > 0) {
            Combo = 0;
        } else {
            Combo -= 1;
        }
        ObjectCombo.show();
        currentLife -= 1 - Combo;
    }


    private void addContadorTick() {
        contadorTick++;
        if (contadorTick > 48) {
            contadorTick = 0;
        }
    }

    private int mil2BackSpaces(float judgeTime) {
        int backs = 0;
        float auxJudge = 0;
        while ((posBuffer - backs) >= 0) {
            auxJudge += Common.beat2Second((double) 4 / 192, BPM) * 1000;
            backs++;
            if (auxJudge >= judgeTime + 23) {
                break;
            }
        }
        return backs;
    }

    private boolean containTaps(byte... row) {
        for (int x : row) {
            if (x % 10 == 1 || x % 10 == 5) {
                return true;
            }
        }
        return false;
    }

    private boolean containLongs(byte... row) {
        for (int x : row) {
            if (x % 10 == 2 || x % 10 == 3 || x == 4) {
                return true;
            }
        }
        return false;
    }

    private boolean containTapNote(byte... row) {
        for (int x : row) {
            if (x % 10 == 5 || x % 10 == 1 || x == 2) {
                return true;
            }
        }
        return false;
    }


    private boolean containSteps(byte... row) {
        for (byte x : row) {
            if (x != 0 && x != 127) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    private ArrayList
    setMetadata(String dataCurrentChar, String dataGeneral) {
        if (dataCurrentChar != null) {
            return stepData.arrayListSpeed(dataCurrentChar);
        } else if (dataGeneral != null && !dataGeneral.equals("")) {
            return stepData.arrayListSpeed(dataGeneral);
        }
        return null;
    }

    private void startEvaluation() {
        BGA.StartEvaluation(new int[]{perfect, great, good, bad, miss, maxCombo});
        BGA.finish();
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