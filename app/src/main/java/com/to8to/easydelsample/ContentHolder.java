package com.to8to.easydelsample;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.to8to.easydel_annotation.Find;
import com.to8to.easydel_annotation.IHolder;
import com.to8to.easydel_annotation.ItemData;
import com.to8to.easydel_annotation.AdapterLayout;

/**
 * Created by same.li on 2018/4/3.
 */

@AdapterLayout(id = R.layout.activity_main, viewType =1)
public class ContentHolder extends RecyclerView.ViewHolder implements IHolder{


    @Find(R.id.test)
    View v_test;



    public ContentHolder(View itemView) {
        super(itemView);
    }





    @Override
    public void update(int postion, ItemData itemData) {

    }
}
