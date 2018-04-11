package com.to8to.easydelsample;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.to8to.easydel.AutoCreateAdapter;
import com.to8to.easydel.EasyDel;
import com.to8to.easydel_annotation.Adapter;
import com.to8to.easydel_annotation.ContainerType;
import com.to8to.easydel_annotation.Find;
import com.to8to.easydel_annotation.OnClick;

import java.util.ArrayList;
import java.util.List;

@ContainerType(ContainerType.TYPE_ACTIVITY)
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


    @OnClick(R.id.list)
    public void testMethod(View view)
    {

    }



    @Find(R.id.list)
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EasyDel.inject(this);
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
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.commit();

    }


}
