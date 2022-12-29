package com.example.rodrigo.sgame.ScreenSelectMusic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.rodrigo.sgame.CommonGame.CustomSprite.SpriteReader;
import com.example.rodrigo.sgame.R;

import java.io.InputStream;

public class ThemeElements extends SurfaceView implements SurfaceHolder.Callback {


    //General Stats and elements
    public float fps;
    public SelectMusicThread thread;
    public SongsGroup songsGroup;
    private Bitmap backgroundBitmap;
    public SpriteReader flash;
    SpriteReader tesla;
    //Bitmap lvlPos;
    public Bitmap banner;


    // musica

    Bitmap disco;
    Rect discoRect = null;

    public ThemeElements(Context context) {
        super(context);
    }

    public ThemeElements(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void biuldObject(Context c, SongsGroup songsGroup) {

        BitmapFactory.Options myOpt = new BitmapFactory.Options();
        myOpt.inSampleSize = 2;
        //elemets


        // load image
        try {
            // get input stream

            // load image as Drawable
            Bitmap[] x = new Bitmap[8];
            Bitmap[] xx = new Bitmap[8];
            for (int w = 1; w < 9; w++) {
                InputStream ims = c.getAssets().open("volt/bolt_sizzle_" + w + ".png");
                x[w - 1] = BitmapFactory.decodeStream(ims, null, myOpt);
                InputStream ims2 = c.getAssets().open("tesla/bolt_tesla_" + w + ".png");
                xx[w - 1] = BitmapFactory.decodeStream(ims2, null, myOpt);
            }
            tesla = new SpriteReader(xx, 0.9f);
            flash = new SpriteReader(x, 1.2f);
            // lvlPos= BitmapFactory.decodeStream(c.getAssets().open("SSM/lvl.png"));
            // set image to ImageView

        } catch (OutOfMemoryError outOfMemoryError) {

        } catch (Exception e) {
            e.printStackTrace();
        }
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        this.songsGroup = songsGroup;
        backgroundBitmap = BitmapFactory.decodeResource(c.getResources(), R.drawable.select1, myOpt);
        //xdisco = BitmapFactory.decodeResource(c.getResources(), R.drawable.blackbox);
        thread = new SelectMusicThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();

        flash.play();
        tesla.play();


    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {


    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        thread.running = false;

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        thread.running = false;
        boolean trys = true;
        try {
            while (trys) {
                thread.join();
                trys = false;

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void update() {
        flash.update();
        tesla.update();
    }


    public void draw(Canvas canvas) {
        super.draw(canvas);

        //Disco Rect


        discoRect = new Rect((int) (canvas.getWidth() * 0.1), (int) (canvas.getHeight() * 0.45), (int) (canvas.getWidth() * 0.9), (int) (canvas.getHeight() * 0.7));
        Rect r = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());

        Paint dibujante = new Paint();
        dibujante.setTextSize(20);
        dibujante.setStyle(Paint.Style.FILL);
        dibujante.setColor(Color.TRANSPARENT);

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        Rect posD1 = new Rect((int) (canvas.getWidth() * (0.11f)), (int) (canvas.getHeight() * (0.39f)), (int) (canvas.getWidth() * (0.21f)), (int) (canvas.getHeight() * (0.44f)));
        Rect posD2 = new Rect((int) (canvas.getWidth() * (0.22f)), (int) (canvas.getHeight() * (0.35f)), (int) (canvas.getWidth() * (0.32f)), (int) (canvas.getHeight() * (0.40f)));
        Rect posD3 = new Rect((int) (canvas.getWidth() * (0.22f)), (int) (canvas.getHeight() * (0.402f)), (int) (canvas.getWidth() * (0.32f)), (int) (canvas.getHeight() * (0.452f)));
        Rect disco = new Rect((int) (canvas.getWidth() * (0.23f)), (int) (canvas.getHeight() * (0.7f)), (int) (canvas.getWidth() * (0.97f)), (int) (canvas.getHeight() * (0.32f)));
        //dibujar disco/*
        //canvas.drawBitmap(disco,null,discoRect, new Paint());


        flash.staticDraw(canvas, new Rect(0, 0, flash.frames[0].getWidth(), flash.frames[0].getHeight()));


        tesla.draw(canvas, new Rect((int) (canvas.getWidth() * (0.22f)), (int) (canvas.getHeight() * (0.302f)), (int) (canvas.getWidth() * (0.90f)), (int) (canvas.getHeight() * (0.352f))));
        canvas.drawBitmap(backgroundBitmap, null, r, new Paint());
        // canvas.drawBitmap(lvlPos,null ,posD1, new Paint());
        // canvas.drawBitmap(lvlPos,null ,posD2, new Paint());
        if (banner != null) {
            canvas.drawBitmap(banner, null, disco, new Paint());
        }
        //


    }


}
