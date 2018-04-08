package com.to8to.easydel;

import android.support.v7.widget.RecyclerView;

import com.to8to.easydel_annotation.ViewType;

import java.util.List;

/**
 * Created by same.li on 2018/4/5.
 */

public  abstract   class AutoCreateAdapter extends RecyclerView.Adapter implements IAutoCreateAdapter {

    public List itemListData;


    @Override
    public Object getItemData(int position) {
        return itemListData.get(position);
    }

    @Override
    public List getItemListData() {
        return itemListData;
    }


    @Override
    public void setItemListData(List itemListData) {
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
        Class<?> aClass = itemListData.get(position).getClass();
        ViewType annotation =aClass.getAnnotation(ViewType.class);
        if(null == annotation)
        {
            throw new RuntimeException(aClass.getName()+"need  support ViewType annotation");
        }
        return annotation.value();
    }



}

