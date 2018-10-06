package com.example.rodrigo.sgame.CommonGame;



public class Level {
    public Level(){

    }

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
