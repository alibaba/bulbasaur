package com.alibaba.pokemon.bulbasaur.task.model;

import com.alibaba.pokemon.bulbasaur.core.CoreModule;
import com.alibaba.pokemon.bulbasaur.core.Result;
import com.alibaba.pokemon.bulbasaur.core.annotation.NeedDAOMeta;
import com.alibaba.pokemon.bulbasaur.core.annotation.StateMeta;
import com.alibaba.pokemon.bulbasaur.core.constants.XmlTagConstants;
import com.alibaba.pokemon.bulbasaur.core.invoke.Invokable;
import com.alibaba.pokemon.bulbasaur.core.invoke.InvokableFactory;
import com.alibaba.pokemon.bulbasaur.core.model.Event;
import com.alibaba.pokemon.bulbasaur.core.model.StateLike;
import com.alibaba.pokemon.bulbasaur.persist.domain.JobDO;
import com.alibaba.pokemon.bulbasaur.persist.domain.ParticipationDO;
import com.alibaba.pokemon.bulbasaur.persist.domain.TaskDO;
import com.alibaba.pokemon.bulbasaur.persist.domain.TaskDOExample;
import com.alibaba.pokemon.bulbasaur.persist.mapper.JobDOMapper;
import com.alibaba.pokemon.bulbasaur.persist.mapper.ParticipationDOMapper;
import com.alibaba.pokemon.bulbasaur.persist.mapper.TaskDOMapper;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.alibaba.pokemon.bulbasaur.schedule.process.JobHelper;
import com.alibaba.pokemon.bulbasaur.task.constant.ParticipationConstant;
import com.alibaba.pokemon.bulbasaur.task.constant.TaskConstant;
import com.alibaba.pokemon.bulbasaur.task.constant.TaskStatusEnum;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * 任务节点
 * 1. 在prepare中，处理任务类型
 * 2. 处理超时
 * 3. 上下文放入 taskids ，用逗号分割
 * <p/>
 * User: yunche.ch - - - - (ว ˙o˙)ง
 * Date: 13-11-6
 * Time: 下午4:56
 */
@StateMeta(t = "task")
@NeedDAOMeta(need = true)
public class Task extends Event {
    private final static Logger logger = LoggerFactory.getLogger(Task.class);

    private final static int MAX_USER = 100;
    //单个或多个人审批
    private String candidateUsers;
    //会签
    private Boolean countersignature;
    //获取审批人
    private Invokable assignmentHandler;
    private Invokable timeoutHandler;
    //易于扩展
    private Map<String, Object> dAOMap;

