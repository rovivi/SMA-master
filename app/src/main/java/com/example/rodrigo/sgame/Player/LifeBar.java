package com.example.rodrigo.sgame.Player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.rodrigo.sgame.CommonGame.TransformBitmap;
import com.example.rodrigo.sgame.R;

public class LifeBar {

    private float life=50,lifeblue=0;
    float aumento=0, aumentLife, auxLife =1f;
    Bitmap tipBlue, tipRed, glowBlue, glueRed,skin, barHot,resplandor, lightRed;
    long timeMark;

    public LifeBar(Context context,String mode){
        BitmapFactory.Options myOpt2 = new BitmapFactory.Options();
        myOpt2.inSampleSize = 4;
        
        tipBlue = BitmapFactory.decodeResource(context.getResources(), R.drawable.blue_tip,myOpt2);
        tipRed = BitmapFactory.decodeResource(context.getResources(), R.drawable.red_tip,myOpt2);
        glowBlue = BitmapFactory.decodeResource(context.getResources(), R.drawable.solid,myOpt2);
        barHot = BitmapFactory.decodeResource(context.getResources(), R.drawable.barhot_double,myOpt2);
        skin= BitmapFactory.decodeResource(context.getResources(), R.drawable.lifeframe,myOpt2);
        glueRed = BitmapFactory.decodeResource(context.getResources(), R.drawable.caution,myOpt2);
        resplandor= BitmapFactory.decodeResource(context.getResources(), R.drawable.resplandor,myOpt2);
        lightRed = BitmapFactory.decodeResource(context.getResources(), R.drawable.resplandor,myOpt2);
        timeMark = System.nanoTime();



    }

    public void draw(Canvas canvas,int x,int y){
        //se calcula la pociocion del tip
        aumento++;

        float porcent= (life /100);
        int totalX=(int) ((x*0.7));
        int pocicionTip= (int) ((totalX*porcent));
        int posIntY= (int) (y*0.01);
        int posIntX= (int) (y*0.15);
        int posFinalY=posIntY+(int) (y*0.045);
        float posBarBlue = (totalX*(porcent+ aumentLife /100));
        Bitmap currentHotBar= TransformBitmap.cutBitmap(barHot, life );


        if (life<95){
            canvas.drawBitmap(glowBlue, null, new Rect(posIntX, posIntY, (int) (posIntX +posBarBlue), posFinalY), new Paint());
            canvas.drawBitmap(tipBlue, null, new Rect(posIntX+pocicionTip, posIntY,  posIntX+pocicionTip+(int)(x*0.05), posFinalY), new Paint());
        }
        if(life<25){
            canvas.drawBitmap(glueRed, null, new Rect(posIntX, posIntY, posIntX+totalX, posFinalY), new Paint());
        }

        canvas.drawBitmap(currentHotBar, null, new Rect(posIntX, posIntY, posIntX +pocicionTip, posFinalY), new Paint());
        if (life>98 ){
            canvas.drawBitmap(        TransformBitmap.makeTransparent(resplandor, (int) (0+ aumentLife *20)), null, new Rect(posIntX, posIntY, posIntX+totalX, posFinalY), new Paint());
        }
        else if (life<15){
            canvas.drawBitmap(        TransformBitmap.makeTransparent(lightRed, (int) (0+ aumentLife *20)), null, new Rect(posIntX, posIntY, posIntX+totalX, posFinalY), new Paint());
        }

        canvas.drawBitmap(skin, null, new Rect(posIntX, posIntY, posIntX+totalX, posFinalY), new Paint());






    }

    public void updateLife(float life)
    {
        if ((System.nanoTime()- timeMark)>100){
            if (aumentLife >3 || aumentLife <0){
                auxLife *=-1;
            }
            aumentLife += auxLife;
            timeMark =System.nanoTime();
        }
        this.life=life;
    }

}
