package com.example.rodrigo.sgame.Player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.rodrigo.sgame.CommonGame.Common;
import com.example.rodrigo.sgame.CommonGame.Note;
import com.example.rodrigo.sgame.CommonGame.ParamsSong;

import java.util.Stack;

public class Steps {

    static private int longInfo[][];
    static private int logPosition[] = {-9999, -9999, -9999, -9999, -9999, -9999, -9999, -9999, -9999, -9999, -9999};
    static private int noteSkin[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    static {
        longInfo = new int[2][10];
        longInfo[0] = logPosition;
        longInfo[1] = noteSkin;
    }

    static NoteSkin[] noteSkins;

    /**
     * Created the step
     *
     * @param context
     * @param gameMode
     */
    Steps(Context context, String gameMode) {
        longInfo[1] = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        switch (gameMode) {
            case "pump-routine":
                noteSkins = new NoteSkin[4];
                noteSkins[0] = new NoteSkin(context, gameMode, "routine1");
                noteSkins[1] = new NoteSkin(context, gameMode, "routine2");
                noteSkins[2] = new NoteSkin(context, gameMode, "routine3");
                noteSkins[3] = new NoteSkin(context, gameMode, "soccer");
                break;
            case "pump-double":
            case "pump-single":
            case "pump-halfdouble":
                noteSkins = new NoteSkin[1];
                noteSkins[0] = new NoteSkin(context, gameMode, ParamsSong.nameNoteSkin);
                //noteSkins[1] = new NoteSkin(context, gameMode, "routine2");
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
        boolean doMagic = false;
        if (doMagic) {
            c = new Canvas(stepsBitmap);
        } else {
            c = ca;
        }

        int posintX2 = posintx;
        int aux2 = (int) (playerSizeY * Common.START_Y);
        int halfDistance = (int) (((playerSizeY - Common.START_Y * Common.HEIGHT) / 2) + Common.START_Y * Common.HEIGHT);
        int currenty;


        for (int j = 0; j < noteSkins[0].receptors.length && !ParamsSong.FD; j++) {//se Dibujan receptores y effectos de las notas si los hay
            noteSkins[0].receptors[j].draw(c, new Rect(posintx + wa * j - 20, aux2, posintx + wa * j + wa, aux2 + wa));
        }


        while (!stackSteps.isEmpty()) {
            Object[] aux = stackSteps.pop();
            Note[] Steps = (Note[]) aux[0];
            if (aux[1] instanceof Double) {
                currenty = (int) (double) aux[1];
            } else {
                currenty = (int) aux[1];
            }
            for (int j = 0; j < noteSkins[0].arrows.length && speed != 0; j++) {
                if (doMagic) {
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
        */
                int posNote = 0;
                byte typeChar = 0;
                byte typeDisplay = 0;
                if (Steps[j].noteType == 100) {
                    typeChar = 100;
                } else if (Steps[j].noteType != 0) {
                    typeChar = (byte) (Steps[j].noteType % 10);
                    typeDisplay = (byte) (Steps[j].noteType / 10);
                }
                boolean sunded = (typeDisplay == 3);
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
                if (typeDisplay == 0 && noteSkins.length > 1) {
                    posNote = 3;
                }
                if (typeDisplay == 7 || typeDisplay == 6 || typeDisplay == 5 || typeDisplay == 0 || (currenty > halfDistance && vanish) || (currenty < halfDistance && sunded)) {
                    switch (typeChar) {
                        case (1):
                        case (5):
                            noteSkins[posNote].arrows[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                            break;
                        case (2):
                            //   longs[j].draw(c, new Rect(posintx + wa * j - 20, currenty + wa / 2, posintx + wa * j + wa, nexty));
                            noteSkins[posNote].longs[j].draw(c, new Rect(posintx + wa * j - 20, currenty + wa / 2, posintx + wa * j + wa, longInfo[0][j]));
                            noteSkins[posNote].arrows[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                            longInfo[0][j] = -9999;
                            break;
                        case (3):
                            verifyLong(j, currenty);
                            noteSkins[posNote].tails[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                            break;
                        case (4):
                            verifyLong(j, currenty);
                            longInfo[1][j] = posNote;
                            break;
                        case (7):
                            noteSkins[posNote].mine.draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
                            break;
                        case (100):
                            noteSkins[longInfo[1][j]].longs[j].draw(c, new Rect(posintx + wa * j - 20, currenty + wa / 2, posintx + wa * j + wa, longInfo[0][j]));
                            longInfo[0][j] = -9999;
                            break;
                        default:
                    }
                } else {
                    switch (typeChar) {
                        case (3):
                        case (4):
                            //    verifyLong(j, currenty);
                            if (vanish) {
                                noteSkins[longInfo[1][j]].longs[j].draw(c, new Rect(posintx + wa * j - 20, currenty + wa / 2, posintx + wa * j + wa, longInfo[0][j]));
                                longInfo[0][j] = -9999;

                            }

                            break;
                        case (100):


                            longInfo[0][j] = -9999;
                            break;
                    }
                }
            }
        }


        try {

        for (int j = 0; j < noteSkins[0].arrows.length; j++) {
            if (longInfo[0][j] != -9999) {
                noteSkins[longInfo[1][j]].longs[j].draw(c, new Rect(posintx + wa * j - 20, 0, posintx + wa * j + wa, longInfo[0][j]));
                longInfo[0][j] = -9999;
            }
        }
        currenty = (int) (playerSizeY * Common.START_Y);

            for (NoteSkin currentNote : noteSkins) {
                for (int j = 0; j < currentNote.arrows.length && speed != 0; j++) {//se Dibujan receptores y effectos de las notas si los hay
                    if (longInfo[0][j] != -9999) {
                        noteSkins[longInfo[1][j]].longs[j].draw(c, new Rect(posintx + wa * j - 20, 0, posintx + wa * j + wa, longInfo[0][j]));
                        longInfo[0][j] = -9999;
                    }
                }
            }

        for (int j = 0; j < noteSkins[0].arrows.length && speed != 0; j++) {//se Dibujan receptores y effectos de las notas si los hay
            noteSkins[0].explotions[j].staticDraw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
            noteSkins[0].explotionTails[j].draw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));
            noteSkins[0].tapsEffect[j].staticDraw(c, new Rect(posintx + wa * j - 20, currenty, posintx + wa * j + wa, currenty + wa));

        }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }


      /*  if (false) {//atacks
            Matrix sken = new Matrix();
            Camera camera = new Camera();
            camera.rotateY(-1);
            camera.rotateX(7);
            camera.rotateZ(0);
            camera.getMatrix(sken);
            ca.drawBitmap(stepsBitmap, sken, new Paint());
        }*/
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
                currentNote.receptors[x].update();

            }
            //   currentNote.receptor.update();
            currentNote.mine.update();
        }
    }


    private void verifyLong(int pos, int y) {


        if (longInfo[0][pos] == -9999 || longInfo[0][pos] < y) {
            longInfo[0][pos] = y;
        }

    }


}

