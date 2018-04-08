package com.to8to.easydelsample;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.to8to.easydel_annotation.AdapterLayout;
import com.to8to.easydel_annotation.Find;
import com.to8to.easydel_annotation.IHolder;



/**
 * Created by same.li on 2018/4/3.
 */

@AdapterLayout(id = R.layout.activity_main, itemModel = TestItemModel.class)
public class ContentHolder extends RecyclerView.ViewHolder
        implements
        //这个是标准接口，需要实现。
        IHolder<TestItemModel>

{

    @Find(R.id.test)
    TextView v_test;


    public ContentHolder(View itemView) {
        super(itemView);
    }


    @Override
    public void update(int postion, TestItemModel data) {
        v_test.setText(data.title);
    }



}