    @Override
    public Result prepare(Map<String, Object> context) {
        Result result = new Result();
        logger.info("<========= Task preInvoke  start [task:" + this.getStateName() + "] =========>");

        Object userListObj = null;
        List<User> userList = Lists.newArrayList();

        //标签设置审批人
        if (StringUtils.isNotBlank(candidateUsers)) {
            userListObj = candidateUsers;
        }

        // assignmentHandler 外部业务获取审批人，优先级高于 candidateUsers
        if (assignmentHandler != null) {
            // 调用方法，异步在invoke父类里面做，非异步在子类里面做
            userListObj = assignmentHandler.invoke(context);
        }

        if (userListObj == null) {
            logger.warn("未设置审批人，直接继续执行操作");
            result.setContinue(true);
            return result;
        }
        // 处理审批人
        try {
            Map<String, String> userMap = Splitter.on(",").withKeyValueSeparator(":").split(userListObj.toString());

            for (Map.Entry<String, String> tmp : userMap.entrySet()) {
                User user = new User();
                user.setId(Long.valueOf(tmp.getKey()));
                user.setName(tmp.getValue());
                userList.add(user);
            }
        } catch (Exception e) {
            throw new RuntimeException("不符合规则的user！userListObj = [" + userListObj + "]");
        }

        if (userList.size() > MAX_USER) {
            logger.warn(String.format("above %s  candidateUsers for task: %s", MAX_USER, this.getStateName()));
            throw new RuntimeException("above 100  candidateUsers for task:" + this.getStateName());
        }

        // 开始持久化task
        TaskDOMapper taskDOMapper = (TaskDOMapper)dAOMap.get("taskDOMapper");
        JobDOMapper jobDOMapper = (JobDOMapper)dAOMap.get("jobDOMapper");

        List<TaskDO> tasks = new ArrayList<TaskDO>();
        TaskDO taskDO = new TaskDO();
        taskDO.setBizId(this.getBizId());
        taskDO.setDefinitionName(this.getDefinitionName());
        taskDO.setName(getStateName());
        taskDO.setStatus(TaskStatusEnum.STATUS_CREATED.getValue());
        taskDO.setGmtCreate(new Date());
        taskDO.setGmtModified(new Date());

        // 从上下文中拿到任务的业务参数
        if (context.get(TaskConstant.BULBASAUR_INNER_CONTEXT_KEY_BIZ_INFO) != null) {
            TaskBizInfo taskBizInfo = (TaskBizInfo)context.get(TaskConstant.BULBASAUR_INNER_CONTEXT_KEY_BIZ_INFO);
            taskDO.setBizInfo(JSON.toJSONString(taskBizInfo.getT()));
        }

        if (userList.size() == 1) {
            // 如果只有一个用户 则忽略会签 持久化为单用户任务
            taskDO.setType(TaskConstant.TASK_TYPE_SINGLE_USER);
            User user = userList.get(0);
            doWithCreator(taskDO, context);
            taskDO.setUserId(user.getId());
            taskDO.setUserName(user.getName());
            //insert DB，并放入返回taskId
            taskDOMapper.insert(taskDO);
            tasks.add(taskDO);
        } else {

            if (countersignature != null && countersignature) {
                // 如果是会签 为每个用户持久化一个任务
                for (User forkUser : userList) {
                    TaskDO forkTask = new TaskDO();
                    BeanUtils.copyProperties(taskDO, forkTask);
                    forkTask.setType(TaskConstant.TASK_TYPE_COUNTERSIGNATURE);
                    forkTask.setUserId(forkUser.getId());
                    forkTask.setUserName(forkUser.getName());
                    doWithCreator(forkTask, context);
                    //insert DB，并放入返回taskId
                    taskDOMapper.insert(forkTask);
                    tasks.add(forkTask);
                }
            } else {
                ParticipationDOMapper participationDOMapper = (ParticipationDOMapper)dAOMap.get(
                    "participationDOMapper");

                // 不是会签 持久化一个任务并持久化 participation
                taskDO.setType(TaskConstant.TASK_TYPE_MULTI_USER);
                doWithCreator(taskDO, context);
                taskDOMapper.insert(taskDO);
                tasks.add(taskDO);
                for (User partUser : userList) {
                    ParticipationDO participation = new ParticipationDO();

                    participation.setTaskId(taskDO.getId());
                    participation.setGmtCreate(new Date());
                    participation.setGmtModified(new Date());
                    // ???TODO
                    participation.setStatus(true);
                    participation.setType(ParticipationConstant.TYPE_ORI_USER);
                    participation.setUserId(partUser.getId());
                    participation.setUserName(partUser.getName());
                    participation.setDefinitionName(this.getDefinitionName());
                    participationDOMapper.insert(participation);
                }
            }

        } //else

        // 处理超时 , 组装taskids
        StringBuilder taskIds = new StringBuilder();
        for (TaskDO mTask : tasks) {
            if (timeoutHandler != null) {
                addTaskTimeoutJob(mTask, context, jobDOMapper, taskDOMapper);
            }

            taskIds.append(mTask.getId()).append(",");
        }
        //在上下文中放入taskids，业务方可以获取，并处理。
        //注意！不会当成必要上下文存储!
        context.put(TaskConstant.BULBASAUR_INNER_CONTEXT_KEY_TASK_IDS, taskIds.toString());

        logger.info("<========= taskPre execute end task:" + getStateName() + "] =========>");
        //执行pre-invokes
        super.prepare(context);
        result.setContinue(false);
        return result;
    }

