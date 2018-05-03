package com.to8to.easydelsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.testmoudle.TestMoudleActivity;
import com.to8to.easydel.AutoCreateAdapter;
import com.to8to.easydel_annotation.Adapter;
import com.to8to.easydel_annotation.ContainerType;

@ContainerType(ContainerType.TYPE_ACTIVITY)
public class MainActivity extends AppCompatActivity {


    @Adapter(ContentHolder.class)
    AutoCreateAdapter autoCreateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, TestMoudleActivity.class));



    }


}
