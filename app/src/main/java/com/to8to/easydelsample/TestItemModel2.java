package com.to8to.easydelsample;


import com.to8to.easydel_annotation.ItemModel;

/**
 * Created by same.li on 2018/4/8.
 */
@ItemModel(viewType = 2)
public class TestItemModel2{
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
