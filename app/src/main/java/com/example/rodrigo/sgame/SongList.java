package com.example.rodrigo.sgame;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.storage.OnObbStateChangeListener;
import android.os.storage.StorageManager;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.crashlytics.android.Crashlytics;
import com.example.rodrigo.sgame.CommonGame.Common;
import com.example.rodrigo.sgame.CommonGame.CustomSprite.SpriteReader;
import com.example.rodrigo.sgame.CommonGame.Level;
import com.example.rodrigo.sgame.CommonGame.ParamsSong;
import com.example.rodrigo.sgame.CommonGame.SSC;
import com.example.rodrigo.sgame.CommonGame.TransformBitmap;
import com.example.rodrigo.sgame.CommonGame.zipUtils;
import com.example.rodrigo.sgame.Player.NoteSkin;
import com.example.rodrigo.sgame.ScreenSelectMusic.AdapterLevel;
import com.example.rodrigo.sgame.ScreenSelectMusic.AdapterSSC;
import com.example.rodrigo.sgame.ScreenSelectMusic.MusicThread;
import com.example.rodrigo.sgame.ScreenSelectMusic.RecyclerItemClickListener;
import com.example.rodrigo.sgame.ScreenSelectMusic.SongsGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.squareup.picasso.Picasso;

import io.fabric.sdk.android.Fabric;


public class SongList extends AppCompatActivity implements View.OnClickListener {
    File[] files;
    String currentSSC;
    String paths;
    String path = "", pathImage;
    ObjectAnimator fadeOut;
    public int spCode, spOpenWindow, spSelect, spSelectSong;
    MediaPlayer mediaPlayer;
    MusicThread musicTimer;
    //VideoView bg;
    public SoundPool changeMusic;
    final List<String> songs = new ArrayList<>();
    final List<String> lvl = new ArrayList<>();
    final List<SongsGroup> groups = new ArrayList<>();
    int currentSongIndex = 0, currentSSCIndex = 0;
    //ThemeElements themeElements;
    // ArrayAdapter<String> adp2;
    Intent i;
    VideoView preview;
    ImageView backgroundBluour, btnLevel, btnSpeed, bgSongList, imageSkin, img_velocity;
    TextView lvlText, titleCurrentSong, authorCurrent, txt_open, tv_record, tv_velocity, tv_judment, tv_apareance, tv_bpm;
    FirebaseAnalytics mFirebaseAnalytics;
    ArrayList<Level> levelArrayList = new ArrayList<>();
    Spinner spinner;

    BitmapDrawable errorAuxImage = null;
    SongsGroup songsGroup = new SongsGroup();

