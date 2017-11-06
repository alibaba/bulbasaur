package com.tmall.pokemon.bulbasaur.schedule.quartz;

import java.util.Properties;

import com.tmall.pokemon.bulbasaur.persist.PersistHelper;
import com.tmall.pokemon.bulbasaur.persist.PersistModule;
import com.tmall.pokemon.bulbasaur.schedule.ScheduleModule;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * User:  yunche.ch ... (ว ˙o˙)ง
 * Date: 17/9/27
 * Time: 下午7:14
 */
public class BulbasaurScheduler extends SchedulerFactoryBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        prepare();
        super.afterPropertiesSet();
    }

    protected void prepare() {
        this.setDataSource(PersistModule.getInstance().getDataSource());
        this.setTransactionManager(PersistHelper.getTransactionManager());
        Properties properties = new Properties();
        // 设置表前缀
        properties.setProperty("org.quartz.jobStore.tablePrefix", ScheduleModule.getInstance().getQuartztablePrefix());
        properties.setProperty("org.quartz.scheduler.instanceName", "BatchScheduler");
        properties.setProperty("org.quartz.scheduler.instanceId", "AUTO");
        properties.setProperty("org.quartz.scheduler.skipUpdateCheck", "true");
        properties.setProperty("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1");
        properties.setProperty("org.quartz.jobStore.misfireThreshold", "60000");
        //properties.setProperty("org.quartz.jobStore.txIsolationLevelSerializable",true);
        properties.setProperty("org.quartz.jobStore.class",
            "org.springframework.scheduling.quartz.LocalDataSourceJobStore");
        properties.setProperty("org.quartz.jobStore.isClustered", "true");
        properties.setProperty("org.quartz.jobStore.useProperties", "false");
        properties.setProperty("org.quartz.jobStore.clusterCheckinInterval", "20000");
        //Configure ThreadPool
        properties.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        properties.setProperty("org.quartz.threadPool.threadCount", "10");
        properties.setProperty("org.quartz.threadPool.threadPriority", "5");
        properties.setProperty("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", "true");
        //Configure Plugins
        // properties.setProperty("org.quartz.plugin.triggHistory.class","org.quartz.plugins.history
        // .LoggingJobHistoryPlugin");
        //properties.setProperty("org.quartz.plugin.shutdownhook.class","org.quartz.plugins.management
        // .ShutdownHookPlugin");
        //properties.setProperty("org.quartz.plugin.shutdownhook.cleanShutdown", "true");

        this.setQuartzProperties(properties);
    }

}
