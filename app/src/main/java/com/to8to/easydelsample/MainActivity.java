package com.to8to.easydelsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.to8to.easydel_annotation.Adapter;

public class MainActivity extends AppCompatActivity {

    @Adapter(ContentHolder.class)
    ContentAdapter  adapter;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new MainActivity$$Adapter();

    }
}