    RecyclerView recyclerView;
    ImageView startImage, back_image;
    RecyclerView recyclerViewLevels;
    FragmenStartMenu currentDF;
    Handler handler = new Handler();
    private AdapterLevel adapterLevel;
    //Sprites
    View root;
    // Sprite flashSpeedSprite, sriteMask;
    //  ThreadSprite threadSprite;
    View.OnClickListener listenerButton = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void onClick(View v) {
            // releaseMediaPlayer();
            showStartSongFragment(false);
        }
    };


    View.OnClickListener listenerlvl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openLevelList();
        }
    };


    Runnable runableLoadSongs = () -> {
        loadSongs();
        changeSong(0);
        root.setVisibility(View.VISIBLE);
        currentDF.dismiss();

    };

    Thread threadLoad = new Thread(runableLoadSongs);

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Windows Decorator
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        try {
            this.getSupportActionBar().hide();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LOW_PROFILE
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_songlist);

        //------Se llenan los valosres de las lineas guia----//

        root = findViewById(R.id.rootId_songList);


        Guideline guideLine = findViewById(R.id.guideStartList);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
        params.guidePercent = 0.40f; // 45% // range: 0 <-> 1
        guideLine.setLayoutParams(params);
        guideLine = findViewById(R.id.guideEndLIst);
        params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
        params.guidePercent = 1.1f; // 45% // range: 0 <-> 1
        guideLine.setLayoutParams(params);


        //------Fin de las lineas guia-----//
        //------get Elemets----------------//
        //sriteMask= findViewById(R.id.sprite_disc_mask);
        btnLevel = findViewById(R.id.imagelevl);
        btnSpeed = findViewById(R.id.imagelevl2);
        lvlText = findViewById(R.id.numberlevel);
        //  themeElements = findViewById(R.id.songElements);
        backgroundBluour = findViewById(R.id.bgBlur);
        recyclerView = findViewById(R.id.recyclerSongs);
        //  bg = findViewById(R.id.bgVideoView);
        preview = findViewById(R.id.preview);
        startImage = findViewById(R.id.startButton);
        titleCurrentSong = findViewById(R.id.current_song_name);
        authorCurrent = findViewById(R.id.current_text_author);
        txt_open = findViewById(R.id.more_txt);
        tv_record = findViewById(R.id.record_text);
        tv_velocity = findViewById(R.id.text_speed);
        tv_judment = findViewById(R.id.text_judment);
        tv_apareance = findViewById(R.id.text_apararience);
        tv_bpm = findViewById(R.id.text_bpm);
        back_image = findViewById(R.id.back_image);
        imageSkin = findViewById(R.id.image_note_preview);
        //img_velocity=findViewById(R.id.)
        titleCurrentSong.setSelected(true);
        titleCurrentSong.setSingleLine(true);
        bgSongList = findViewById(R.id.bg_song_list);

        preview.setOnClickListener(v -> showStartSongFragment(false));
        //lista

        back_image.setOnClickListener(v -> {
            startActivity(new Intent(SongList.this, MainScreenActivity.class));
        });
        if (ParamsSong.listCuadricula) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        }


        recyclerView.setNestedScrollingEnabled(true);
        TextView msjlvl = findViewById(R.id.yourlvlmsjtv);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/font2.ttf");
        //Typeface custom_font3 = Typeface.createFromAsset(getAssets(), "fonts/font3.ttf");


        msjlvl.setTypeface(custom_font2);
        txt_open.setTypeface(custom_font);
        tv_bpm.setTypeface(custom_font);
        lvlText.setTypeface(custom_font2);

        preview.setOnErrorListener((mp, what, extra) -> {
            //    Log.d("video", "setOnErrorListener ");
            if (what == -1010 || extra == -1010) {

            }
//            preview.setBackground(errorAuxImage);
            playVideoError();
            return true;
        });


        //---------Listeners-------//


        btnLevel.setOnClickListener(listenerlvl);
        btnLevel.setOnLongClickListener(v -> {
            //showEditFragment();

            return false;
        });


        imageSkin.setImageBitmap(NoteSkin.maskImage(ParamsSong.nameNoteSkin, this));


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                changeSong(position
                );
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        txt_open.setOnClickListener(v -> {
            showEditFragment();
        });


        Picasso.get().load(R.drawable.bg_disc_square).into(bgSongList);
        //-------------se crea la lista de canciones------------//
        try {
            changeMusic = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
            spCode = changeMusic.load(this, R.raw.change_song, 0);
            spOpenWindow = changeMusic.load(this, R.raw.select_3, 0);
            spSelect = changeMusic.load(this, R.raw.command_mod, 0);
            spSelectSong = changeMusic.load(this, R.raw.transformation, 0);


            i = new Intent(this, MainActivity.class);
            i = new Intent(this, PlayerBga.class);
            mediaPlayer = new MediaPlayer();

            preview.setOnPreparedListener(mediaPlayer -> {
                mediaPlayer.setLooping(true);
                mediaPlayer.setVolume(0, 0);
            });


            btnSpeed.setOnClickListener(v -> {
                ParamsSong.speed += 0.5f;
                if (ParamsSong.speed > 7) {
                    ParamsSong.speed = 1f;
                }
//                flashSpeedSprite.image.play();

                showStartSongFragment(false);
            });

        } catch (Exception e) {

            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


        ///-----------
        adapterLevel = new AdapterLevel(levelArrayList, this, getAssets());
        recyclerViewLevels = findViewById(R.id.rv_leves);
        recyclerViewLevels.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //recyclerViewLevels.setLayoutManager(new GridLayoutManager(this, 7, GridLayoutManager.VERTICAL, false));
        recyclerViewLevels.setAdapter(adapterLevel);
        recyclerViewLevels.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                changeLvl(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));


        ///////Sprites
        //flashSpeedSprite = findViewById(R.id.sprite_velocity);
        //flashSpeedSprite.staticDraw=true;

