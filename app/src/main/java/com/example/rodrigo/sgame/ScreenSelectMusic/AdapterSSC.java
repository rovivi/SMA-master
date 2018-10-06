package com.example.rodrigo.sgame.ScreenSelectMusic;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rodrigo.sgame.CommonGame.Common;
import com.example.rodrigo.sgame.R;
import com.squareup.picasso.Picasso;

import java.io.File;

public class AdapterSSC extends RecyclerView.Adapter<AdapterSSC.ViewHolderSSC> {

    private int index;
    private SongsGroup songsGroup;
    SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int lastPosition = -1;
    Activity a;

    public AdapterSSC(SongsGroup songsGroup, int index, Activity a) {
        this.index = index;
        this.songsGroup = songsGroup;
        this.a=a;
    }


    @Override
    public AdapterSSC.ViewHolderSSC onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item, null, false);
        return new ViewHolderSSC(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterSSC.ViewHolderSSC holder, int position) {
        //ciando se selecciona  un elemento en la carga
        holder.setInfo(songsGroup, position);
       // holder.setSelected( selectedItems.get(position, false));
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return songsGroup.listOfSongs.size();
    }


    //HolderDel SSC

    public class ViewHolderSSC extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        TextView tv1;
        TextView Artist;
        ImageView banner;

        public ViewHolderSSC(View v) {
            super(v);
            tv1 = v.findViewById(R.id.Name);
            Artist = v.findViewById(R.id.artist);
            banner = v.findViewById(R.id.banner);

        }

        public void setInfo(SongsGroup sg, int index) {
            tv1.setText(sg.listOfSongs.get(index).songInfo.get("TITLE"));
            Artist.setText(sg.listOfSongs.get(index).songInfo.get("ARTIST"));
            File f=new File(sg.listOfSongs.get(index).path.getPath() + "/" + sg.listOfSongs.get(index).songInfo.get("BANNER"));
            //Bitmap bitmap = BitmapFactory.decodeFile(sg.listOfPaths.get(index).getPath() + "/" + sg.listOfSongs.get(index).songInfo.get("BANNER").toString());
            //banner.setImageBitmap(bitmap);
            Picasso.get().load(f).error(R.drawable.no_banner).resize(200,120).centerCrop().into(banner);

        }

        public void setSelected(boolean is) {
            Artist.setText(is+"");
        }

        @Override
        public void onClick(View v) {

            if (selectedItems.get(getAdapterPosition(), false)) {
                selectedItems.delete(getAdapterPosition());
                setSelected(true);
            } else {
                selectedItems.put(getAdapterPosition(), true);
                setSelected(true);
            }
        }
    }


    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation;
            //Animation animation =
            // AnimationUtils.loadAnimation(a, android.R.anim.slide_in_left);
            if (Common.getRandomNumberInRange(0,10)>5){
                 animation = AnimationUtils.loadAnimation(a, R.anim.bounce);
            }
            else{
                 animation = AnimationUtils.loadAnimation(a, android.R.anim.slide_in_left);
            }
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
