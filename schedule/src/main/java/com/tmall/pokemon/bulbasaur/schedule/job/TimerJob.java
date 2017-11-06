package com.tmall.pokemon.bulbasaur.schedule.job;

import com.tmall.pokemon.bulbasaur.persist.domain.JobDO;
import com.tmall.pokemon.bulbasaur.schedule.ScheduleMachineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 超时(人工任务)
 * User: yunche.ch - - - - (ว ˙o˙)ง
 * Date: 13-11-1
 * Time: 下午4:29
 */
public class TimerJob extends AnsyJob {
    private static Logger logger = LoggerFactory.getLogger(TimerJob.class);

    private TimerJob() {
    }

    public TimerJob(JobDO jobDO, ScheduleMachineFactory scheduleMachineFactory) {
        this.jobDO = jobDO;
        this.scheduleMachineFactory = scheduleMachineFactory;
    }

}