//        Bitmap sprite = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_mask_disc);
//        SpriteReader spriteReaderMaskDisc = new SpriteReader(sprite, 5, 2, 1f);
//
//        SpriteReader spriFlash = new SpriteReader(BitmapFactory.decodeResource(getResources(), R.drawable.glow_command), 1, 2, 0.5f);
//        //flashSpeedSprite.create(spriFlash);
        //sriteMask.create(spriteReaderMaskDisc);
        //threadSprite = new ThreadSprite(flashSpeedSprite,sriteMask);


        try {
            //   loadSongs();
        } catch (Exception e) {

            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        if (ParamsSong.av==0){
            //tv_velocity.set
        }

    }


    private ArrayList<Float[]>
    setMetadata(SSC stepData, String dataCurrentChar, String dataGeneral) {
        if (dataCurrentChar != null) {
            return stepData.arrayListSpeed(dataCurrentChar);
        } else if (dataGeneral != null && !dataGeneral.equals("")) {
            return stepData.arrayListSpeed(dataGeneral);
        }
        return null;
    }


    public void playSoundPool(int spCode) {
        changeMusic.play(spCode, 0.5f, 0.5f, 1, 0, 1.1f);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void changeSong(int position) {
        changeMusic.play(spCode, 1, 1, 1, 0, 1.0f);
        releaseMediaPlayer();
        currentSSCIndex = position;
        paths = songsGroup.listOfSongs.get(position).path.getPath();
        try {
            // themeElements.flash.play();
            SSC auxStep = songsGroup.listOfSongs.get(position);
            Float sampleStart = Float.parseFloat(auxStep.songInfo.get("SAMPLESTART"));
            int offset = (int) (sampleStart * 1000);
            titleCurrentSong.setText(auxStep.songInfo.get("TITLE") != null ? auxStep.songInfo.get("TITLE") : "No title");


            ArrayList<Float[]> bpms = setMetadata(auxStep, null, auxStep.songInfo.get("BPMS"));


            boolean checkBPM = (bpms != null) && (bpms.get(0) != null);
            tv_bpm.setText(checkBPM ? "B.P.M. " + bpms.get(0)[1].intValue() : "B.P.M. ???");
            tv_bpm.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in));

            authorCurrent.setText(auxStep.songInfo.get("ARTIST") != null ? "Composed by:" + auxStep.songInfo.get("ARTIST") : "No Artist");
            titleCurrentSong.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in));
            authorCurrent.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_in));
            currentSSC = songsGroup.listOfSongs.get(position).pathSSC;
            lvl.clear();
            levelArrayList.clear();
            adapterLevel.notifyDataSetChanged();
            adapterLevel.lastPosition = -1;
            for (Map currentChart : auxStep.chartsInfo) {
                if (currentChart == null) {
                    break;
                }
                int lvl = (currentChart.get("METER") != null) ? Integer.parseInt(currentChart.get("METER").toString()) : 0;
                String tipo = (currentChart.get("STEPSTYPE") != null) ? currentChart.get("STEPSTYPE").toString() : "";
                String tag = (currentChart.get("STEPSTYPE") != null) ? currentChart.get("STEPSTYPE").toString() : "";
                levelArrayList.add(new Level(lvl, tipo, tag));
            }

            if (auxStep.isPropitarySongs) {


            } else {
                //if the song provides from SD

                File audio = new File(paths + "/" + auxStep.songInfo.get("MUSIC"));
                File video = new File(paths + "/" + auxStep.songInfo.get("PREVIEWVID"));
                File bg = new File(paths + "/" + auxStep.songInfo.get("BACKGROUND"));
                Bitmap transparent;
                if (video.exists() && (video.getPath().endsWith(".mpg") || video.getPath().endsWith(".mp4") || video.getPath().endsWith(".avi"))) {
                    preview.setBackground(null);
                    // Uri uri = CustomAPEZProvider.buildUri(video.getPath().replace(obbMountPath + "/", ""));
                    //preview.setVideoURI(uri);
                    preview.setVideoPath(video.getPath());
                    preview.start();
                    transparent = TransformBitmap.makeTransparent(BitmapFactory.decodeFile(bg.getPath()), 180);
                    this.errorAuxImage = new BitmapDrawable(transparent);

                } else {
                    playVideoError();

                    if (bg.exists() && bg.isFile()) {
                        transparent = TransformBitmap.makeTransparent(BitmapFactory.decodeFile(bg.getPath()), 180);
                        this.errorAuxImage = new BitmapDrawable(transparent);

                    } else {
                        transparent = TransformBitmap.makeTransparent(BitmapFactory.decodeResource(getResources(), R.drawable.no_banner), 180);
                        this.errorAuxImage = new BitmapDrawable(transparent);

                    }

                    preview.setBackground(errorAuxImage);
                }

                if (bg.exists() && bg.isFile()) {

                    Bitmap ww = TransformBitmap.makeTransparent(TransformBitmap.myblur(BitmapFactory.decodeFile(bg.getPath()), getApplicationContext()), 150);
                    backgroundBluour.setImageBitmap(BitmapFactory.decodeFile(bg.getPath()));
                    pathImage = bg.getPath();
                    backgroundBluour.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.slide_in_left));
                    // Picasso.get().load(bg.getPath()).into(backgroundBluour);


                }
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setVolume(1f, 1f);

