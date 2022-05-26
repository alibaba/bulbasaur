package com.alibaba.pokemon.bulbasaur.schedule.job;

/**
 * User: yunche.ch - - - - (ว ˙o˙)ง
 * Date: 13-11-4
 * Time: 上午11:22
 */
public interface JobConstant {
    String JOB_STATUS_INIT = "INIT";
    String JOB_STATUS_RUNNING = "RUNNING";
    String JOB_STATUS_DONE = "DONE";
    String JOB_STATUS_FAILED = "FAILED";

    String JOB_ENVENT_TYPE_FAILEDRETRY = "FailedRetry";
    String JOB_ENVENT_TYPE_TIMEOUT = "TimeOut";
    String JOB_ENVENT_TYPE_TIMER = "Timer";

}
