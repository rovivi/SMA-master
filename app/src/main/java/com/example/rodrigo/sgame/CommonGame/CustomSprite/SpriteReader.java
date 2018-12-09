package com.example.rodrigo.sgame.CommonGame.CustomSprite;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;

import com.example.rodrigo.sgame.CommonGame.TransformBitmap;
import com.example.rodrigo.sgame.Player.Attack;

import java.util.ArrayList;

/**
 * Created by Rodrigo Vidal February 2018
 */
public class SpriteReader {
    public Bitmap[] frames;
    private int frameIndex;
    private float frameTime;
    private long lastFrame;
    private boolean isPlaying = false;
    private float lapsedtime;
    private int interpolateIndex;
    boolean rotate = false;

    double seconds;
    Paint paint, painShader;
    ArrayList<String[]> attacksList = new ArrayList<String[]>();


    /**
     * This constructor request bitmap array for each frame in the sprite animation
     * @param frames Frame as bitmap
     * @param timeFrame Time the bitmap will apear in the screen
     */
    public SpriteReader(Bitmap[] frames, float timeFrame) {
        this.frameIndex = 0;
        this.frames = frames;
        frameTime = timeFrame / frames.length;
    }


    /**
     * Create sprite since a long bitmap resource and create square whit the parameter size X and size Y
     * @param sprite bitmap of sprite image
     * @param sizeX number of sprite squares horizontally
     * @param sizeY nombre of sprite squares vertically
     * @param timeFrame Time the bitmap will appears in the screen
     */
    public SpriteReader(Bitmap sprite, int sizeX, int sizeY, float timeFrame) {
        paint = new Paint();
        painShader = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        frames = new Bitmap[sizeX * sizeY];
        int frameWhith = (sprite.getWidth() / sizeX);
        int frameHeight = (sprite.getHeight() / sizeY);
        int count = 0;
        try {
            for (int y = 0; y < sizeY; y++) {
                for (int x = 0; x < sizeX; x++) {
                    frames[count] = Bitmap.createBitmap(sprite, x * frameWhith, y * frameHeight, frameWhith, frameHeight);
                    count++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.frameIndex = 0;
        frameTime = timeFrame / frames.length;

        Shader shaderA = new LinearGradient(0, 0, frames[0].getWidth(), frames[0].getHeight(), 0xffffffff, 0x00ffffff, Shader.TileMode.CLAMP);
        Shader shaderB = new BitmapShader(frames[0], Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        painShader.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        painShader.setShader(new LinearGradient(0, 0, 50, 0, 0x00000000, 0xFF000000, Shader.TileMode.CLAMP));


        painShader.setShader(new ComposeShader(shaderA, shaderB, PorterDuff.Mode.SRC_IN));


    }



    public void play() {
        isPlaying = true;
        frameIndex = 0;
        lastFrame = System.currentTimeMillis();
    }

    public void stop() {
        isPlaying = false;
    }

    public void update() {
        lapsedtime = System.currentTimeMillis() - lastFrame;
        seconds += lapsedtime;
        if (lapsedtime > frameTime * 1000) {
            frameIndex++;
            if (frameIndex == frames.length) {
                frameIndex = 0;

            }
            lastFrame = System.currentTimeMillis();
        }
    }


    /**
     * Draw the sprite in the canvas
     * @param canvas destinated canvas
     * @param destiny sprite position as rect
     */
    public void draw(Canvas canvas, Rect destiny) {
        if (!isPlaying) {
            return;
        }
        if (rotate) {
            canvas.drawBitmap(TransformBitmap.RotateBitmap(frames[frameIndex], (float) (45)), null, destiny, paint);
        } else if (attacksList.size() < 1) {
            canvas.drawBitmap(frames[frameIndex], null, destiny, paint);
        }
    }


    public void drawWhitShader(Canvas canvas, Rect destino, int percent) {
        Bitmap backing = Bitmap.createBitmap(frames[frameIndex].getWidth(), frames[frameIndex].getHeight(), Bitmap.Config.ARGB_8888);
        Canvas offscreen = new Canvas(backing);
        offscreen.drawBitmap(frames[frameIndex], 0, 0, null);
        Paint paint2 = new Paint();
        paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        paint2.setShader(new LinearGradient(0, 0, 0, frames[0].getHeight(), 0xFF000000, 0x00000000, Shader.TileMode.CLAMP));
        offscreen.drawRect(0, 0, frames[0].getWidth(), frames[0].getHeight(), paint2);
        canvas.drawBitmap(backing, null, destino, paint2);

    }


    /**
     * Draw only a one time the sprite
     * @param canvas
     * @param destiny
     */
    public void staticDraw(Canvas canvas, Rect destiny) {
        if (!isPlaying) {
            return;
        }
        if ((1 + frameIndex) == frames.length) {
            isPlaying = false;
        } else {
            canvas.drawBitmap(frames[frameIndex], null, destiny, paint);
        }
    }
}

