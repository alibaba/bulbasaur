package com.tmall.pokemon.bulbasaur.schedule.process;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User:  yunche.ch ... (ว ˙o˙)ง
 * Date: 17/9/27
 * Time: 下午9:00
 */
public abstract class AbstractBulbasaurProcessor<T, M> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractBulbasaurProcessor.class);

    public static final Integer DEFAULT_THREAD_NUM = 20;//固定线程数
    public static final Integer DEFAULT_TIME_OUT = 1;//固定超时时间
    public static final int PAGE_SIZE = 200;

    @Resource
    BulbasaurExecutorHelper bulbasaurExecutorHelper;

    public void handle(T example) throws Exception {

        int pageSize = querySupportPageSize();
        int totalCount = queryTotalCount(example);
        if (totalCount == 0) {
            return;
        }

        logger.warn("本次查询总数为:" + totalCount);

        Integer totalPages = PageSizeHelper.calcTotalPages(totalCount, pageSize);
        logger.warn(String.format("本次查询总得分页数:[%s]", totalPages));

        int actualWorkThreadNum = totalPages;

        /**
         * 页数大于线程数,就抢
         */
        if (totalPages > queryThreadNum()) {
            actualWorkThreadNum = queryThreadNum();
        }

        AtomicInteger pageAtomicInteger = new AtomicInteger(totalPages + 1);
        CountDownLatch countDownLatch = new CountDownLatch(actualWorkThreadNum);

        /**
         * 这里用 0 来处理分片的开始。下面都加1 因为接口分页是从1开始的
         */
        for (int i = 0; i < actualWorkThreadNum; i++) {
            bulbasaurExecutorHelper.execute(new WorkerThread(pageAtomicInteger, countDownLatch, example));
        }
        logger.warn("------------------------------- 处理数据,主线程进入等待---------------------------");
        Preconditions.checkArgument(countDownLatch.await(defaultTimeOut(), TimeUnit.MINUTES), "处理数据异常!");
        logger.warn("------------------------------- 处理数据完成---------------------------");

    }

    private class WorkerThread implements Runnable {
        AtomicInteger pageAtomicInteger;
        CountDownLatch countDownLatch;
        T example;

        public WorkerThread(AtomicInteger pageAtomicInteger, CountDownLatch countDownLatch, T example
        ) {
            this.pageAtomicInteger = pageAtomicInteger;
            this.countDownLatch = countDownLatch;
            this.example = example;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    int i = pageAtomicInteger.decrementAndGet();
                    if (i <= 0) {
                        break;
                    }

                    logger.error(String.format("本次是该请求的第 [%s] 页数据", i));
                    List<M> list = query(i, example);

                    if (list != null && !list.isEmpty()) {
                        // 处理数据
                        shoot(list);
                    } else {
                        logger.error("这个页数怎么可能没有数据呢！！ i= " + i);
                        continue;
                    }

                    logger.info(String.format("处理数据第 [%s] 页完成。", i));

                }
                countDownLatch.countDown();
            } catch (Exception e) {
                logger.error(String
                    .format("查询数据失败！ 参数 = [%s] \n e = [%s]", ReflectionToStringBuilder.reflectionToString(example),
                        ExceptionUtils
                            .getStackTrace(e)));
            } finally {
                //等待主线程超时
            }

        }

    }

    protected abstract void shoot(List<M> list) throws ParseException;

    public abstract List<M> query(int pageNo, T example);

    protected int queryThreadNum() {
        return DEFAULT_THREAD_NUM;
    }

    protected abstract int querySupportPageSize();

    protected int defaultTimeOut() {
        return DEFAULT_TIME_OUT;
    }

    public abstract int queryTotalCount(T example);

}
