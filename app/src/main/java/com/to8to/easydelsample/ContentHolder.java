package com.to8to.easydelsample;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.to8to.easydel_annotation.AdapterLayout;
import com.to8to.easydel_annotation.Find;
import com.to8to.easydel_annotation.IHolder;
import com.to8to.easydel_annotation.OnItemClickListener;
import com.to8to.easydel_annotation.OnItemLongClickListener;


/**
 * Created by same.li on 2018/4/3.
 */

@AdapterLayout(id = R.layout.adapter_type1, itemModel = TestItemModel.class)
public class ContentHolder extends RecyclerView.ViewHolder
        implements
        //这个是标准接口，需要实现。
        IHolder<TestItemModel>,
        OnItemClickListener<TestItemModel, View>,
        OnItemLongClickListener<TestItemModel, View>

{

    @Find(R.id.tv_title)
    TextView tvTitle;


    public ContentHolder(View itemView) {
        super(itemView);
    }


    @Override
    public void update(int postion, TestItemModel data) {
        tvTitle.setText(data.getTitle());
    }


    @Override
    public void onItemClick(int position, TestItemModel itemData, View view) {
        Toast.makeText(view.getContext(), "onItemClick position"+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(int position, TestItemModel itemData, View view) {
        Toast.makeText(view.getContext(), "onItemLongClick position"+position, Toast.LENGTH_SHORT).show();
        return false;
    }
}
