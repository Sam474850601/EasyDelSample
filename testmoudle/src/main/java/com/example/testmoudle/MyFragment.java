package com.example.testmoudle;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.to8to.easydel_annotation.ContainerType;
import com.to8to.easydel_annotation.Find;
import com.to8to.easydel_annotation.OnClick;
import com.to8to.easydel_annotation.ViewLayout;

/**
 * Created by same.li on 2018/4/11.
 */
@ContainerType(ContainerType.TYPE_FRAGMENT)
@ViewLayout(MR.layout.layout)
public class MyFragment extends Fragment{


    @Find(MR.id.tv_hao)
    TextView textView;

    @OnClick(MR.id.tv_hao)
    public void list(View view)
    {

    }


}
