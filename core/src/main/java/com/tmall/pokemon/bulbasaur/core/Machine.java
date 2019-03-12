package com.tmall.pokemon.bulbasaur.core;

import com.tmall.pokemon.bulbasaur.core.model.Definition;
import com.tmall.pokemon.bulbasaur.core.model.KeyWords;
import com.tmall.pokemon.bulbasaur.core.model.StateLike;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 一个流程实例 ，与状态绑定，非单例
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-11 上午10:23:39
 */
public class Machine {

    private final static Logger logger = LoggerFactory.getLogger(Machine.class);
    // 上下文
    protected Map<String, Object> context = new HashMap<String, Object>();
    // 当前节点
    protected String currentStateName = null;
    // 流程定义
    protected Definition definition = null;
    private Place place;

    /**
     * protected的构造方法，不对外开放，请使用newInstance方法获取实例
     *
     * @param processName    流程名
     * @param processVersion 流程版本号
     */
    protected Machine(String processName, int processVersion, Place place) {
        if (place == null) {
            place = Bulbasaur.getInnerBeanFactory().getBean("place", Place.class);
        }
        // 要单例
        this.place = place;
        definition = place.getDefinition(processName, processVersion);
        currentStateName = definition.getFirst();

        context.put(KeyWords.CURRENT_PROCESS_NAME, definition.getName());
        context.put(KeyWords.CURRENT_STATE_NAME, currentStateName);
    }

    /**
     * 返回一个只读版本的上下文map，所有在此返回上的修改都是无效的
     *
     * @return unmodifiableMap
     */
    public Map<String, Object> getContext() {
        // 返回只读的context 版本
        return Collections.unmodifiableMap(context);
    }

    // run from current state
    public void run() {
        run(currentStateName);
    }

    public void run(String stateName) {
        run(stateName, null);
    }

    public void run(String stateName, String outGoing) {
        if (CoreModule.getInstance().isShutDown()) {
            throw new IllegalStateException("bulbasaur is shutdown now, no more machine request allowed");
        }
        // 查找现在的状态
        StateLike currentState = run0_findCurrent(stateName);
        while (currentState != null) {
            // 开始执行
            logger.debug("run state:" + currentState.getStateName());
            if (StringUtils.isNotBlank(outGoing)) {
                currentState.setOutGoing(outGoing);
                outGoing = null;
            }
            Result result;
            try {
                result = run_atom(currentState);
            } catch (RuntimeException e) {
                // 如果出错了 要把状态回滚回去
                currentStateName = currentState.getStateName();
                context.put(KeyWords.CURRENT_STATE_NAME, currentStateName);
                throw e;
            }
            if (result.needContinue()) {
                // 如果需要继续执行 就获取下一个节点作为当前节点 如果流程已经结束 那么这里获取到的是null
                currentState = result.getState();
            } else {
                // 否则直接置为null
                currentState = null;
            }
        }

    }

    /**
     * 做事务的时候，原子性,子类中重写
     *
     * @param currentState
     * @return Result
     * @since 2012-12-27
     */
    protected Result run_atom(StateLike currentState) {
        return run0(currentState);
    }

    /**
     * 一个节点进来，先执行自己的execute方法，再算出下一个节点，并执行下一个节点的prepare方法
     *
     * @param currentState
     * @return Result
     * @since 2012-12-12
     */
    final protected Result run0(StateLike currentState) {
        // setp1:执行当前节点的execute 方法
        if (!run0_execute(currentState)) {
            // 执行失败
            logger.error("节点的execute方法执行失败!节点：" + currentState.getStateName());
            // 出错 暂停执行
            return new Result(false);
        } else {
            // 执行完本次节点，替换成下一个节点，并把下一个节点的 prepare执行掉
            StateLike nextState = run0_calcNext(currentState);
            if (nextState == null) {
                // 没有下一个节点 意味着流程结束了 但也是正常状态
                return new Result(true);
            } else {
                currentStateName = nextState.getStateName();
                context.put(KeyWords.CURRENT_STATE_NAME, currentStateName);
                // 执行下一个节点的prepare方法
                return new Result(run0_prepare(nextState), nextState);
            }
        }
    }

    /**
     * 节点前置方法
     *
     * @return void
     * @author: yunche.ch@taobao.com
     * @since 2012-12-12 下午9:19:29
     */
    protected boolean run0_prepare(StateLike currentState) {

        Result result = currentState.prepare(context);
        // merge 下返回的context
        mergeContext(result.getModels());
        return result.needContinue();
    }

    /**
     * 将执行的结果merge到context
     *
     * @return void
     * @author: yunche.ch@taobao.com
     * @since 2012-12-14 下午02:24:03
     */
    protected Map<String, Object> mergeContext(Map<String, Object> incrMap) {
        if (incrMap != null && !incrMap.isEmpty() && incrMap != context) {
            context.putAll(incrMap);
        }
        return context;
    }

    /**
     * 计算下一个节点
     *
     * @return void
     * @throws Throwable
     * @author: yunche.ch@taobao.com
     * @since 2012-12-12 下午8:51:48
     */
    protected StateLike run0_calcNext(StateLike currentState) {

        //判断是否有直接跳转
        String next = null;
        if (currentState.getJumpTo() != null) {
            next = currentState.getJumpTo();
        } else {
            next = currentState.willGo(context);
        }
        if (!StringUtils.isBlank(next)) {
            // 不为空，则有下一节点
            return definition.getState(next);
        }
        return null;
    }

    /**
     * 节点execute方法
     *
     * @return boolean
     * @throws Throwable
     * @author: yunche.ch@taobao.com
     * @since 2012-12-12 下午8:22:53
     */
    protected boolean run0_execute(StateLike currentState) {
        // 执行当前节点
        Result result = currentState.execute(context);
        mergeContext(result.getModels());
        return result.needContinue();

    }

    /**
     * 获得当前节点
     *
     * @return State
     * @author: yunche.ch@taobao.com
     * @since 2012-12-11 下午8:57:45
     */
    protected StateLike run0_findCurrent(String stateName) {
        return definition.getState(stateName);
    }

    public String getProcessName() {
        return definition.getName();
    }

    public int getProcessVersion() {
        return definition.getVersion();
    }

    public Definition getDefinition() {
        return definition;
    }

    public String getCurrentStateName() {
        return currentStateName;
    }

    // CRUD context start
    public void addContext(String key, Object value) {
        this.context.put(key, value);
    }

    public void addContext(Map<String, Object> map) {
        this.context.putAll(map);
    }

    public void removeContext(String key) {
        this.context.remove(key);
    }

    public Object getContext(String key) {
        return context.get(key);
    }

}
