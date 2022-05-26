package com.alibaba.pokemon.bulbasaur.persist.domain;

import java.util.Date;

/**
 *
 *
 * @author ch
 * @date 2017-09-27 17:51:29
 *
 */
public class StateDO {
    /**
     * id
     */
    private Long id;

    /**
     * biz id, see biz_id in pikachu_p
     */
    private String bizId;

    /**
     * state name
     */
    private String stateName;

    /**
     * 标识
     */
    private String ownSign;

    /**
     * state''s status\n  1. ready - ready to execute\n  2. complete - completed\n  3. rollback - rollbacked
     */
    private String status;

    /**
     * gmt_create
     */
    private Date gmtCreate;

    /**
     * gmt_modified
     */
    private Date gmtModified;

    /**
     * 别名，主要维护中文名
     */
    private String alias;

    /**
     * 执行信息
     */
    private String exeInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId == null ? null : bizId.trim();
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName == null ? null : stateName.trim();
    }

    public String getOwnSign() {
        return ownSign;
    }

    public void setOwnSign(String ownSign) {
        this.ownSign = ownSign == null ? null : ownSign.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias == null ? null : alias.trim();
    }

    public String getExeInfo() {
        return exeInfo;
    }

    public void setExeInfo(String exeInfo) {
        this.exeInfo = exeInfo == null ? null : exeInfo.trim();
    }
}
