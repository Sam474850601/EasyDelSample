package com.to8to.easydelsample;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.to8to.easydel_annotation.AdapterLayout;
import com.to8to.easydel_annotation.Find;
import com.to8to.easydel_annotation.IHolder;
import com.to8to.easydel_annotation.OnChildItemClick;
import com.to8to.easydel_annotation.OnItemClickListener;
import com.to8to.easydel_annotation.OnItemLongClickListener;

/**
 * Created by same.li on 2018/4/3.
 */

@AdapterLayout(id = R.layout.adapter_type2, viewType = 2, itemModel = TestItemModel2.class)
public class ContentHolder2 extends RecyclerView.ViewHolder implements IHolder<TestItemModel2>{



    public ContentHolder2(View itemView) {
        super(itemView);
    }

    @OnChildItemClick(R.id.iv_right)
    public void onLeftImageViewClike(int position, TestItemModel2 itemData, View view) {
        Toast.makeText(view.getContext(), "TestItemModel2 message:"+itemData.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @OnChildItemClick(R.id.tv_title)
    public void tv_title(int position, TestItemModel2 itemData, View view)
    {
        Toast.makeText(view.getContext(), "TestItemModel2 message:"+itemData.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Find(R.id.iv_right)
    View testdd;

    @Find(R.id.tv_title)
    View dsfadsfasdf;




    @Override
    public void update(int postion, TestItemModel2 itemData) {

    }




}
