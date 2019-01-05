package com.example.rodrigo.sgame.Player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.rodrigo.sgame.CommonGame.Common;
import com.example.rodrigo.sgame.CommonGame.CustomSprite.SpriteReader;
import com.example.rodrigo.sgame.CommonGame.TransformBitmap;
import com.example.rodrigo.sgame.R;

import java.io.InputStream;

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
            myOpt.inSampleSize = 0;

            BitmapFactory.Options myOpt2 = new BitmapFactory.Options();
            myOpt2.inSampleSize = 4;


            arrows = new SpriteReader[numberSteps];
            tails = new SpriteReader[numberSteps];
            longs = new SpriteReader[numberSteps];
            explotionTails = new SpriteReader[numberSteps];
            tapsEffect = new SpriteReader[10];
            explotions = new SpriteReader[numberSteps];
            receptors = new SpriteReader[numberSteps];


            for (int j = 0; j < 5; j++) {
                tapsEffect[j] = new SpriteReader(TransformBitmap.customSpriteArray(BitmapFactory.decodeResource(context.getResources(), R.drawable.pad_pressed), 5, 2, j, j + 5, j + 5, j), 0.2f);
                tapsEffect[j + 5] = new SpriteReader(TransformBitmap.customSpriteArray(BitmapFactory.decodeResource(context.getResources(), R.drawable.pad_pressed), 5, 2, j, j + 5, j + 5, j), 0.2f);
                if (assetPath) {
                    InputStream stream1 = context.getAssets().open(pathNS + Common.PIU_ARROW_NAMES[j] + "tap.png");
                    InputStream stream2 = context.getAssets().open(pathNS + Common.PIU_ARROW_NAMES[j] + "hold.png");
                    InputStream stream3 = context.getAssets().open(pathNS + Common.PIU_ARROW_NAMES[j] + "hold_end.png");
                    InputStream stream4 = context.getAssets().open(pathNS + Common.PIU_ARROW_NAMES[j] + "receptor.png");
                    arrows[j] = new SpriteReader(BitmapFactory.decodeStream(stream1, null, myOpt), 3, 2, 0.2f);
                    tails[j] = new SpriteReader(BitmapFactory.decodeStream(stream3, null, myOpt), 6, 1, 0.2f);
                    longs[j] = new SpriteReader(BitmapFactory.decodeStream(stream2, null, myOpt), 6, 1, 0.2f);
                    receptors[j] = new SpriteReader(TransformBitmap.customSpriteArray(BitmapFactory.decodeStream(stream4, null, myOpt), 1, 3, 0, 1), 0.8f);

                } else {
                    //stuff for reading on SD
                }
            }


            Bitmap r1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.s1, myOpt2);
            Bitmap r2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.s2, myOpt2);
            Bitmap r3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.s3, myOpt2);
            Bitmap r4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.s4, myOpt2);
            Bitmap r5 = BitmapFactory.decodeResource(context.getResources(), R.drawable.s5, myOpt2);
            for (int w = 0; (type.equals("pump-double") || type.equals("pump-routine")) && w < 5; w++) {
                arrows[w + 5] = arrows[w];
                tails[5 + w] = tails[w];
                longs[5 + w] = longs[w];
                receptors[5 + w] = receptors[w];

                // tails[5+w] = tails[w];
            }


            for (int cd = 0; cd < numberSteps; cd++) {
                explotions[cd] = new SpriteReader(new Bitmap[]{
                        TransformBitmap.returnMaskCut(arrows[cd].frames[0], TransformBitmap.returnMask(arrows[cd].frames[0], r1)),
                        TransformBitmap.returnMaskCut(arrows[cd].frames[1], TransformBitmap.returnMask(arrows[cd].frames[1], r2)),
                        TransformBitmap.returnMaskCut(arrows[cd].frames[2], TransformBitmap.returnMask(arrows[cd].frames[2], r3)),
                        TransformBitmap.returnMaskCut(arrows[cd].frames[3], TransformBitmap.returnMask(arrows[cd].frames[3], r3)),
                        TransformBitmap.returnMaskCut(arrows[cd].frames[4], TransformBitmap.returnMask(arrows[cd].frames[4], r4)),
                        TransformBitmap.returnMaskCut(arrows[cd].frames[5], TransformBitmap.returnMask(arrows[cd].frames[5], r5)),
                }, 0.15f);
                explotionTails[cd] = explotions[cd];
            }


            //Bitmap exploAux=BitmapFactory.decodeResource(context.getResources(),R.drawable.explosion_6x1);
            //Bitmap[] res = TransformBitmap.customSpriteArray(exploAux,6,1,0,1,2,3,4,5);


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

}
