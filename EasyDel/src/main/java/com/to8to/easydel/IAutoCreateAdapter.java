package com.to8to.easydel;
import com.to8to.easydel_annotation.ItemModel;

import java.util.List;

/**
 * Created by same.li on 2018/4/5.
 */

public interface IAutoCreateAdapter<T extends ItemModel> {
    T getItemData(int position);

    List<T> getItemListData();

    void setItemListData(List<T> itemListData);
}
