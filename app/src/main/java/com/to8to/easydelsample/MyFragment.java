package com.to8to.easydelsample;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.to8to.easydel_annotation.ContainerType;
import com.to8to.easydel_annotation.Find;
import com.to8to.easydel_annotation.OnClick;

/**
 * Created by same.li on 2018/4/11.
 */
@ContainerType(ContainerType.TYPE_FRAGMENT)
public class MyFragment extends Fragment{


    @Find(R.id.list)
    RecyclerView recyclerView2;

    @Find(R.id.list)
    RecyclerView recyclerView3;

    @OnClick(R.id.list)
    public void list(View view)
    {

    }

    @OnClick(R.id.tv_title)
    public void tv_title(View view)
    {

    }

    @OnClick(R.id.top)
    public void top(View view)
    {

    }


}
