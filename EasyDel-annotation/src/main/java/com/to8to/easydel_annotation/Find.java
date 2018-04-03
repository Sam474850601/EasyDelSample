package com.to8to.easydel_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by same.li on 2018/4/2.
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface Find {
    int value();
}
