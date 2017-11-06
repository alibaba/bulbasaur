package com.tmall.pokemon.bulbasaur.core.invoke;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 调用接口，并可异步执行mvel的脚本调用
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-27 下午06:37:55
 */
public abstract class Invokable {
    private final static Logger logger = LoggerFactory.getLogger(Invokable.class);

    protected boolean async = false;
    protected String returnKey;

    public Invokable init(Element elem) {
        returnKey = elem.attributeValue("return");
        return this;
    }

    public String getReturnKey() {
        return returnKey;
    }

    private static ExecutorService executorService;

    /**
     * 初始化Executor
     *
     * @author: yunche.ch@taobao.com
     * @since 2012-12-27 下午06:38:19
     */
    public static void initExecutor(int corePoolSize, int maximumPoolSize) {
        executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>());
        logger.warn(
            "init async threadPool for Invokable component, thread num:" + corePoolSize + "-" + maximumPoolSize);
    }

    public Object invoke(Map<String, Object> context) {
        if (async) {
            executorService.execute(new Go(this, context));
            return null;
        } else {
            return invoke0(context);
        }
    }

    /**
     * 子类实现了
     *
     * @param context
     * @return
     * @author: yunche.ch@taobao.com
     * @since 2012-12-27 下午06:38:52
     */
    public abstract Object invoke0(Map<String, Object> context);

    private static class Go implements Runnable {
        protected Map<String, Object> context;
        protected Invokable invokable;

        public Go(Invokable invokable, Map<String, Object> context) {
            this.invokable = invokable;
            this.context = context;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            logger.debug("异步执行,context为：" + context);
            try {
                invokable.invoke0(context);
            } catch (Throwable e) {
                logger.error(String.format("异步执行invoke方法发生错误!  context=%s,e=%s", context, e.getMessage()), e);
            }
        }

    }
}
