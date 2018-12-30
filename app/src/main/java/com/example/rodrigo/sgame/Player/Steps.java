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

import com.example.rodrigo.sgame.CommonGame.Common;
import com.example.rodrigo.sgame.CommonGame.CustomSprite.SpriteReader;
import com.example.rodrigo.sgame.CommonGame.ParamsSong;
import com.example.rodrigo.sgame.CommonGame.TransformBitmap;
import com.example.rodrigo.sgame.R;

import java.util.Stack;

public class Steps {

    static private int Longinfo[] = {-9999, -9999, -9999, -9999, -9999, -9999, -9999, -9999, -9999, -9999, -9999};
    static public boolean efecto = false;
    Point screenSize;
    static NoteSkin[] noteSkins;

    /**
     * Created the step
     *
     * @param context
     * @param gameMode
     */
    Steps(Context context, String gameMode) {

        switch (gameMode) {
            case "pump-routine":
                noteSkins = new NoteSkin[3];
                noteSkins[0] = new NoteSkin(context, gameMode, "routine1");
                noteSkins[1] = new NoteSkin(context, gameMode, "routine2");
                noteSkins[2] = new NoteSkin(context, gameMode, "routine3");
                break;
            case "pump-double":
            case "pump-single":
            case "pump-halfdouble":
                noteSkins = new NoteSkin[1];
                noteSkins[0] = new NoteSkin(context, gameMode, ParamsSong.nameNoteSkin);
                break;
            case "dance-single":
                break;
            case "":
                break;

        }


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
        int aux2 = (int) (playerSizeY * Common.START_Y);
        int halfDistance = (int) (((playerSizeY - Common.START_Y * Common.HEIGHT) / 2) + Common.START_Y * Common.HEIGHT);
        int currenty;

        while (!stackSteps.isEmpty()) {
            Object[] aux = stackSteps.pop();
            byte[] Steps = (byte[]) aux[0];

            if (aux[1] instanceof Double) {
                currenty = (int) (double) aux[1];

            } else {
                currenty = (int) aux[1];

            }


            for (int j = 0; j < noteSkins[0].arrows.length && speed != 0; j++) {

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


                int posNote = 0;
                Steps[j] = (byte) Math.abs(Steps[j]);
                byte typechar = 0;
                byte typeDisplay = 0;

                if (Steps[j] == 100) {
                    typechar = 100;

                } else if (Steps[j] != 0) {
                    typechar = (byte) (Steps[j] % 10);
                    typeDisplay = (byte) (Steps[j] / 10);

                }

                boolean sundded = (typeDisplay == 3);
                boolean vanish = (typeDisplay == 1);
                boolean hidden = typeDisplay == 2;

                if (typeDisplay == 5) {
                    posNote = 0;
                }
                if (typeDisplay == 6) {
                    posNote = 1;
                }
                if (typeDisplay == 7) {
                    posNote = 2;
                }


                if (typeDisplay == 6 || typeDisplay == 5 || typeDisplay == 0 || (currenty < halfDistance && vanish) || (currenty > halfDistance && sundded)) {

                    switch (typechar) {
                        case (1):
                        case (5):
                            noteSkins[posNote].arrows[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                            break;
                        case (2):

                            //   longs[j].draw(c, new Rect(posintx + wa * j - 20, currenty + wa / 2, posintx + wa * j + wa, nexty));
                            noteSkins[posNote].longs[j].draw(c, new Rect(posintx + wa * j - 20, currenty + wa / 2, posintx + wa * j + wa, Longinfo[j]));
                            noteSkins[posNote].arrows[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                            Longinfo[j] = -9999;
                            break;
                        case (3):

                            verifyLong(j, currenty);
                            noteSkins[posNote].tails[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                            break;
                        case (4):
                            verifyLong(j, currenty);
                            break;
                        case (7):
                            noteSkins[posNote].mine.draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                            break;
                        case (100):
                            noteSkins[posNote].longs[j].draw(c, new Rect(posintx + wa * j - 20, currenty + wa / 2, posintx + wa * j + wa, Longinfo[j]));
                            Longinfo[j] = -9999;
                            break;
                        default:
                    }
                } else {
                    switch (typechar) {
                        case (3):
                        case (4):
                            verifyLong(j, currenty);
                            break;
                        case (100):
                            Longinfo[j] = -9999;
                            break;
                    }
                }
            }
        }
        for (int j = 0; j < noteSkins[0].arrows.length; j++) {
            if (Longinfo[j] != -9999) {
                noteSkins[0].longs[j].draw(c, new Rect(posintx + wa * j - 20, 0, posintx + wa * j + wa, Longinfo[j]));
                Longinfo[j] = -9999;
            }
        }


        currenty = (int) (playerSizeY * Common.START_Y);


        for (NoteSkin currentNote : noteSkins) {
            for (int j = 0; j < currentNote.arrows.length && speed != 0; j++) {//se Dibujan receptores y effectos de las notas si los hay
                currentNote.explotions[j].staticDraw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                currentNote.explotionTails[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                currentNote.tapsEffect[j].staticDraw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));

                if (Longinfo[j] != -9999) {
                    noteSkins[0].longs[j].draw(c, new Rect(posintx + wa * j - 20, 0, posintx + wa * j + wa, Longinfo[j]));
                    Longinfo[j] = -9999;
                }
            }
        }


        if (false) {//atacks
            Matrix sken = new Matrix();
            Camera camera = new Camera();
            camera.rotateY(-1);
            camera.rotateX(7);
            camera.rotateZ(0);
            camera.getMatrix(sken);
            ca.drawBitmap(stepsBitmap, sken, new Paint());
        }


    }

    public void draw33333OLD(Canvas ca, Stack<String[]> stackSteps, int currenty, int speed, int posintx, int wa, int playersizex, int playerSizeY) {
        Canvas c;
        Bitmap stepsBitmap = Bitmap.createBitmap(playersizex, playerSizeY, Bitmap.Config.ARGB_8888);
        if (efecto) {
            c = new Canvas(stepsBitmap);
        } else {
            c = ca;
        }


        int posintX2 = posintx;
        int aux2 = (int) (playerSizeY * Common.START_Y);
        currenty = (int) (playerSizeY * Common.START_Y);
        noteSkins[0].receptor.draw(c, new Rect((int) (playersizex * 0.24), currenty, (int) (playersizex * 0.742
        ), currenty + wa));
        while (!stackSteps.isEmpty()) {
            String[] aux = stackSteps.pop();
            String Steps = aux[0];
            currenty = Integer.parseInt(aux[1]);
            int nexty = Integer.parseInt(aux[2]);
            for (int j = 0; j < Steps.length() && speed != 0; j++) {

                if (false) {
                    posintx = (int) (posintX2 + Math.sin((double) (aux2 - currenty) / wa / 1.2) * wa * 0.8);
                }


                switch (Steps.charAt(j)) {
                    case ('1'):
                        noteSkins[0].arrows[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                        break;
                    case ('F'):
                        noteSkins[0].arrows[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                        break;
                    case ('2'):
                        //   longs[j].draw(c, new Rect(posintx + wa * j - 20, currenty + wa / 2, posintx + wa * j + wa, nexty));
                        noteSkins[0].longs[j].draw(c, new Rect(posintx + wa * j - 20, currenty + wa / 2, posintx + wa * j + wa, Longinfo[j]));
                        noteSkins[0].arrows[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));

                        Longinfo[j] = -9999;
                        break;
                    case ('3'):
                        verifyLong(j, currenty);
                        noteSkins[0].tails[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                        break;
                    case ('L'):
                        verifyLong(j, currenty);

                        break;
                    case ('M'):
                        //c.drawBitmap(items.frames[6], null, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa), new Paint());
                        noteSkins[0].mine.draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                        break;
                    case ('P'):
                        //c.drawBitmap(items.frames[6], null, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa), new Paint());

                        noteSkins[0].longs[j].draw(c, new Rect(posintx + wa * j - 20, currenty + wa / 2, posintx + wa * j + wa, Longinfo[j]));
                        Longinfo[j] = -9999;
                        break;
                    default:
                }
            }
        }
        for (int j = 0; j < 10; j++) {
            if (Longinfo[j] != -9999) {
                noteSkins[0].longs[j].draw(c, new Rect(posintx + wa * j - 20, 0, posintx + wa * j + wa, Longinfo[j]));
                Longinfo[j] = -9999;
            }
        }
        currenty = (int) (playerSizeY * 0.085);
        for (int j = 0; j < 10 && speed != 0; j++) {
            noteSkins[0].explotions[j].staticDraw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
            noteSkins[0].tapsEffect[j].staticDraw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
            noteSkins[0].explotionTails[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
            if (Longinfo[j] != -9999) {
                noteSkins[0].longs[j].draw(c, new Rect(posintx + wa * j - 20, 0, posintx + wa * j + wa, Longinfo[j]));
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
        for (NoteSkin currentNote : noteSkins) {
            for (int x = 0; x < currentNote.arrows.length; x++) {
                currentNote.arrows[x].update();
                currentNote.tails[x].update();
                currentNote.longs[x].update();
                currentNote.explotions[x].update();
                currentNote.explotionTails[x].update();
                currentNote.tapsEffect[x].update();
            }
            currentNote.receptor.update();
            currentNote.mine.update();
        }
    }


    private void verifyLong(int pos, int y) {


        if (Longinfo[pos] == -9999 || Longinfo[pos] < y) {
            Longinfo[pos] = y;
        }

    }


    public void makeDDR(Context context) {

    }

}

