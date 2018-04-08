package com.to8to.easydelsample;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.to8to.easydel.IAutoCreateAdapter;

import java.util.List;

/**
 * Created by same.li on 2018/4/3.
 */

public    class CustomAutoCreateAdapter extends RecyclerView.Adapter implements IAutoCreateAdapter {


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public Object getItemData(int position) {
        return null;
    }

    @Override
    public List getItemListData() {
        return null;
    }

    @Override
    public void setItemListData(List itemListData) {

    }
}
