package com.church.elim.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created with IntelliJ IDEA.
 * User: adi
 * Date: 5/30/13
 * Time: 11:07 PM
 */
public class ReflectionUtils {
    public static Class getActualTypeOfGenericParameter(Object object, int genericTypeIndex){
        Type mySuperclass = object.getClass().getGenericSuperclass();
        return (Class)((ParameterizedType)mySuperclass).getActualTypeArguments()[0];
    }
}