    private void doWithCreator(TaskDO taskDO, Map<String, Object> context) {
        if (taskDO == null || context == null) {
            return;
        }

        if (context.get(TaskConstant.BULBASAUR_INNER_CONTEXT_KEY_CREATOR_ID) != null) {
            try {
                // string or long or int
                taskDO.setCreatorId(
                    Long.valueOf(context.get(TaskConstant.BULBASAUR_INNER_CONTEXT_KEY_CREATOR_ID).toString()));
            } catch (Exception e) {
                logger.error(String.format("上下文中creatorId = %s 有误! 完整上下文为 = %s",
                    context.get(TaskConstant.BULBASAUR_INNER_CONTEXT_KEY_CREATOR_ID), context));
            }
        }

        if (context.get(TaskConstant.BULBASAUR_INNER_CONTEXT_KEY_CREATOR_NAME) != null) {
            taskDO.setCreatorName(context.get(TaskConstant.BULBASAUR_INNER_CONTEXT_KEY_CREATOR_NAME).toString());
        }

    }

    @Override
    public Result execute(Map<String, Object> context) {

        TaskDOMapper taskDOMapper = (TaskDOMapper)dAOMap.get("taskDOMapper");
        TaskDOExample taskDOExample = new TaskDOExample();
        taskDOExample.createCriteria().andNameEqualTo(this.getStateName()).andBizIdEqualTo(this.getBizId());

        List<TaskDO> queryResultList = taskDOMapper.selectByExample(taskDOExample);

        Preconditions.checkArgument(queryResultList != null && !queryResultList.isEmpty(),
            String.format("未查到相应任务！bizId = [%s],name = [%s]", this.getBizId(), this.getStateName()));

        TaskDO validTaskDO = null;
        if (countersignature != null && countersignature) {
            //TODO 会签
        } else {

            for (TaskDO taskDO : queryResultList) {

                if (taskDO.getAssignUserId() != null) {
                    continue;
                }

                logger.warn(String.format("bizid = [%s] ,task id = [%s],task type = [%s]"
                    , taskDO.getBizId(), taskDO.getId(), taskDO.getType()));
                //有脏数据，会以最后一次为准
                validTaskDO = taskDO;
            }

        }

        if (StringUtils.isBlank(validTaskDO.getStatus())) {
            logger.warn("任务未审批！不允许继续执行");
            Result result = new Result();
            result.setContinue(false);
            return result;
        }

        //FIXME 用到提供的接口里面

        //        if (StringUtils.isNotBlank(queryResult.getStatus()) && TaskConstant.STATUS_CREATED.equals
        // (queryResult.getStatus())) {
        //            logger.error("任务还没有被任何人或者超时任务处理过，不允许继续执行！bizId = [" + this.getBizId() + "],name = [" + this
        // .getStateName() + "]");
        //            Result result = new Result();
        //            result.setContinue(false);
        //            return result;
        //
        //        }
        //        String oldStatus = validTaskDO.getStatus();
        //        validTaskDO.setStatus(TaskConstant.STATUS_COMPLETED);
        //        queryResult.setEndTime(new Date());
        //        long duration = now.getTime() - queryResult.getGmtCreate().getTime();
        //        queryResult.setDuration(duration);
        //
        //
        //        // 乐观锁方式修改task状态 拦截并发
        //        int row = taskDAO.updateByStatusAndUser(queryResult, oldStatus);
        //        if (row == 0) {
        //            // 发生并发 抛出错误以便回滚事务
        //            throw new RuntimeException("CompleteTask时出现并发请求，丢弃一次 - bizId: " + query.getBizId() + " | name
        // :" + query.getName());
        //        }

        return super.execute(context);

    }

