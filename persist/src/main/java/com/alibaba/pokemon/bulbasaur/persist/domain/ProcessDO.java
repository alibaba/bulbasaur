package com.alibaba.pokemon.bulbasaur.persist.domain;

import java.util.Date;

/**
 *
 *
 * @author ch
 * @date 2017-09-27 17:51:28
 *
 */
public class ProcessDO {
    /**
     * id

     */
    private Long id;

    /**
     * biz id from biz system
     */
    private String bizId;

    /**
     * definition name, see pikachu_d
     */
    private String definitionName;

    /**
     * definition version, see pikachu_d
     */
    private Integer definitionVersion;

    /**
     * sign to separate between different runtime or app
     */
    private String ownSign;

    /**
     * this process''s current state''s name
     */
    private String currentStateName;

    /**
     * this process''s current state
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

    public String getDefinitionName() {
        return definitionName;
    }

    public void setDefinitionName(String definitionName) {
        this.definitionName = definitionName == null ? null : definitionName.trim();
    }

    public Integer getDefinitionVersion() {
        return definitionVersion;
    }

    public void setDefinitionVersion(Integer definitionVersion) {
        this.definitionVersion = definitionVersion;
    }

    public String getOwnSign() {
        return ownSign;
    }

    public void setOwnSign(String ownSign) {
        this.ownSign = ownSign == null ? null : ownSign.trim();
    }

    public String getCurrentStateName() {
        return currentStateName;
    }

    public void setCurrentStateName(String currentStateName) {
        this.currentStateName = currentStateName == null ? null : currentStateName.trim();
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
}
