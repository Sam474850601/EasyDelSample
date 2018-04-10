package com.to8to.easydel;

import com.to8to.easydel_annotation.HelperName;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by same.li on 2018/4/9.
 */

public final class EasyDel {

    public static void inject(Object object)
    {
        Class<?> aClass = object.getClass();
        try {
            Class<?> helperClass = Class.forName(aClass.getName() + "$$"
                    + HelperName.NAME_SIMPLECLASS_ADAPTER_HELPER);
            Method method = helperClass.getMethod(HelperName.NAME_METHOD_ADAPTER_INJECT, new Class[]{object.getClass()});
            method.invoke(null, object);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
