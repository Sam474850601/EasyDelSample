package com.to8to.easydelsample;


import com.to8to.easydel_annotation.ItemModel;

/**
 * Created by same.li on 2018/4/8.
 */

public class TestItemModel implements ItemModel {
    public String title;

    @Override
    public int getViewType() {
        return 1;
    }
}