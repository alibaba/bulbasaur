package com.alibaba.pokemon.bulbasaur.persist.domain;

import java.util.Date;

/**
 *
 *
 * @author ch
 * @date 2017-09-27 17:51:28
 *
 */
public class DefinitionDO {
    /**
     * id
     */
    private Long id;

    /**
     * definition name
     */
    private String definitionName;

    /**
     * definition version
     */
    private Integer definitionVersion;

    /**
     * sign to separate between different runtime or app
     */
    private String ownSign;

    /**
     * status
     */
    private Boolean status;

    /**
     * gmt_create
     */
    private Date gmtCreate;

    /**
     * gmt_modified
     */
    private Date gmtModified;

    /**
     * 别名，主要维护中文名称
     */
    private String definitionAlias;

    /**
     * definition content
     */
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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

    public String getDefinitionAlias() {
        return definitionAlias;
    }

    public void setDefinitionAlias(String definitionAlias) {
        this.definitionAlias = definitionAlias == null ? null : definitionAlias.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}
