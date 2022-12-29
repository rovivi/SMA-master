package com.example.rodrigo.sgame;

import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rodrigo.sgame.CommonGame.Common;
import com.example.rodrigo.sgame.ScreenSelectMusic.AdapterSSC;
import com.example.rodrigo.sgame.ScreenSelectMusic.SongsGroup;

public class Recycler extends AppCompatActivity {
    RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        recyclerView= findViewById(R.id.recicler1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false ));
        SongsGroup songsGroup = new SongsGroup(Common.checkDirSongsFolders(this));
        AdapterSSC adapterSSC= new AdapterSSC(songsGroup,0);
        recyclerView.setAdapter(adapterSSC);

    }
}
