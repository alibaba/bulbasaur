package com.tmall.pokemon.bulbasaur.persist;

import com.tmall.pokemon.bulbasaur.core.CoreModule;
import com.tmall.pokemon.bulbasaur.core.Machine;
import com.tmall.pokemon.bulbasaur.core.Result;
import com.tmall.pokemon.bulbasaur.core.annotation.NeedDAOMeta;
import com.tmall.pokemon.bulbasaur.core.model.StateLike;
import com.tmall.pokemon.bulbasaur.persist.constant.ProcessConstant;
import com.tmall.pokemon.bulbasaur.persist.constant.StateConstant;
import com.tmall.pokemon.bulbasaur.persist.domain.*;
import com.tmall.pokemon.bulbasaur.persist.mapper.*;
import com.tmall.pokemon.bulbasaur.persist.tx.TransactionRun;
import com.tmall.pokemon.bulbasaur.persist.util.TSONUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.*;

/**
 * 持久化的 一个machine实例
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-18 下午02:34:46
 */
public class PersistMachine extends Machine {
    private final static Logger logger = LoggerFactory.getLogger(PersistMachine.class);
    final private String bizId;
    // need inject
    private ProcessDOMapper processDOMapper;
    private StateDOMapper stateDOMapper;
    private TaskDOMapper taskDOMapper;
    private ParticipationDOMapper participationDOMapper;
    private JobDOMapper jobDOMapper;
    private PersistHelper persistHelper;
    // need inject
    private ProcessDO processDO;
    private List<StateDOWithBLOBs> stateDOList = new ArrayList<StateDOWithBLOBs>();

    protected PersistMachine(String bizId, String processName, int processVersion, ProcessDO processDO,
                             ProcessDOMapper processDOMapper
        , StateDOMapper stateDOMapper
        , PersistHelper persistHelper
        , TaskDOMapper taskDOMapper
        , ParticipationDOMapper participationDOMapper
        , JobDOMapper jobDOMapper) {
        super(processName, processVersion, null);

        // inject use construct method
        this.processDOMapper = processDOMapper;
        this.stateDOMapper = stateDOMapper;
        this.persistHelper = persistHelper;
        this.processDO = processDO;
        this.taskDOMapper = taskDOMapper;
        this.participationDOMapper = participationDOMapper;
        this.jobDOMapper = jobDOMapper;

        // 如果是存在的流程 就把stateList也读出来
        if (processDO != null) {

            StateDOExample stateDOExample = new StateDOExample();
            stateDOExample.setOrderByClause("id asc");
            stateDOExample.createCriteria().andBizIdEqualTo(bizId).andOwnSignEqualTo(
                CoreModule.getInstance().getOwnSign());

            List<StateDOWithBLOBs> stateList = stateDOMapper.selectByExampleWithBLOBs(stateDOExample);
            if (stateList != null && !stateList.isEmpty()) {
                this.stateDOList = stateList;
                StateDOWithBLOBs lastState = stateList.get(stateList.size() - 1);
                currentStateName = lastState.getStateName();
                // 全部加入进去
                context.putAll(TSONUtils.fromTSON(lastState.getPreBizInfo()));
            }
        }

        context.put("bizId", bizId);
        this.bizId = bizId;

    }// 构造函数结束

    /**
     * @return 返回PersistMachine对应的数据库StateDO的List，这是一个不可修改的List
     */
    public List<StateDOWithBLOBs> getStateDOList() {
        // return an unmodifiable version
        return Collections.unmodifiableList(stateDOList);
    }

    public String getStatePathString() {
        StringBuilder sb = new StringBuilder();
        if (stateDOList.isEmpty()) {
            return "not start";
        }
        for (StateDO s : stateDOList) {
            sb.append(s.getStateName()).append("(").append(s.getStatus()).append(") -> ");
        }
        return sb.substring(0, sb.length() - 4);
    }

    @Override
    protected StateLike run0_findCurrent(String stateName) {
        return super.run0_findCurrent(stateName);
    }

    @Override
    protected Result run_atom(final StateLike currentState) {
        return persistHelper.tx(new TransactionRun<Result>() {
            @Override
            public Result run() {
                StateLike copyCurrentState = currentState;//currentState 为final，做替身
                if (currentState.getOutGoing() != null) {
                    //先complete 之前停掉的
                    completeState(currentState);
                    // 直接替换
                    copyCurrentState = run0_findCurrent(currentState.getOutGoing());
                    //先初始化当前
                    initNextState(copyCurrentState);
                }
                Result result = run0(copyCurrentState);
                if (result.getState() == null) {
                    if (result.needContinue()) {
                        // 尾节点流程结束，status位置成complete
                        completeState(copyCurrentState);
                        updateProcessCurrentStatus(ProcessConstant.STATE_COMPLETE);
                    } else {
                        // 失败节点
                    }
                } else {
                    completeState(copyCurrentState);
                    initNextState(result.getState());
                }
                return result;
            }
        });
    }

