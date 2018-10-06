package com.example.rodrigo.sgame.CommonGame;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Common {



    public final static double[] JudgeSJ = {41.6, 41.6, 41.6, 83.3 + 30};
    public final static double[] JudgeEJ = {41.6, 41.6, 41.6, 58.3 + 30};
    public final static double[] JudgeNJ = {41.6, 41.6, 41.6, 41.6 + 30};
    public final static double[] JudgeHJ = {41.6, 41.6, 41.6, 25.5 + 30};
    public final static double[] JudgeVJ = {33.3, 33.3, 33.3, 8.5 + 30};
    public final static double[] JudgeXJ = {41.6, 1, 16.6, 16.6, 16.6 + 30};
    public final static double[] JudgeUJ = {41.6, 1, 8.3, 8.3, 8.3 + 30};
    public final static double[][] juicios = {JudgeSJ, JudgeEJ, JudgeNJ, JudgeHJ, JudgeVJ, JudgeXJ, JudgeUJ};


    public final static String[] possibleDirectorys = {

            Environment.getExternalStoragePublicDirectory("piu").getPath() + "/Songs",
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() + "/Songs",
            System.getenv("EXTERNAL_STORAGE") + "/piu/Songs",
            System.getenv("SECONDARY_STORAGE") + "/piu/Songs",
            System.getenv("EXTERNAL_SDCARD_STORAGE") + "/piu/Songs",
            //"/storage/self/primary/piu/Songs",
            "/storage/self/secondary/piu/Songs",
            Environment.DIRECTORY_DOCUMENTS + "/piu/Songs",
            Environment.getExternalStoragePublicDirectory("SONGS").getPath(),
            "/sdcard/piu/Songs"
            //        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath(),

    };


    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static File checkDirSongsFolders() {
        File directory = null;
        for (String currentPath : possibleDirectorys) {
            File f = new File(currentPath);
            if (f.exists()) {
                return f;
            } else if (f.mkdirs() && f.isDirectory()) {
                return f;
            }
        }
        return directory;
    }


    public static String checkBGADir(String currentpath, String bganame) {
        String directory = null;
        if (new File(System.getenv("EXTERNAL_STORAGE") + "/SONGMOVIES/" + bganame).exists()) {
            directory = new File(System.getenv("EXTERNAL_STORAGE") + "/SONGMOVIES/" + bganame).getPath();
        } else if (new File(System.getenv("SECONDARY_STORAGE") + "/SONGMOVIES/" + bganame).exists()) {
            directory = new File(System.getenv("SECONDARY_STORAGE") + "/SONGMOVIES/" + bganame).getPath();
        } else if (new File(System.getenv("EXTERNAL_SDCARD_STORAGE") + "/SONGMOVIES/" + bganame).exists()) {
            directory = new File(System.getenv("EXTERNAL_SDCARD_STORAGE") + "/SONGMOVIES/" + bganame).getPath();
        } else if (new File(Environment.getExternalStorageDirectory().getPath() + "/SONGMOVIES/" + bganame).exists()) {
            directory = new File(Environment.getExternalStorageDirectory().getPath() + "/SONGMOVIES/" + bganame).getPath();
        } else if (new File(Environment.getDataDirectory().getPath() + "/SONGMOVIES/" + bganame).exists()) {
            directory = new File(Environment.getDataDirectory() + "/SONGMOVIES/" + bganame).getPath();
        } else if (new File(currentpath + "/" + bganame).exists()) {
            directory = currentpath + "/" + bganame;
        }
        return directory;
    }

    public static String convertStreamToString(FileInputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static double second2Beat(double second, double BPM) {
        return second / (60 / BPM);
    }

    public static double beat2Second(double beat, double BPM) {
        return beat * (60 / BPM);
    }

    public static String changeCharInPosition(int position, char ch, String str) {
        char[] charArray = str.toCharArray();
        charArray[position] = ch;
        return new String(charArray);
    }


    private static Point getScreenResolution(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return new Point(metrics.widthPixels, metrics.heightPixels);
    }


    public static boolean isAppInLowMemory(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.lowMemory;
    }


    public static String is2String(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }


    static public boolean bgaExist( String url) {
        try {
            return new checkBga().execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;

        }
    }





    public static class checkBga extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con = (HttpURLConnection) new URL(params[0]).openConnection();
                con.setRequestMethod("HEAD");
                System.out.println(con.getResponseCode());
                return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            boolean bResponse = result;
            if (bResponse == true) {
                //Toast.makeText(MainActivity.this, "File exists!", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(MainActivity.this, "File does not exist!", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
