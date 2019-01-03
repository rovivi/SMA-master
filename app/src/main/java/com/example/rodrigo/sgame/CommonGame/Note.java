package com.example.rodrigo.sgame.CommonGame;

public class Note {
    public byte noteType = 0;
    boolean sudden = false;
    boolean hidden = false;
    public boolean fake = false;


    public Note(byte note){
        noteType= note;

    }

    public Note() {

    }
}
