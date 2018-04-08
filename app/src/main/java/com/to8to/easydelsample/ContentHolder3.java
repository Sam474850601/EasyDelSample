package com.to8to.easydelsample;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.to8to.easydel_annotation.AdapterLayout;
import com.to8to.easydel_annotation.IHolder;

/**
 * Created by same.li on 2018/4/3.
 */

@AdapterLayout(id = R.layout.activity_main, viewType = 3)
public class ContentHolder3 extends RecyclerView.ViewHolder implements IHolder {


    public ContentHolder3(View itemView) {
        super(itemView);
    }



    @Override
    public void update(int postion, Object itemData) {

    }
}



