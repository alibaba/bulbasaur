package com.tmall.pokemon.bulbasaur.core;

import com.tmall.pokemon.bulbasaur.core.invoke.Invokable;
import com.tmall.pokemon.bulbasaur.core.invoke.InvokableFactory;
import com.tmall.pokemon.bulbasaur.core.invoke.MvelScriptInvokable;
import com.tmall.pokemon.bulbasaur.core.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

/**
 * 核心module
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-3 上午11:52:34
 */
public class CoreModule extends Module implements InitializingBean {
    private final static Logger logger = LoggerFactory.getLogger(CoreModule.class);
    private static CoreModule self = null;
    private String ownSign = "default";
    private boolean shutDown = false;
    /**
     * *****************************************************************
     * 这两个默认值用来创建一个无界的动态线程池，相当于调用Executors.newCachedThreadPool()产生的
     * 之所以手动创建的好处是允许用户自定义大小
     * ******************************************************************
     */
    private int corePoolSize = 0;
    private int maximumPoolSize = Integer.MAX_VALUE;
    private Parser parser;
    private Place place;

    protected CoreModule() {
    }

    public synchronized static CoreModule getInstance() {
        if (self == null) {
            self = new CoreModule();
        }
        return self;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public String getOwnSign() {
        return ownSign;
    }

    public void setOwnSign(String ownSign) {
        this.ownSign = ownSign;
    }

    public boolean isShutDown() {
        return shutDown;
    }

    public void shutDown() {
        shutDown = true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterInit(String ownSign, String quartzTablePrefix) {
        logger.warn(this.toString());
        // 初始化节点类型
        setStateClasses(Start.class, State.class, Event.class, BizInfo.class);
        // 初始化调用方式类型
        setInvokableClasses(MvelScriptInvokable.class);
        place = Bulbasaur.getInnerBeanFactory().getBean("place", Place.class);
        if (parser != null) {
            logger.warn("a customer parser provide:" + parser.getClass().getName());
            place.setParser(parser);
        }
        // 初始化Invokable的Executor,默认线程个数10，20
        Invokable.initExecutor(corePoolSize, maximumPoolSize);
    }

    /**
     * 注册State的class 用来解析和执行模板中的自定义的state
     *
     * @param stateClasses state class array
     */
    public void setStateClasses(Class<? extends StateLike>... stateClasses) {
        for (Class<? extends StateLike> clazz : stateClasses) {
            StateFactory.applyState(clazz);
        }
    }

    /**
     * 注册Invokable的class 用来解析和执行模板中的自定义invoke
     *
     * @param invokableClasses invoke class array
     */
    public void setInvokableClasses(Class<? extends Invokable>... invokableClasses) {
        for (Class<? extends Invokable> clazz : invokableClasses) {
            InvokableFactory.applyInvokable(clazz);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        self = this;
    }

    /**
     * 设置parser 在初始化之前和之后都有效
     *
     * @param parser
     * @since 2012-12-27 下午06:48:19
     */
    public void setParser(Parser parser) {
        this.parser = parser;
        // 这个时候Place的bean已经初始化好了，这里要更改
        if (place != null) {
            place.setParser(parser);
        }
    }

    public void setSpringContext(ApplicationContext context) {
        Bulbasaur.setSpringContext(context);
    }

    @Override
    public String toString() {
        return "CoreModule{" + "ownSign='" + ownSign + '\'' + ", corePoolSize=" + corePoolSize + ", maximumPoolSize="
            + maximumPoolSize + ", parser=" + parser + '}';
    }

}
