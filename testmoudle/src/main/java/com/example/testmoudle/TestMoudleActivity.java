package com.example.testmoudle;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.to8to.easydel.EasyDel;
import com.to8to.easydel_annotation.ContainerType;
import com.to8to.easydel_annotation.Find;
import com.to8to.easydel_annotation.OnClick;

/**
 * Created by same.li on 2018/5/3.
 */
@ContainerType(ContainerType.TYPE_ACTIVITY)
public class TestMoudleActivity extends Activity {



    @Find(MR.id.tv_hao)
    TextView tvTitle;


    @OnClick(MR.id.tv_hao)
    public void onTitleClick(View view)
    {
        Toast.makeText(this, "a", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        EasyDel.inject(this);

        tvTitle.setText("呵呵打");

    }
}
