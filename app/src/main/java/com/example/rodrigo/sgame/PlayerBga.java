package com.example.rodrigo.sgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.VideoView;

import com.crashlytics.android.Crashlytics;
import com.example.rodrigo.sgame.CommonGame.Common;
import com.example.rodrigo.sgame.CommonGame.ParamsSong;
import com.example.rodrigo.sgame.CommonGame.SSC;
import com.example.rodrigo.sgame.Player.GamePlay;
import com.example.rodrigo.sgame.Player.MainThread;

import java.io.FileInputStream;
import java.io.IOException;

import io.fabric.sdk.android.Fabric;

public class PlayerBga extends Activity {
    GamePlay gpo;
    public VideoView bg;
    public ImageView bgPad;
    MainThread hilo;
    Guideline gl;
    Intent i;
    AudioManager audio;
    byte[] pad = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};


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
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //evaluationIntet = new Intent(this, EvaluationActivity.class);
        bg = findViewById(R.id.bgVideoView2);
        gl = findViewById(R.id.guideline);
        String rawscc = getIntent().getExtras().getString("ssc");
        String path = getIntent().getExtras().getString("path");
        int nchar = getIntent().getExtras().getInt("nchar");
        gpo = findViewById(R.id.gamePlay);
        bgPad=findViewById(R.id.bg_pad);
        hilo = gpo.mainTread;
        try {
            //gpo.setZOrderOnTop(true);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            String z = Common.convertStreamToString(new FileInputStream(rawscc));

            gpo.build1Object(getBaseContext(), new SSC(z,false), nchar, path, this, pad, width, height);
            //System.gc();

        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
        }
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) gl.getLayoutParams();

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            params.guidePercent = 1f;
            gl.setLayoutParams(params);
        }
        else {
            params.guidePercent = 0.575f;
            gl.setLayoutParams(params);

        }

        bg.setOnErrorListener((mp, what, extra) -> {
            String path2 = "android.resource://" + getPackageName() + "/" + R.raw.bgaoff;
            bg.setVideoPath(path2);
            bg.start();
            return true;
        });



    }


    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent ev) {


        return super.dispatchGenericMotionEvent(ev);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if (keyCode == KeyEvent.KEYCODE_BACK) {


            gpo.stop();

            super.onBackPressed();

        }


        // Toast.makeText(getApplicationContext(),""+keyCode,Toast.LENGTH_LONG).show();
        switch (keyCode) {
            case 145:
            case 288:
                pad[5] = 1;
                break;
            case 157:
            case 293:
                pad[6] = 1;
                break;
            case 149:
            case 295:
                pad[7] = 1;
                break;
            case 153:
                pad[8] = 1;
                break;
            case 147:
                pad[9] = 1;
                break;
            case KeyEvent.KEYCODE_Z:
            case 290:
                pad[0] = 1;
                break;
            case KeyEvent.KEYCODE_Q:
            case 296:
                pad[1] = 1;
                break;
            case KeyEvent.KEYCODE_S:
            case 292:
                pad[2] = 1;
                break;
            case KeyEvent.KEYCODE_E:
            case KeyEvent.KEYCODE_DPAD_DOWN_LEFT:
            case KeyEvent.KEYCODE_SYSTEM_NAVIGATION_LEFT:

                pad[3] = 1;
                break;
            case KeyEvent.KEYCODE_C:
            case KeyEvent.KEYCODE_SYSTEM_NAVIGATION_DOWN:
            case KeyEvent.KEYCODE_DPAD_DOWN:

                pad[4] = 1;
                gpo.evaluate();
                break;
            case KeyEvent.KEYCODE_F8:
                gpo.autoplay = !gpo.autoplay;
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                return true;

            default:
        }


        return super.onKeyDown(keyCode, event);

    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        // Toast.makeText(getApplicationContext(),""+keyCode,Toast.LENGTH_LONG).show();
        switch (keyCode) {
            case 145:
                pad[5] = 0;
                break;
            case 157:
                pad[6] = 0;
                break;
            case 149:
                pad[7] = 0;
                break;
            case 153:
                pad[8] = 0;
                break;
            case 147:
                pad[9] = 0;
                break;
            case KeyEvent.KEYCODE_Z:
                pad[0] = 0;
                break;
            case KeyEvent.KEYCODE_Q:
                pad[1] = 0;
                break;
            case KeyEvent.KEYCODE_S:
                pad[2] = 0;
                break;
            case KeyEvent.KEYCODE_E:
                pad[3] = 0;
                break;
            case KeyEvent.KEYCODE_C:
                pad[4] = 0;


                break;
            default:
        }


        return true;
    }


    @Override
    public void onPause() {
        super.onPause();
    }

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
        bg.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
                mediaPlayer.setVolume(0, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(ParamsSong.rush));// Esto será para el rush
                }

            }
        });
        bg.start();

    }

    public void StartEvaluation(int[] params) {
        i.putExtra("evaluation",params);
        i.putExtra("pathbg",gpo.pathImage);
        i.putExtra("name",gpo.stepData.songInfo.get("TITLE").toLowerCase()  );

        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        gpo.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);


        gpo.startGame();

    }












    public Point getresolution() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return new Point(width, height);
    }

}
