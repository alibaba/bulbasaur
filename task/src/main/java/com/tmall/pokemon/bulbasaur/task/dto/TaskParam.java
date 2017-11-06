package com.tmall.pokemon.bulbasaur.task.dto;

import java.io.Serializable;
import java.util.Map;

import com.google.common.collect.Maps;
import com.tmall.pokemon.bulbasaur.task.model.User;

/**
 * User:  yunche.ch ... (ว ˙o˙)ง
 * Date: 17/3/14
 * Time: 下午9:23
 *
 * @author ch
 * @date 2017/03/14
 */
public class TaskParam implements Serializable {
    private static final long serialVersionUID = 4044447359959123083L;

    //任务Id
    private Long taskId;
    //任务结束后的分支选择
    private String outGoing;
    //操作的用户
    private User user;
    // 处理结果，业务方自己决定传入什么，比如 ok ，fail等。数据库单独字段，且有索引，支持查询
    private String dealResult;
    //处理意见或者备注说明
    private String memo;
    // 业务参数，可以放到流程执行的上下文中
    Map<String, Object> bizMap = Maps.newHashMap();

    public String getDealResult() {
        return dealResult;
    }

    public void setDealResult(String dealResult) {
        this.dealResult = dealResult;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getOutGoing() {
        return outGoing;
    }

    public void setOutGoing(String outGoing) {
        this.outGoing = outGoing;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Map<String, Object> getBizMap() {
        return bizMap;
    }

    public void setBizMap(Map<String, Object> bizMap) {
        this.bizMap = bizMap;
    }
}
