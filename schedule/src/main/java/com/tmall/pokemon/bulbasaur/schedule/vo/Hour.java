package com.tmall.pokemon.bulbasaur.schedule.vo;

/**
 * User:  yunche.ch ... (ว ˙o˙)ง
 * Date: 17/4/13
 * Time: 下午4:40
 */
public class Hour extends Interval {

    @Override
    public int calcDelay(int param) {

        if (param <= start) {
            return start - param;
        } else if (param > start && param <= end) {
            return 0;
        } else {
            return 24 - param + start;
        }
    }
}
