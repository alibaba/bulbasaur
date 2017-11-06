package com.tmall.pokemon.bulbasaur.task.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.tmall.pokemon.bulbasaur.persist.PersistMachine;
import com.tmall.pokemon.bulbasaur.persist.PersistMachineFactory;
import com.tmall.pokemon.bulbasaur.persist.domain.*;
import com.tmall.pokemon.bulbasaur.persist.domain.TaskDOExample.Criteria;
import com.tmall.pokemon.bulbasaur.persist.mapper.ParticipationDOMapper;
import com.tmall.pokemon.bulbasaur.persist.mapper.TaskDOMapper;
import com.tmall.pokemon.bulbasaur.task.constant.TaskConstant;
import com.tmall.pokemon.bulbasaur.task.constant.TaskStatusEnum;
import com.tmall.pokemon.bulbasaur.task.dto.TaskParam;
import com.tmall.pokemon.bulbasaur.task.dto.TaskQuery;
import com.tmall.pokemon.bulbasaur.task.exception.TaskException;
import com.tmall.pokemon.bulbasaur.task.model.User;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * User:  yunche.ch ... (ว ˙o˙)ง
 * Date: 14-12-22
 * Time: 下午2:17
 */
@Component("taskAccessor")
public class TaskAccessorImpl implements TaskAccessor {
    private static Logger logger = LoggerFactory.getLogger(TaskAccessorImpl.class);
    private static final int DEFAULT_PARTICIPATIN_SIZE = 1000;
    @Resource
    private TaskDOMapper taskDOMapper;
    @Resource
    private ParticipationDOMapper participationDOMapper;
    @Resource
    private PersistMachineFactory persistMachineFactory;

    private ExecutorService excecutorService;

    @PostConstruct
    public void init() {
        excecutorService = Executors.newFixedThreadPool(5);
    }

