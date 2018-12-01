package com.example.rodrigo.sgame.CommonGame;


/**
 * Creator : Rodrigo Vidal Villaseñor
 */
public class Level {

    /**
     * Void Constructor
     */
    public Level(){}

    /**
     * Create a Level object for the adapter
     * @param numberLevel Number of level
     * @param gameType Type of step
     * @param tag Some additional information
     */
    public Level(int numberLevel, String gameType, String tag) {
        this.numberLevel = numberLevel;
        this.gameType = gameType;
        this.tag = tag;
    }

    public int getNumberLevel() {
        return numberLevel;
    }

    public void setNumberLevel(int numberLevel) {
        this.numberLevel = numberLevel;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    int numberLevel;
    String gameType,tag;
}
