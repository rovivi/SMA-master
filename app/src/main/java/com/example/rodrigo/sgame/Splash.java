package com.example.rodrigo.sgame;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.multidex.MultiDex;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;


public class Splash extends AppCompatActivity {

    Handler handler;
    ImageView splashImage;
    Animation anim;

    Runnable startActivity = () -> startSongList();
    Runnable runAnimation = new Runnable() {
        public void run() {
            splashImage.setVisibility(View.VISIBLE);
            splashImage.setAnimation(anim);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                splashImage.animate().withEndAction(() -> requestPermission()).start();
            }
        }
    };


    private void requestPermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        70);
                /*ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        70);*/

            }
        } else {
            handler.postDelayed(startActivity, 1500);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MultiDex.install(getApplicationContext());
        super.onCreate(savedInstanceState);


        //Bitmap prueba = BitmapFactory.decodeResource(getResources(), R.drawable.press_raw);
        //Bitmap[] test211 = TransformBitmap.customSpriteArray(prueba, 5, 2, 0, 5);


        setContentView(R.layout.activity_splash);
        try {
            this.getSupportActionBar().hide();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            View mContentView = findViewById(R.id.fullscreen_content);
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } catch (Exception e) {

        }
        splashImage = findViewById(R.id.ivSpash);
        // splashImage.setVisibility(View.GONE);

        anim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        handler = new Handler();

        splashImage.setVisibility(View.GONE);


    }

    public void startSongList() {
        splashImage.setVisibility(View.GONE);

        Intent i = new Intent(this, MainScreenActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();


        //  Common.setParamsGlobal(this);


        handler.postDelayed(runAnimation, 1000);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 69: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    handler.postDelayed(startActivity, 1500);
                } else {
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        //Can add more as per requirement

                        Toast.makeText(getBaseContext(), "Read archive permission is needed", Toast.LENGTH_LONG).show();
                        finish();
                        ActivityCompat.requestPermissions(this,
                                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                123);

                    }
                }
                return;
            }
            case 70: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    handler.postDelayed(startActivity, 1500);
                } else {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        //Can add more as per requirement

                        Toast.makeText(getBaseContext(), "Read archive permission is needed", Toast.LENGTH_LONG).show();
                        finish();
                        ActivityCompat.requestPermissions(this,
                                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                123);

                    }
                }
                return;
            }
        }
    }

}


