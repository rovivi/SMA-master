package com.example.rodrigo.sgame.Player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.rodrigo.sgame.CommonGame.Common;
import com.example.rodrigo.sgame.CommonGame.CustomSprite.SpriteReader;
import com.example.rodrigo.sgame.CommonGame.TransformBitmap;
import com.example.rodrigo.sgame.R;

public class Combo {

    private SpriteReader judgeSprite, numberCombo;
    private Bitmap combo, badCombo, currentBitMapCombo;
    private float ratiostep, aumento, ratiostepcombo, aumentolabel, tiemposcombo;
    private int posIntY,x,y, posIntXCombo, posIntYCombo, posintync, posintx ,Combo = 0,aumentCombo = -220;

    public short posjudge = 0;

    public Combo(Context c, int x, int y) {
        this.x=x;
        this.y=y;
        BitmapFactory.Options myOpt2 = new BitmapFactory.Options();
        myOpt2.inSampleSize = Common.Compression2D;
        Bitmap juez = BitmapFactory.decodeResource(c.getResources(), R.drawable.judgelabels15, myOpt2);
        numberCombo = new SpriteReader(BitmapFactory.decodeResource(c.getResources(), R.drawable.combo4_4, myOpt2), 4, 4, 1f);
        judgeSprite = new SpriteReader(juez, 1, 5, 1f);
        combo = BitmapFactory.decodeResource(c.getResources(), R.drawable.combo, myOpt2);
        badCombo = BitmapFactory.decodeResource(c.getResources(), R.drawable.combobad, myOpt2);
        // dibujante.setColor(Color.TRANSPARENT);


        ////////Medidades
            /*
            Perfect = (x=20%,y=6&) y max (x=30%,y=8.4&)
            Combo   =   (x=10%,y=3.1&) y max (x=15%,y=5&)
            1000    =  (x=5%,y=5&)
            */
        /////////
        ratiostep = 0.66f;
        aumento = 0.0425f;
        aumentolabel = 2 * aumento / 3;
        ratiostepcombo = 2 * ratiostep / 3;

        posIntY = (int) (y / 2 - (y * 0.084) / 2);
        posintync = 0;


    }


    public void start() {
        tiemposcombo = System.currentTimeMillis();
    }

    public void show() {
        aumentCombo = 8;
        if (Combo >= 0) {
            currentBitMapCombo = combo;
        } else {
            currentBitMapCombo = badCombo;
        }


    }

    public void update(int nc) {
        aumentCombo -= 1;
        Combo = nc;
    }

    public void draw(Canvas canvas) {
        posintx = (int) (x / 2 - (x * 0.4 * (ratiostep + aumento * aumentCombo)) / 2);
        posIntXCombo = (int) (x / 2 - (x * 0.13) * (ratiostep + aumento * aumentCombo) / 2);
        posIntYCombo = (int) (posIntY + (y * 0.039) + (y * 0.084) * (ratiostepcombo + aumentolabel * aumentCombo) / 2);// (int) (y / 2 - (y * 0.05) / 2);


        if (aumentCombo > -12) {
            if (aumentCombo >= 0) {

                canvas.drawBitmap(judgeSprite.frames[posjudge], null, new Rect(posintx, posIntY, posintx + (int) ((x * 0.4) * (ratiostep + aumento * aumentCombo)), posIntY + (int) ((y * 0.084) * (ratiostep + aumento * aumentCombo))), new Paint());
                posintync = posIntYCombo + (int) ((combo.getHeight()) * (ratiostepcombo + aumentolabel * aumentCombo) * 0.7);

            } else if (aumentCombo < -6) {
                posintx = (int) (x / 2 - (x * 0.4 * (ratiostep)) / 2);
                int opacidad = (100 + 5 * aumentCombo);
                int xt = aumentCombo * -1;
                int yt = aumentCombo * 1;
                canvas.drawBitmap(TransformBitmap.makeTransparent(judgeSprite.frames[posjudge], opacidad), null, new Rect(posintx, posIntY, posintx + (int) (x * 0.4 * (ratiostep) * ((float) xt * 5 / 100)), 10 * aumentCombo + posIntY + (int) ((y * 0.084) * (ratiostep + aumento * aumentCombo) * ((float) yt * 5 / 100))), new Paint());

            } else {
                posintx = (int) (x / 2 - (x * 0.4 * (ratiostep)) / 2);
                canvas.drawBitmap(judgeSprite.frames[posjudge], null, new Rect(posintx, posIntY, posintx + (int) (x * 0.4 * ratiostep), posIntY + (int) (y * 0.084 * ratiostep)), new Paint());
                // canvas.drawBitmap(currentBitMapCombo, null, new Rect(posIntXCombo, posIntYCombo, posIntXCombo + (int) ((x * 0.17) * (ratiostepcombo + aumentolabel)), posIntYCombo + (int) ((y * 0.053) * (ratiostepcombo + aumentolabel))), new Paint());
            }

            //draw
            //canvas.drawText(""+Combo,x/2-50,y/2,dibujante);
            posintync = posIntYCombo + (int) ((y * 0.05) * (ratiostepcombo + aumentolabel * aumentCombo));
            if (Combo > 3 || Combo < -3) {
                canvas.drawBitmap(currentBitMapCombo, null, new Rect(posIntXCombo, posIntYCombo, posIntXCombo + (int) ((x * 0.17) * (ratiostepcombo + aumentolabel * aumentCombo)), posIntYCombo + (int) ((y * 0.053) * (ratiostepcombo + aumentolabel * aumentCombo))), new Paint());
                ////
                long lc = 100000000 + Math.abs(Combo);
                String sc = lc + "";
                String sc2 = Math.abs(Combo) + "";
                int nveces = 4;//dice el numero de veces que tienes

                if (sc2.length() > 3) {
                    nveces = sc2.length() + 1;
                }
                for (int w = 1; w < nveces; w++) {
                    int leng = (nveces - 1) * (int) (x * 0.05);
                    int posxnc = (int) ((0.9 * (leng / 2) + x / 2) - x * 0.05 * w * 1.0);
                    int n = Integer.parseInt(sc.charAt(sc.length() - w) + "");
                    canvas.drawBitmap(numberCombo.frames[n], null, new Rect(posxnc, posintync, posxnc + (int) (x * 0.05), posintync + (int) (x * 0.05)), new Paint());
                }
            }

        }

    }
}
