package com.alibaba.pokemon.bulbasaur.task.service;

import com.alibaba.pokemon.bulbasaur.persist.domain.InvokeResult;
import com.alibaba.pokemon.bulbasaur.persist.domain.QueryResult;
import com.alibaba.pokemon.bulbasaur.persist.domain.TaskDO;
import com.alibaba.pokemon.bulbasaur.task.dto.TaskParam;
import com.alibaba.pokemon.bulbasaur.task.model.User;
import com.alibaba.pokemon.bulbasaur.task.dto.TaskQuery;
import com.alibaba.pokemon.bulbasaur.task.exception.TaskException;

import java.util.List;

/**
 * 该接口提供了多个任务查询方法，另外还有申领，完成，转交等动作
 * User:  yunche.ch ... (ว ˙o˙)ง
 * Date: 14-12-22
 * Time: 下午2:18
 */
public interface TaskAccessor {

    /**
     * 判断用户是否有权限申领指定的任务
     * <p/>
     *
     * @param taskId      任务
     * @param currentUser 处理人
     */
    InvokeResult<Void> hasTaskTakenPermission(Long taskId, User currentUser) throws TaskException;

    /**
     * 申领一个任务
     *
     * @param taskId      任务Id
     * @param currentUser 处理人
     */
    InvokeResult<Void> takenTask(Long taskId, User currentUser) throws TaskException;

    /**
     * 释放一个任务，任务状态转为 created
     *
     * @param taskId      任务Id
     * @param currentUser 处理人
     * @return InvokeResult<Void> 返回值包括是否成功，异常信息
     */
    InvokeResult<Void> releaseTask(Long taskId, User currentUser) throws TaskException;

    /**
     * 判断用户是否有权限处理给定的任务
     * 包括 release and complete
     * <p/>
     *
     * @param taskDO      任务
     * @param currentUser 处理人
     */
    InvokeResult<Void> hasTaskDealPermission(TaskDO taskDO, User currentUser) throws TaskException;

    /**
     * 完成一个任务
     * 同步
     *
     * @param taskId     任务Id
     * @param user       操作的用户
     * @param dealResult 处理结果
     * @param memo       处理备注
     * @return InvokeResult<Boolean> 返回值包括是否成功，异常信息
     */
    InvokeResult<Void> completeTask(Long taskId, User user, String dealResult, String memo) throws TaskException;

    /**
     * 完成一个任务
     * 同步
     *
     * @return InvokeResult<Boolean> 返回值包括是否成功，异常信息
     */
    InvokeResult<Void> completeTask(TaskParam taskParam) throws TaskException;

    /**
     * 同步取消一个任务，给放一个业务参数的机会，只会改task 的状态
     *
     * @param taskId   任务Id
     * @param outGoing 任务结束后的分支选择
     * @param user     操作的用户
     * @param memo     备注
     * @return InvokeResult<Boolean> 返回值包括是否成功，异常信息
     */
    InvokeResult<Void> cancelTask(Long taskId, String outGoing, User user, String memo, String bizKey, Object bizValue)
        throws TaskException;

    /**
     * 未经权限判断处理任务，业务系统尽量不要使用此接口
     *
     * @param task     任务
     * @param user     用户
     * @param outGoing 分支选择
     * @param memo     备注
     * @return 返回上下文变量
     */
    //    public Map<String, Object> innerCompleteTask(Task task, User user, String outGoing, String memo);

    /**
     * 当前所有人将任务转给指定用户
     * <p/>
     * 每次转交都将使原任务失效同时创建一个新的任务<br>
     * 只有当任务是单用户任务并且处于created或者taken状态的时候才允许转交
     *
     * @param taskId      任务Id
     * @param currentUser 任务当前所属用户
     * @param assignUser  转给的用户
     * @param memo        备注
     * @return 返回结果包括是否成功[isSuccess()]、提示信息[getInfo()]以及被转交的Task[getResultData()]
     */
    InvokeResult<Void> assignTaskWithResult(Long taskId, User currentUser, User assignUser, String memo)
        throws TaskException;

    /**
     * 根据Id获取任务
     *
     * @param taskId 任务ID
     * @return Task 任务
     */
    TaskDO queryTask(Long taskId) throws TaskException;

    /**
     * 根据bizId查询任务
     * 一般情况下，list的size是1
     */
    List<TaskDO> queryTaskByBizId(String bizId) throws TaskException;

    /**
     * 任务类型:
     * 1.1 TaskConstant.STATUS_CREATED       可申领
     * 1.2 TaskConstant.STATUS_TAKEN         已申领
     * 1.3 TaskConstant.STATUS_COMPLETED     处理完成
     * 1.4 TaskConstant.STATUS_CANCEL        取消
     * <p/>
     * 需要注意的是，如果查询条件有userId，只能查询到单用户任务<br>
     * 如果想在结果集中包含多用户任务，请使用{@link #queryTasksByUser(TaskQuery, Long)}
     * <p/>
     * belowCreateTime < createTime < aboveCreateTime
     * 通过条件查询任务<br>
     *
     * @return 任务列表
     */
    QueryResult<TaskDO> queryTasks(TaskQuery taskQuery) throws TaskException;

    /**
     * 查询的任务包含多用户任务时使用此接口查询
     * <p>
     * 该接口使用时,taskQuery的userId无效<br>
     * userId请通过单独参数传入<br>
     * 通过此接口查询，单用户任务也是能够正确查询到的
     *
     * @param taskQuery 查询条件
     * @param userId    用户id
     * @return 任务列表
     */
    QueryResult<TaskDO> queryTasksByUser(TaskQuery taskQuery, Long userId) throws TaskException;

