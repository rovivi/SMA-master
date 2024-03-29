package com.kyadevs.stepdroid.ScreenSelectMusic;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.kyadevs.stepdroid.CommonGame.Common;
import com.kyadevs.stepdroid.CommonGame.SSC;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class SongsGroup implements Serializable {
    public String description = "";
    public String name;
    private String text;

    //public SoundPool audio;
    public String banner = null;
    public ArrayList<SSC> listOfSongs = new ArrayList<>();
    // public ArrayList<File> listOfPaths = new ArrayList<>();
    //public ArrayList<String> listOfPathsSSC = new ArrayList<>();
    public ArrayList<SSC> assetsListOfSongs = new ArrayList<>();

    public void setText(String text) {
        this.text = text;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public SongsGroup(File path) {

        add2List(path, false);

    }


    public SongsGroup() {
    }

    public void addList(File path) {
        add2List(path, false);
    }

    public void addListOBB(File path) {
        add2List(path, true);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void addAssetsSongs(AssetManager am) {
        try {
            String[] files = am.list("songs");
            for (String currentFolder : files) {
                String[] filesInside = am.list("songs/" + currentFolder);
                for (String currentFile : filesInside) {
                    if (currentFile.toLowerCase().endsWith(".ssc")) {
                        String ssc = Common.is2String(am.open("songs/" + currentFolder + "/" + currentFile));
                        SSC aux = new SSC(ssc,true);
                        aux.pathSSC = "songs/" + currentFolder ;
                        aux.path = new File("songs/" + currentFolder + "/" + currentFile);
                        listOfSongs.add(aux);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void add2List(File path, boolean isPropietary) {
        name = path.getName();
        File[] songsPaths = path.listFiles();
        for (File i : songsPaths) {
            if (i.isDirectory()) {
                File[] songFiles = i.listFiles();
                if (i.getName().contains("info")) {
                    try {
                        String x = Common.convertStreamToString(new FileInputStream(i.getPath() + "/data.txt"));
                        description = x;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    int size = songFiles.length;
                    int currentIndex = 0;
                    for (File j : songFiles) {
                        if (j.getPath().endsWith(".ssc") ) {
                            try {
                                SSC aux = new SSC(Common.convertStreamToString(new FileInputStream(j)), true, isPropietary);
                                aux.path = i;
                                aux.pathSSC = j.getPath();
                                listOfSongs.add(aux);
                                //listOfPathsSSC.add(j.getPath());
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (text != null) {
                            int percet = (int) (100 * (float) (currentIndex / size));
                            text = percet + "%";
                        }
                        currentIndex++;
                    }
                }
            } else if (i.getName().contains("banner")) {
                banner = i.getPath();
            }
        }


    }


    public void saveIt(Context context, String path) throws IOException {
        FileOutputStream fos = context.openFileOutput("SongList.dat", Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(this);
        os.close();
        fos.close();
    }

    public void saveIt(Context context) throws IOException {
        FileOutputStream fos = context.openFileOutput("SongList.dat", Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(this);
        os.close();
        fos.close();

    }


    public static SongsGroup readIt(Context context) {
        try {
            FileInputStream fis = context.openFileInput("SongList.dat");
            ObjectInputStream is = new ObjectInputStream(fis);
            SongsGroup simpleClass = (SongsGroup) is.readObject();
            is.close();
            fis.close();
            return simpleClass;
        } catch (Exception e) {
            return null;
        }
    }

}
