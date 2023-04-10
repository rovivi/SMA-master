package com.kyadevs.stepdroid;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import android.view.Window;
import android.view.WindowManager;

import com.kyadevs.stepdroid.Player.GamePlay;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import com.kyadevs.stepdroid.CommonGame.SSC;


public class MainActivity extends Activity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        String rawscc = getIntent().getExtras().getString("ssc");
        String path = getIntent().getExtras().getString("path");
        int nchar = getIntent().getExtras().getInt("nchar");
        File SSC = new File(rawscc);
        try {
            FileInputStream fis = new FileInputStream(SSC);
            rawscc = convertStreamToString(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(new GamePlay(this, new SSC("", false, false), nchar, path, null, 0, 0));
    }


    public static String convertStreamToString(FileInputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }


}
