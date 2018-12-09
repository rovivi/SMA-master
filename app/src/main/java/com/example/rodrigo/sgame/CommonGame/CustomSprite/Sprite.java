package com.example.rodrigo.sgame.CommonGame.CustomSprite;
/**
 * Author Rodrigo Vidal
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Sprite extends SurfaceView implements SurfaceHolder.Callback {
    public SpriteReader image;
    Boolean staticDraw = false;

    public Sprite(Context context) {
        super(context);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
    }

    public Sprite(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setWillNotDraw(false);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.pause();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //destroy stuff
    }


    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (staticDraw) {
            image.staticDraw(canvas, new Rect(0, 0, getWidth(), getHeight()));

        } else {
            image.draw(canvas, new Rect(0, 0, getWidth(), getHeight()));
        }
    }


    public void update() {
        image.update();
    }


    public void pause() {

    }

    /*public void play() {
        if (image!=null){
            threadSprite.running=true;
            threadSprite.start();
        }
    }*/


    public void create(SpriteReader sprite) {
        this.image = sprite;
        sprite.play();

    }
}