    @Override
    protected boolean run0_execute(StateLike currentState) {
        make(currentState);
        return super.run0_execute(currentState);
    }

    @Override
    protected boolean run0_prepare(StateLike currentState) {
        make(currentState);
        return super.run0_prepare(currentState);
    }

    private void make(StateLike currentState) {
        //为每个可运行的节点，放入流程id
        currentState.setBizId(bizId);
        currentState.setDefinitionName(definition.getName());
        injectDAO(currentState);
    }

    /**
     * 检查state类型，如果是task，把DAO放进去
     *
     * @param currentState
     */
    protected void injectDAO(StateLike currentState) {
        if (currentState != null) {
            NeedDAOMeta needDAOMeta = currentState.getClass().getAnnotation(NeedDAOMeta.class);
            if (needDAOMeta != null && needDAOMeta.need()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("taskDOMapper", taskDOMapper);
                map.put("participationDOMapper", participationDOMapper);
                map.put("jobDOMapper", jobDOMapper);
                currentState.setDAOMap(map);
            }
        }

    }

    private void updateProcessCurrentStatus(String stateComplete) {
        processDO.setStatus(stateComplete);

        ProcessDOExample processDOExample = new ProcessDOExample();
        processDOExample.createCriteria().andBizIdEqualTo(processDO.getBizId()).andOwnSignEqualTo(
            CoreModule.getInstance().getOwnSign());
        processDOMapper.updateByExampleSelective(processDO, processDOExample);
    }

    protected void initNextState(StateLike nextState) {
        StateDOWithBLOBs nextStateDO = new StateDOWithBLOBs();
        nextStateDO.setBizId(bizId);
        nextStateDO.setStateName(nextState.getStateName());
        nextStateDO.setAlias(nextState.getStateAlias());
        // 带下划线的才存入DB,eg:_i
        nextStateDO.setPreBizInfo(TSONUtils.toTSONwithFilter(context));
        nextStateDO.setStatus(StateConstant.STATE_READY);
        // insert state table
        insertState(nextStateDO);
        updateProcessCurrentState(nextState.getStateName());
    }

    protected void updateProcessCurrentState(String stateName) {
        processDO.setCurrentStateName(stateName);
        ProcessDOExample processDOExample = new ProcessDOExample();
        processDOExample.createCriteria().andBizIdEqualTo(processDO.getBizId()).andOwnSignEqualTo(
            CoreModule.getInstance().getOwnSign());
        processDOMapper.updateByExampleSelective(processDO, processDOExample);
    }

    private void completeState(StateLike currentState) {
        if (processDO == null) {
            // if null,the process is just start ,insert the process and  first state record
            ProcessDO newProcessDO = new ProcessDO();
            newProcessDO.setBizId(bizId);
            newProcessDO.setDefinitionName(definition.getName());
            newProcessDO.setDefinitionVersion(definition.getVersion());
            newProcessDO.setAlias(definition.getAlias());
            newProcessDO.setStatus(ProcessConstant.STATE_READY);
            newProcessDO.setOwnSign(CoreModule.getInstance().getOwnSign());
            newProcessDO.setGmtCreate(new Date());
            newProcessDO.setGmtModified(new Date());
            processDOMapper.insert(newProcessDO);
            processDO = newProcessDO;

            StateDOWithBLOBs stateDO = new StateDOWithBLOBs();
            stateDO.setBizId(bizId);
            stateDO.setStateName(currentState.getStateName());
            stateDO.setAlias(currentState.getStateAlias());
            stateDO.setPreBizInfo(TSONUtils.toTSONwithFilter(context));
            stateDO.setStatus(StateConstant.STATE_COMPLETE);
            insertState(stateDO);
        } else {
            // else complete with Optimistic Locking
            StateDOWithBLOBs lastState = stateDOList.get(stateDOList.size() - 1);
            if (lastState.getStateName().equals(currentState.getStateName()) && lastState.getStatus().equals(
                StateConstant.STATE_READY)) {

                lastState.setStatus("complete");
                lastState.setGmtModified(new Date());

                StateDOExample stateDOExample = new StateDOExample();
                stateDOExample.createCriteria().andIdEqualTo(lastState.getId()).andStatusEqualTo("ready")
                    .andOwnSignEqualTo(CoreModule.getInstance().getOwnSign());
                int re = stateDOMapper.updateByExampleWithBLOBs(lastState, stateDOExample);

                if (re == 0) {
                    throw new IllegalStateException(String
                        .format("duplication complete request find for bizId: %s and state: %s ", bizId,
                            currentState.getStateName()));
                }
            } else {
                throw new IllegalStateException(
                    "no incomplete state find for bizId: " + bizId + " and state: " + currentState.getStateName());
            }
        }
    }

