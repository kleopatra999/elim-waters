package com.church.elim;

import org.apache.commons.lang.math.RandomUtils;

/**
 * Created with IntelliJ IDEA.
 * User: adi
 * Date: 5/15/13
 * Time: 11:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class ElimRandom {
    public static Long newId(){
        return RandomUtils.nextLong();
    }

    public static String newName(String prefix){
        return prefix + RandomUtils.nextLong();
    }
}
