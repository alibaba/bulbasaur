package com.tmall.pokemon.bulbasaur.task.constant;

/**
 * User: yunche.ch - - - - (ว ˙o˙)ง
 * Date: 13-11-7
 * Time: 下午5:21
 */
public class TaskConstant {
    /**
     * 类型
     */
    // 多人单任务
    public static final String TASK_TYPE_MULTI_USER = "multiUserTask";
    // 单人单任务
    public static final String TASK_TYPE_SINGLE_USER = "singleUserTask";
    // 会签
    public static final String TASK_TYPE_COUNTERSIGNATURE = "counterSignature";

    /**
     * job 类型
     */
    public static final String TASK_JOB_TYPE = "TimeOut";

    /**
     * participation 状态
     */
    public static final boolean PARTICIPATION_VALID = true;
    public static final boolean PARTICIPATION_INVALID = false;

    /**
     * !!!! 因为任务而存在的特殊流程上线文key，名字起的要别致一些，不能被业务方重名覆盖  !!!!
     * 所有key 都不带 "_",所有state 上下文中不会存储
     */

    //同一个流程中， 针对没一个task会set一次，第二个task会把前一次覆盖。这个东西主要用途是给业务方一个任务生成之后的回调，无关审核状态
    // 在上下文中放入taskids，业务方可以处理
    public static final String BULBASAUR_INNER_CONTEXT_KEY_TASK_IDS = "bulbasaurInnerContextKeyTaskIds";
    // 任务发起人
    public static final String BULBASAUR_INNER_CONTEXT_KEY_CREATOR_ID = "bulbasaurInnerContextKeyCreatorId";
    public static final String BULBASAUR_INNER_CONTEXT_KEY_CREATOR_NAME = "bulbasaurInnerContextKeyCreatorName";
    // 业务参数，比如审核商品的审核detail，优惠券发放的detail
    public static final String BULBASAUR_INNER_CONTEXT_KEY_BIZ_INFO = "bulbasaurInnerContextKeyBizInfo";

}