    public void rollback() {
        rollbackTo(null);
    }

    /**
     * 给定state回滚
     *
     * @return void
     * @author: yunche.ch@taobao.com
     * @since 2012-12-19 下午04:49:42
     */
    private void rollbackTo(String rollbackName) {
        int rollbackIndex = 0;
        if (StringUtils.isBlank(rollbackName)) {
            // 回到当前节点的前一个
            rollbackIndex = stateDOList.size() - 2;
        } else {
            for (StateDO stateDO : stateDOList) {
                if (rollbackName.equals(stateDO.getStateName()) && StateConstant.STATE_COMPLETE.equals(
                    stateDO.getStatus())) {
                    // 获得指定对象的最大索引，前提是有重复
                    rollbackIndex = stateDOList.lastIndexOf(stateDO);
                    break;
                }
            }
        }

        final StateDOWithBLOBs oldRollbackState = stateDOList.get(rollbackIndex);
        final StateDOWithBLOBs newRollbackState = new StateDOWithBLOBs();
        newRollbackState.setBizId(oldRollbackState.getBizId());
        newRollbackState.setStateName(oldRollbackState.getStateName());
        newRollbackState.setAlias(oldRollbackState.getAlias());
        newRollbackState.setPreBizInfo(oldRollbackState.getPreBizInfo());
        newRollbackState.setStatus(StateConstant.STATE_READY);
        final List<StateDOWithBLOBs> passedState = new ArrayList<StateDOWithBLOBs>();
        for (int i = rollbackIndex + 1; i < stateDOList.size(); i++) {
            passedState.add(stateDOList.get(i));
        }

        persistHelper.tx(new TransactionRun<Object>() {
            @Override
            public Object run() {
                // rollback
                for (StateDOWithBLOBs stateDO : passedState) {

                    stateDO.setStatus("rollback");
                    stateDO.setGmtModified(new Date());
                    stateDOMapper.updateByPrimaryKeySelective(stateDO);
                }
                oldRollbackState.setStatus("rollback");
                oldRollbackState.setGmtModified(new Date());
                stateDOMapper.updateByPrimaryKeySelective(oldRollbackState);

                // insert rollback target state
                insertState(newRollbackState);
                updateProcessCurrentState(newRollbackState.getStateName());
                // 不管是否是ready，回滚了就置成ready
                updateProcessCurrentStatus(StateConstant.STATE_READY);
                return null;
            }
        });

        // 既然已经回滚了，整理一下context
        currentStateName = newRollbackState.getStateName();
        context.clear();
        context.putAll(TSONUtils.fromTSON(newRollbackState.getPreBizInfo()));
        context.put("bizId", bizId);

        logger.info(String.format("process [%s] with bizId [%s] rollback to", definition, bizId, currentStateName));

    }

    protected void insertState(StateDOWithBLOBs stateDO) {
        stateDO.setGmtCreate(new Date());
        stateDO.setGmtModified(new Date());
        stateDO.setOwnSign(CoreModule.getInstance().getOwnSign());
        stateDOMapper.insert(stateDO);
        stateDOList.add(stateDO);
    }

    public String getBizId() {
        return bizId;
    }

    /**
     * @return 流程实例是否已经存在（是否已在数据库中有持久化）
     */
    public boolean isExist() {
        return processDO != null;
    }

    /**
     * @return 流程实例是否已经完成了
     */
    public boolean isCompleted() {
        return processDO != null && ProcessConstant.STATE_COMPLETE.equals(processDO.getStatus());
    }

    /**
     * @return 返回PersistMachine对应的数据库ProcessDO实例的副本
     */
    public ProcessDO getProcessDO() {

        if (processDO == null) {
            return processDO;
        }

        // return a clone
        ProcessDO copy = new ProcessDO();
        BeanUtils.copyProperties(processDO, copy);
        return copy;
    }
}
