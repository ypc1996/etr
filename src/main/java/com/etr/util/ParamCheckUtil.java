package com.etr.util;

import java.util.List;

public class ParamCheckUtil {
    public static boolean checkParamsNotNull(Object ... objects){
        for (Object s : objects) {
            if (null == s) {
                return false;
            }
        }

        return true;
    }

    public static boolean checkIntListEmpty(List<Integer> objects) {
        return objects == null || objects.size() == 0;
    }
}