    private static ThreadLocal TL_DATE_FORMAT = new ThreadLocal() {
        @Override
        protected synchronized Object initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    public InvokeResult<Void> hasTaskTakenPermission(TaskDO taskDO, User currentUser) throws TaskException {

        InvokeResult<Void> result = new InvokeResult<Void>();

        Preconditions.checkArgument(taskDO != null, "taskDO 不能为 null");
        Preconditions.checkArgument(currentUser != null && currentUser.getId() != null,
            "currentUser 不能为 null ，并且currentUser.getId 不能为null!");

        if (TaskStatusEnum.STATUS_ASSIGNED.getValue().equals(taskDO.getStatus())) {
            result.setSuccess(false);
            result.setInfo("任务已经被转交");
        }
        if (TaskStatusEnum.STATUS_TAKEN.getValue().equals(taskDO.getStatus())) {
            result.setSuccess(false);
            result.setInfo("任务已经被申领");
        }
        if (TaskStatusEnum.STATUS_COMPLETED.getValue().equals(taskDO.getStatus())) {
            result.setSuccess(false);
            result.setInfo("任务已经完成");
        }

        //单人任务
        if (TaskConstant.TASK_TYPE_SINGLE_USER.equals(taskDO.getType())) {
            if (!taskDO.getUserId().equals(currentUser.getId())) {
                result.setSuccess(false);
                result.setInfo("传入用户无申领权限");
            }

            result.setSuccess(true);
        } else if (TaskConstant.TASK_TYPE_MULTI_USER.equals(taskDO.getType())) {
            //多人任务
            ParticipationDOExample participationDOExample = new ParticipationDOExample();
            participationDOExample.createCriteria().andTaskIdEqualTo(taskDO.getId());

            List<ParticipationDO> list = participationDOMapper.selectByExample(participationDOExample);

            if (list == null) {
                throw new TaskException("多人任务，未查到参与者记录！");
            }

            for (ParticipationDO participationDO : list) {
                if (participationDO.getUserId().equals(currentUser.getId())) {
                    result.setSuccess(true);
                }

            }

        } else {
            //            TODO   会签处理
            //出错
            throw new TaskException("任务类型出错！taskId = " + taskDO.getId());
        }

        return result;

    }

    /**
     * 1. 校验 taskId 不为空
     * 2. 校验 currentUser , currentUser.getId() 不为空
     * 3. 校验 由taskId 能查到 taskDO
     *
     * @param taskId
     * @param currentUser
     * @return
     */
    private TaskDO checkTaskExist(Long taskId, User currentUser) {
        Preconditions.checkArgument(taskId != null, "taskId 不能为null");
        Preconditions.checkArgument(currentUser != null && currentUser.getId() != null,
            "currentUser 不能为 null ，并且currentUser.getId 不能为null!");
        TaskDO queryResult = taskDOMapper.selectByPrimaryKey(taskId);
        if (queryResult == null) {
            throw new TaskException("taskId 未查询到记录！");
        }
        return queryResult;

    }

    @Override
    public InvokeResult<Void> hasTaskTakenPermission(Long taskId, User currentUser) throws TaskException {
        TaskDO queryResult = checkTaskExist(taskId, currentUser);
        return hasTaskTakenPermission(queryResult, currentUser);
    }

    @Override
    @Transactional
    public InvokeResult<Void> takenTask(Long taskId, User currentUser) throws TaskException {
        InvokeResult<Void> result = new InvokeResult();
        TaskDO queryResult = checkTaskExist(taskId, currentUser);
        if (!this.hasTaskTakenPermission(queryResult, currentUser).isSuccess()) {
            logger.warn(String.format("用户无权限申领该任务taskId = [%s],userId = [%s]", taskId, currentUser.getId()));
            result.setInfo("用户无权限申领该任务");
            result.setException(new TaskException(new Exception(
                "用户无权限申领该任务")));
            result.setSuccess(false);
            return result;
        }

        queryResult.setStatus(TaskStatusEnum.STATUS_TAKEN.getValue()); //申领
        queryResult.setUserId(currentUser.getId());
        queryResult.setUserName(currentUser.getName());
        //这边传入的是oldStatus

        TaskDOExample taskDOExample = new TaskDOExample();
        taskDOExample.createCriteria().andStatusEqualTo(TaskStatusEnum.STATUS_CREATED.getValue()).andIdEqualTo(
            queryResult.getId());
        taskDOMapper.updateByExampleSelective(queryResult, taskDOExample);

        // 多人单任务，处理 participation ,申领后，就变成了单人单任务
        if (TaskConstant.TASK_TYPE_MULTI_USER.equals(queryResult.getType())) {
            ParticipationDOExample participationDOExample = new ParticipationDOExample();
            participationDOExample.createCriteria().andTaskIdEqualTo(queryResult.getId())
                .andStatusEqualTo(TaskConstant.PARTICIPATION_VALID);

            List<ParticipationDO> participationDOs = participationDOMapper.selectByExample(participationDOExample);

            //全部更新掉
            for (ParticipationDO forUpdate : participationDOs) {
                forUpdate.setStatus(TaskConstant.PARTICIPATION_INVALID);
                //FIXME 处理update 失败这种异常？
                forUpdate.setGmtModified(new Date());
                participationDOMapper.updateByPrimaryKey(forUpdate);
            }

        }
        result.setSuccess(true);
        return result;
    }

    @Override
    public InvokeResult<Void> releaseTask(Long taskId, User currentUser) throws TaskException {
        InvokeResult<Void> result = new InvokeResult<Void>();
        TaskDO queryResult = checkTaskExist(taskId, currentUser);
        InvokeResult<Void> checkDealResult = hasTaskDealPermission(queryResult, currentUser);

        if (!checkDealResult.isSuccess()) {
            result.setSuccess(false);
            result.setInfo(checkDealResult.getInfo());
            return result;

        }
        //释放，task要先处理。 status 改为 created
        queryResult.setStatus(TaskStatusEnum.STATUS_CREATED.getValue());
        if (TaskConstant.TASK_TYPE_SINGLE_USER.equals(queryResult.getType())) {
            queryResult.setUserId(null);
            queryResult.setUserName(null);
            //这边传入的是oldStatus
            TaskDOExample taskDOExample = new TaskDOExample();
            taskDOExample.createCriteria().andStatusEqualTo(TaskStatusEnum.STATUS_TAKEN.getValue()).andIdEqualTo(
                queryResult.getId());
            taskDOMapper.updateByExample(queryResult, taskDOExample);

        } else if (TaskConstant.TASK_TYPE_MULTI_USER.equals(queryResult.getType())) {

            TaskDOExample taskDOExample = new TaskDOExample();
            taskDOExample.createCriteria().andStatusEqualTo(TaskStatusEnum.STATUS_TAKEN.getValue()).andIdEqualTo(
                queryResult.getId());
            taskDOMapper.updateByExample(queryResult, taskDOExample);

            ParticipationDOExample participationDOExample = new ParticipationDOExample();
            participationDOExample.createCriteria().andTaskIdEqualTo(queryResult.getId())
                .andStatusEqualTo(TaskConstant.PARTICIPATION_VALID);

            List<ParticipationDO> participationDOs = participationDOMapper.selectByExample(participationDOExample);

            //全部更新掉
            for (ParticipationDO forUpdate : participationDOs) {
                forUpdate.setStatus(TaskConstant.PARTICIPATION_VALID);
                //FIXME 处理update 失败这种异常？
                participationDOMapper.updateByPrimaryKey(forUpdate);
            }

        }

        return result;
    }

    @Override
    public InvokeResult<Void> hasTaskDealPermission(TaskDO taskDO, User currentUser) throws TaskException {

        Preconditions.checkArgument(taskDO != null, "taskDO 不允许为空");
        Preconditions.checkArgument(currentUser != null && currentUser.getId() != null, "currentUser 不允许为空");

        InvokeResult<Void> result = new InvokeResult<Void>();

        if (TaskStatusEnum.STATUS_ASSIGNED.getValue().equals(taskDO.getStatus())) {
            result.setSuccess(false);
            result.setInfo("任务已经被转交");
        }
        if (TaskStatusEnum.STATUS_CREATED.getValue().equals(taskDO.getStatus())) {
            result.setSuccess(false);
            result.setInfo("任务未被申领");
        }
        if (TaskStatusEnum.STATUS_COMPLETED.getValue().equals(taskDO.getStatus())) {
            result.setSuccess(false);
            result.setInfo("任务已经完成");
        }

        if (taskDO.getUserId() == null || !taskDO.getUserId().equals(currentUser.getId())) {
            result.setSuccess(false);
            result.setInfo("无权处理任务");
        }
        return result;

    }

    @Override
    public InvokeResult<Void> completeTask(Long taskId, User user, String dealResult, String memo)
        throws TaskException {
        TaskParam taskParam = new TaskParam();
        taskParam.setTaskId(taskId);
        taskParam.setUser(user);
        taskParam.setDealResult(dealResult);
        taskParam.setMemo(memo);
        return completeTask(taskParam);
    }

    @Override
    @Transactional
    public InvokeResult<Void> completeTask(TaskParam taskParam) throws TaskException {
        InvokeResult<Void> result = new InvokeResult();

        Preconditions.checkArgument(taskParam.getTaskId() != null, "taskId 不能为 null ");
        Preconditions.checkArgument(taskParam.getUser() != null && taskParam.getUser().getId() != null,
            "user 不能为 null ，user.getId 不能为null!");

        TaskDO queryResult = taskDOMapper.selectByPrimaryKey(taskParam.getTaskId());

        Preconditions.checkArgument(queryResult != null, "taskId 未查到记录");

        InvokeResult<Void> dealPermisssionResult = this.hasTaskDealPermission(queryResult, taskParam.getUser());
        if (!dealPermisssionResult.isSuccess()) {
            logger.warn(String
                .format("用户无权限审批该任务taskId = [%s],userId = [%s]", taskParam.getTaskId(), taskParam.getUser().getId()));
            result.setInfo(dealPermisssionResult.getInfo());
            result.setSuccess(false);
            return result;
        }

        queryResult.setDealResult(taskParam.getDealResult());
        queryResult.setMemo(taskParam.getMemo());
        queryResult.setEndTime(new Date());
        queryResult.setStatus(TaskStatusEnum.STATUS_COMPLETED.getValue());
        queryResult.setUserId(taskParam.getUser().getId());
        queryResult.setUserName(taskParam.getUser().getName());
        queryResult.setGmtModified(new Date());
        //这边传入的是oldStatus

        TaskDOExample taskDOExample = new TaskDOExample();
        taskDOExample.createCriteria().andIdEqualTo(queryResult.getId()).andStatusEqualTo(
            TaskStatusEnum.STATUS_TAKEN.getValue());
        int updateResult = taskDOMapper.updateByExampleSelective(queryResult, taskDOExample);
        Preconditions.checkArgument(updateResult > 0, "更新task表失败");

        //FIXME 是 同步run ? 异步run?
        PersistMachine persistMachine = persistMachineFactory.newInstance(queryResult.getBizId());
        if (taskParam.getBizMap() != null && !taskParam.getBizMap().isEmpty()) {
            persistMachine.addContext(taskParam.getBizMap());
        }

        persistMachine.run(queryResult.getName(),
            StringUtils.isNotBlank(taskParam.getOutGoing()) ? taskParam.getOutGoing() : null);
        result.setSuccess(true);
        result.setInfo("审批成功!");

        return result;
    }

    @Override
    public InvokeResult<Void> cancelTask(Long taskId, String outGoing, User user, String memo, String bizKey,
                                         Object bizValue) throws TaskException {
        InvokeResult<Void> result = new InvokeResult();

        Preconditions.checkArgument(taskId != null, "taskId 不能为 null ");
        Preconditions.checkArgument(StringUtils.isNotBlank(outGoing), "outGoing 不能为空 ");
        Preconditions.checkArgument(user != null && user.getId() != null, "user 不能为 null ，user.getId 不能为null!");

        TaskDO queryResult = taskDOMapper.selectByPrimaryKey(taskId);
        String oldStatus = queryResult.getStatus();

        Preconditions.checkArgument(queryResult != null, "taskId 未查到记录");

        queryResult.setMemo(memo);
        queryResult.setEndTime(new Date());
        queryResult.setStatus(TaskStatusEnum.STATUS_CANCEL.getValue());
        queryResult.setUserId(user.getId());
        queryResult.setUserName(user.getName());
        queryResult.setGmtModified(new Date());
        //这边传入的是oldStatus

        TaskDOExample taskDOExample = new TaskDOExample();
        taskDOExample.createCriteria().andStatusEqualTo(oldStatus);
        taskDOMapper.updateByExampleSelective(queryResult, taskDOExample);

        //FIXME 是 同步run ? 异步run?
        PersistMachine persistMachine = persistMachineFactory.newInstance(queryResult.getBizId());
        if (StringUtils.isNotBlank(bizKey) && bizValue != null) {
            persistMachine.addContext(bizKey, bizValue);
        }
        persistMachine.run(queryResult.getName(), outGoing);
        result.setSuccess(true);
        result.setInfo("审批取消成功!");

        return result;
    }

    @Override
    public InvokeResult<Void> assignTaskWithResult(Long taskId, User currentUser, User assignUser, String memo) {
        InvokeResult<Void> result = new InvokeResult<Void>();

        Preconditions.checkArgument(taskId != null, "taskId 不能为 null ");
        TaskDO queryResult = taskDOMapper.selectByPrimaryKey(taskId);

        Preconditions.checkArgument(queryResult != null, "taskId 未查到记录");

        Preconditions.checkArgument(TaskConstant.TASK_TYPE_SINGLE_USER.equals(queryResult.getType()), "只能转交单人单任务");

        if (!hasTaskDealPermission(queryResult, currentUser).isSuccess()) {
            logger.error(String.format("用户无权限转交该任务taskId = [%s],userId = [%s]", taskId, currentUser.getId()));
            result.setInfo("用户无权转交该任务");
            result.setSuccess(false);
        }

        queryResult.setStatus(TaskStatusEnum.STATUS_ASSIGNED.getValue());
        queryResult.setAssignUserId(assignUser.getId());
        queryResult.setAssignUserName(assignUser.getName());
        queryResult.setAssignTime(new Date());

        taskDOMapper.updateByPrimaryKey(queryResult);

        //insert new Task
        queryResult.setId(null);
        queryResult.setAssignUserId(null);
        queryResult.setAssignUserName(null);
        queryResult.setStatus(TaskStatusEnum.STATUS_CREATED.getValue());
        queryResult.setUserId(assignUser.getId());
        queryResult.setUserName(assignUser.getName());
        queryResult.setType(TaskConstant.TASK_TYPE_SINGLE_USER);

        int insertResult = taskDOMapper.insert(queryResult);

        if (insertResult > 0) {
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
        }
        return result;
    }

    @Override
    public TaskDO queryTask(Long taskId) {
        Preconditions.checkArgument(taskId != null, "taskId 不能为 null ");
        return taskDOMapper.selectByPrimaryKey(taskId);
    }

    @Override
    public List<TaskDO> queryTaskByBizId(String bizId) throws TaskException {
        Preconditions.checkArgument(bizId != null, "bizId 不能为空");
        TaskDOExample taskDOExample = new TaskDOExample();
        taskDOExample.createCriteria().andBizIdEqualTo(bizId);

        return taskDOMapper.selectByExample(taskDOExample);
    }

    @Override
    public QueryResult<TaskDO> queryTasks(TaskQuery taskQuery) throws TaskException {

        TaskDOExample taskDOExample = new TaskDOExample();
        taskDOExample.setOffset((taskQuery.getPageNo() - 1) * taskQuery.getPageSize());
        taskDOExample.setLimit(taskQuery.getPageSize());
        Criteria criteria = taskDOExample.createCriteria();

        if (taskQuery.getTaskId() != null) {
            criteria.andIdEqualTo(taskQuery.getTaskId());
        }

        if (taskQuery.getUserId() != null) {
            criteria.andUserIdEqualTo(taskQuery.getUserId());
        }

        if (StringUtils.isNotBlank(taskQuery.getDefinitionName())) {
            criteria.andDefinitionNameEqualTo(taskQuery.getDefinitionName());
        }
        if (StringUtils.isNotBlank(taskQuery.getDealResult())) {
            criteria.andDealResultEqualTo(taskQuery.getDealResult());
        }

        if (taskQuery.getTaskStatusEnum() != null) {
            criteria.andStatusEqualTo(taskQuery.getTaskStatusEnum().getValue());
        }

        if (taskQuery.getAboveCreateTime() != null) {
            criteria.andGmtCreateGreaterThanOrEqualTo(taskQuery.getAboveCreateTime());
        }
        if (taskQuery.getBelowCreateTime() != null) {
            criteria.andGmtCreateLessThanOrEqualTo(taskQuery.getAboveCreateTime());
        }

        if (taskQuery.getTaskStatusEnum() != null) {
            if (TaskStatusEnum.STATUS_CREATED_TAKEN_COMPLETE.equals(taskQuery.getTaskStatusEnum())) {
                criteria.andStatusIn(TaskStatusEnum.VALID_STATUS_VALUE);
            } else {
                criteria.andStatusEqualTo(taskQuery.getTaskStatusEnum().getValue());
            }
        }

        return _queryTask(taskDOExample, taskQuery.getUserId());
    }

    private QueryResult<TaskDO> _queryTask(TaskDOExample taskDOExample, Long userId) {
        QueryResult<TaskDO> queryResult = new QueryResult<TaskDO>();

        try {

            Integer count = taskDOMapper.countByExample(taskDOExample);
            queryResult.setTotal(count == null ? 0 : count);
            if (count == null || count == 0) {
                queryResult.setDataList(new ArrayList<TaskDO>());
                return queryResult;
            }

            List<TaskDO> list = taskDOMapper.selectByExample(taskDOExample);

            if (list != null && !list.isEmpty()) {
                for (TaskDO taskDO : list) {
                    if (TaskConstant.TASK_TYPE_MULTI_USER.equals(taskDO.getType())
                        && StringUtils.isBlank(taskDO.getUserName())
                        && userId != null) {
                        taskDO.setUserId(userId);
                        taskDO.setUserName(queryParticipationUserName(taskDO.getId(), taskDO.getUserId()));
                    }
                }
            }

            queryResult.setDataList(list);
        } catch (Exception e) {
            logger.error("查询任务列表异常！e = " + ExceptionUtils.getStackTrace(e));
            queryResult.setSuccess(false);
            queryResult.setInfo("查询任务列表异常！");
        }

        return queryResult;

    }

    private String queryParticipationUserName(Long taskId, Long userId) {

        ParticipationDOExample participationDOExample = new ParticipationDOExample();

        participationDOExample.createCriteria().andTaskIdEqualTo(taskId)
            .andUserIdEqualTo(userId);

        List<ParticipationDO> participationDOList = participationDOMapper.selectByExample(participationDOExample);

        if (participationDOList != null && !participationDOList.isEmpty()) {
            return participationDOList.get(0).getUserName();
        } else {
            return "";
        }

    }

    @Override
    public QueryResult<TaskDO> queryTasksByUser(TaskQuery taskQuery, Long userId) throws TaskException {
        Preconditions.checkArgument(userId != null, "userId 不能为 null ");
        Preconditions.checkArgument(taskQuery.getTaskStatusEnum() != null, "taskStatusEnum 不能为 null ");

        TaskDOExample taskDOExample = new TaskDOExample();
        taskDOExample.setOffset((taskQuery.getPageNo() - 1) * taskQuery.getPageSize());
        taskDOExample.setLimit(taskQuery.getPageSize());
        Criteria criteria = taskDOExample.createCriteria();

        criteria.andUserIdEqualTo(userId).andDefinitionNameEqualTo(taskQuery.getDefinitionName());
        if (taskQuery.getTaskId() != null) {
            criteria.andIdEqualTo(taskQuery.getTaskId());
        }
        if (taskQuery.getAboveCreateTime() != null) {
            criteria.andGmtCreateGreaterThanOrEqualTo(taskQuery.getAboveCreateTime());
        }
        if (taskQuery.getBelowCreateTime() != null) {
            criteria.andGmtCreateLessThanOrEqualTo(taskQuery.getAboveCreateTime());
        }

        if (taskQuery.getTaskStatusEnum() != null) {
            if (TaskStatusEnum.STATUS_CREATED_TAKEN_COMPLETE.equals(taskQuery.getTaskStatusEnum())) {
                criteria.andStatusIn(TaskStatusEnum.VALID_STATUS_VALUE);
            } else {
                criteria.andStatusEqualTo(taskQuery.getTaskStatusEnum().getValue());
            }
        }

        if (taskQuery.getTaskId() != null && taskQuery.getTaskId() > 0) {
            criteria.andIdEqualTo(taskQuery.getTaskId());
        } else {
            List<Long> taskIds = queryParticipation(taskQuery, userId);
            if (taskIds != null && !taskIds.isEmpty()) {
                Criteria forPtpCriteria = taskDOExample.createCriteria();
                forPtpCriteria.andIdIn(taskIds);
                taskDOExample.or(forPtpCriteria);
            }

        }

        return _queryTask(taskDOExample, userId);
    }

    @Override
    public InvokeResult<Void> update(Long taskId, String dealResult, String memo) throws TaskException {
        InvokeResult<Void> result = new InvokeResult<Void>();
        Preconditions.checkArgument(taskId != null, "taskId 不能为空");
        Preconditions.checkArgument(StringUtils.isNotBlank(dealResult), "dealResult 不能为空");

        TaskDO update = new TaskDO();
        update.setId(taskId);
        update.setDealResult(dealResult);
        if (StringUtils.isNotBlank(memo)) {
            update.setMemo(memo);
        }
        Integer updateResult = taskDOMapper.updateByPrimaryKeySelective(update);
        if (updateResult > 0) {
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
        }

        return result;
    }

    @Override
    public InvokeResult<Void> update(Long taskId, Object bizInfo) throws TaskException {
        InvokeResult<Void> result = new InvokeResult<Void>();
        Preconditions.checkArgument(taskId != null, "taskId不能为空");
        Preconditions.checkArgument(bizInfo != null, "bizInfo不能为空");

        TaskDO update = new TaskDO();
        update.setId(taskId);
        update.setBizInfo(bizInfo.toString());
        Integer updateResult = taskDOMapper.updateByPrimaryKeySelective(update);
        if (updateResult > 0) {
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
        }

        return result;

    }

    @Override
    public QueryResult<TaskDO> asynQueryTask(TaskQuery taskQuery) {
        return null;
    }

    @Override
    public InvokeResult<Void> deleteTask(Long taskId) {
        // 删除op_bulbasaur_t 和op_bulbasaur_ptp两张表的数据
        InvokeResult<Void> result = new InvokeResult<Void>();
        try {
            Integer tCount = taskDOMapper.deleteByPrimaryKey(taskId);

            ParticipationDOExample participationDOExample = new ParticipationDOExample();
            participationDOExample.createCriteria().andTaskIdEqualTo(taskId);
            Integer ptpCount = participationDOMapper.deleteByExample(participationDOExample);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            logger.error("delete task error", e);
        }

        return result;

    }

    /**
     * 只有 TaskConstant.STATUS_CREATED   可申领 ，会查询
     *
     * @return
     */
    private List<Long> queryParticipation(TaskQuery taskQuery, Long userId) {
        List<Long> taskIds = new ArrayList<Long>();
        if (!TaskStatusEnum.STATUS_CREATED.equals(taskQuery.getTaskStatusEnum())) {
            return taskIds;
        }

        ParticipationDOExample participationDOExample = new ParticipationDOExample();
        participationDOExample.setOffset(0);
        participationDOExample.setLimit(DEFAULT_PARTICIPATIN_SIZE);

        ParticipationDOExample.Criteria criteria = participationDOExample.createCriteria();
        criteria.andStatusEqualTo(TaskConstant.PARTICIPATION_VALID)
            .andDefinitionNameEqualTo(taskQuery.getDefinitionName())
            .andUserIdEqualTo(userId);

        if (taskQuery.getTaskId() != null) {
            criteria.andTaskIdEqualTo(taskQuery.getTaskId());
        }
        if (taskQuery.getAboveCreateTime() != null) {
            criteria.andGmtCreateGreaterThanOrEqualTo(taskQuery.getAboveCreateTime());
        }
        if (taskQuery.getBelowCreateTime() != null) {
            criteria.andGmtCreateLessThanOrEqualTo(taskQuery.getAboveCreateTime());
        }

        List<ParticipationDO> list = participationDOMapper.selectByExample(participationDOExample);

        if (list != null && !list.isEmpty()) {
            for (ParticipationDO participationDO : list) {
                taskIds.add(participationDO.getTaskId());
            }
        }

        return taskIds;
    }

}
