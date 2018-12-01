package com.example.rodrigo.sgame;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.OnObbStateChangeListener;
import android.os.storage.StorageManager;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.crashlytics.android.Crashlytics;
import com.example.rodrigo.sgame.CommonGame.Common;
import com.example.rodrigo.sgame.CommonGame.Level;
import com.example.rodrigo.sgame.CommonGame.SSC;
import com.example.rodrigo.sgame.CommonGame.TransformBitmap;
import com.example.rodrigo.sgame.ScreenSelectMusic.AdapterSSC;
import com.example.rodrigo.sgame.ScreenSelectMusic.MusicThread;
import com.example.rodrigo.sgame.ScreenSelectMusic.RecyclerItemClickListener;
import com.example.rodrigo.sgame.ScreenSelectMusic.SongsGroup;
import com.example.rodrigo.sgame.ScreenSelectMusic.ThemeElements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

import io.fabric.sdk.android.Fabric;


public class SongList extends AppCompatActivity implements View.OnClickListener {
    File[] files;
    String currentSSC;
    String paths;
    String path = "";
    public int spCode, spOpenWindow, spSelect;
    MediaPlayer mediaPlayer;
    MusicThread musicTimer;
    //VideoView bg;
    public SoundPool changeMusic;
    final List<String> songs = new ArrayList<>();
    final List<String> lvl = new ArrayList<>();
    final List<SongsGroup> groups = new ArrayList<>();
    int currentSongIndex = 0;
    ThemeElements themeElements;
    // ArrayAdapter<String> adp2;
    Intent i;
    VideoView preview;
    ImageView backgroundBluour, btnLevel;
    TextView lvlText;
    FirebaseAnalytics mFirebaseAnalytics;
    ArrayList<Level> levelArrayList = new ArrayList<>();


    BitmapDrawable errorAuxImage = null;
    SongsGroup songsGroup = new SongsGroup();
    RecyclerView recyclerView;
    ImageView startImage;


