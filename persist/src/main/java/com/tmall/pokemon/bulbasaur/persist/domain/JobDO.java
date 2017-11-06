package com.tmall.pokemon.bulbasaur.persist.domain;

import java.util.Date;

/**
 * 
 *
 * @author ch
 * @date 2017-09-27 17:51:29
 *
 */
public class JobDO {
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
     * biz id from biz system
     */
    private String bizId;

    /**
     * definition name
     */
    private String definitionName;

    /**
     * state_name
     */
    private String stateName;

    /**
     * 任务类型，FailedRetry，TimeOut
     */
    private String eventType;

    /**
     * INIT，RUNNING，DONE，FAILED
     */
    private String status;

    /**
     * 重复类型
     */
    private String repetition;

    /**
     * ignore_weekend
     */
    private Boolean ignoreWeekend;

    /**
     * 处理策略
     */
    private String dealStrategy;

    /**
     * 重复测试
     */
    private Long repeatTimes;

    /**
     * task_id
     */
    private Long taskId;

    /**
     * tbschedule 计算因子
     */
    private Long modNum;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 异常栈
     */
    private String lastExceptionStack;

    /**
     * 超时跳转目标节点
     */
    private String outGoing;

    /**
     * own_sign
     */
    private String ownSign;

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

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName == null ? null : stateName.trim();
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType == null ? null : eventType.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getRepetition() {
        return repetition;
    }

    public void setRepetition(String repetition) {
        this.repetition = repetition == null ? null : repetition.trim();
    }

    public Boolean getIgnoreWeekend() {
        return ignoreWeekend;
    }

    public void setIgnoreWeekend(Boolean ignoreWeekend) {
        this.ignoreWeekend = ignoreWeekend;
    }

    public String getDealStrategy() {
        return dealStrategy;
    }

    public void setDealStrategy(String dealStrategy) {
        this.dealStrategy = dealStrategy == null ? null : dealStrategy.trim();
    }

    public Long getRepeatTimes() {
        return repeatTimes;
    }

    public void setRepeatTimes(Long repeatTimes) {
        this.repeatTimes = repeatTimes;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getModNum() {
        return modNum;
    }

    public void setModNum(Long modNum) {
        this.modNum = modNum;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getLastExceptionStack() {
        return lastExceptionStack;
    }

    public void setLastExceptionStack(String lastExceptionStack) {
        this.lastExceptionStack = lastExceptionStack == null ? null : lastExceptionStack.trim();
    }

    public String getOutGoing() {
        return outGoing;
    }

    public void setOutGoing(String outGoing) {
        this.outGoing = outGoing == null ? null : outGoing.trim();
    }

    public String getOwnSign() {
        return ownSign;
    }

    public void setOwnSign(String ownSign) {
        this.ownSign = ownSign == null ? null : ownSign.trim();
    }
}