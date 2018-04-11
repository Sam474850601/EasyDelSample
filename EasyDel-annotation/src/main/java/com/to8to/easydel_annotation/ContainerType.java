package com.to8to.easydel_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by same.li on 2018/4/2.
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface ContainerType {
    int TYPE_ADAPTER = 0;
    int TYPE_ACTIVITY = 1;
    int TYPE_FRAGMENT = 2;
    int value();
}