    /**
     * 超时的任务处理，task状态为 TaskConstant.STATUS_TIMEOUT_JOB
     *
     * @param mTask void
     * @since 2013-11-8 下午09:10:37
     */
    private void addTaskTimeoutJob(TaskDO mTask, Map<String, Object> context, JobDOMapper jobDOMapper,
                                   TaskDOMapper taskDOMapper) {
        // timeout类型存job表
        JobDO jobDO = new JobDO();
        jobDO.setBizId(mTask.getBizId());
        jobDO.setStateName(mTask.getName());
        jobDO.setGmtCreate(new Date());
        jobDO.setGmtModified(new Date());
        jobDO.setEndTime(new Date());

        jobDO.setStatus("INIT");
        jobDO.setModNum(JobHelper.BKDRHash(mTask.getBizId()));
        jobDO.setEventType(TaskConstant.TASK_JOB_TYPE);
        jobDO.setTaskId(mTask.getId());
        jobDO.setOwnSign(CoreModule.getInstance().getOwnSign());
        // 调用方法，异步在invoke父类里面做，非异步在子类里面做
        Object timeoutStrategy = timeoutHandler.invoke(context);
        calcTimeoutStrategy(timeoutStrategy, jobDO);
        //超时情况
        TaskDO taskDO = new TaskDO();
        taskDO.setId(mTask.getId());
        String oldStatus = mTask.getStatus();
        taskDO.setStatus(TaskStatusEnum.STATUS_TIMEOUT_JOB.getValue());

        TaskDOExample taskDOExample = new TaskDOExample();
        taskDOExample.createCriteria().andStatusEqualTo(oldStatus);
        taskDOMapper.updateByExampleSelective(taskDO, taskDOExample);

        jobDOMapper.insert(jobDO);

    }

    private void calcTimeoutStrategy(Object timeoutStrategy, JobDO jobDO) {
        Preconditions.checkArgument(timeoutStrategy != null, "获取超时配置信息失败！");
        Preconditions.checkArgument(timeoutStrategy instanceof Map, "返回超时配置格式不正确！");

        Map<String, Object> timeoutStrategyMap = (Map<String, Object>)timeoutStrategy;

        Preconditions.checkArgument(timeoutStrategyMap.get("outGoing") != null, "outGoing 必须配置");
        Preconditions.checkArgument(timeoutStrategyMap.get("period") != null, "period不能为空 ");
        // endTime  优先
        //        if (timeoutStrategyMap.get("endTime") != null) {
        //            try {
        //                DateTime dateTime = new DateTime(timeoutStrategyMap.get("endTime"));
        //                jobDO.setEndTime(dateTime.toDate());
        //            } catch (Exception e) {
        //                throw new RuntimeException("时间配置endtime格式有误");
        //            }
        //        } else {
        //
        //            Preconditions.checkArgument(timeoutStrategyMap.get("period") != null, "endTime , period 不能都为空 ");
        //            String meet = (String) timeoutStrategyMap.get("period");
        //
        //            // 识别 s , m , d
        //            DateTime dateTime = null;
        //            try {
        //                if (CharMatcher.anyOf(meet).matchesAllOf("s")) {
        //                    dateTime = new DateTime().plusSeconds(Integer.valueOf(CharMatcher.DIGIT.retainFrom
        // (meet)));
        //                } else if (CharMatcher.anyOf(meet).matchesAllOf("m")) {
        //                    dateTime = new DateTime().plusMinutes(Integer.valueOf(CharMatcher.DIGIT.retainFrom
        // (meet)));
        //                } else if (CharMatcher.anyOf(meet).matchesAllOf("d")) {
        //                    dateTime = new DateTime().plusDays(Integer.valueOf(CharMatcher.DIGIT.retainFrom(meet)));
        //                } else {
        //                    throw new RuntimeException("不支持的时间格式类型");
        //                }
        //            } catch (Exception e) {
        //                throw new RuntimeException("不支持的时间格式类型");
        //            }
        //
        //            jobDO.setEndTime(dateTime.toDate());
        //        }
        String meet = String.valueOf(timeoutStrategyMap.get(XmlTagConstants.PERIOD_TAG));
        Preconditions.checkArgument(
            CharMatcher.anyOf(meet).matchesAllOf(XmlTagConstants.MINUTE_TAG)
                | CharMatcher.anyOf(meet).matchesAllOf(XmlTagConstants.HOUR_TAG)
                | CharMatcher.anyOf(meet).matchesAllOf(XmlTagConstants.DAY_TAG), "period 必须指定 minute or hour or day ");
        jobDO.setRepetition(meet);

        //必须指定jumpTo，否则引擎不知道超时后如何处理！！
        jobDO.setOutGoing(String.valueOf(timeoutStrategyMap.get(XmlTagConstants.OUTGOING_TAG)));

        if (timeoutStrategyMap.get(XmlTagConstants.IGNOREWEEKEND_TAG) != null) {
            Boolean ignoreWeekend = (Boolean)timeoutStrategyMap.get(XmlTagConstants.IGNOREWEEKEND_TAG);
            jobDO.setIgnoreWeekend(ignoreWeekend);
        }

    }

