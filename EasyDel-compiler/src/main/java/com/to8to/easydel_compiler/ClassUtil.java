package com.to8to.easydel_compiler;

/**
 * Created by same.li on 2018/4/4.
 */

public class ClassUtil {
    public static String[] getPackagAnName(String className)
    {
        final int lastpackagePoint = className.lastIndexOf(".");
        final  String packageName = className.substring(0, lastpackagePoint);
        final  String simpleName = className.substring(lastpackagePoint+1, className.length());
        return new String[]{packageName,simpleName };
    }

}
