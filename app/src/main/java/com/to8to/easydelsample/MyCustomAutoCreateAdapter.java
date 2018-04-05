package com.to8to.easydelsample;

import com.to8to.easydel.IAutoCreateAdapter;
import com.to8to.easydel_annotation.ItemData;

import java.util.List;

/**
 * Created by same.li on 2018/4/3.
 */

public    class MyCustomAutoCreateAdapter implements IAutoCreateAdapter {


    @Override
    public ItemData getItemData(int position) {
        return null;
    }

    @Override
    public List<ItemData> getItemListData() {
        return null;
    }

    @Override
    public void setItemListData(List<ItemData> itemListData) {

    }
}
