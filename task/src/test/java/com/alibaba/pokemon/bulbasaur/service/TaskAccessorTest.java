package com.alibaba.pokemon.bulbasaur.service;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.pokemon.bulbasaur.JUnit4ClassRunner;
import com.alibaba.pokemon.bulbasaur.persist.domain.InvokeResult;
import com.alibaba.pokemon.bulbasaur.persist.domain.QueryResult;
import com.alibaba.pokemon.bulbasaur.persist.domain.TaskDO;
import com.alibaba.pokemon.bulbasaur.schedule.ScheduleMachine;
import com.alibaba.pokemon.bulbasaur.schedule.ScheduleMachineFactory;
import com.alibaba.pokemon.bulbasaur.task.constant.TaskConstant;
import com.alibaba.pokemon.bulbasaur.task.constant.TaskStatusEnum;
import com.alibaba.pokemon.bulbasaur.task.dto.TaskQuery;
import com.alibaba.pokemon.bulbasaur.task.model.TaskBizInfo;
import com.alibaba.pokemon.bulbasaur.task.model.User;
import com.alibaba.pokemon.bulbasaur.task.service.TaskAccessor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;

/**
 * User:  yunche.ch ... (ว ˙o˙)ง
 * Date: 14-12-22
 * Time: 下午4:20
 */

@RunWith(JUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/taskMachineTest.xml"})
public class TaskAccessorTest {

    @Resource
    private TaskAccessor taskAccessor;
    @Autowired
    ScheduleMachineFactory scheduleMachineFactory;
    private AtomicLong count = new AtomicLong(0);

    public static final long userId = 00001L;
    public static final String userName = "测试人员";
    public static User user;
    public static final String DEFAULT_DEAL_RESULT = "ok";
    public static final String DEFAULT_MEMO = "我是测试，谢谢";

    static {
        user = new User();
        user.setId(userId);
        user.setName(userName);

    }

    private String getBizId() {
        return String.valueOf(System.currentTimeMillis()) + count.getAndAdd(1L);
    }

    @Test
    public void testSingleCreateUntilComplate() {
        String definationName = "singleTask";
        String bizId = getBizId();
        System.out.println("bizId:" + bizId);

        ScheduleMachine m = scheduleMachineFactory.newInstance(bizId, definationName);
        m.addContext("goto", 2);
        m.addContext("_i", 3);
        m.addContext(TaskConstant.BULBASAUR_INNER_CONTEXT_KEY_CREATOR_ID, 00001L);
        m.addContext(TaskConstant.BULBASAUR_INNER_CONTEXT_KEY_CREATOR_NAME, "测试人员创建的");

        TaskBizInfo<User> taskBizInfo = new TaskBizInfo(user);
        m.addContext(TaskConstant.BULBASAUR_INNER_CONTEXT_KEY_BIZ_INFO, taskBizInfo);
        m.run();

        TaskQuery taskQuery = new TaskQuery();
        taskQuery.setUserId(userId);
        taskQuery.setTaskStatusEnum(TaskStatusEnum.STATUS_CREATED);
        taskQuery.setDefinitionName(definationName);
        QueryResult<TaskDO> queryResult = taskAccessor.queryTasks(taskQuery);
        Collection<TaskDO> taskDOs = queryResult.getDataList();
        for (TaskDO taskDO : taskDOs) {

            User ext = JSON.parseObject(taskDO.getBizInfo(), User.class);

            Assert.assertEquals(ext, user);

            // 是否可以申领
            InvokeResult<Void> invokeResult = taskAccessor.hasTaskTakenPermission(taskDO.getId(), user);
            Assert.assertTrue(invokeResult != null && invokeResult.isSuccess());

            // 尝试直接结束
            invokeResult = taskAccessor.completeTask(taskDO.getId(), user, DEFAULT_DEAL_RESULT, DEFAULT_MEMO);
            Assert.assertTrue(!invokeResult.isSuccess());//false 就对的

            invokeResult = taskAccessor.takenTask(taskDO.getId(), user);
            Assert.assertTrue(invokeResult != null && invokeResult.isSuccess());
            invokeResult = taskAccessor.completeTask(taskDO.getId(), user, DEFAULT_DEAL_RESULT, DEFAULT_MEMO);
            Assert.assertTrue(invokeResult != null && invokeResult.isSuccess());

        }

    }

    @Test
    public void testMultipleCreateUntilComplete() {
        String definationName = "multipleTask";
        String bizId = getBizId();
        System.out.println("bizId:" + bizId);

        ScheduleMachine m = scheduleMachineFactory.newInstance(bizId, definationName);
        m.addContext("goto", 2);
        m.addContext("_i", 3);
        m.run();

        TaskQuery taskQuery = new TaskQuery();
        taskQuery.setTaskStatusEnum(TaskStatusEnum.STATUS_CREATED);
        taskQuery.setDefinitionName(definationName);
        QueryResult<TaskDO> queryResult = taskAccessor.queryTasksByUser(taskQuery, userId);
        Collection<TaskDO> taskDOs = queryResult.getDataList();
        Assert.assertTrue(taskDOs != null && !taskDOs.isEmpty());
        for (TaskDO taskDO : taskDOs) {

            // 是否可以申领
            InvokeResult<Void> invokeResult = taskAccessor.hasTaskTakenPermission(taskDO.getId(), user);
            Assert.assertTrue(invokeResult != null && invokeResult.isSuccess());

            // 尝试直接结束
            invokeResult = taskAccessor.completeTask(taskDO.getId(), user, DEFAULT_DEAL_RESULT, DEFAULT_MEMO);
            Assert.assertTrue(!invokeResult.isSuccess());//false 就对的

            invokeResult = taskAccessor.takenTask(taskDO.getId(), user);
            Assert.assertTrue(invokeResult != null && invokeResult.isSuccess());
            invokeResult = taskAccessor.completeTask(taskDO.getId(), user, DEFAULT_DEAL_RESULT, DEFAULT_MEMO);
            Assert.assertTrue(invokeResult != null && invokeResult.isSuccess());

        }

    }

    @Test
    public void testQueryTasks() throws Exception {

        TaskQuery taskQuery = new TaskQuery();
        taskQuery.setUserId(userId);
        taskQuery.setDealResult("ok1");
        QueryResult<TaskDO> taskDOQueryResult = taskAccessor.queryTasks(taskQuery);

        Assert.assertTrue(taskDOQueryResult != null
            && taskDOQueryResult.getDataList() != null
            && !taskDOQueryResult.getDataList().isEmpty());

    }

    @Test
    public void testQueryTasksByUser() throws Exception {

        Long userId = 00001L;
        String definationName = "multipleTask";
        TaskQuery taskQuery = new TaskQuery();
        taskQuery.setTaskStatusEnum(TaskStatusEnum.STATUS_CREATED);
        taskQuery.setDefinitionName(definationName);
        QueryResult<TaskDO> taskDOQueryResult = taskAccessor.queryTasksByUser(taskQuery, userId);

        Assert.assertTrue(taskDOQueryResult != null
            && taskDOQueryResult.getDataList() != null
            && !taskDOQueryResult.getDataList().isEmpty());

        System.out.println("=========" + taskDOQueryResult.getTotal());

    }
}