    /**
     * 更新处理结果和意见
     *
     * @param taskId     必填
     * @param dealResult 必填
     * @param memo       可空
     * @return
     * @throws TaskException
     */
    InvokeResult<Void> update(Long taskId, String dealResult, String memo) throws TaskException;

    /**
     * 按taskId，全量覆盖bizInfo
     */
    InvokeResult<Void> update(Long taskId, Object bizInfo) throws TaskException;

    /**
     * 单独查询task表
     *
     * @param taskQuery
     * @return
     */
    QueryResult<TaskDO> asynQueryTask(TaskQuery taskQuery);

    /**
     * 删除任务
     *
     * @param taskId
     */
    public InvokeResult<Void> deleteTask(Long taskId);

    /**
     * 未经权限判断处理任务，业务系统尽量不要使用此接口
     *
     * @param task     任务
     * @param user     用户
     * @param outGoing 分支选择
     * @param memo     备注
     * @return 返回上下文变量
     */
    //    public Map<String, Object> innerCompleteTask(Task task, User user, String outGoing, String memo);

    //    /**
    //     * 通过taskId修改该任务的超时时间（如果有的话）
    //     * <p/>
    //     * 废弃，请使用{@link #addTaskTimeout(Long, int, int)}
    //     *
    //     * @param taskId  任务id
    //     * @param timeOut 超时时间描述字串
    //     */
    //    @Deprecated
    //    void modifyTaskTimeout(Long taskId, String timeOut) throws TaskException;
    //
    //    /**
    //     * 通过taskId修改该任务的超时时间（如果有的话）
    //     * <p/>
    //     * 为指定任务加上指定单位偏移量的超时时间，该方法不返回任何值，如果任务的超时动作不存在则NPE<br>
    //     * 时间最多被减为0，不会被减为负数
    //     *
    //     * @param taskId 任务id
    //     * @param field  时间单位<br>
    //     *               包括{@link #TIMEOUT_DAY}, {@link #TIMEOUT_HOUR}, {@link #TIMEOUT_MINUTE}
    //     * @param offset 偏移量
    //     * @throws NullPointerException 如果任务并不存在超时动作
    //     */
    //    void addTaskTimeout(Long taskId, int field, int offset) throws TaskException;
    //
    //    /**
    //     * 通过taskId修改该任务的提醒剩余时间（如果有的话）
    //     * <p/>
    //     * 为指定任务加上指定单位偏移量的提醒剩余时间，该方法不返回任何值，如果任务的提醒动作不存在则NPE<br>
    //     * 时间最多被减为0，不会被减为负数
    //     *
    //     * @param taskId 任务id
    //     * @param field  时间单位<br>
    //     *               包括{@link #TIMEOUT_DAY}, {@link #TIMEOUT_HOUR}, {@link #TIMEOUT_MINUTE}
    //     * @param offset 偏移量
    //     * @throws NullPointerException 如果任务并不存在提醒动作
    //     */
    //    void addTaskReminderTime(Long taskId, int field, int offset) throws TaskException;
    //
    //    /**
    //     * 刷新task的timeout（如果有的话），使timeout时间重新计时
    //     *
    //     * @param taskId 任务id
    //     * @throws NullPointerException 如果任务并不存在超时动作
    //     */
    //    void refreshTaskTimeout(Long taskId) throws TaskException;
    //
    //    /**
    //     * 通过taskId获取该任务的超时剩余时间
    //     * <p/>
    //     * 不存在超时动作时返回-1
    //     *
    //     * @param taskId 任务id
    //     * @return 超时剩余时间毫秒值 -1如果任务不存在超时动作
    //     */
    //    Long queryTaskRemainingTime(Long taskId) throws TaskException;
    //
    //    /**
    //     * 暂停或激活任务的超时完成处理
    //     * <p/>
    //     * 暂停或激活一个任务的超时处理，不存在超时动作时NPE
    //     *
    //     * @param taskId 任务id
    //     * @param active false为暂停 true为重新激活
    //     * @return [！注意！]这里返回的并非操作是否成功，而是超时动作的原始状态，true为激活，false为暂停
    //     * @throws NullPointerException 如果任务并不存在超时动作
    //     */
    //    boolean activeTaskTimeout(Long taskId, boolean active) throws TaskException;
    //
    //    /**
    //     * 移除任务的超时job
    //     * <p>
    //     * 移除任务的超时job，不再发生超时动作
    //     * </p>
    //     *
    //     * @param taskId 任务id
    //     */
    //    void removeTaskTimeout(Long taskId) throws TaskException;
    //
    //    /**
    //     * 移除流程所属的所有任务的超时job
    //     * <p>
    //     * 一次性移除所有任务的超时job
    //     * </p>
    //     *
    //     * @param processInstanceId
    //     */
    //    void removeAllTaskTimeout(Long processInstanceId) throws TaskException;
    //
    //    /**
    //     * 获取任务超时动作的状态
    //     * <p/>
    //     * true为激活，false为暂停，不存在超时动作时NPE
    //     *
    //     * @param taskId 任务id
    //     * @return true为激活，false为暂停
    //     * @throws NullPointerException 如果任务并不存在超时动作
    //     */
    //    boolean isTaskTimeoutRunning(Long taskId) throws TaskException;

}
