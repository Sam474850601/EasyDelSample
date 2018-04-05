package com.to8to.easydel;

import com.to8to.easydel_annotation.ItemData;

import java.util.List;

/**
 * Created by same.li on 2018/4/5.
 */

public interface IAutoCreateAdapter {
    ItemData getItemData(int position);

    List<ItemData> getItemListData();

    void setItemListData(List<ItemData> itemListData);
}
