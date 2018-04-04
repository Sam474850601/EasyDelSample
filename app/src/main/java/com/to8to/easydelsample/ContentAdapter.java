package com.to8to.easydelsample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by same.li on 2018/4/3.
 */

public  abstract class ContentAdapter  extends RecyclerView.Adapter{


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater.from(parent.getContext()).inflate(0, parent, false);
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {}

    @Override
    public int getItemCount() {
        return 0;
    }
}
