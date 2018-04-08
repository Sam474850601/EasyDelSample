package com.to8to.easydel;

import android.support.v7.widget.RecyclerView;

import com.to8to.easydel_annotation.ItemModel;

import java.util.List;

/**
 * Created by same.li on 2018/4/5.
 */

public abstract  class AutoCreateAdapter extends RecyclerView.Adapter implements IAutoCreateAdapter<ItemModel> {

    public List<ItemModel> itemListData;


    @Override
    public List<ItemModel> getItemListData() {
        return itemListData;
    }


    @Override
    public void setItemListData(List<ItemModel> itemListData) {
        this.itemListData = itemListData;
    }

    @Override
    public int getItemCount() {
        if(null == itemListData)
            return 0;
        return itemListData.size();
    }


    @Override
    public int getItemViewType(int position) {
        return itemListData.get(position).getViewType();
    }

    @Override
    public ItemModel getItemData(int position)
    {
        return itemListData.get(position);
    }

}

