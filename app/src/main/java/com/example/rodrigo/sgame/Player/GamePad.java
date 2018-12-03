package com.example.rodrigo.sgame.Player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.WindowManager;

import com.example.rodrigo.sgame.CommonGame.Common;
import com.example.rodrigo.sgame.CommonGame.ParamsSong;
import com.example.rodrigo.sgame.CommonGame.TransformBitmap;
import com.example.rodrigo.sgame.R;


public class GamePad {

    private Bitmap[] arrows;
    private Bitmap panel, panel2;
    private TransformBitmap TB;
    private Boolean[] waspressed;
    private Point[] arrowsPosition;
    private Rect[] arrowsPosition2;
    private Point posPanel = new Point(0, 0);
    public byte pad[];
    private Bitmap bg=null;


    public void setBg(Bitmap bg) {
        this.bg = bg;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public GamePad(Context context, String type, byte[] pad) {
        int width=Common.WIDTH ;
        int height= Common.HEIGHT;

        panel = BitmapFactory.decodeResource(context.getResources(), R.drawable.touch_controls);
        float starty1=0.76f;
        float starty2=0.51f;
        float starty3=0.62f;
        if (ParamsSong.padOption==0){
             starty1=0.826f;//76
             starty2=0.576f;
             starty3=0.686f;


        }

        switch (type) {



            case "pump-single":

                panel = BitmapFactory.decodeResource(context.getResources(), R.drawable.touch_controls);
                Bitmap step7_On = BitmapFactory.decodeResource(context.getResources(), R.drawable.stomp7_on);
                Bitmap step1_On = BitmapFactory.decodeResource(context.getResources(), R.drawable.stomp1_on);
                Bitmap step7_Off = BitmapFactory.decodeResource(context.getResources(), R.drawable.stomp7_off);
                Bitmap step1_Off = BitmapFactory.decodeResource(context.getResources(), R.drawable.stomp1_off);
                this.TB = new TransformBitmap();
                this.pad = pad;
                if (true) {
                    Bitmap[] arrowOFF = new Bitmap[5];
                    arrows = new Bitmap[5];
                    waspressed = new Boolean[]{false, false, false, false, false};
                    arrowsPosition = new Point[]{new Point(5, +250), new Point(5, 8), new Point(142, 145), new Point(280, 8), new Point(+280, +250)};
                    arrows[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.stomp1_on);   ;//1
                    arrows[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.stomp7_on);                          //7
                    arrows[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.stop5_on);                     //5
                    arrows[3] = TransformBitmap.FlipBitmap(step7_On, true); //9
                    arrows[4] = TransformBitmap.FlipBitmap(step1_On, true);//3

                    arrowOFF[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.stomp1_off);
                    arrowOFF[1] =  BitmapFactory.decodeResource(context.getResources(), R.drawable.stomp7_off);                //7
                    arrowOFF[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.stop5_off);                      //5
                    arrowOFF[3] = TransformBitmap.FlipBitmap(step7_Off, true); //9
                    arrowOFF[4] = TransformBitmap.FlipBitmap(step1_Off, true);//3
                    float startx1=0.0f;
                    float startx2=0.635f;
                    float startx3=0.255f;

                    float widthStep7=0.365f;
                    float widthStep5=widthStep7+0.10f;
                    float heightStep7=0.174f;
                    float heightStep5=0.202f;



                    arrowsPosition2 = new Rect[10];
                    arrowsPosition2[0] = new Rect((int) (width * startx1), (int) (height * starty1), (int) (width * (startx1+widthStep7)), (int) (height *(starty1+heightStep7)));
                    arrowsPosition2[1] = new Rect((int) (width * startx1), (int) (height * starty2), (int) (width * (startx1+widthStep7)), (int) (height *(starty2+heightStep7)));
                    arrowsPosition2[2] = new Rect((int) (width *startx3), (int) (height * starty3), (int) (width * (startx3+widthStep5)), (int) (height * (starty3+heightStep5)));
                    arrowsPosition2[3] = new Rect((int) (width * startx2), (int) (height * starty2), (int) (width * (startx2+widthStep7)), (int) (height * (starty2+heightStep7)));
                    arrowsPosition2[4] = new Rect((int) (width * startx2), (int) (height * starty1), (int) (width * (startx2+widthStep7)), (int) (height * (starty1+heightStep7)));

                    panel2 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                    Canvas c = new Canvas(panel2);
                    for (int w = 0; w < 5; w++) {
                        c.drawBitmap(arrowOFF[w], null, arrowsPosition2[w], new Paint());
                    }

                }
                break;
            case "pump-double":
            case "pump-halfdouble":
            case "pump-routine":
                panel = BitmapFactory.decodeResource(context.getResources(), R.drawable.touch_controls);
                 step7_On = BitmapFactory.decodeResource(context.getResources(), R.drawable.stomp7_on);
                 step1_On = BitmapFactory.decodeResource(context.getResources(), R.drawable.stomp1_on);
                 step7_Off = BitmapFactory.decodeResource(context.getResources(), R.drawable.stomp7_off);
                 step1_Off = BitmapFactory.decodeResource(context.getResources(), R.drawable.stomp1_off);
                this.TB = new TransformBitmap();
                this.pad = pad;
                if (true) {
                    Bitmap[] arrowOFF = new Bitmap[10];
                    arrows = new Bitmap[10];
                    waspressed = new Boolean[]{false, false, false, false, false};
                    arrowsPosition = new Point[]{new Point(5, +250), new Point(5, 8), new Point(142, 145), new Point(280, 8), new Point(+280, +250)};
                    arrows[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.stomp1_on);   ;//1
                    arrows[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.stomp7_on);                          //7
                    arrows[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.stop5_on);                     //5
                    arrows[3] = TransformBitmap.FlipBitmap(step7_On, true); //9
                    arrows[4] = TransformBitmap.FlipBitmap(step1_On, true);//3
                    arrows[5] = arrows[0];//1
                    arrows[6] = arrows[1];//1
                    arrows[7] = arrows[2];//1
                    arrows[8] = arrows[3];//1
                    arrows[9] = arrows[4];//1

                    arrowOFF[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.stomp1_off);
                    arrowOFF[1] =  BitmapFactory.decodeResource(context.getResources(), R.drawable.stomp7_off);                //7
                    arrowOFF[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.stop5_off);                      //5
                    arrowOFF[3] = TransformBitmap.FlipBitmap(step7_Off, true); //9
                    arrowOFF[4] = TransformBitmap.FlipBitmap(step1_Off, true);//3
                    arrowOFF[5]= arrowOFF[0];
                    arrowOFF[6]= arrowOFF[1];
                    arrowOFF[7]= arrowOFF[2];
                    arrowOFF[8]= arrowOFF[3];
                    arrowOFF[9]= arrowOFF[4];
                    /*float startx1=0.0f;
                    float startx2=0.333f;
                    float startx3=0.165f;
                    float starty1=0.76f;
                    float starty2=0.51f;
                    float starty3=0.645f;
                    float widthStep7=0.365f/2;
                    float heightStep7=0.174f;
                    float widthStep5=widthStep7;
                    float heightStep5=0.152f;*/
                    float startx1=-0.005f;
                    float startx2=0.333f;
                    float startx3=0.158f;

                     //heightStep7=0.174f;
                    //float heightStep5=0.202f;

                    float widthStep7=0.365f/2+0.033f;
                    float heightStep7=0.174f;
                    float widthStep5=widthStep7+0.03f;
                   // float heightStep5=0.152f;
                    float heightStep5=0.202f;
                    panel2 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                    arrowsPosition2 = new Rect[10];
                    arrowsPosition2[0] = new Rect((int) (width * startx1), (int) (height * starty1), (int) (width * (startx1+widthStep7)), (int) (height *(starty1+heightStep7)));
                    arrowsPosition2[1] = new Rect((int) (width * startx1), (int) (height * starty2), (int) (width * (startx1+widthStep7)), (int) (height *(starty2+heightStep7)));
                    arrowsPosition2[2] = new Rect((int) (width *startx3), (int) (height * starty3), (int) (width * (startx3+widthStep5)), (int) (height * (starty3+heightStep5)));
                    arrowsPosition2[3] = new Rect((int) (width * startx2), (int) (height * starty2), (int) (width * (startx2+widthStep7)), (int) (height * (starty2+heightStep7)));
                    arrowsPosition2[4] = new Rect((int) (width * startx2), (int) (height * starty1), (int) (width * (startx2+widthStep7)), (int) (height * (starty1+heightStep7)));
                    arrowsPosition2[5] = new Rect((int) (width * (startx1+0.5)), (int) (height * starty1), (int) (width * (startx1+widthStep7+0.5)), (int) (height *(starty1+heightStep7)));
                    arrowsPosition2[6] = new Rect((int) (width * (startx1+0.5)), (int) (height * starty2), (int) (width * (startx1+widthStep7+0.5)), (int) (height *(starty2+heightStep7)));
                    arrowsPosition2[7] = new Rect((int) (width * (startx3+0.5)), (int) (height * starty3), (int) (width * (startx3+widthStep5+0.5)), (int) (height * (starty3+heightStep5)));
                    arrowsPosition2[8] = new Rect((int) (width * (startx2+0.5)), (int) (height * starty2), (int) (width * (startx2+widthStep7+0.5)), (int) (height * (starty2+heightStep7)));
                    arrowsPosition2[9] = new Rect((int) (width * (startx2+0.5)), (int) (height * starty1), (int) (width * (startx2+widthStep7+0.5)), (int) (height * (starty1+heightStep7)));
                    Canvas c = new Canvas(panel2);
                    for (int w = 0; w < arrowsPosition2.length; w++) {
                        c.drawBitmap(arrowOFF[w], null, arrowsPosition2[w], new Paint());
                    }

                }
                break;
    
            case "dance-single":

                panel2 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Bitmap upOn = BitmapFactory.decodeResource(context.getResources(), R.drawable.dance_pad_up_on);
                Bitmap upOFF = BitmapFactory.decodeResource(context.getResources(), R.drawable.dance_pad_up_on);
                Bitmap[] arrowOFF = new Bitmap[10];
                this.pad = pad;
                arrows = new Bitmap[4];//0 to 5 per step
                waspressed = new Boolean[]{false, false, false, false};
                arrowsPosition = new Point[]{new Point(5, +250), new Point(5, 8), new Point(142, 145), new Point(280, 8), new Point(+280, +250)};
                arrows[0] = TransformBitmap.RotateBitmap(upOn, 270);
                arrows[1] = TransformBitmap.RotateBitmap(upOn, 180);
                arrows[2] = upOn;                     //5
                arrows[3] = TransformBitmap.RotateBitmap(upOn, 90); //9
                arrowOFF[0] =  TransformBitmap.RotateBitmap(upOFF, 270);
                arrowOFF[1] = TransformBitmap.RotateBitmap(upOFF, 180);                         //7
                arrowOFF[2] = upOFF;                   //5
                arrowOFF[3] = TransformBitmap.RotateBitmap(upOFF, 90); //9

                float startx1=0.0f;
                float startx2=0.33f;
                float startx3=0.635f;
                 starty1=0.66f;
                 starty2=0.51f;
                 starty3=0.82f;
                float widthStepUP=0.365f;
                float heightStepUP=0.184f;
                float heightStepLeft=0.1875f;
                float widthStepLeft=0.345f;



                arrowsPosition2 = new Rect[4];
                arrowsPosition2[0] = new Rect((int) (width * startx1), (int) (height * starty1), (int) (width * (startx1+widthStepUP)), (int) (height *(starty1+heightStepUP)));
                arrowsPosition2[1] = new Rect((int) (width * startx2), (int) (height * starty3), (int) (width * (startx2+widthStepLeft)), (int) (height *(starty3+heightStepLeft)));
                arrowsPosition2[2] = new Rect((int) (width *startx2), (int) (height * starty2), (int) (width * (startx2+widthStepLeft)), (int) (height * (starty2+heightStepLeft)));
                arrowsPosition2[3] = new Rect((int) (width * startx3), (int) (height * starty1), (int) (width * (startx3+widthStepUP)), (int) (height * (starty1+heightStepUP)));


                Canvas c = new Canvas(panel2);
                for (int w = 0; w < arrowsPosition2.length; w++) {
                    c.drawBitmap(arrowOFF[w], null, arrowsPosition2[w], new Paint());
                }
                break;

            default:

        }


    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();

        if (bg!=null){
         //   canvas.drawBitmap(bg, null,new Rect(0,Common.HEIGHT/2,Common.WIDTH,Common.HEIGHT), paint);
        }
       // posPanel.set((Common.WIDTH/ 2) - (panel.getWidth() / 2), Common.WIDTH / 2);
        // canvas.drawBitmap(panel, posPanel.x, posPanel.y, paint);
        canvas.drawBitmap(panel2, 0, 0, paint);
        if (pad != null) {
            for (int i = 0; i < arrows.length; i++) {
                if (pad[i] != 0) {
                    canvas.drawBitmap(arrows[i], null, arrowsPosition2[i], paint);
                }

            }
        }
    }
    //Controles

    public void clearPad() {
        for (int j = 0; j < pad.length; j++) {
            pad[j] = 0;
        }
    }


    public void checkInputs(int[][] positions) {
        for (int j = 0; j < arrows.length; j++) {
            boolean wasPressed = false;
            for (int[] k: positions) {
                int x = k[0];
                int y = k[1];
                if (arrowsPosition2[j].contains(x,y)){
                    if (pad[j] == 0) { //by this way confirm if the curret pad is off
                        pad[j] = 1;
                    }
                    wasPressed = true;
                    break;

                }
            }
            if (!wasPressed) {
                pad[j] = 0;
            }
        }
    }


    public void unpress(float x, float y){
        for (int j = 0; j < arrows.length; j++) {//checa cada felcha
            if (arrowsPosition2[j].contains((int)x, (int)y)) {
               // pad[j]=0;
            }
        }
    }
}