package com.example.rodrigo.sgame.ScreenSelectMusic;

import android.annotation.TargetApi;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.rodrigo.sgame.CommonGame.Common;
import com.example.rodrigo.sgame.CommonGame.SSC;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SongsGroup {
    public String description = "";
    public String name;
    public SoundPool audio;
    public Bitmap banner = null;
    public static ArrayList<SSC> listOfSongs = new ArrayList<>();
   // public ArrayList<File> listOfPaths = new ArrayList<>();
    //public ArrayList<String> listOfPathsSSC = new ArrayList<>();
    public ArrayList<SSC> assetsListOfSongs = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public SongsGroup(File path) {
        add2List(path,false);
    }

    public SongsGroup() {
    }

    public void addList(File path) {
        add2List(path,false);
    }
    public void addListOBB(File path) {
        add2List(path,true);
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void addAssetsSongs(AssetManager am) throws IOException {
        String[] archivos = am.list("Songs");

        for (String currentFodler : archivos) {
            String[] archivos2 = am.list("Songs/" + currentFodler);
            for (String currentFile : archivos2) {
                if (currentFile.toLowerCase().endsWith(".ssc")) {
                    String ssc = Common.is2String(am.open("Songs/" + currentFodler + "/" + currentFile));
                    assetsListOfSongs.add(new SSC(ssc, true,false));

                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void add2List(File path,boolean isPropietary) {
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
                    for (File j : songFiles) {
                        if (j.getPath().endsWith(".ssc")) {
                            try {
                                SSC aux = new SSC(Common.convertStreamToString(new FileInputStream(j)), true,isPropietary);
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
                    }
                }
            } else if (i.getName().contains("banner")) {
                banner = BitmapFactory.decodeFile(i.getPath());
            }
        }


    }


}
