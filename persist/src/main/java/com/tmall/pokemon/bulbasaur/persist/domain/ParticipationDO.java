package com.tmall.pokemon.bulbasaur.persist.domain;

import java.util.Date;

/**
 * 
 *
 * @author ch
 * @date 2017-09-27 17:51:29
 *
 */
public class ParticipationDO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 审批人id
     */
    private Long userId;

    /**
     * 审批人名称
     */
    private String userName;

    /**
     * 暂时无用。\n参与类型（任务参与人的类型）\nAgentUser：代理人\nOriUser：本人
     */
    private String type;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 是否有效。1有效，0无效
     */
    private Boolean status;

    /**
     * 冗余
     */
    private String definitionName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getDefinitionName() {
        return definitionName;
    }

    public void setDefinitionName(String definitionName) {
        this.definitionName = definitionName == null ? null : definitionName.trim();
    }
}