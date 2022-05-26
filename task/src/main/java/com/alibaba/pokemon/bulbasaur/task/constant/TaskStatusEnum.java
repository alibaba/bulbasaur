package com.alibaba.pokemon.bulbasaur.task.constant;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * User:  yunche.ch ... (ว ˙o˙)ง
 * Date: 17/3/14
 * Time: 下午9:44
 *
 * @author ch
 * @date 2017/03/14
 */
public enum TaskStatusEnum {

    STATUS_CREATED_TAKEN_COMPLETE("created_taken_completed", "创建或者已申领或者已完成"),
    STATUS_CREATED("created", "创建"),
    STATUS_TIMEOUT_JOB("timeOutJob", "超时"),
    STATUS_ASSIGNED("assigned", "已被转交的"),
    STATUS_TAKEN("taken", "已申领"),
    STATUS_CANCEL("cancel", "取消"),
    STATUS_COMPLETED("completed", "已完成");

    public static List<TaskStatusEnum> VALID_STATUS_ENUM = Lists.newArrayList(STATUS_CREATED_TAKEN_COMPLETE
        , STATUS_CREATED
        , STATUS_TAKEN
        , STATUS_COMPLETED
    );
    public static List<String> VALID_STATUS_VALUE = Lists.newArrayList(
        STATUS_CREATED.getValue()
        , STATUS_TAKEN.getValue()
        , STATUS_COMPLETED.getValue()
    );

    private String value;
    private String desc;

    TaskStatusEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return this.value;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * @param value
     * @return
     */
    public static TaskStatusEnum queryByValue(String value) {
        TaskStatusEnum taskStatusEnum = null;
        if (null != value) {
            for (TaskStatusEnum tmp : TaskStatusEnum.values()) {
                if (tmp.value.equals(value)) {
                    taskStatusEnum = tmp;
                    break;
                }
            }
        }
        return taskStatusEnum;
    }
}
