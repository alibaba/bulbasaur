package com.alibaba.pokemon.bulbasaur.task.dto;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.pokemon.bulbasaur.task.constant.TaskStatusEnum;

/**
 * User:  yunche.ch ... (ว ˙o˙)ง
 * Date: 17/3/14
 * Time: 下午9:32
 *
 * @author ch
 * @date 2017/03/14
 */
public class TaskQuery implements Serializable {
    private static final long serialVersionUID = 8667268124225266369L;

    private Long taskId;
    private Long userId;
    private String definitionName;
    private TaskStatusEnum taskStatusEnum;

    // 业务方自己存入的处理结果
    private String dealResult;

    private Date belowCreateTime;
    private Date aboveCreateTime;

    private int pageSize = 20;
    private int pageNo = 1;

    public String getDealResult() {
        return dealResult;
    }

    public void setDealResult(String dealResult) {
        this.dealResult = dealResult;
    }

    public String getDefinitionName() {
        return definitionName;
    }

    public void setDefinitionName(String definitionName) {
        this.definitionName = definitionName;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public Date getBelowCreateTime() {
        return belowCreateTime;
    }

    public void setBelowCreateTime(Date belowCreateTime) {
        this.belowCreateTime = belowCreateTime;
    }

    public Date getAboveCreateTime() {
        return aboveCreateTime;
    }

    public void setAboveCreateTime(Date aboveCreateTime) {
        this.aboveCreateTime = aboveCreateTime;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public TaskStatusEnum getTaskStatusEnum() {
        return taskStatusEnum;
    }

    public void setTaskStatusEnum(TaskStatusEnum taskStatusEnum) {
        this.taskStatusEnum = taskStatusEnum;
    }
}
