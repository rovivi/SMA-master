package com.kyadevs.stepdroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyadevs.stepdroid.CommonGame.Common;
import com.kyadevs.stepdroid.CommonGame.ParamsSong;
import com.kyadevs.stepdroid.CommonGame.TransformBitmap;
import com.kyadevs.stepdroid.Player.NoteSkin;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import az.plainpie.PieView;
import az.plainpie.animation.PieAngleAnimation;

public class EvaluationActivity extends AppCompatActivity {
    ImageView bg;
    TextView[] textView = new TextView[12];
    TextView title,continueText,danceGrade,tvJudge,tvLvl;
    PieView animatedPie;
    float percent = 0;
    Handler handler = new Handler();
    int indexLabel = 0;
    SoundPool soundPool;
    //Animation animation;
    Animation animation2,animationDown,animationZoom;
    String letter="f";
    ImageView letterGrade,skinImg,imageLvl;
    Bitmap letterGradeBitmap;
    Bitmap[] lettersArray;
    ConstraintLayout constraintLayout;



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
                soundPool.play(spScore, 0.1f, 0.9f, 0, 0, 1.1f);
                soundPool.play(spScore, 0.9f, 0.9f, 0, 0, 1.2f);
                soundPool.play(spScore, 0.9f, 0.9f, 0, 0, 1.2f);
                soundPool.play(spScore, 0.9f, 0.9f, 0, 0, 1.2f);
                soundPool.play(spScore, 0.9f, 0.9f, 0, 0, 1.2f);
                soundPool.play(spScore, 0.9f, 0.9f, 0, 0, 1.2f);
                soundPool.play(spScore, 0.9f, 0.9f, 0, 0, 1.2f);

                handler.postDelayed(animateInfo, 350);

            } else {
                soundPool.stop(spScore);
                showGrade();
               // title.append("     "+letter);
            }


        }
    };
    private Animation animationZoom2;
    private TextView tvTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        //publicidad








        //  animation = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        animation2 = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.translate_down);
        animationZoom = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        animationZoom2 = AnimationUtils.loadAnimation(this, R.anim.zoom_in);


        ArrayList <String>skins = NoteSkin.arraySkin(this);



        this.getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int[] params = getIntent().getIntArrayExtra("evaluation");
            danceGrade = findViewById(R.id.dance_grade_text);
        letterGrade= findViewById(R.id.letterGrade);
        bg = findViewById(R.id.bgBlurEvaluation);
        tvJudge= findViewById(R.id.lvl_judment_eval);
        skinImg= findViewById(R.id.skin_image);
        constraintLayout=findViewById(R.id.layout_1);
        imageLvl=findViewById(R.id.image_level);
        tvLvl =findViewById(R.id.text_lvl_ecal);
        tvTP=findViewById(R.id.text_tp);

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

        letterGradeBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.letters);
        lettersArray= TransformBitmap.customSpriteArray(letterGradeBitmap,1,8,0,1,2,3,4,5,6,7);

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
        tvTP.setTypeface(custom_font);
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/font2.ttf");
        tvLvl.setTypeface(custom_font2);
        //animatedPie.setMainBackgroundColor(Color.BLUE);
        int index = 0   ;
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
        animatedPie.setPercentageTextSize(16);
        animatedPie.setInnerText((double) Math.round(100 * percent * 100000d) / 100000d + "%");
        animatedPie.setPieInnerPadding(10);
        //listener
        String secretName= getIntent().getExtras().getString("name")+getIntent().getExtras().getInt("nchar");


            if(       Common.compareRecords(this,secretName,percent*100)){
                Common.writeRecord(this,secretName,percent*100);
            }


       continueText = findViewById(R.id.buttonContinue);
            continueText.setTypeface(custom_font);
        continueText.setOnClickListener(new View.OnClickListener() {
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


        if (ParamsSong.stepType2Evaluation.contains("double")){
            Picasso.get().load(R.drawable.hexa_double).into(imageLvl);
        }
        else if (ParamsSong.stepType2Evaluation.contains("single")){
            Picasso.get().load(R.drawable.hexa_single).into(imageLvl);
        }
        else{
            Picasso.get().load(R.drawable.hexa_performance).into(imageLvl);
        }
        skinImg.setImageBitmap(NoteSkin.maskImage(skins.get(ParamsSong.skinIndex),
                this));
        setTxtJudge();
        tvLvl.setText(ParamsSong.stepLevel);
        animationZoom.setDuration(500);
        animationZoom2.setDuration(600);

        animationZoom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animateInfo.run();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }








    @Override
    protected void onStart() {
        super.onStart();
        danceGrade.startAnimation(animationZoom2);
        continueText.startAnimation(animationZoom2);
        tvLvl.startAnimation(animationZoom2);
        imageLvl.startAnimation(animationZoom2);

        title.startAnimation(AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_in));
        animatedPie.startAnimation(AnimationUtils.loadAnimation(getBaseContext(),  android.R.anim.slide_in_left));
        constraintLayout.startAnimation(animationZoom);
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
                letterGrade.setImageBitmap(lettersArray[0]);
                break;
            case "S":
                res = R.raw.rank_1;
                res2= R.raw.rank_1_b;
                letterGrade.setImageBitmap(lettersArray[1]);

                break;
            case "s":
                res = R.raw.rank_2;
                res2= R.raw.rank_2_b;
                letterGrade.setImageBitmap(lettersArray[2]);

                break;
            case "a":
                res = R.raw.rank_3;
                res2= R.raw.rank_3_b;
                letterGrade.setImageBitmap(lettersArray[3]);

                break;
            case "b":
                res = R.raw.rank_4;
                res2= R.raw.rank_4_b;
                letterGrade.setImageBitmap(lettersArray[4]);

                break;
            case "c":
                res = R.raw.rank_5;
                res2= R.raw.rank_5_b;
                letterGrade.setImageBitmap(lettersArray[5]);

                break;
            case "d":
                res = R.raw.rank_6;
                res2= R.raw.rank_6_b;
                letterGrade.setImageBitmap(lettersArray[6]);

                break;
            case "f":
                res = R.raw.rank_7;
                res2= R.raw.rank_7_b;
                letterGrade.setImageBitmap(lettersArray[7]);

                break;
        }

        letterGrade.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_letter));

        mediaPlayer= MediaPlayer.create(this,res);
        mediaPlayerbg= MediaPlayer.create(this,res2);

        mediaPlayer.start();
        mediaPlayerbg.start();
    }



    private void setTxtJudge() {
        String text = "";
        switch (ParamsSong.judgment) {
            case 0://SJ
                text = "SJ";
                break;
            case 1://EJ
                text = "EJ";
                break;
            case 2://NJ
                text = "NJ";
                break;
            case 3://HJ
                text = "HJ";
                break;
            case 4://VJ
                text = "VJ";
                break;
            case 5://XJ
                text = "XJ";
                break;
            case 6://UJ
                text = "UJ";
                break;
        }
        tvJudge.setText(text);



    }



}
