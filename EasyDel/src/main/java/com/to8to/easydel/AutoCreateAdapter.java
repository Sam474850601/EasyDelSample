package com.to8to.easydel;

import android.support.v7.widget.RecyclerView;

import com.to8to.easydel_annotation.ItemData;

import java.util.List;

/**
 * Created by same.li on 2018/4/5.
 */

public abstract  class AutoCreateAdapter extends RecyclerView.Adapter implements IAutoCreateAdapter {

    public List<ItemData> itemListData;


    @Override
    public List<ItemData> getItemListData() {
        return itemListData;
    }


    @Override
    public void setItemListData(List<ItemData> itemListData) {
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
        return itemListData.get(position).viewType;
    }

    @Override
    public ItemData getItemData(int position)
    {
        return itemListData.get(position);
    }

}

