package com.to8to.easydel_annotation;

import javax.swing.text.View;

/**
 * Created by same.li on 2018/4/8.
 */

public interface OnItemClickListener<T, V> {
    void onItemClick(int position, ItemData<T> itemData, V view);
}
