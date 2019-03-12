package com.tmall.pokemon.bulbasaur.schedule;

import java.util.Date;

import com.tmall.pokemon.bulbasaur.core.CoreModule;
import com.tmall.pokemon.bulbasaur.core.Result;
import com.tmall.pokemon.bulbasaur.core.model.State;
import com.tmall.pokemon.bulbasaur.core.model.StateLike;
import com.tmall.pokemon.bulbasaur.exception.BizException;
import com.tmall.pokemon.bulbasaur.persist.PersistHelper;
import com.tmall.pokemon.bulbasaur.persist.PersistMachine;
import com.tmall.pokemon.bulbasaur.persist.domain.JobDO;
import com.tmall.pokemon.bulbasaur.persist.domain.ProcessDO;
import com.tmall.pokemon.bulbasaur.persist.domain.StateDOExample;
import com.tmall.pokemon.bulbasaur.persist.domain.StateDOWithBLOBs;
import com.tmall.pokemon.bulbasaur.persist.mapper.*;
import com.tmall.pokemon.bulbasaur.schedule.process.JobHelper;
import com.tmall.pokemon.bulbasaur.schedule.util.CommentUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 增加了调度能力
 * User: yunche.ch - - - - (ว ˙o˙)ง
 * Date: 13-11-1
 * Time: 下午6:03
 */
public class ScheduleMachine extends PersistMachine {
    private final static Logger logger = LoggerFactory.getLogger(ScheduleMachine.class);
    public static final int EXE_INFO_MAX_LENGTH = 4900;
    //需要构造的时候传入
    private JobHelper jobHelper;
    private JobDOMapper jobDOMapper;
    private StateDOMapper stateDOMapper;

    protected ScheduleMachine(String bizId, String processName, int processVersion, ProcessDO processDO,
                              ProcessDOMapper processDOMapper, StateDOMapper stateDOMapper,
                              PersistHelper _persistHelper, JobDOMapper jobDOMapper, JobHelper _jobHelper,
                              TaskDOMapper taskDOMapper, ParticipationDOMapper participationDOMapper) {
        super(bizId, processName, processVersion, processDO, processDOMapper, stateDOMapper, _persistHelper,
            taskDOMapper, participationDOMapper, jobDOMapper);
        this.jobDOMapper = jobDOMapper;
        this.stateDOMapper = stateDOMapper;
        this.jobHelper = _jobHelper;
    }

    @Override
    protected Result run_atom(final StateLike currentState) {
        Result result = new Result();
        try {
            result = super.run_atom(currentState);
        } catch (RuntimeException e) {
            result.setContinue(false);
            if (e instanceof BizException) {
                // 在这里需要打监控信息 需要判断是否是重试次数完了什么的
                logger.error("单节点执行中发生业务错误:" + currentState.getStateName(), e.getMessage());

                // 在节点上面存一下执行信息
                StateDOWithBLOBs stateDOWithBLOBs = new StateDOWithBLOBs();
                stateDOWithBLOBs.setGmtModified(new Date());
                stateDOWithBLOBs.setExeInfo(subString(e.getMessage()));
                StateDOExample stateDOExample = new StateDOExample();
                stateDOExample.createCriteria().andOwnSignEqualTo(CoreModule.getInstance().getOwnSign())
                    .andStateNameEqualTo(currentState.getStateName())
                    .andBizIdEqualTo(currentState.getBizId());
                stateDOMapper.updateByExampleSelective(stateDOWithBLOBs, stateDOExample);

                //判断是否配置了重试，没配置直接扔出去
                if (currentState instanceof State) {
                    if (StringUtils.isBlank(((State)currentState).getRepeatList())) {
                        throw e;
                    }
                }
                // 存在重试记录的，更新重试记录
                ProcessDO currentProcessDO = getProcessDO();
                JobDO jobDO = jobHelper.getJob(getProcessDO().getBizId());
                if (jobDO != null) {
                    // 如果存在 就更新错误栈 重试次数以及时间
                    jobHelper.updateJobStack(jobDO, e.getCause());
                    logger.info(String
                        .format("流程[%s]的节点[%s],还可以重试[%s]次！", getProcessDO().getBizId(), jobDO.getStateName(),
                            jobDO.getRepeatTimes()));
                    return result;
                }

                // 插入一个新的job 等待重试
                jobHelper.add(currentProcessDO.getBizId(), currentProcessDO.getDefinitionName(), currentState, e);

                return result;
            } else {
                throw e;
            }

        }

        return result;

    }

    private String subString(String message) {

        if (CommentUtil.getLength(message) <= EXE_INFO_MAX_LENGTH) {
            return StringUtils.remove(message, "'");
        } else {
            return StringUtils.remove(CommentUtil.subStr(message, EXE_INFO_MAX_LENGTH), "'");
        }

    }

}

