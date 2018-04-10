package com.to8to.easydelsample;


import com.to8to.easydel_annotation.ItemModel;

/**
 * Created by same.li on 2018/4/8.
 */
@ItemModel(viewType = 3)
public class TestItemModel3  {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
