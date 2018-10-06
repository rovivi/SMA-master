package com.example.rodrigo.sgame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rodrigo.sgame.CommonGame.Common;
import com.example.rodrigo.sgame.CommonGame.TransformBitmap;

import java.io.File;
import java.util.ArrayList;

import az.plainpie.PieView;
import az.plainpie.animation.PieAngleAnimation;

public class EvaluationActivity extends AppCompatActivity {
    ImageView bg;
    TextView[] textView = new TextView[12];
    TextView title;
    PieView animatedPie;
    float percent = 0;
    Handler handler = new Handler();
    int indexLabel = 0;
    SoundPool soundPool;
    Animation animation;
    Animation animation2;
    String letter="f";

    int spScore;
    Runnable animateInfo = new Runnable() {
        @Override
        public void run() {
            if (indexLabel < 6) {
                textView[indexLabel].setVisibility(View.VISIBLE);
                textView[indexLabel + 6].setVisibility(View.VISIBLE);
                textView[indexLabel].startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.bounce));
                textView[indexLabel + 6].startAnimation(AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.slide_in_left));
                indexLabel++;
                soundPool.play(spScore, 0.7f, 0.9f, 0, 1, 1.1f);
                soundPool.play(spScore, 0.7f, 0.9f, 0, 1, 1.2f);
                soundPool.play(spScore, 0.7f, 0.9f, 0, 1, 1.2f);

                handler.postDelayed(animateInfo, 350);

            } else {
                soundPool.stop(spScore);
                showGrade();
                title.append("     "+letter);
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        animation = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        animation2 = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);

        this.getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int[] params = getIntent().getIntArrayExtra("evaluation");

        bg = findViewById(R.id.bgBlurEvaluation);
        animatedPie = findViewById(R.id.pieView);
        textView[0] = findViewById(R.id.tvPerfect1);
        textView[1] = findViewById(R.id.tvGreat);
        textView[2] = findViewById(R.id.tvGood);
        textView[3] = findViewById(R.id.tvBad);
        textView[4] = findViewById(R.id.tvMiss);
        textView[5] = findViewById(R.id.tv_Combo);
        textView[6] = findViewById(R.id.tv_perfect2);
        textView[7] = findViewById(R.id.tv_great2);
        textView[8] = findViewById(R.id.tv_good2);
        textView[9] = findViewById(R.id.tv_bad2);
        textView[10] = findViewById(R.id.tv_miss2);
        textView[11] = findViewById(R.id.tv_maxCombo);
        title = findViewById(R.id.title_evaluation);
        //
        File bgImage = new File(getIntent().getExtras().getString("pathbg"));
        if (bgImage.exists() && bgImage.isFile()) {
            Bitmap ww = BitmapFactory.decodeFile(bgImage.getPath());
            if (ww != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                ww = TransformBitmap.makeTransparent(TransformBitmap.myblur(ww, getApplicationContext()), 130);


            }

            bg.setImageBitmap(ww);
        }
        title.setText(getIntent().getExtras().getString("name"));

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        title.setTypeface(custom_font);
        int index = 0;
        for (TextView tv : textView) {
            tv.setTypeface(custom_font);
            tv.setTextSize(25);
            tv.setVisibility(View.GONE);
            if (index > 5) {
                tv.setText(params[index - 6] + "");
            }
            index++;

        }

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 100);
        spScore = soundPool.load(this, R.raw.tick, 0);

        float total = params[0] + params[1] + params[2] + params[3] + params[4];
        percent = ((float) params[0] / total) +
                ((float) params[1] / total * 0.8f) +
                ((float) params[2] / total * 0.6f) +
                ((float) params[3] / total * 0.4f);
        animatedPie.setPercentage(percent * 100);
        animatedPie.setPercentageTextSize(32);
        animatedPie.setInnerText((double) Math.round(100 * percent * 100000d) / 100000d + "%");
        //listener
        Button b1 = findViewById(R.id.buttonContinue);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (percent==1){
            letter="SS";
        }
        else if(params[2]==0&&params[3]==0&& params[4]==0){
            letter="S";

        }
        else if (params[4]==0){
            letter="s";

        }
        else if(percent>=0.9f){
            letter="a";

        }
        else if(percent>=0.8f){
            letter="b";

        }
        else if(percent>=0.7f){
            letter="c";
        }
        else if(percent>=0.6f){
            letter="d";
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        animateInfo.run();

        PieAngleAnimation animation3 = new PieAngleAnimation(animatedPie);
        animation3.setDuration(2000); //This is the duration of the animation in millis
        animatedPie.startAnimation(animation3);

        //handler.postDelayed(animateInfo,10);
    }


    public void showGrade() {
        MediaPlayer mediaPlayer;
        MediaPlayer mediaPlayerbg;

        int res=-1;
        int res2=-1;

        switch (letter) {
            case "SS":
                res = R.raw.rank_0;
                res2= R.raw.rank_0_b;
                break;
            case "S":
                res = R.raw.rank_1;
                res2= R.raw.rank_1_b;
                break;
            case "s":
                res = R.raw.rank_2;
                res2= R.raw.rank_2_b;
                break;
            case "a":
                res = R.raw.rank_3;
                res2= R.raw.rank_3_b;
                break;
            case "b":
                res = R.raw.rank_4;
                res2= R.raw.rank_4_b;
                break;
            case "c":
                res = R.raw.rank_5;
                res2= R.raw.rank_5_b;
                break;
            case "d":
                res = R.raw.rank_6;
                res2= R.raw.rank_6_b;
                break;
            case "f":
                res = R.raw.rank_7;
                res2= R.raw.rank_7_b;
                break;
        }

        mediaPlayer= MediaPlayer.create(this,res);
        mediaPlayerbg= MediaPlayer.create(this,res2);

        mediaPlayer.start();
        mediaPlayerbg.start();
    }


}
