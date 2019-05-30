package com.etr.util;

/**
 * @Author: ypc
 * @Date: 2019/5/23 23:00
 * @Description:
 */
public class RandomUtils {
    public static int getRamdomCode() {
        int radomNum=0;
        for (int j = 0; j < 100; j++) {
            radomNum=(int) ((Math.random() * 9 + 1) * 100000);
        }
        return radomNum;
    }
}
