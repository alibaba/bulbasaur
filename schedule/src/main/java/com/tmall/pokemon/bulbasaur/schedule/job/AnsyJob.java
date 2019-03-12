package com.tmall.pokemon.bulbasaur.schedule.job;

import java.util.concurrent.*;

import com.alibaba.ttl.threadpool.TtlExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.tmall.pokemon.bulbasaur.persist.domain.JobDO;
import com.tmall.pokemon.bulbasaur.schedule.ScheduleMachine;
import com.tmall.pokemon.bulbasaur.schedule.ScheduleMachineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User:  yunche.ch ... (ว ˙o˙)ง
 * <p>
 * Date: 14-12-17
 * <p>
 * Time: 下午11:48
 */
public abstract class AnsyJob implements Job {
    private static Logger logger = LoggerFactory.getLogger(AnsyJob.class);
    protected static ExecutorService executorService;
    protected JobDO jobDO;
    protected ScheduleMachineFactory scheduleMachineFactory;
    private static final int corePoolSize = 64;
    private static final int maximumPoolSize = 128;

    /**
     * 初始化Executor
     *
     * @author: yunche.ch@taobao.com
     * @since 2012-12-27 下午06:38:19
     */
    public static void initExecutor() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("AnsyJob-pool-%d").build();

        RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                if (!executor.isShutdown()) {
                    try {
                        executor.getQueue().put(r);
                    } catch (InterruptedException e) {
                        logger.error(String.format("线程池执行任务阻塞后异常,不可恢复! runnable=%s", r));
                    }
                }
            }
        };

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
            1L, TimeUnit.SECONDS, new LinkedBlockingQueue(), namedThreadFactory, rejectedExecutionHandler);

        /**
         * 在allowCoreThreadTimeOut设置为true时，ThreadPoolExecutor的keepAliveTime参数必须大于0。
         */
        threadPoolExecutor.allowCoreThreadTimeOut(true);

        /**
         * 增强 InheritableThreadLocal
         */
        executorService = TtlExecutors.getTtlExecutorService(threadPoolExecutor);

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
