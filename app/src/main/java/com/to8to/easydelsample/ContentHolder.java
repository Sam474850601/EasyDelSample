package com.to8to.easydelsample;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.to8to.easydel_annotation.Find;
import com.to8to.easydel_annotation.IHolder;
import com.to8to.easydel_annotation.ItemData;
import com.to8to.easydel_annotation.AdapterLayout;
import com.to8to.easydel_annotation.OnClick;
import com.to8to.easydel_annotation.OnItemClickListener;
import com.to8to.easydel_annotation.OnItemLongClickListener;



/**
 * Created by same.li on 2018/4/3.
 */

@AdapterLayout(id = R.layout.activity_main)
public class ContentHolder extends RecyclerView.ViewHolder
        implements
        //这个是标准接口，需要实现。
        IHolder<String>,
        //点击接口可不实现
        OnItemClickListener<String, View>,
        //长按接口可不实现
        OnItemLongClickListener<String, View>

{


    @Find(R.id.test)
    TextView v_test;


    public ContentHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) {}});
    }


    @OnClick(R.id.test)
    public void testClike(View view)
    {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
    }

    @OnClick(R.id.all)
    public void allClike(View view)
    {

    }


    @Override
    public void update(int postion, ItemData<String> itemData) {
        v_test.setText(itemData.data);
    }


    @Override
    public void onItemClick(int position, ItemData<String> itemData, View view) {
        Log.e("ContentHolder", "onItemClick:position="+position
                +",data="+itemData.data);
    }

    public void onItemClick(int position, ItemData<String> itemData) {
        Log.e("ContentHolder", "onItemClick:position="+position
                +",data="+itemData.data);
    }

    @Override
    public boolean onItemLongClick(int position, ItemData<String> itemData, View parent) {
        Log.e("ContentHolder", "onItemLongClick:position="+position
                +",data="+itemData.data);
        return false;
    }
}
