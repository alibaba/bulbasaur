package com.tmall.pokemon.bulbasaur.schedule;

import com.tmall.pokemon.bulbasaur.core.Bulbasaur;
import com.tmall.pokemon.bulbasaur.core.CoreModule;
import com.tmall.pokemon.bulbasaur.core.Module;
import com.tmall.pokemon.bulbasaur.persist.PersistModule;
import com.tmall.pokemon.bulbasaur.schedule.job.AnsyJob;
import com.tmall.pokemon.bulbasaur.schedule.model.Delay;
import com.tmall.pokemon.bulbasaur.schedule.model.Timer;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author yunche.ch@taobao.com
 * @Description:
 * @date 2012-12-6 下午1:16:23
 */
public class ScheduleModule extends Module implements InitializingBean, ApplicationListener<ContextRefreshedEvent> {
    private final static Logger logger = LoggerFactory.getLogger(ScheduleModule.class);
    public static ScheduleModule self = null;
    public static ApplicationContext applicationContext;
    // 默认是 QRTZ_
    private String quartztablePrefix = "QRTZ_";

    //是否删除过期job,默认不删除
    private Boolean deleteOverdueJob = false;

    public boolean getDeleteOverdueJob() {
        return deleteOverdueJob;
    }

    public void setDeleteOverdueJob(Boolean deleteOverdueJob) {
        this.deleteOverdueJob = deleteOverdueJob;
    }

    public ScheduleModule() {
    }

    public static ScheduleModule getInstance() {
        if (self == null) {
            self = new ScheduleModule();
        }
        return self;
    }

    @Override
    public Module[] require() {
        return new Module[] {CoreModule.getInstance(),
            PersistModule.getInstance()};
    }

    @Override
    public void afterInit() {
        AnsyJob.initExecutor();
        checkParam();
        //初始化 Timer 节点，set到coreModule里面去
        CoreModule.getInstance().setStateClasses(Timer.class, Delay.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        self = this;

    }

    private void checkParam() {
        // nothing
    }

    public String getQuartztablePrefix() {
        return quartztablePrefix;
    }

    public void setQuartztablePrefix(String quartztablePrefix) {
        this.quartztablePrefix = quartztablePrefix;
    }

    /**
     * 监听整个容器启动完成 ,整个容器启动完，外部上下文 和 bulbasaur 内部上下文的父子关系已经建立，才能子容器拿不到 拿父容器
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        /**
         * 只有根容器初始化完成搞一把，其他子容器比如spring mvc 完成就不要重复处理了
         */
        if (event.getApplicationContext().getParent() == null) {
            logger.warn("整个容器初始化完成，与业务容器对接完成！");
        }

    }
}