/*
                Uri uriAudio = CustomAPEZProvider.buildUri(audio.getPath().replace(obbMountPath + "/", ""));
                mediaPlayer.setDataSource(this, uriAudio);*/
                mediaPlayer.setDataSource(audio.getPath());
            }
            mediaPlayer.prepare();
            mediaPlayer.seekTo(offset);
            musicTimer = new MusicThread();
            musicTimer.player = mediaPlayer;
            musicTimer.time = (long) (Double.parseDouble(auxStep.songInfo.get("SAMPLELENGTH")) * 1000);
            musicTimer.start();
            mediaPlayer.start();
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        setRecord();

        if (recyclerViewLevels.getVisibility() == View.INVISIBLE) {
            changeLevel();
        }

    }



    public void changeSongDemo (int position ){

        changeMusic.play(spCode, 1, 1, 1, 0, 1.0f);
        releaseMediaPlayer();
        currentSSCIndex = position;
        paths = songsGroup.listOfSongs.get(position).path.getPath();
    }



    @Override
    protected void onStart() {
        super.onStart();
        //  hide();


        startImage.setOnClickListener(listenerButton);


        Trace myTrace = FirebasePerformance.getInstance().newTrace("test_trace");
        myTrace.start();


        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);
//        threadSprite.running = true;

        //se carga el OBB
        /*File obbFile = new File(getObbDir().getPath() + "/main.1." + getPackageName() + ".obb");

        if (false && obbFile.exists()) {//desabilitado por el momento
            //ObbInfo info =ObbScanner.getObbInfo(obbFile.getPath());
            String superinfoinfo = mountObbFile(obbFile.getPath());
        } else {
            loadSongs();
        }*/


    }


    @Override
    public void onResume() {
        super.onResume();
        Common.setParamsGlobal(this);
        //  this.bg.start();
        //  threadSprite.running = true;
        if (fadeOut != null && fadeOut.isRunning()) {
            fadeOut.cancel(); // Cancel the opposite animation if it is running or else you get funky looks
        }
        if (songsGroup.listOfSongs.size() == 0 || Common.RELOAD_SONGS) {
            //  root.setVisibility(View.INVISIBLE);
            loadSongs();

            // handler.postDelayed(runableLoadSongs,100);
        } else {
            try {
                changeSong(currentSSCIndex);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        // new LoadSongTask().execute(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (musicTimer != null) {
            this.musicTimer.isRunning = false;

        }
        releaseMediaPlayer();
    }

    public void onPause() {
        super.onPause();

        //this.musicTimer.isRunning = false;
        try {
            releaseMediaPlayer();

        } catch (Exception e) {

        }
    }

    private void releaseMediaPlayer() {
        try {
            if (mediaPlayer != null) {
                preview.suspend();
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;

                if (musicTimer != null) {
                    if (musicTimer.isRunning) {
                        musicTimer.isRunning = false;
                        //  musicTimer.suspend();
                        musicTimer.join();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showEditFragment() {
        FragmenSongOptions newFragment = new FragmenSongOptions();
        //newFragment.lista = levelArrayList;
        newFragment.setSongList(this);
        newFragment.show(getFragmentManager(), "");
        playSoundPool(spOpenWindow);
    }


    public void showStartSongFragment(boolean type) {
        FragmenStartMenu newFragment = new FragmenStartMenu();
        currentDF = newFragment;
        newFragment.setSongList(this);
        newFragment.loadingScreen = type;
        newFragment.show(getFragmentManager(), "");
        // playSoundPool(spOpenWindow);
    }


    public void changeLevel() {
        if (levelArrayList.size() == 0) {
            return;
        }
        if (currentSongIndex >= levelArrayList.size()) {
            currentSongIndex = 0;
        }

        ParamsSong.stepType2Evaluation = levelArrayList.get(currentSongIndex).getGameType();
        ParamsSong.stepLevel = levelArrayList.get(currentSongIndex).getNumberLevel() < 99 ? levelArrayList.get(currentSongIndex).getNumberLevel() + "" : "??";


        setRecord();
        animateLvl();
    }

    public void changeLvl(int index) {
        currentSongIndex = index;
        changeMusic.play(spCode, 1, 1, 1, 0, 1.0f);
        btnLevel.setOnClickListener(listenerlvl);
        recyclerViewLevels.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.slide_out_right));
        btnLevel.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in));
        lvlText.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in));
        recyclerViewLevels.setVisibility(View.INVISIBLE);

        ParamsSong.stepType2Evaluation = levelArrayList.get(currentSongIndex).getGameType();
        ParamsSong.stepLevel = levelArrayList.get(currentSongIndex).getNumberLevel() < 99 ? levelArrayList.get(currentSongIndex).getNumberLevel() + "" : "??";


        animateLvl();
        setRecord();
    }

    public void openLevelList() {
        playSoundPool(spOpenWindow);
        btnLevel.setOnClickListener(null);
        recyclerViewLevels.setVisibility(View.VISIBLE);
        btnLevel.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_out));

        lvlText.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_out));




        recyclerViewLevels.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.slide_in_left));

    }


    public void animateLvl() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.blink);
        lvlText.setText(levelArrayList.get(currentSongIndex).getNumberLevel() > 95 ? "DP" : levelArrayList.get(currentSongIndex).getNumberLevel() + "");
        anim.setRepeatCount(0);
        btnLevel.startAnimation(anim);
        if (levelArrayList.get(currentSongIndex).getGameType().endsWith("double")) {

            Picasso.get().load(R.drawable.hexa_double).into(btnLevel);

            //  lvlText.setTextColor(Color.rgb(188, 244, 66));

        } else if (levelArrayList.get(currentSongIndex).getGameType().endsWith("single")) {
            //  lvlText.setTextColor(Color.rgb(239, 140, 33));
            Picasso.get().load(R.drawable.hexa_single).into(btnLevel);

        } else if (levelArrayList.get(currentSongIndex).getGameType().endsWith("routine")) {
            //  lvlText.setTextColor(Color.rgb(92, 66, 244))
            // ;
            Picasso.get().load(R.drawable.hexa_performance).into(btnLevel);

        }

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void loadSongs() {
        songsGroup = SongsGroup.readIt(getBaseContext());//busca su propio archivo
        if (songsGroup == null || Common.RELOAD_SONGS) {//Si no existe o si se esta recargando
            reloadSongs();
            AdapterSSC adapterSSC = new AdapterSSC(songsGroup, currentSongIndex);
            recyclerView.setAdapter(adapterSSC);
            //  themeElements.biuldObject(this, songsGroup);
        } else {
            AdapterSSC adapterSSC = new AdapterSSC(songsGroup, currentSongIndex);
            recyclerView.setAdapter(adapterSSC);
            concurrentSort(songsGroup.listOfSongs, songsGroup.listOfSongs);
            changeSong(0);

        }
        //changeSong(cu);
        changeLevel();
    }


    private void hide() {
        backgroundBluour.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onClick(View v) {
        hide();
    }

    public static void concurrentSort(
            final List<SSC> key, List<?>... lists) {
        // Create a List of indices
        List<Integer> indices = new ArrayList<Integer>();
        for (int i = 0; i < key.size(); i++)
            indices.add(i);
        // Sort the indices list based on the key
        Collections.sort(indices, new Comparator<Integer>() {
            @Override
            public int compare(Integer i, Integer j) {
                return key.get(i).songInfo.get("TITLE").compareToIgnoreCase(key.get(j).songInfo.get("TITLE"));

            }
        });
        // Create a mapping that allows sorting of the List by N swaps.
        // Only swaps can be used since we do not know the type of the lists
        Map<Integer, Integer> swapMap = new HashMap<Integer, Integer>(indices.size());
        List<Integer> swapFrom = new ArrayList<Integer>(indices.size()),
                swapTo = new ArrayList<Integer>(indices.size());
        for (int i = 0; i < key.size(); i++) {
            int k = indices.get(i);
            while (i != k && swapMap.containsKey(k))
                k = swapMap.get(k);
            swapFrom.add(i);
            swapTo.add(k);
            swapMap.put(i, k);
        }
        // use the swap order to sort each list by swapping elements
        for (List<?> list : lists)
            for (int i = 0; i < list.size(); i++)
                Collections.swap(list, swapFrom.get(i), swapTo.get(i));
    }


    String obbMountPath = "nono paso na";
    OnObbStateChangeListener obi = new OnObbStateChangeListener() {
        @SuppressLint("NewApi")
        @Override
        public void onObbStateChange(String path, int state) {
            super.onObbStateChange(path, state);
            if (state == OnObbStateChangeListener.MOUNTED) {

                StorageManager storage = (StorageManager) getSystemService(STORAGE_SERVICE);
                obbMountPath = storage.getMountedObbPath(path);
                songsGroup.addList(new File(obbMountPath));
                //AdapterSSC adapterSSC = new AdapterSSC(songsGroup, currentSongIndex, this);
                //recyclerView.setAdapter(adapterSSC);
                //themeElements.biuldObject(this, songsGroup);
                //changeSong(0);

                Log.i("OBB-MOUNT", "state: " + state);

            } else {

                Log.i("OBB-MOUNT", "state: " + state);
                obbMountPath = "ERROR";
            }

            //  loadSongs();
        }
    };

    public String mountObbFile(String obbFile) {
        StorageManager storageManager = (StorageManager) getSystemService(STORAGE_SERVICE);
        storageManager.mountObb(obbFile, null, obi);
        String w = storageManager.getMountedObbPath(obbFile);
        boolean x = storageManager.isObbMounted(obbMountPath);
        return obbMountPath;
    }


    private void playVideoError() {
        Uri uriVideoBgError = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bg_no_banner);
        preview.setVideoURI(uriVideoBgError);
        preview.start();

    }

    public void startSong() {


        // root.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_out));
        releaseMediaPlayer();

        preview.suspend();
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double availableMegs = mi.availMem / 0x100000L;
        double percentAvail = mi.availMem / (double) mi.totalMem * 100.0;

        if (availableMegs < 100) {
            //     songsGroup.listOfSongs.clear();
            //    songsGroup = null;
            //
            // System.gc();
        }

        i.putExtra("ssc", currentSSC);
        i.putExtra("nchar", currentSongIndex);
        i.putExtra("path", paths);
        i.putExtra("pathDisc", pathImage);
        startActivity(i);
        //finish();
        startImage.setOnClickListener(null);
    }

    private void setRecord() {
        String secretName;
        secretName = (String) titleCurrentSong.getText() + currentSongIndex;
        if (Common.getRecords(this, secretName).contains("N")) {
            tv_record.setText("Non Played");
        } else {
            tv_record.setText(Common.getRecords(this, secretName));
        }
    }


    public void reloadSongs() {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        songsGroup = new SongsGroup();
        File aux = Common.checkDirSongsFolders(this);
        if (aux == null) {//implement Chose folder stuff

        } else {

            files = new File(aux.getPath() + "/songs").listFiles();
            if (   files.length < 1) {
                Toast.makeText(getBaseContext(), "There are't files in" + aux.getPath() + "/songs please chose a forder or put song data", Toast.LENGTH_LONG).show();
                try {
                    zipUtils zip= new zipUtils();
                    zip.unpackZip(aux.getPath() ,getAssets().open("songs/had.zip"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //implements add From assets Folder
                songsGroup.addList(new File(aux.getPath() + "/songs"));

            }
        }
        if (songsGroup.listOfSongs.size() < 1) {
            startActivity(new Intent(SongList.this, MainScreenActivity.class));
            finish();
            Toast.makeText(getBaseContext(), "No song found please verify  ¯\\_(⊙_ʖ⊙)_/¯", Toast.LENGTH_LONG).show();
        } else {//all things are ok
            concurrentSort(songsGroup.listOfSongs, songsGroup.listOfSongs);
            try {
                songsGroup.saveIt(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            prefsEditor.putBoolean("reload_songs", false);
            prefsEditor.apply();
            changeSong(0);
        }
    }

}





    