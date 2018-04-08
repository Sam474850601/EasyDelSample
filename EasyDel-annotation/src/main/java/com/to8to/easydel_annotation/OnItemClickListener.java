package com.to8to.easydel_annotation;

/**
 * Created by same.li on 2018/4/8.
 */

public interface OnItemClickListener<T, V> {
    void onItemClick(int position, T itemData, V view);
}
