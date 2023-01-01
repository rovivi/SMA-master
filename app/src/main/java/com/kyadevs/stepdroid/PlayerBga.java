package com.kyadevs.stepdroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

//import com.crashlytics.android.Crashlytics;
import com.kyadevs.stepdroid.CommonGame.Common;
import com.kyadevs.stepdroid.CommonGame.ParamsSong;
import com.kyadevs.stepdroid.CommonGame.SSC;
import com.kyadevs.stepdroid.Player.GamePlay;
import com.kyadevs.stepdroid.Player.MainThread;

import java.io.FileInputStream;

import game.StepObject;
//import io.fabric.sdk.android.Fabric;
import parsers.FileSSC;

public class PlayerBga extends Activity {
    GamePlay gpo;
    public VideoView bg;
    public ImageView bgPad;
    TextView tvMsj;
    MainThread hilo;
    Guideline gl;
    Intent i;
    AudioManager audio;
    Boolean gamePlayError = false;
    Handler handler = new Handler();
    int nchar;
    int indexMsj = 0;
    byte[] pad = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private String[] msjs = {"", "Ready", "GO!"};
    Runnable textAnimator = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run() {
            if (indexMsj < msjs.length) {
                tvMsj.setText(msjs[indexMsj]);
                tvMsj.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in));
                handler.postDelayed(textAnimator, 500);
                indexMsj++;
            } else {
                tvMsj.setText("");
                Common.AnimateFactor = 0;
                bgPad.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_in));
                tvMsj.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.slide_out_right));
                startGamePlay();
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        i = new Intent(this, EvaluationActivity.class);
        try {
            //this.getSupportActionBar().hide();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        } catch (NullPointerException e) {

        }

        setContentView(R.layout.activity_playerbga);
        tvMsj = findViewById(R.id.gamemsg);
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //evaluationIntet = new Intent(this, EvaluationActivity.class);
        bg = findViewById(R.id.bgVideoView2);
        gl = findViewById(R.id.guideline);

        nchar = getIntent().getExtras().getInt("nchar");
        gpo = findViewById(R.id.gamePlay);
        bgPad = findViewById(R.id.bg_pad);
        hilo = gpo.mainTread;
        String pathImg = getIntent().getExtras().getString("pathDisc", null);
        if (pathImg != null) {
            bgPad.setImageBitmap(BitmapFactory.decodeFile(pathImg));
        }

        bg.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.setVolume(0,0);
            }
        });


    }


    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent ev) {


        return super.dispatchGenericMotionEvent(ev);
    }



    @Override
    public void onPause() {
        super.onPause();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume() {
        super.onResume();
    }


    public void setVideoPath(String uri) {
        if (uri != null) {
            bg.setVideoPath(uri);
        }

    }

    public void startVideo() {
        bg.setOnPreparedListener(mediaPlayer -> {
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(0, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(ParamsSong.rush));// Esto será para el rush
            }
        });
        bg.start();
    }

    public void StartEvaluation(int[] params) {
        i.putExtra("evaluation", params);
        i.putExtra("pathbg", "");
        i.putExtra("name", "Noame");
        i.putExtra("nchar", nchar);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onStart() {
        super.onStart();
        gpo.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//        final Fabric fabric = new Fabric.Builder(this)
//                .kits(new Crashlytics())
//                .debuggable(true)           // Enables Crashlytics debugger
//                .build();
//        Fabric.with(fabric);

        if (Common.ANIM_AT_START){
            textAnimator.run();
        }
        else {
            startGamePlay();
        }
    }

    public Point getresolution() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return new Point(width, height);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startGamePlay() {
        try {

            gpo.setTop(0);
            String rawscc = getIntent().getExtras().getString("ssc");
            String path = getIntent().getExtras().getString("path");
            String s = Common.convertStreamToString(new FileInputStream(rawscc));

            try {
                StepObject step = new FileSSC(s,nchar).parseData();
                step.setPath(path);
                gpo.build1Object(getBaseContext(), new SSC(s, false), nchar, path, this, pad, Common.WIDTH, Common.HEIGHT);
   //             gpo.build1Object(bg,step);
            } catch (Exception e) {
                e.printStackTrace();
                gamePlayError = true;
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) gl.getLayoutParams();
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            params.guidePercent = 1f;
            gl.setLayoutParams(params);
        } else {
            params.guidePercent = 0.65f;
            gl.setLayoutParams(params);
        }
        bg.setOnErrorListener((mp, what, extra) -> {
            String path2 = "android.resource://" + getPackageName() + "/" + R.raw.bgaoff;
            bg.setVideoPath(path2);
            bg.start();
            return true;
        });

        if (!gamePlayError && gpo != null) {
            gpo.startGame();
        } else {
            finish();
        }
    }


}
