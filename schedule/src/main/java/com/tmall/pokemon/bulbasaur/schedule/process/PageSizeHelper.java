package com.tmall.pokemon.bulbasaur.schedule.process;

/**
 * User:  yunche.ch ... (ว ˙o˙)ง
 * Date: 15/12/29
 * Time: 下午7:30
 */

public class PageSizeHelper {

    public static int calcTotalPages(int totalCounts, int pageSize) {
        int temp = (totalCounts / pageSize);
        int mod = (totalCounts % pageSize);
        if (temp == 0 && mod != 1) {
            return 1;
        } else if (temp == 1 && mod == 0) {
            return 1;
        } else {
            return temp + 1;
        }
    }

    public static int calcOffset(int pageNo, int pageSize) {
        return (pageNo - 1) * pageSize;
    }

}
