package com.example.rodrigo.sgame.Player;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.rodrigo.sgame.CommonGame.Common;
import com.example.rodrigo.sgame.CommonGame.CustomSprite.SpriteReader;
import com.example.rodrigo.sgame.CommonGame.TransformBitmap;
import com.example.rodrigo.sgame.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class NoteSkin {
    private Context context;
    public SpriteReader mine, potion;
    public SpriteReader[] arrows, longs, tails;
    public SpriteReader[] explotions, explotionTails, tapsEffect, receptors;


    public NoteSkin(Context c, String type, String name) {
        context = c;

        if (type.contains("pump")) {
            loadPiuSkins(name, type);
        } else if (type.contains("dance")) {
            loadDanceSkins(name, type);
        }


    }


    private void loadPiuSkins(String name, String type) {

        try {
            String pathNS = "NoteSkins/pump/" + name + "/";
            byte numberSteps;
            boolean assetPath = true;


            switch (type) {
                case "pump-single":
                    numberSteps = 5;
                    break;
                case "pump-double":
                case "pump-routine":
                    numberSteps = 10;
                    break;
                default:
                    numberSteps = 6;//half
                    break;
            }
            //   receptor = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.base), 1, 2, 0.5f);
            //receptor.play();

            BitmapFactory.Options myOpt = new BitmapFactory.Options();
            myOpt.inSampleSize = 1 * Common.Compression2D;

            BitmapFactory.Options myOpt2 = new BitmapFactory.Options();
            myOpt2.inSampleSize = 4 * Common.Compression2D;


            arrows = new SpriteReader[numberSteps];
            tails = new SpriteReader[numberSteps];
            longs = new SpriteReader[numberSteps];
            explotionTails = new SpriteReader[numberSteps];
            tapsEffect = new SpriteReader[10];
            explotions = new SpriteReader[numberSteps];
            receptors = new SpriteReader[numberSteps];

            for (int j = 0; j < 5; j++) {
              //  tapsEffect[j] = new SpriteReader(TransformBitmap.customSpriteArray(BitmapFactory.decodeResource(context.getResources(), R.drawable.pad_pressed, myOpt), 5, 2, j, j + 5, j + 5, j), 0.2f);

                    Bitmap [] arrayAux = new Bitmap[2];
                 //   arrayAux[0]=receptorArray[j];
              //  tapsEffect[j + 5] = new SpriteReader(TransformBitmap.customSpriteArray(BitmapFactory.decodeResource(context.getResources(), R.drawable.pad_pressed, myOpt), 5, 2, j, j + 5, j + 5, j), 0.2f);
                if (assetPath) {
                    InputStream stream1 = context.getAssets().open(pathNS + Common.PIU_ARROW_NAMES[j] + "tap.png");
                    InputStream stream2 = context.getAssets().open(pathNS + Common.PIU_ARROW_NAMES[j] + "hold.png");
                    InputStream stream3 = context.getAssets().open(pathNS + Common.PIU_ARROW_NAMES[j] + "hold_end.png");
                    InputStream stream4 = context.getAssets().open(pathNS + Common.PIU_ARROW_NAMES[j] + "receptor.png");
                    Bitmap [] arrayAux2 =TransformBitmap.customSpriteArray(Objects.requireNonNull(BitmapFactory.decodeStream(stream4, null, myOpt)), 1, 3, 0, 1,2);
                    arrows[j] = new SpriteReader(Objects.requireNonNull(BitmapFactory.decodeStream(stream1, null, myOpt)), 3, 2, 0.32f);
                    tails[j] = new SpriteReader(Objects.requireNonNull(BitmapFactory.decodeStream(stream3, null, myOpt)), 6, 1, 0.32f);
                    longs[j] = new SpriteReader(Objects.requireNonNull(BitmapFactory.decodeStream(stream2, null, myOpt)), 6, 1, 0.32f);
                    receptors[j] = new SpriteReader(Arrays.copyOfRange(arrayAux2,0,1), 0.8f);
                    arrayAux[0]=arrayAux2[2];
                    arrayAux[1]=arrayAux2[2];
                    tapsEffect[j] = new SpriteReader(arrayAux,0.3f);
                    tapsEffect[j+5] = new SpriteReader(arrayAux,0.3f);
                } else {
                    //stuff for reading on SD
                }
            }


            for (int w = 0; (type.equals("pump-double") || type.equals("pump-routine")) && w < 5; w++) {
                arrows[w + 5] = arrows[w];
                tails[5 + w] = tails[w];
                longs[5 + w] = longs[w];
                receptors[5 + w] = receptors[w];

                // tails[5+w] = tails[w];
            }


            Bitmap explotion= BitmapFactory.decodeStream(context.getAssets().open(pathNS + "_explosion 6x1.png"));
           // Bitmap explotion= TransformBitmap.replaceColor(BitmapFactory.decodeStream(context.getAssets().open(pathNS + "_explosion 6x1.png")),Color.BLACK,Color.TRANSPARENT);

            Bitmap[] explotionArray= TransformBitmap.customSpriteArray(explotion,6,1,0,1,2,3,4,5);

            for (int cd = 0; cd < numberSteps; cd++) {
                Bitmap[] explotionArray2= new Bitmap[6];
                for (int j=0;j<6;j++){
                    explotionArray2[j]=TransformBitmap.returnMaskCut(arrows[cd].frames[j], TransformBitmap.returnMask(arrows[cd].frames[j], explotionArray[j]));
                 //   explotionArray2[j]=TransformBitmap.returnMask(arrows[cd].frames[j], explotionArray[j]);

                }
                explotions[cd] = new SpriteReader( explotionArray2, 0.15f);
                explotionTails[cd] = explotions[cd];
            }


            for (int x = 0; x < arrows.length; x++) {
                arrows[x].play();
                longs[x].play();
                tails[x].play();
                receptors[x].play();
            }
            mine = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.mine), 3, 2, 0.2f);
            mine.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadDanceSkins(String name, String type) {
        Bitmap upOn = BitmapFactory.decodeResource(context.getResources(), R.drawable.dance_pad_up_on);

        explotionTails[0] = explotionTails[2];
        explotionTails[1] = explotionTails[2];
        explotionTails[3] = explotionTails[2];
        tails[0] = tails[2];
        tails[1] = tails[2];
        tails[3] = tails[2];
        arrows[0] = new SpriteReader(TransformBitmap.RotateBitmap(upOn, 270), 1, 1, 0.2f);
        arrows[1] = new SpriteReader(TransformBitmap.RotateBitmap(upOn, 180), 1, 1, 0.2f);
        arrows[2] = new SpriteReader(upOn, 1, 1, 0.2f);
        arrows[3] = new SpriteReader(TransformBitmap.RotateBitmap(upOn, 90), 1, 1, 0.2f);
        arrows[0].play();
        arrows[1].play();
        arrows[2].play();
        arrows[3].play();

    }


    public static boolean validNoteSkin(String name,Context c) {
        String pathNS = "NoteSkins/pump/" + name + "/";
        for (int j = 0; j < 5; j++) {
            if (true) {
                try {
                    InputStream stream1 = c.getAssets().open(pathNS + Common.PIU_ARROW_NAMES[j] + "tap.png");
                    InputStream stream2 = c.getAssets().open(pathNS + Common.PIU_ARROW_NAMES[j] + "hold.png");
                    InputStream stream3 = c.getAssets().open(pathNS + Common.PIU_ARROW_NAMES[j] + "hold_end.png");
                    InputStream stream4 = c.getAssets().open(pathNS + Common.PIU_ARROW_NAMES[j] + "receptor.png");
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                //stuff for reading on SD
            }
        }
        return true;
    }

    public static Bitmap maskImage(String name,Context c) {
        String pathNS = "NoteSkins/pump/" + name + "/";
        InputStream stream1 = null;
        try {
            stream1 = c.getAssets().open(pathNS + Common.PIU_ARROW_NAMES[2] + "tap.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return TransformBitmap.customSpriteArray(BitmapFactory.decodeStream(stream1), 3, 2, 0)[0];
    }


    public static ArrayList arraySkin(Context c) {
        String pathNS = "NoteSkins/pump";
        ArrayList<String> sendedArray = new ArrayList<>();

        Resources res = c.getResources(); //if you are in an activity
        AssetManager am = res.getAssets();
        String fileList[];
        try {
            fileList = am.list(pathNS);
            for (int i = 0; i < fileList.length; i++) {
                if (validNoteSkin(fileList[i],c)){
                    sendedArray.add(fileList[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sendedArray;
    }


    private void listFiles(String dirFrom) {

    }


}
