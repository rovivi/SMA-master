package com.kyadevs.stepdroid.CommonGame;

/**
 * Created : Rodrigo Vidal
 * This class is used to manage the preferences in the game
 */
public class ParamsSong {
    public static float speed = 2f;
    public static int judgment = 3;
    public static int av = -1;
    public static int delayMS = 0;
    public static Float rush = 1.0f;
    public static  boolean autoplay=false;
    public static  String nameNoteSkin="prime";
    public static  String stepType2Evaluation ="single";
    public static  String stepLevel="15";

    public static int skinIndex = 0;


    /**
     * Game mode
     * 0 50-50 mode
     * 1 70-30 mode
     * 2 DM mode
     * 3 Tile mode
     */
    public static Integer gameMode = 0;
    public static short padOption = 1;




    //List mode
    public static Boolean listCuadricula = true;
    public static boolean FD=false;
}
