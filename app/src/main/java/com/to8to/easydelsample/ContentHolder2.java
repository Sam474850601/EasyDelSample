package com.to8to.easydelsample;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.to8to.easydel_annotation.AdapterLayout;
import com.to8to.easydel_annotation.IHolder;
import com.to8to.easydel_annotation.ItemData;

/**
 * Created by same.li on 2018/4/3.
 */

@AdapterLayout(id = R.layout.activity_main, viewType = 2)
public class ContentHolder2 extends RecyclerView.ViewHolder implements IHolder{


    public ContentHolder2(View itemView) {
        super(itemView);
    }

    @Override
    public void update(int postion, ItemData itemData) {

    }
}
