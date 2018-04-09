package com.to8to.easydelsample;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.to8to.easydel_annotation.AdapterLayout;
import com.to8to.easydel_annotation.IHolder;
import com.to8to.easydel_annotation.OnChildItemClick;

/**
 * Created by same.li on 2018/4/3.
 */

@AdapterLayout(id = R.layout.adapter_type3, viewType = 3, itemModel = TestItemModel3.class)
public class ContentHolder3 extends RecyclerView.ViewHolder implements IHolder<TestItemModel3> {


    public ContentHolder3(View itemView) {
        super(itemView);
    }

    @OnChildItemClick(R.id.iv_left)
    public void onRightImageViewClike(int position, TestItemModel3 itemData, View view)
    {
        Toast.makeText(view.getContext(), "TestItemModel3 value:"+itemData.getValue(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void update(int postion, TestItemModel3 itemData) {

    }
}



