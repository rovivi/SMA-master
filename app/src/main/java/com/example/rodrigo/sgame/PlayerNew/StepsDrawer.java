package com.example.rodrigo.sgame.PlayerNew;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.rodrigo.sgame.CommonGame.Common;
import com.example.rodrigo.sgame.CommonGame.CustomSprite.SpriteReader;
import com.example.rodrigo.sgame.CommonGame.ParamsSong;
import com.example.rodrigo.sgame.CommonGame.TransformBitmap;
import com.example.rodrigo.sgame.CommonSteps;
import com.example.rodrigo.sgame.Player.NoteSkin;

import java.util.ArrayList;
import java.util.Stack;

import game.Note;

public class StepsDrawer {


    //Constantes de clases
    private static byte SELECTED_SKIN = 0;
    private static byte ROUTINE0_SKIN = 1;
    private static byte ROUTINE1_SKIN = 3;
    private static byte ROUTINE2_SKIN = 4;
    private static byte ROUTINE3_SKIN = 5;


    private int sizeNote;
    private int posInitialX;
    private int sizeArrows;

    static private int[][] longInfo;
    static private int[] logPosition = {-9999, -9999, -9999, -9999, -9999, -9999, -9999, -9999, -9999, -9999, -9999};
    static private int[] noteSkin = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

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
    StepsDrawer(Context context, String gameMode) {
        //que tipo de tablero y nivel es aqui se tiene que calcular las medidas necesarias


        posInitialX = (int) (Common.WIDTH * 0.1);
        longInfo[1] = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};


        noteSkins = new NoteSkin[1];
        switch (gameMode) {
            case "pump-routine":
                noteSkins = new NoteSkin[4];
                noteSkins[ROUTINE0_SKIN] = new NoteSkin(context, gameMode, "routine1");
                noteSkins[ROUTINE1_SKIN] = new NoteSkin(context, gameMode, "routine2");
                noteSkins[ROUTINE2_SKIN] = new NoteSkin(context, gameMode, "routine3");
                noteSkins[ROUTINE3_SKIN] = new NoteSkin(context, gameMode, "soccer");
                sizeNote = (int) ((Common.WIDTH * 0.8) / 10);
                break;
            case "pump-double":
                sizeNote = (int) ((Common.WIDTH * 0.8) / 10);
                noteSkins[SELECTED_SKIN] = new NoteSkin(context, gameMode, ParamsSong.nameNoteSkin);
                break;
            case "pump-single":
                sizeNote = (int) ((Common.WIDTH * 0.7) / 5);
                noteSkins[SELECTED_SKIN] = new NoteSkin(context, gameMode, ParamsSong.nameNoteSkin);
                break;
            case "pump-halfdouble":
                noteSkins[SELECTED_SKIN] = new NoteSkin(context, gameMode, ParamsSong.nameNoteSkin);
                break;
            //noteSkins[1] = new NoteSkin(context, gameMode, "routine2");
            case "dance-single":
                break;
            case "":
                break;

        }
    }


    public void draw(Canvas canvas, ArrayList<Note> notes, int posY) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(20f);
        short count = 0;
        for (Note note : notes) {
            if (note != null && note.getType() != CommonSteps.NOTE_EMPTY) {
                SpriteReader currentArrow = null;

                switch (note.getType()) {
                    case CommonSteps.NOTE_TAP:
                    case CommonSteps.NOTE_FAKE:
                    case CommonSteps.NOTE_LONG_START:
                        currentArrow = noteSkins[SELECTED_SKIN].arrows[count];
                        break;
                    case CommonSteps.NOTE_LONG_END:
                        currentArrow = noteSkins[SELECTED_SKIN].tails[count];
                        break;
                    case CommonSteps.NOTE_LONG_BODY:
                        currentArrow = noteSkins[SELECTED_SKIN].longs[count];
                        break;
                    case CommonSteps.NOTE_MINE:
                        currentArrow = noteSkins[SELECTED_SKIN].mine;
                        break;
                }
                if (currentArrow != null)
                    currentArrow.draw(canvas, new Rect(posInitialX + sizeNote * count - 20, posY, posInitialX + sizeNote * count + sizeNote, posY + sizeNote));
                canvas.drawText("awa",0,posY,paint);
            }
            count++;
        }
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

}

