package com.tmall.pokemon.bulbasaur.schedule.job;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tmall.pokemon.bulbasaur.persist.domain.JobDO;
import com.tmall.pokemon.bulbasaur.schedule.ScheduleMachine;
import com.tmall.pokemon.bulbasaur.schedule.ScheduleMachineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User:  yunche.ch ... (ว ˙o˙)ง
 * Date: 14-12-17
 * Time: 下午11:48
 */

public abstract class AnsyJob implements Job {
    private static Logger logger = LoggerFactory.getLogger(AnsyJob.class);
    protected static ExecutorService executorService;
    protected JobDO jobDO;
    protected ScheduleMachineFactory scheduleMachineFactory;

    /**
     * 初始化Executor
     *
     * @author: yunche.ch@taobao.com
     * @since 2012-12-27 下午06:38:19
     */
    public static void initExecutor() {
        executorService = Executors.newFixedThreadPool(8);
    }

    @Override
    public void doJob() {

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                logger.warn("========= AnsyJob ============ 开始执行Job！BizId = [" + jobDO.getBizId() + "]");
                ScheduleMachine scheduleMachine = scheduleMachineFactory.newInstance(jobDO.getBizId());
                scheduleMachine.run(jobDO.getStateName(), jobDO.getOutGoing());
                logger.warn("======== AnsyJob ========== 执行job成功！");
            }
        });
    }

}
