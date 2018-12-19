package com.example.rodrigo.sgame.ScreenSelectMusic;

import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rodrigo.sgame.CommonGame.Common;
import com.example.rodrigo.sgame.CommonGame.Level;
import com.example.rodrigo.sgame.R;
import com.example.rodrigo.sgame.SongList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterLevel extends RecyclerView.Adapter<AdapterLevel.LvlHolder> {
    SongList songList;
    ArrayList<Level> levels;
    AssetManager am;
    public int lastPosition = -1;

    public AdapterLevel(ArrayList<Level> levels, SongList sl, AssetManager am) {
        this.levels = levels;
        this.songList = sl;
        this.am = am;
    }

    @Override
    public AdapterLevel.LvlHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lvl_item, null, false);
        return new LvlHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterLevel.LvlHolder holder, int position) {
        holder.setAttr(position);
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return levels.size();
    }

    public class LvlHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView bg;

        public LvlHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_level);

            bg= itemView.findViewById(R.id.bg_lvl);
            Typeface custom_font = Typeface.createFromAsset(am, "fonts/font.ttf");

            textView.setTypeface(custom_font);


        }

        public void setAttr(int index) {
            textView.setText(levels.get(index).getNumberLevel() + "");
            if (levels.get(index).getGameType().endsWith("double")) {
               // textView.setTextColor(Color.rgb(188, 244, 66));

                Picasso.get().load(R.drawable.hexa_double).into(bg);


            } else if (levels.get(index).getGameType().endsWith("single")) {
                //textView.setTextColor(Color.rgb(239, 140, 33));
                Picasso.get().load(R.drawable.hexa_single).into(bg);

            }
            else {

                Picasso.get().load(R.drawable.hexa_performance).into(bg);

            }

        }

    }


    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation;
            //Animation animation =
            // AnimationUtils.loadAnimation(a, android.R.anim.slide_in_left);
            if (Common.getRandomNumberInRange(0, 10) > 5) {
                animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.bounce);
            } else {
                animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_in_left);
            }
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


}
