package com.alibaba.pokemon.bulbasaur.schedule.job;

import com.alibaba.pokemon.bulbasaur.persist.domain.JobDO;
import com.alibaba.pokemon.bulbasaur.schedule.ScheduleMachine;
import com.alibaba.pokemon.bulbasaur.schedule.ScheduleMachineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 失败重试, 同步的
 * User: yunche.ch - - - - (ว ˙o˙)ง
 * Date: 13-11-1
 * Time: 下午4:29
 */
public class FailedRetryJob implements Job {
    private static Logger logger = LoggerFactory.getLogger(FailedRetryJob.class);
    private JobDO jobDO;
    private ScheduleMachineFactory scheduleMachineFactory;

    private FailedRetryJob() {
    }

    public FailedRetryJob(JobDO jobDO, ScheduleMachineFactory scheduleMachineFactory) {
        this.jobDO = jobDO;
        this.scheduleMachineFactory = scheduleMachineFactory;
    }

    @Override
    public void doJob() {

        //查出流程，继续执行
        logger.warn("========= FailedRetryJob ============ 开始执行Job！BizId = [" + jobDO.getBizId() + "]");
        ScheduleMachine scheduleMachine = scheduleMachineFactory.newInstance(jobDO.getBizId());
        scheduleMachine.run(jobDO.getStateName());
        logger.warn("======== FailedRetryJob ========== 执行job成功！");

    }
}
