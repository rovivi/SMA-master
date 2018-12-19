package com.example.rodrigo.sgame.ScreenSelectMusic;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rodrigo.sgame.CommonGame.Common;
import com.example.rodrigo.sgame.CommonGame.Level;
import com.example.rodrigo.sgame.R;

import java.util.ArrayList;

public class ArrayAdapterLevel extends ArrayAdapter<Level> {
    int lastPosition =-1;
    public ArrayAdapterLevel(@NonNull Context context, ArrayList<Level> resource) {
        super(context, 0,resource);
    }

    public ArrayAdapterLevel( @NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,  @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position,  @Nullable View convertView,  @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position,View convertedView,ViewGroup parent){

        if(convertedView== null){
            convertedView=LayoutInflater.from(getContext()).inflate(R.layout.lvl_item,parent,false);
        }

        ImageView imageView = convertedView.findViewById(R.id.bg_lvl);
        TextView lvl = convertedView.findViewById(R.id.tv_level);
        Level current = getItem(position);

        if(current!=null){
            lvl.setText(current.getNumberLevel()+"");
        }


        return  convertedView;
    }



}
