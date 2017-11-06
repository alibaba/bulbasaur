package com.tmall.pokemon.bulbasaur.persist.domain;

public class StateDOWithBLOBs extends StateDO {
    /**
     * 业务信息
     */
    private String preBizInfo;

    /**
     * current biz context info
     */
    private String bizInfo;

    public String getPreBizInfo() {
        return preBizInfo;
    }

    public void setPreBizInfo(String preBizInfo) {
        this.preBizInfo = preBizInfo == null ? null : preBizInfo.trim();
    }

    public String getBizInfo() {
        return bizInfo;
    }

    public void setBizInfo(String bizInfo) {
        this.bizInfo = bizInfo == null ? null : bizInfo.trim();
    }
}