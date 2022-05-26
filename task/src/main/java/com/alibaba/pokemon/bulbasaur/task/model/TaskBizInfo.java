package com.alibaba.pokemon.bulbasaur.task.model;

import java.io.Serializable;

/**
 * Task 中用于存储外部信息，存于Task表的bizInfo字段
 * 业务方自己维护要存入的数据类型，必须为可序列化的！
 * 自己存入，比如String 或者 json，自己再读取使用
 *
 * User:  yunche.ch ... (ว ˙o˙)ง
 * Date: 17/3/10
 * Time: 下午5:55
 */
public class TaskBizInfo<T> implements Serializable {
    private static final long serialVersionUID = 3994965683944923241L;

    public TaskBizInfo(T t) {
        this.t = t;
    }

    // 业务方存入内容
    T t;

    public void setT(T t) {
        this.t = t;
    }

    public T getT() {
        return t;
    }
}
