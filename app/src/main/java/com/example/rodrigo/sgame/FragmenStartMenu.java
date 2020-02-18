package com.example.rodrigo.sgame;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class FragmenStartMenu extends DialogFragment {
    public static boolean loadingScreen = false;
    //public ArrayList<Level> lista;

    SongList songList;
    ImageView hexagons[] = new ImageView[3];
    TextView exit,loading;
    public TextView percent;
    public String textPercent="";
    ImageView startImage,startImage2;
    ValueAnimator anim;
    private boolean wanrStartSong = false;
    Handler handler=new Handler();

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
        percent=view.findViewById(R.id.percent_text_fragment);
        loading=view.findViewById(R.id.loading_text_dialog);
        exit = view.findViewById(R.id.tv_damiss);

        exit.setOnClickListener(v -> dismiss());
        startImage = view.findViewById(R.id.start_image);
        startImage2 = view.findViewById(R.id.start_blour);


        if (!loadingScreen) {
            startImage.setOnClickListener(v -> {
                        anim.start();
                        wanrStartSong = true;
                    }
            );
        } else {
            startImage2.setVisibility(View.INVISIBLE);
            startImage.setVisibility(View.INVISIBLE);
            exit.setVisibility(View.INVISIBLE);
          //  getDialog().setCancelable(false);
            loading.setVisibility(View.VISIBLE);
        //    percent.setVisibility(View.VISIBLE);


        }


        final int from = Color.argb(100, 00, 00, 00);
        final int to = Color.argb(100, 255, 255, 255);

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

        if (songList!=null){
            songList.playSoundPool(songList.spSelectSong);

        }

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

    }

    @Override
    public void onResume() {
        super.onResume();

        hexagons[0].startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.rotate));
        hexagons[1].startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.rotate2));
        hexagons[0].startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.rotate));
        if (!loadingScreen){
            startImage2.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.fade_half));
        }

    }

    @Override
    public void onDetach() {
        if (wanrStartSong) {
            if (songList!=null){
            songList.startSong();
            }
        }
        super.onDetach();
    }

}
