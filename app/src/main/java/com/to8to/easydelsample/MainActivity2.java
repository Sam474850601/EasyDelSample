package com.to8to.easydelsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.to8to.easydel.AutoCreateAdapter;
import com.to8to.easydel.EasyDel;
import com.to8to.easydel_annotation.Adapter;


public class MainActivity2 extends AppCompatActivity {


    @Adapter(ContentHolder.class)
    AutoCreateAdapter contentAdapter;


    @Adapter({
                    ContentHolder.class,
                    ContentHolder2.class,
                    ContentHolder3.class,
                    //无限制holder数目
    })
    AutoCreateAdapter contentAdapter2;


    //不想继承AutoCreateAdapter，如下使用
    @Adapter(ContentHolder.class)
    CustomAutoCreateAdapter customAdapter;


    @Adapter(value = ContentHolder.class, extendsClass =  MyCustomAutoCreateAdapter.class)
    CustomAutoCreateAdapter customAdapter2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EasyDel.inject(this);





    }


}
