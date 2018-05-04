package com.example.testmoudle;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.to8to.easydel.EasyDel;
import com.to8to.easydel_annotation.ContainerType;
import com.to8to.easydel_annotation.ViewLayout;

/**
 * Created by same.li on 2018/5/4.
 */
@ContainerType(ContainerType.TYPE_ACTIVITY)
@ViewLayout(MR.layout.layout)
public abstract class BaseActivity extends Activity {


    protected  abstract void init();

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(EasyDel.inflateLayoutView(this));
        EasyDel.inject(this);
        init();
    }
}
