package com.to8to.easydelsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.to8to.easydel_annotation.Adapter;


public class MainActivity extends AppCompatActivity {


    @Adapter(
            holders = {
                    "com.to8to.easydelsample.ContentHolder",
                    "com.to8to.easydelsample.ContentHolder2",
                    "com.to8to.easydelsample.ContentHolder3"
            }
    )
    RecyclerView.Adapter contentAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


}