    View.OnClickListener listenerButton = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void onClick(View v) {
            // releaseMediaPlayer();
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
            startActivity(i);
            //finish();
            startImage.setOnClickListener(null);
        }
    };


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
         /*   getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LOW_PROFILE
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

*/


        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_songlist);

        //------Se llenan los valosres de las lineas guia----//

        Guideline guideLine = findViewById(R.id.guideStartList);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
        params.guidePercent = 0.5f; // 45% // range: 0 <-> 1
        guideLine.setLayoutParams(params);
        guideLine = findViewById(R.id.guideEndLIst);
        params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
        params.guidePercent = 0.93f; // 45% // range: 0 <-> 1
        guideLine.setLayoutParams(params);
        guideLine = findViewById(R.id.guidePreviewH1);
        params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
        params.guidePercent = 0.23f; // 45% // range: 0 <-> 1
        guideLine.setLayoutParams(params);
        guideLine = findViewById(R.id.guidePreviewH2);
        params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
        params.guidePercent = 0.97f; // 45% // range: 0 <-> 1
        guideLine.setLayoutParams(params);
        guideLine = findViewById(R.id.guideStartPreviewV);
        params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
        params.guidePercent = 0.07f; // 45% // range: 0 <-> 1
        guideLine.setLayoutParams(params);
        guideLine = findViewById(R.id.guideEndPreviewV);
        params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
        params.guidePercent = 0.32f; // 45% // range: 0 <-> 1
        guideLine.setLayoutParams(params);


        //--
        guideLine = findViewById(R.id.guidehor1);
        params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
        params.guidePercent = 0.32f; // 45% // range: 0 <-> 1
        guideLine.setLayoutParams(params);
        guideLine = findViewById(R.id.guidehor2);
        params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
        params.guidePercent = 0.52f; // 45% // range: 0 <-> 1
        guideLine.setLayoutParams(params);
        guideLine = findViewById(R.id.guidever1);
        params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
        params.guidePercent = 0.48f; // 45% // range: 0 <-> 1
        guideLine.setLayoutParams(params);
        guideLine = findViewById(R.id.guidever2);
        params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
        params.guidePercent = 0.7f; // 45% // range: 0 <-> 1
        guideLine.setLayoutParams(params);


        //------Fin de las lineas guia-----//
        //------get Elemets----------------//
        btnLevel = findViewById(R.id.imagelevl);
        lvlText = findViewById(R.id.numberlevel);
        themeElements = findViewById(R.id.songElements);
        backgroundBluour = findViewById(R.id.bgBlur);
        recyclerView = findViewById(R.id.recyclerSongs);
        //  bg = findViewById(R.id.bgVideoView);
        preview = findViewById(R.id.preview);
        startImage = findViewById(R.id.startButton);
        FrameLayout relativeLayout;
        relativeLayout = findViewById(R.id.frameLayoutVideoPreview);


        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(true);
        TextView msjlvl = findViewById(R.id.yourlvlmsjtv);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        msjlvl.setTypeface(custom_font);
        preview.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                //    Log.d("video", "setOnErrorListener ");
                if (what == -1010 || extra == -1010)
                    preview.setBackground(errorAuxImage);
                return true;
            }
        });

        lvlText.setTypeface(custom_font);

        //---------Listeners-------//


        btnLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditFragment();

                //                changeLevel();
            }
        });

        btnLevel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //showEditFragment();

                return false;
            }
        });


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


        //-------------se crea la lista de canciones------------//
        try {
            changeMusic = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

            spCode = changeMusic.load(this, R.raw.change_song, 0);
            spOpenWindow = changeMusic.load(this, R.raw.command_toggle, 0);
            spSelect = changeMusic.load(this, R.raw.command_mod, 0);


            i = new Intent(this, MainActivity.class);
            i = new Intent(this, PlayerBga.class);
            mediaPlayer = new MediaPlayer();


            //   Uri uriVideoBg = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bg);
      /*      bg.setVideoURI(uriVideoBg);
            bg.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setLooping(true);
                    mediaPlayer.setVolume(0, 0);
                }
            });
         */
            preview.setOnPreparedListener(mediaPlayer -> {
                mediaPlayer.setLooping(true);
                mediaPlayer.setVolume(0, 0);
            });


            // loadSongs();

            // preview.setZOrderOnTop(true);
            //themeElements.setZOrderOnTop(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // bg.setZ(0);
                backgroundBluour.setZ(1000000);
                preview.setZ(23000000);
                relativeLayout.setZ(23000000);
                // themeElements.setZ(21000000);
                startImage.setZ(21000000);
                btnLevel.setZ(21000000);
                lvlText.setZ(21000000);
                msjlvl.setZ(21000000);
                recyclerView.setZ(21000000);

            }


            //   bg.start();

        } catch (Exception e) {

            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


        //se carga el OBB
        File obbFile = new File(getObbDir().getPath() + "/main.1." + getPackageName() + ".obb");

        if (obbFile.exists()) {
            //ObbInfo info =ObbScanner.getObbInfo(obbFile.getPath());
            String superinfoinfo = mountObbFile(obbFile.getPath());


        } else {
            loadSongs();

        }

    }


    public void playSoundPool(int spCode) {

        changeMusic.play(spCode, 1, 1, 1, 0, 1.0f);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void changeSong(int position) {

        changeMusic.play(spCode, 1, 1, 1, 0, 1.0f);
        releaseMediaPlayer();

        paths = songsGroup.listOfSongs.get(position).path.getPath();
        try {
            themeElements.flash.play();
            SSC auxStep = songsGroup.listOfSongs.get(position);
            Float sampleStart = Float.parseFloat(auxStep.songInfo.get("SAMPLESTART"));
            int offset = (int) (sampleStart * 1000);

            currentSSC = songsGroup.listOfSongs.get(position).pathSSC;
            lvl.clear();
            levelArrayList.clear();
            for (Map currentChart : auxStep.chartsInfo) {
                if (currentChart == null) {
                    break;
                }
                int lvl = (currentChart.get("METER") != null) ? Integer.parseInt(currentChart.get("METER").toString()) : 0;
                String tipo = (currentChart.get("STEPSTYPE") != null) ? currentChart.get("STEPSTYPE").toString() : "";
                String tag = (currentChart.get("STEPSTYPE") != null) ? currentChart.get("STEPSTYPE").toString() : "";
                levelArrayList.add(new Level(lvl, tipo, tag));
            }


            //If the song provides from OBB
            if (auxStep.isPropitarySongs) {


            } else {
                //if the song provides from SD

                File audio = new File(paths + "/" + auxStep.songInfo.get("MUSIC"));
                File video = new File(paths + "/" + auxStep.songInfo.get("PREVIEWVID"));
                File bg = new File(paths + "/" + auxStep.songInfo.get("BACKGROUND"));

                if (video.exists() && (video.getPath().endsWith(".mpg") || video.getPath().endsWith(".mp4") || video.getPath().endsWith(".avi"))) {
                    preview.setBackground(null);
                    // Uri uri = CustomAPEZProvider.buildUri(video.getPath().replace(obbMountPath + "/", ""));
                    //preview.setVideoURI(uri);
                    preview.setVideoPath(video.getPath());
                    preview.start();

                } else {
                    if (bg.exists() && bg.isFile()) {
                        this.errorAuxImage = new BitmapDrawable(BitmapFactory.decodeFile(bg.getPath()));
                        preview.setBackground(errorAuxImage);
                    }
                }

                if (bg.exists() && bg.isFile()) {
                    Bitmap ww = TransformBitmap.makeTransparent(TransformBitmap.myblur(BitmapFactory.decodeFile(bg.getPath()), getApplicationContext()), 150);
                    backgroundBluour.setImageBitmap(ww);
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
        changeLevel();

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

    }


    @Override
    public void onResume() {
        super.onResume();
        //  this.bg.start();


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
        FragmentLevelList newFragment = new FragmentLevelList();
        newFragment.lista = levelArrayList;
        newFragment.setSongList(this);
        newFragment.show(getFragmentManager(), "");
        playSoundPool(spOpenWindow);
    }


    public void changeLevel() {
        if (levelArrayList.size() == 0) {
            return;
        }
        currentSongIndex++;
        if (currentSongIndex >= levelArrayList.size()) {
            currentSongIndex = 0;
        }
        animelvl();
    }

    public void changelvl(int index) {
        currentSongIndex = index;
        animelvl();
    }


    public void animelvl() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.blink);
        lvlText.setText(levelArrayList.get(currentSongIndex).getNumberLevel() > 95 ? "DP" : levelArrayList.get(currentSongIndex).getNumberLevel() + "");
        anim.setRepeatCount(0);
        btnLevel.startAnimation(anim);
        if (levelArrayList.get(currentSongIndex).getGameType().endsWith("double")) {
            lvlText.setTextColor(Color.rgb(188, 244, 66));

        } else if (levelArrayList.get(currentSongIndex).getGameType().endsWith("single")) {
            lvlText.setTextColor(Color.rgb(239, 140, 33));
        } else if (levelArrayList.get(currentSongIndex).getGameType().endsWith("routine")) {
            lvlText.setTextColor(Color.rgb(92, 66, 244));
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void loadSongs() {


        File aux = Common.checkDirSongsFolders();
        path = aux.getPath();
        files = Common.checkDirSongsFolders().listFiles();
        if (aux == null) {
            File archivo = new File(this.getBaseContext().getFilesDir().getPath() + "/piu/Songs/");
            if (archivo.exists()) {
                files = archivo.listFiles();
                path = archivo.getPath();
            } else if (archivo.mkdirs()) {
                files = archivo.listFiles();
                path = archivo.getPath();
            } else {
                Toast.makeText(getBaseContext(), "No se podra por hoy :C ", Toast.LENGTH_LONG).show();
            }
        }
        if (files.length <= 0) {
            Toast.makeText(getBaseContext(), "No hay archivos :c en:" + path, Toast.LENGTH_LONG).show();
        } else {
            songsGroup.addList(Common.checkDirSongsFolders());
            if (songsGroup.listOfSongs.size() <= 0) {
                Toast.makeText(getBaseContext(), ":c No hay archivos que pueda leer" +
                        " en:" + path, Toast.LENGTH_LONG).show();
            } else {

            }

        }
        AdapterSSC adapterSSC = new AdapterSSC(songsGroup, currentSongIndex, this);
        recyclerView.setAdapter(adapterSSC);
        themeElements.biuldObject(this, songsGroup);
        concurrentSort(songsGroup.listOfSongs, songsGroup.listOfSongs);
        changeSong(0);

    }


    private void hide() {
        themeElements.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
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


    public static <T extends Comparable<T>> void concurrentSort(
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

            loadSongs();
        }
    };

    public String mountObbFile(String obbFile) {
        StorageManager storageManager = (StorageManager) getSystemService(STORAGE_SERVICE);
        storageManager.mountObb(obbFile, null, obi);
        String w = storageManager.getMountedObbPath(obbFile);
        boolean x = storageManager.isObbMounted(obbMountPath);
        return obbMountPath;
    }


}