    @Override
    public StateLike parse(Element elem) {
        super.parse(elem);

        if (elem.attributeValue(XmlTagConstants.CANDIDATE_USERS_TAG) != null) {
            candidateUsers = elem.attributeValue(XmlTagConstants.CANDIDATE_USERS_TAG);
        }
        //        if (elem.attributeValue("noAssigneeTo") != null)
        //            noAssigneeTo = elem.attributeValue("noAssigneeTo");
        if (elem.attributeValue(XmlTagConstants.COUNTERSIGNATURE_TAG) != null) {
            countersignature = Boolean.valueOf(elem.attributeValue(XmlTagConstants.COUNTERSIGNATURE_TAG));
        }

        Element ah = (Element)elem.selectSingleNode(XmlTagConstants.ASSIGNMENT_HANDLER_TAG);
        if (ah != null) {
            List<Element> scList = ah.elements();
            if (scList != null) {
                try {
                    assignmentHandler = InvokableFactory.newInstance(scList.get(0).getName(), scList.get(0));
                } catch (RuntimeException re) {
                    logger.error(String.format("实例 %s 时候出错，类型为：%s , 异常为： %s"
                        , XmlTagConstants.ASSIGNMENT_HANDLER_TAG
                        , scList.get(0).getName()
                        , re.toString()));
                    throw re;
                } catch (Throwable e) {
                    logger.error(String.format("实例 %s 时候出错，类型为：%s , 异常为： %s"
                        , XmlTagConstants.ASSIGNMENT_HANDLER_TAG
                        , scList.get(0).getName()
                        , e.toString()));
                    throw new UndeclaredThrowableException(e,
                        "error happened when newInstance Invokable class:" + scList.get(0).getName());
                }
            }
        }

        Element to = (Element)elem.selectSingleNode(XmlTagConstants.TIMEOUT_HANDLER_TAG);
        if (to != null) {
            List<Element> toList = to.elements();
            if (toList != null) {
                try {
                    timeoutHandler = InvokableFactory.newInstance(toList.get(0).getName(), toList.get(0));
                } catch (RuntimeException re) {
                    logger.error(String.format("实例 %s 出错，类型为：%s , 异常为： %s"
                        , XmlTagConstants.TIMEOUT_HANDLER_TAG
                        , toList.get(0).getName()
                        , re.toString()));
                    throw re;
                } catch (Throwable e) {
                    logger.error(String.format("实例 %s 出错，类型为：%s , 异常为： %s"
                        , XmlTagConstants.TIMEOUT_HANDLER_TAG
                        , toList.get(0).getName()
                        , e.toString()));
                    throw new UndeclaredThrowableException(e,
                        "error happened when newInstance Invokable class:" + toList.get(0).getName());
                }
            }
        }

        return this;
    }

    @Override
    public void setDAOMap(Map<String, ?> map) {
        this.dAOMap = (Map<String, Object>)map;
    }

}
