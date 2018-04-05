package com.to8to.easydelsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.to8to.easydel.AutoCreateAdapter;
import com.to8to.easydel.IAutoCreateAdapter;
import com.to8to.easydel_annotation.Adapter;


public class MainActivity extends AppCompatActivity {


    @Adapter(
            holders = {
                    "com.to8to.easydelsample.ContentHolder",
            }
    )
    AutoCreateAdapter contentAdapter;


    @Adapter(
            holders = {
                    "com.to8to.easydelsample.ContentHolder",
                    "com.to8to.easydelsample.ContentHolder1",
                    "com.to8to.easydelsample.ContentHolder2"
                    //无限制holder数目
            }
    )
    AutoCreateAdapter contentAdapter2;


    //不想继承AutoCreateAdapter，如下使用
    @Adapter(
            holders = {
                    "com.to8to.easydelsample.ContentHolder"
            }
    )
    CustomAutoCreateAdapter customAdapter;


    @Adapter(
            holders = {
                    "com.to8to.easydelsample.ContentHolder"
            },
            extend =  "com.to8to.easydelsample.MyCustomAutoCreateAdapter"
    )
    IAutoCreateAdapter customAdapter2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new MainActivity$$ContentAutoCreateAdapter();
        new MainActivity$$ContentAutoCreateAdapter2();
        new MainActivity$$CustomAutoCreateAdapter();
        new MainActivity$$CustomAutoCreateAdapter2();
    }


}
