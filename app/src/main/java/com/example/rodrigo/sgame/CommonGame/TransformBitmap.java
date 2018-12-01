package com.example.rodrigo.sgame.CommonGame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;

public class TransformBitmap {
    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


    public static Bitmap FlipBitmap(Bitmap source, Boolean eje) {
        Matrix matrix = new Matrix();
        if (eje) {
            matrix.postScale(-1, 1);
        } else {
            matrix.postScale(1, -1);
        }
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap makeTransparent(Bitmap src, int value) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap transBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(transBitmap);
        canvas.drawARGB(0, 0, 0, 0);
        // config paint
        final Paint paint = new Paint();
        paint.setAlpha(value);

        canvas.drawBitmap(src, 0, 0, paint);
        return transBitmap;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap myblur(Bitmap image, Context context) {

        if (image == null) {
            return null;
        }
        try {
            final float BITMAP_SCALE = 0.4f;
            final float BLUR_RADIUS = 8.5f;
            int width = Math.round(image.getWidth() * BITMAP_SCALE);
            int height = Math.round(image.getHeight() * BITMAP_SCALE);
            Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
            Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
            RenderScript rs = RenderScript.create(context);
            ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
            Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
            theIntrinsic.setRadius(BLUR_RADIUS);
            theIntrinsic.setInput(tmpIn);
            theIntrinsic.forEach(tmpOut);
            tmpOut.copyTo(outputBitmap);
            return outputBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }


    public static Bitmap getscaledBitmap(Bitmap bm, int percent) {

        int newWidth = (int) (bm.getWidth() * (float) percent / 100);
        int newHeight = (int) (bm.getWidth() * (float) percent / 100);
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }


    public static Bitmap cutBitmap(Bitmap fullLengthBitmap, float percent) {
        percent /= 100;
        if (percent > 0.05) {
            Bitmap backDrop = Bitmap.createBitmap((int) (fullLengthBitmap.getWidth() * percent), fullLengthBitmap.getHeight(), Bitmap.Config.RGB_565);
            Canvas can = new Canvas(backDrop);
            can.drawBitmap(fullLengthBitmap, 0, 0, null);
            return backDrop;
        } else {
            return fullLengthBitmap;
        }

    }

    public static Bitmap overlay(Bitmap bmp2, Bitmap bmp1) {
        Bitmap bmOverlay = Bitmap.createBitmap(200, 200, bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp2, null, new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), new Paint());
        canvas.drawBitmap(bmp1, null, new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), new Paint());
        return bmOverlay;
    }

    public static Bitmap adjustedContrast(Bitmap src, double value) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap

        // create a mutable empty bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

        // create a canvas so that we can draw the bmOut Bitmap from source bitmap
        Canvas c = new Canvas();
        c.setBitmap(bmOut);

        // draw bitmap to bmOut from src bitmap so we can modify it
        c.drawBitmap(src, 0, 0, new Paint(Color.BLACK));


        // color information
        int A, R, G, B;
        int pixel;
        // get contrast value
        double contrast = Math.pow((100 + value) / 100, 2);

        // scan through all pixels
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel);
                R = (int) (((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (R < 0) {
                    R = 0;
                } else if (R > 255) {
                    R = 255;
                }

                G = Color.green(pixel);
                G = (int) (((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (G < 0) {
                    G = 0;
                } else if (G > 255) {
                    G = 255;
                }

                B = Color.blue(pixel);
                B = (int) (((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (B < 0) {
                    B = 0;
                } else if (B > 255) {
                    B = 255;
                }

                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return bmOut;
    }

    public static Bitmap returnMask(Bitmap original, Bitmap mask) {

        //ImageView mImageView= (ImageView)findViewById(R.id.imageview_id);
        /* Bitmap original = BitmapFactory.decodeResource(getResources(),R.drawable.content_image);
         Bitmap mask = BitmapFactory.decodeResource(getResources(),R.drawable.mask);
        */
        Bitmap result = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
        mCanvas.drawBitmap(original, 0, 0, null);
        // mCanvas.drawBitmap(mask, , paint);
        mCanvas.drawBitmap(mask, null, new Rect(0, 0, original.getWidth(), original.getHeight()), paint);


        paint.setXfermode(null);
        return result;
    }

    public static Bitmap returnMaskCut(Bitmap original, Bitmap mask) {

        //ImageView mImageView= (ImageView)findViewById(R.id.imageview_id);
        /* Bitmap original = BitmapFactory.decodeResource(getResources(),R.drawable.content_image);
         Bitmap mask = BitmapFactory.decodeResource(getResources(),R.drawable.mask);
        */
        Bitmap result = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        mCanvas.drawBitmap(original, 0, 0, null);
        // mCanvas.drawBitmap(mask, , paint);
        mCanvas.drawBitmap(mask, null, new Rect(0, 0, original.getWidth(), original.getHeight()), paint);


        paint.setXfermode(null);
        return result;
    }

    /**
     * @param sprite rawImage Sprite
     * @param sizeX  divide on X blocks bitmap
     * @param sizeY  divide on Y blocks bitmap
     * @param params array of  coordinates in sprite
     *               follow the next sequence
     *               012
     *               345
     *               678
     *               representes of square 3x3
     * @return
     */
    public static Bitmap[] customSpriteArray(Bitmap sprite, int sizeX, int sizeY, int... params) {
        Bitmap[] spriteArray = new Bitmap[params.length];//crete array
        int frameWhith = (sprite.getWidth() / sizeX);
        int frameHeight = (sprite.getHeight() / sizeY);
        for (int index = 0; index < params.length; index++) {
            if (params[index] < (sizeX * sizeY) && sizeX > 0 && sizeY > 0) {//verify for errors
                int x = params[index] % sizeX;
                int y = params[index] / sizeX;
                spriteArray[index] = Bitmap.createBitmap(sprite, x * frameWhith, y * frameHeight, frameWhith, frameHeight);
            }
        }
        return spriteArray ;
    }


}