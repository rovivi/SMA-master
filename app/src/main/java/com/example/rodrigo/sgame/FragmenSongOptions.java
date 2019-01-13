package com.example.rodrigo.sgame;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.rodrigo.sgame.CommonGame.Level;
import com.example.rodrigo.sgame.CommonGame.ParamsSong;
import com.example.rodrigo.sgame.Player.NoteSkin;
import com.example.rodrigo.sgame.ScreenSelectMusic.AdapterLevel;
import com.example.rodrigo.sgame.ScreenSelectMusic.RecyclerItemClickListener;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class FragmenSongOptions extends DialogFragment {
    //public ArrayList<Level> lista;

    SongList songList;
    TextView msj2;
    ImageView pp1, pp0_5, ll1, ll0_5, note_button, note_image;
    TextView tvRush, tvJudge;
    Switch switch_autoplay;
    ArrayList skins = new ArrayList<>();
    int indexNS = 0;

    float velocity = ParamsSong.speed;

    public void setSongList(SongList songList) {
        this.songList = songList;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //adapterLevel = new AdapterLevel(lista, null, getActivity().getAssets());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getDialog().getWindow())
                    .getAttributes().windowAnimations = R.style.DialogAnimation2;
            Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }
        View view = inflater.inflate(R.layout.fragment_fragment__levels, container, false);
        TextView msj = view.findViewById(R.id.tv_msj_level_modal);
        msj2 = view.findViewById(R.id.tv_msj_level_modal2);

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/font.ttf");
        msj.setTypeface(custom_font);
      //  msj.setTextColor(Color.rgb(96, 242, 220));
      //  msj2.setTextColor(Color.rgb(96, 242, 220));
        msj2.setTypeface(custom_font);
        pp1 = view.findViewById(R.id.iv1pp);
        pp0_5 = view.findViewById(R.id.iv0_5pp);
        ll1 = view.findViewById(R.id.iv1ll);
        ll0_5 = view.findViewById(R.id.iv0_5ll);
        note_button = view.findViewById(R.id.iv2);
        note_image = view.findViewById(R.id.image_skin);
        switch_autoplay = view.findViewById(R.id.switch_autoplay);
        TextView tv1 = view.findViewById(R.id.tvsped);
        TextView tv2 = view.findViewById(R.id.tvsped2);
        TextView tv3 = view.findViewById(R.id.tvsped3);
        TextView tv4 = view.findViewById(R.id.tvsped4);
        TextView tv5 = view.findViewById(R.id.title_apear);

        tvRush = view.findViewById(R.id.tvrush);
        tvJudge = view.findViewById(R.id.tv_judge);

        TextView title3 = view.findViewById(R.id.tv_msj_level_modal3);
      //  title3.setTextColor(Color.rgb(96, 242, 220));
       //tv1.setTypeface(custom_font);
        //tv2.setTypeface(custom_font);
       //tv3.setTypeface(custom_font);
       // tv4.setTypeface(custom_font);
        title3.setTypeface(custom_font);
        tv5.setTypeface(custom_font);
        //tvRush.setTypeface(custom_font);
        tvJudge.setTypeface(custom_font);
        switch_autoplay.setTypeface(custom_font);


        tvRush.setOnClickListener(v -> {
            ParamsSong.rush += 0.1f;
            if (ParamsSong.rush > 1.5f) {
                ParamsSong.rush = 0.8f;
            }
            songList.playSoundPool(songList.spSelect);
            setTxtRush();
        });
        switch_autoplay.setChecked(ParamsSong.autoplay);
        switch_autoplay.setOnCheckedChangeListener((buttonView, isChecked) -> ParamsSong.autoplay = isChecked);
        tvJudge.setOnClickListener(v -> {
            ParamsSong.judgment = (ParamsSong.judgment + 1) % 7;
            setTxtJudge();
            songList.playSoundPool(songList.spSelect);
        });
        setTxtJudge();
        setTxtRush();


        pp1.setOnClickListener(v -> {
            velocity += 1;
            changeVelocity();
            songList.playSoundPool(songList.spSelect);

        });

        pp0_5.setOnClickListener(v -> {
            velocity += 0.5f;
            songList.playSoundPool(songList.spSelect);

            changeVelocity();
        });

        ll1 = view.findViewById(R.id.iv1ll);
        ll1.setOnClickListener(v -> {
            velocity -= 1;
            songList.playSoundPool(songList.spSelect);

            changeVelocity();
        });

        ll0_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                velocity -= 0.5f;
                songList.playSoundPool(songList.spSelect);

                changeVelocity();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            skins = NoteSkin.arraySkin(getContext());
            indexNS = ParamsSong.skinIndex;
            setImageNS();
        }


        note_button.setOnClickListener(v -> changeNS());
        note_image.setOnClickListener(v -> changeNS());


        changeVelocity();
        return view;
    }


    private void changeNS() {
        indexNS++;
        indexNS = (indexNS % skins.size());
        ParamsSong.skinIndex = indexNS;
        ParamsSong.nameNoteSkin = skins.get(indexNS).toString();
        setImageNS();
    }

    private void setImageNS() {
        note_image.setImageBitmap(NoteSkin.maskImage(skins.get(indexNS).toString(), getContext()));
        songList.imageSkin.setImageBitmap(NoteSkin.maskImage(skins.get(indexNS).toString(), getContext()));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        songList.playSoundPool(songList.spOpenWindow);

    }

    public void changeVelocity() {
        velocity = velocity < 0.5 ? 0.5f : velocity;
        ParamsSong.speed = velocity;
        msj2.setText("Velocity  " + velocity);
        songList.tv_velocity.setText("x" + (int)velocity);
    }


    private void setTxtJudge() {
        String text = "";
        switch (ParamsSong.judgment) {
            case 0://SJ
                text = "SJ";
                break;
            case 1://EJ
                text = "EJ";
                break;
            case 2://NJ
                text = "NJ";
                break;
            case 3://HJ
                text = "HJ";
                break;
            case 4://VJ
                text = "VJ";
                break;
            case 5://XJ
                text = "XJ";
                break;
            case 6://UJ
                text = "UJ";
                break;
        }
        tvJudge.setText(text);
        songList.tv_judment.setText(text);


    }

    private void setTxtRush() {

        int x = (int) (ParamsSong.rush * 100);
        tvRush.setText(x + "");


    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation2;
    }


}
