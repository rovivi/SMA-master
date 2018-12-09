package com.example.rodrigo.sgame.Player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.rodrigo.sgame.CommonGame.CustomSprite.SpriteReader;
import com.example.rodrigo.sgame.CommonGame.TransformBitmap;
import com.example.rodrigo.sgame.R;

public class Combo {
    int aumentocombo = -200;
    SpriteReader judgesprite,numbercombo;
    int Combo=0;

    int x=1,y=1;
    Bitmap combo,badcombo,currenttmapcombo;


    long tiemposcombo=0;
     public short posjudge=0;

    public Combo(Context c){
        BitmapFactory bf = new BitmapFactory();
        Bitmap juez = BitmapFactory.decodeResource(c.getResources(), R.drawable.judgelabels15);
        numbercombo = new SpriteReader(BitmapFactory.decodeResource(c.getResources(), R.drawable.combo4_4), 4, 4, 1f);
        judgesprite = new SpriteReader(juez, 1, 5, 1f);
        combo = BitmapFactory.decodeResource(c.getResources(), R.drawable.combo);
        badcombo= BitmapFactory.decodeResource(c.getResources(), R.drawable.combobad);
    }


    public void start(){
        tiemposcombo = System.currentTimeMillis();
    }
    public void  show (){
        aumentocombo=8;
        if (Combo>=0){
            currenttmapcombo=combo;
        }
        else {
            currenttmapcombo=badcombo;
        }


    }

    public void update(int nc){


        if (System.currentTimeMillis() - tiemposcombo > 25) {
            aumentocombo -= 1;
        }
        Combo=nc;
    }
    public void draw(Canvas canvas,int x,int y){
        if (aumentocombo > -12) {
            float ratiostep = 0.66f;
            float aumento = 0.0425f;
            float ratiostepcombo = 2 * ratiostep / 3;
            float aumentolabel = 2 * aumento / 3;
            int posintx = (int) (x / 2 - (x * 0.3) / 2);
            int posinty = (int) (y / 2 - (y * 0.084) / 2);
            int posintxcombo = (int) (x / 2 - (x * 0.13)*(ratiostep + aumento * aumentocombo) / 2);
            int posintycombo =(int)(posinty+(y * 0.039)+(y * 0.084) * (ratiostepcombo + aumentolabel * aumentocombo) / 2);// (int) (y / 2 - (y * 0.05) / 2);
            int posintync = 0;
            posintx = (int) (x / 2 - (x * 0.4* (ratiostep + aumento * aumentocombo)) / 2);
            // dibujante.setColor(Color.TRANSPARENT);


            ////////Medidades
            /*
            Perfect = (x=20%,y=6&) y max (x=30%,y=8.4&)
            Combo   =   (x=10%,y=3.1&) y max (x=15%,y=5&)
            1000    =  (x=5%,y=5&)
            */
            /////////
        if (aumentocombo >= 0) {

            canvas.drawBitmap(judgesprite.frames[posjudge], null, new Rect(posintx, posinty, posintx + (int) ((x * 0.4) * (ratiostep + aumento * aumentocombo)), posinty + (int) ((y * 0.084) * (ratiostep + aumento * aumentocombo))), new Paint());
            posintync = posintycombo + (int) ((combo.getHeight()) * (ratiostepcombo + aumentolabel * aumentocombo) * 0.7);

        } else if (aumentocombo < -6 ) {

            posintx = (int) (x / 2 - (x * 0.4* (ratiostep )) / 2);
            int opacidad =  (100 + 5 * aumentocombo);
            int xt = aumentocombo * -1;
            int yt = aumentocombo * 1;
            canvas.drawBitmap(TransformBitmap.makeTransparent(judgesprite.frames[posjudge], opacidad), null, new Rect(posintx, posinty, posintx + (int)(x * 0.4* (ratiostep )*((float) xt*5/100)) , 10 * aumentocombo + posinty + (int) ((y * 0.084) * (ratiostep + aumento * aumentocombo )*((float) yt*5/100))), new Paint());

        } else {
            posintx = (int) (x / 2 - (x * 0.4* (ratiostep )) / 2);
            canvas.drawBitmap(judgesprite.frames[posjudge], null, new Rect(posintx, posinty, posintx + (int) (x * 0.4 * ratiostep), posinty + (int) (y * 0.084 * ratiostep)), new Paint());
           // canvas.drawBitmap(currenttmapcombo, null, new Rect(posintxcombo, posintycombo, posintxcombo + (int) ((x * 0.17) * (ratiostepcombo + aumentolabel)), posintycombo + (int) ((y * 0.053) * (ratiostepcombo + aumentolabel))), new Paint());

        }

        //draw
        //canvas.drawText(""+Combo,x/2-50,y/2,dibujante);
            posintync = posintycombo + (int) ((y * 0.05) * (ratiostepcombo + aumentolabel * aumentocombo));
        if (Combo > 3||Combo<-3) {
            canvas.drawBitmap(currenttmapcombo, null, new Rect(posintxcombo, posintycombo, posintxcombo + (int) ((x * 0.17) * (ratiostepcombo + aumentolabel * aumentocombo)), posintycombo + (int) ((y * 0.053) * (ratiostepcombo + aumentolabel * aumentocombo))), new Paint());
            ////
            long lc = 100000000 +  Math.abs(Combo);
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
                canvas.drawBitmap(numbercombo.frames[n], null, new Rect(posxnc, posintync, posxnc + (int) (x * 0.05), posintync + (int) (x * 0.05)), new Paint());
            }
        }

    }

    }
}
