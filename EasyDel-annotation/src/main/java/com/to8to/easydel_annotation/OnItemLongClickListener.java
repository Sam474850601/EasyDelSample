package com.to8to.easydel_annotation;

/**
 * Created by same.li on 2018/4/8.
 */

public interface OnItemLongClickListener<T extends ItemModel, V> {
    boolean onItemLongClick(int position, T itemData, V parent);
}
