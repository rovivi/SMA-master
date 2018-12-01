package com.example.rodrigo.sgame.Player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.example.rodrigo.sgame.CommonGame.SpriteReader;
import com.example.rodrigo.sgame.CommonGame.TransformBitmap;
import com.example.rodrigo.sgame.R;

import java.util.Stack;

public class Steps {
    static public SpriteReader mine, receptor;
    static private SpriteReader[] arrows, longs, tails;
    static public SpriteReader[] explotions, explotionTails;
    static private int Longinfo[] = {-9999, -9999, -9999, -9999, -9999, -9999, -9999, -9999, -9999, -9999, -9999};
    static public boolean efecto = false;
    Point screenSize;

    /**
     * Created the step
     * @param context
     * @param screenSize
     */
    Steps(Context context, Point screenSize) {
        receptor = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.base), 1, 2, 0.5f);
        receptor.play();
        int sizeArrow = (int) (screenSize.x * 0.8 / 10);
        this.screenSize = screenSize;

        BitmapFactory.Options myOpt = new BitmapFactory.Options();
        myOpt.inSampleSize = 1;

        BitmapFactory.Options myOpt2 = new BitmapFactory.Options();
        myOpt2.inSampleSize = 4;


        try {
            if (arrows == null || tails == null || longs == null || explotions == null) {
                arrows = new SpriteReader[10];
                tails = new SpriteReader[10];
                longs = new SpriteReader[10];
                explotionTails = new SpriteReader[10];
                arrows[0] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_down_left_tap, myOpt), 6, 1, 0.2f);
                arrows[1] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_up_left_tap, myOpt), 6, 1, 0.2f);
                arrows[2] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_center_tap, myOpt), 6, 1, 0.2f);
                arrows[3] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_up_right_tap, myOpt), 6, 1, 0.2f);
                arrows[4] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_down_right_tap, myOpt), 6, 1, 0.2f);
                tails[0] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_down_left_tail, myOpt), 6, 1, 0.2f);
                tails[1] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_up_left_tail, myOpt), 6, 1, 0.2f);
                tails[2] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_center_tail, myOpt), 6, 1, 0.2f);
                tails[3] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_up_right_tail, myOpt), 6, 1, 0.2f);
                tails[4] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_down_right_tail, myOpt), 6, 1, 0.2f);
                longs[0] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_down_left_body, myOpt), 6, 1, 0.2f);
                longs[1] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_up_left_body, myOpt), 6, 1, 0.2f);
                longs[2] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_center_body, myOpt), 6, 1, 0.2f);
                longs[3] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_up_right_body, myOpt), 6, 1, 0.2f);
                longs[4] = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.prime_down_right_body, myOpt), 6, 1, 0.2f);

                Bitmap r1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.s1, myOpt2);
                Bitmap r2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.s2, myOpt2);
                Bitmap r3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.s3, myOpt2);
                Bitmap r4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.s4, myOpt2);
                Bitmap r5 = BitmapFactory.decodeResource(context.getResources(), R.drawable.s5, myOpt2);
                explotions = new SpriteReader[10];
                for (int w =0;w<5;w++){
                    arrows[w+5] = arrows[w];
                    tails[5+w] = tails[w];
                    longs[5+w] = longs[w];
                    // tails[5+w] = tails[w];
                }

                for (int cd = 0; cd < 10; cd++) {
                    explotions[cd] = new SpriteReader(new Bitmap[]{
                            TransformBitmap.returnMaskCut(arrows[cd].frames[0], TransformBitmap.returnMask(arrows[cd].frames[0], r1)),
                            TransformBitmap.returnMaskCut(arrows[cd].frames[1], TransformBitmap.returnMask(arrows[cd].frames[1], r2)),
                            TransformBitmap.returnMaskCut(arrows[cd].frames[2], TransformBitmap.returnMask(arrows[cd].frames[2], r3)),
                            TransformBitmap.returnMaskCut(arrows[cd].frames[3], TransformBitmap.returnMask(arrows[cd].frames[3], r3)),
                            TransformBitmap.returnMaskCut(arrows[cd].frames[4], TransformBitmap.returnMask(arrows[cd].frames[4], r4)),
                            TransformBitmap.returnMaskCut(arrows[cd].frames[5], TransformBitmap.returnMask(arrows[cd].frames[5], r5)),
                    }, 0.15f);
                    explotionTails[cd]=explotions[cd];
                }




            }//resplandor
        } catch (OutOfMemoryError E) {
           // System.gc();
            for (int cd = 0; cd < 10; cd++) {
                explotions[cd] = arrows[cd];

            }

        }

        for (int x = 0; x < arrows.length; x++) {
            arrows[x].play();
            longs[x].play();
            tails[x].play();
        }
        mine = new SpriteReader(BitmapFactory.decodeResource(context.getResources(), R.drawable.mine), 3, 2, 0.2f);
        mine.play();

    }


    public void draw(Canvas ca, Stack<Object[]> stackSteps, float speed, int posintx, int wa, int playersizex, int playerSizeY) {
        Canvas c;
        Bitmap stepsBitmap = Bitmap.createBitmap(playersizex, playerSizeY, Bitmap.Config.ARGB_8888);
        if (efecto) {
            c = new Canvas(stepsBitmap);
        } else {
            c = ca;
        }

        int posintX2 = posintx;
        int aux2 = (int) (playerSizeY * 0.085);
        int currenty;
        /*receptor.draw(c, new Rect((int) (playersizex * 0.24), currenty, (int) (playersizex * 0.742
        ), currenty + wa));*/
        while (!stackSteps.isEmpty()) {
            Object[] aux = stackSteps.pop();
            byte[] Steps = (byte[]) aux[0];

            if (aux[1] instanceof Double) {
                currenty = (int) (double) aux[1];

            } else {
                currenty = (int) aux[1];

            }


            for (int j = 0; j < Steps.length && speed != 0; j++) {

                if (efecto) {
                    posintx = (int) (posintX2 + Math.sin((double) (aux2 - currenty) / wa / 1.2) * wa * 0.8);
                }
 /*  0 null char
            1 normal step
            2 start long
            3 end long
            4 body long
            5 fake
            6 hidden
            7 mine
            8 poisson
            +10 vanish
            +20
            +30 sundden
            +40 fit
            50 --PERFORMANCE TAP
            51 --PERFORMANCE START LONG
            52 --PERFORMANCE END LONG
            53 --PERFORMANCE BODY
            THE SAME P2+10 +P3+20



            255 presed

        */

                switch (Steps[j]) {
                    case (11):
                        if (currenty < screenSize.y *0.20f) {
                           // arrows[j].drawWhitShader(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa), 100);
//arrows[j].drawWhitShader(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa), 100);
                        }
                        else if (currenty > screenSize.y *0.27f){
                            arrows[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                        } else{
                            arrows[j].drawWhitShader(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa), 100);
                        }

                        break;
                    case (1):
                    case (5):
                    case (51):
                    case (61):
                    case (71):
                        arrows[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                        break;
                    case (2):
                    case (52):
                    case (62):
                    case (72):
                        //   longs[j].draw(c, new Rect(posintx + wa * j - 20, currenty + wa / 2, posintx + wa * j + wa, nexty));
                        longs[j].draw(c, new Rect(posintx + wa * j - 20, currenty + wa / 2, posintx + wa * j + wa, Longinfo[j]));
                        arrows[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                        Longinfo[j] = -9999;
                        break;
                    case (3):
                    case (53):
                    case (63):
                    case (73):

                        verifyLong(j, currenty);
                        tails[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                        break;
                    case (4):
                    case (54):
                    case (64):
                    case (74):

                        verifyLong(j, currenty);

                        break;
                    case (7):

                        mine.draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                        break;
                    case (100):
                        longs[j].draw(c, new Rect(posintx + wa * j - 20, currenty + wa / 2, posintx + wa * j + wa, Longinfo[j]));
                        Longinfo[j] = -9999;
                        break;
                    default:
                }
            }
        }
        for (int j = 0; j < 10; j++) {
            if (Longinfo[j] != -9999) {
                longs[j].draw(c, new Rect(posintx + wa * j - 20, 0, posintx + wa * j + wa, Longinfo[j]));
                Longinfo[j] = -9999;
            }
        }
        currenty = (int) (playerSizeY * 0.085);
        for (int j = 0; j < 10 && speed != 0; j++) {
            explotions[j].staticDraw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
            explotionTails[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
            if (Longinfo[j] != -9999) {
                longs[j].draw(c, new Rect(posintx + wa * j - 20, 0, posintx + wa * j + wa, Longinfo[j]));
                Longinfo[j] = -9999;
            }
        }


        if (efecto) {
            Matrix sken = new Matrix();
            Camera camera = new Camera();
            camera.rotateY(-1);
            camera.rotateX(7);
            camera.rotateZ(0);
            camera.getMatrix(sken);
            ca.drawBitmap(stepsBitmap, sken, new Paint());
        }



    }

    public void draw(Canvas ca, Stack<String[]> stackSteps, int currenty, int speed, int posintx, int wa, int playersizex, int playerSizeY) {
        Canvas c;
        Bitmap stepsBitmap = Bitmap.createBitmap(playersizex, playerSizeY, Bitmap.Config.ARGB_8888);
        if (efecto) {
            c = new Canvas(stepsBitmap);
        } else {
            c = ca;
        }





        int posintX2 = posintx;
        int aux2 = (int) (playerSizeY * 0.085);
        currenty = (int) (playerSizeY * 0.085);
        /*receptor.draw(c, new Rect((int) (playersizex * 0.24), currenty, (int) (playersizex * 0.742
        ), currenty + wa));*/
        while (!stackSteps.isEmpty()) {
            String[] aux = stackSteps.pop();
            String Steps = aux[0];
            currenty = Integer.parseInt(aux[1]);
            int nexty = Integer.parseInt(aux[2]);
            for (int j = 0; j < Steps.length() && speed != 0; j++) {

                if (efecto) {
                    posintx = (int) (posintX2 + Math.sin((double) (aux2 - currenty) / wa / 1.2) * wa * 0.8);
                }


                switch (Steps.charAt(j)) {
                    case ('1'):
                        arrows[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                        break;
                    case ('F'):
                        arrows[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                        break;
                    case ('2'):
                        //   longs[j].draw(c, new Rect(posintx + wa * j - 20, currenty + wa / 2, posintx + wa * j + wa, nexty));
                        longs[j].draw(c, new Rect(posintx + wa * j - 20, currenty + wa / 2, posintx + wa * j + wa, Longinfo[j]));
                        arrows[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));

                        Longinfo[j] = -9999;
                        break;
                    case ('3'):
                        verifyLong(j, currenty);
                        tails[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                        break;
                    case ('L'):
                        verifyLong(j, currenty);

                        break;
                    case ('M'):
                        //c.drawBitmap(items.frames[6], null, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa), new Paint());
                        mine.draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                        break;
                    case ('P'):
                        //c.drawBitmap(items.frames[6], null, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa), new Paint());

                        longs[j].draw(c, new Rect(posintx + wa * j - 20, currenty + wa / 2, posintx + wa * j + wa, Longinfo[j]));
                        Longinfo[j] = -9999;
                        break;
                    default:
                }
            }
        }
        for (int j = 0; j < 10; j++) {
            if (Longinfo[j] != -9999) {
                longs[j].draw(c, new Rect(posintx + wa * j - 20, 0, posintx + wa * j + wa, Longinfo[j]));
                Longinfo[j] = -9999;
            }
        }
        currenty = (int) (playerSizeY * 0.085);
        for (int j = 0; j < 10 && speed != 0; j++) {
            explotions[j].staticDraw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
            explotionTails[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
            if (Longinfo[j] != -9999) {
                longs[j].draw(c, new Rect(posintx + wa * j - 20, 0, posintx + wa * j + wa, Longinfo[j]));
                Longinfo[j] = -9999;
            }
        }

        Matrix sken = new Matrix();
        if (efecto) {
            Camera camera = new Camera();
            camera.rotateY(-1);
            camera.rotateX(7);
            camera.rotateZ(0);
            camera.getMatrix(sken);
        }
        ca.drawBitmap(stepsBitmap, sken, new Paint());


    }

    public void update() {
        for (int x = 0; x < 10; x++) {
            arrows[x].update();
            tails[x].update();
            longs[x].update();
            explotions[x].update();
            explotionTails[x].update();
        }
        receptor.update();
        mine.update();
    }


    private void verifyLong(int pos, int y) {
        if (Longinfo[pos] == -9999 || Longinfo[pos] < y) {
            Longinfo[pos] = y;
        }

    }


    public void makeDDR(Context context) {
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

