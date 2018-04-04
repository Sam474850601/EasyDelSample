package com.to8to.easydel_annotation;



/**
 * Created by same.li on 2018/4/2.
 */

public interface IHolder<T> {
    void update(int postion, ItemData<T> itemData);
}
