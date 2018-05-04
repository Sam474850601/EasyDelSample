package com.to8to.easydel;

import android.view.View;

import com.to8to.easydel_annotation.HelperName;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by same.li on 2018/4/9.
 */

public final class EasyDel {


    //注入引用
    public static void inject(Object object)
    {
        try {
            invoke(object, HelperName.NAME_METHOD_INJECT);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static Object invoke(Object object, String methodName) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        Class<?> hostClass = object.getClass();
        String helperClassName = hostClass.getName() + "$$"
                + HelperName.NAME_SIMPLECLASS_ADAPTER_HELPER;
        Class<?> helperClass = Class.forName(helperClassName);
        Method method = helperClass.getMethod(methodName, new Class[]{hostClass});
        return method.invoke(null, object);

    }

    //加载布局
    public static View inflateLayoutView(Object object)
    {
        try {
           return (View) invoke(object, HelperName.NAME_METHOD_LOAD_LAYOUTVIEW);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
