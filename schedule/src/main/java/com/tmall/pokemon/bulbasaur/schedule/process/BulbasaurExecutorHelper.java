package com.tmall.pokemon.bulbasaur.schedule.process;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * User:  yunche.ch ... (ว ˙o˙)ง
 * Date: 15-12-25
 * Time: 下午1:19
 */
@Component
public class BulbasaurExecutorHelper {
    private static final Logger logger = LoggerFactory.getLogger(BulbasaurExecutorHelper.class);

    private final int corePoolSize = 5;
    private final int maximumPoolSize = 200;

    ExecutorService executorService = null;

    @PostConstruct
    public void init() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("demo-pool-%d").build();

        //通用线程池
        executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

    public void execute(Runnable runnable) {
        executorService.execute(runnable);
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
