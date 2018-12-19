package com.example.rodrigo.sgame;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.rodrigo.sgame.CommonGame.ParamsSong;

import java.util.Objects;

public class FragmenStartMenu extends DialogFragment {
    //public ArrayList<Level> lista;

    SongList songList;
    ImageView hexagons[] = new ImageView[3];
    TextView exit;
    ImageView startImage;
    ValueAnimator anim;
    private boolean wanrStartSong=false;
    public void setSongList(SongList s) {
        this.songList = s;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //adapterLevel = new AdapterLevel(lista, null, getActivity().getAssets());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getDialog().getWindow())
                    .getAttributes().windowAnimations = R.style.DialogAnimation;
            Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }

        View view = inflater.inflate(R.layout.fragment_fragment__start_menu, container, false);

        hexagons[0] = view.findViewById(R.id.iv_hexagon1);
        hexagons[1] = view.findViewById(R.id.iv_hexagon2);
        hexagons[2] = view.findViewById(R.id.iv_hexagon3);
        exit = view.findViewById(R.id.tv_damiss);

        exit.setOnClickListener(v -> dismiss());
        startImage = view.findViewById(R.id.start_image);
        startImage.setOnClickListener(v -> {


                    anim.start();
                    wanrStartSong=true;

                   // songList.startSong();
                }


        );


        final int from = Color.argb(100,00,00,00);
        final int to = Color.argb(100,255,255,255);

        anim = new ValueAnimator();
        anim.setIntValues(from, to);
        anim.setEvaluator(new ArgbEvaluator());
        anim.addUpdateListener(valueAnimator -> view.setBackgroundColor((Integer) valueAnimator.getAnimatedValue()));

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
dismiss();
            }
        });
        anim.setDuration(250);



        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        songList.playSoundPool(songList.spSelectSong);

    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Override
    public void onStart() {
        super.onStart();

        hexagons[0].startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.rotate));
        hexagons[1].startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.rotate2));
        hexagons[0].startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.rotate));


    }


    @Override
    public void onDetach() {
        if (wanrStartSong){

            songList.startSong();
        }


        super.onDetach();
    }
}
