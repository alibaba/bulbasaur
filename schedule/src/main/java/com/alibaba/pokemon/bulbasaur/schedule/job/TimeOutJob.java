package com.alibaba.pokemon.bulbasaur.schedule.job;

import com.alibaba.pokemon.bulbasaur.persist.domain.JobDO;
import com.alibaba.pokemon.bulbasaur.schedule.ScheduleMachineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时任务job，异步的
 * User: yunche.ch - - - - (ว ˙o˙)ง
 * Date: 13-11-1
 * Time: 下午4:29
 */
public class TimeOutJob extends AnsyJob {
    private static Logger logger = LoggerFactory.getLogger(TimeOutJob.class);

    private TimeOutJob() {
    }

    public TimeOutJob(JobDO jobDO, ScheduleMachineFactory scheduleMachineFactory) {
        this.scheduleMachineFactory = scheduleMachineFactory;
        this.jobDO = jobDO;
    }
}
