package com.to8to.easydelsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.to8to.easydel.AutoCreateAdapter;
import com.to8to.easydel.EasyDel;
import com.to8to.easydel_annotation.Adapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


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

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EasyDel.injectAdapter(this);
        recyclerView = findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(contentAdapter2);

        TestItemModel testItemModel =new TestItemModel();
        testItemModel.setTitle("哈哈哈");
        TestItemModel2 testItemModel2 =new TestItemModel2();
        testItemModel2.setMessage("啦啦啦啦");
        TestItemModel3 testItemModel3 =new TestItemModel3();
        testItemModel3.setValue("桀桀借");
        List list =new ArrayList<>();
        list.add(testItemModel);
        list.add(testItemModel2);
        list.add(testItemModel3);
        TestItemModel testItemModel4 = new TestItemModel();
        testItemModel4.setTitle("呵呵呵");
        list.add(testItemModel4);
        contentAdapter2.setItemListData(list);
    }


}
