package com.to8to.easydelsample;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.to8to.easydel_annotation.AdapterLayout;
import com.to8to.easydel_annotation.IHolder;
import com.to8to.easydel_annotation.ItemModel;
import com.to8to.easydel_annotation.OnChildItemClick;
import com.to8to.easydel_annotation.OnItemClickListener;
import com.to8to.easydel_annotation.OnItemLongClickListener;

/**
 * Created by same.li on 2018/4/3.
 */

@AdapterLayout(id = R.layout.activity_main, viewType = 2, itemModel = TestItemModel2.class)
public class ContentHolder2 extends RecyclerView.ViewHolder implements IHolder<TestItemModel2> ,
        //点击接口可不实现
        OnItemClickListener<TestItemModel2, View>,
        //长按接口可不实现
        OnItemLongClickListener<TestItemModel2, View> {


    public ContentHolder2(View itemView) {
        super(itemView);
    }


    @Override
    public void update(int postion, TestItemModel2 itemData) {

    }



    @OnChildItemClick(R.id.test)
    public void testClike(int position, TestItemModel2 itemData, View view)
    {

    }

    @OnChildItemClick(R.id.all)
    public void allClike(int position, TestItemModel2 itemData, View view)
    {

    }

    @Override
    public void onItemClick(int position, TestItemModel2 itemData, View view) {

    }

    @Override
    public boolean onItemLongClick(int position, TestItemModel2 itemData, View parent) {
        return false;
    }
}
