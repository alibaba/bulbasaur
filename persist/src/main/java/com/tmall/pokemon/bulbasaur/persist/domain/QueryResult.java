package com.tmall.pokemon.bulbasaur.persist.domain;

import java.io.Serializable;
import java.util.Collection;

/**
 * User:  yunche.ch ... (ว ˙o˙)ง
 * Date: 14-12-22
 * Time: 上午10:32
 */
public class QueryResult<T> implements Serializable {
    private static final long serialVersionUID = -8722768053248374040L;

    private boolean success = true;
    private long total = 0;
    private Collection<T> dataList;
    private String info;
    private String key;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Collection<T> getDataList() {
        return dataList;
    }

    public void setDataList(Collection<T> dataList) {
        this.dataList = dataList;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}

