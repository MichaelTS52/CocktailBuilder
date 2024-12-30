package com.mts.cocktailbuilder.Utility;

public class Utils {

    public static String nullSafetoString(Object obj) {
        if(obj == null){
            return "";
        }
        return obj.toString();
    }
}
