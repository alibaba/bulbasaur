package com.alibaba.pokemon.bulbasaur.schedule.vo;

import com.google.common.base.Preconditions;

/**
 * User:  yunche.ch ... (ว ˙o˙)ง
 * Date: 17/4/13
 * Time: 下午4:38
 */
public abstract class Interval {

    protected int start;
    protected int end;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public static void check(int start, int end) {
        Preconditions.checkArgument(start <= end, "开始只能小于等于结束");
    }

    public abstract int calcDelay(int param);

}
