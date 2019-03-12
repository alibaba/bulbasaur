package com.tmall.pokemon.bulbasaur.schedule;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.common.collect.Maps;
import com.tmall.pokemon.bulbasaur.core.CoreModule;
import com.tmall.pokemon.bulbasaur.core.Module;
import com.tmall.pokemon.bulbasaur.persist.PersistHelper;
import com.tmall.pokemon.bulbasaur.persist.PersistModule;
import com.tmall.pokemon.bulbasaur.schedule.job.AnsyJob;
import com.tmall.pokemon.bulbasaur.schedule.model.Delay;
import com.tmall.pokemon.bulbasaur.schedule.model.Timer;
import com.tmall.pokemon.bulbasaur.schedule.process.BulbasaurCleanerPrcessor;
import com.tmall.pokemon.bulbasaur.schedule.process.BulbasaurJobCleanerProcessor;
import com.tmall.pokemon.bulbasaur.schedule.process.BulbasaurJobProcessor;
import com.tmall.pokemon.bulbasaur.schedule.quartz.MyDetailQuartzJobBean;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author yunche.ch@taobao.com
 * @Description:
 * @date 2012-12-6 下午1:16:23
 */
public class ScheduleModule extends Module
    implements InitializingBean, BeanDefinitionRegistryPostProcessor, ApplicationListener<ContextRefreshedEvent> {
    private final static Logger logger = LoggerFactory.getLogger(ScheduleModule.class);
    public static ScheduleModule self = null;

    /**
     * 是否删除过期job,默认不删除
     */
    private Boolean deleteOverdueJob = false;

    /**
     * 自动清理process，防止过度膨胀
     */
    private List<String> autoCleanDefinitionNameList;

    public boolean getDeleteOverdueJob() {
        return deleteOverdueJob;
    }

    public void setDeleteOverdueJob(Boolean deleteOverdueJob) {
        this.deleteOverdueJob = deleteOverdueJob;
    }

    public List<String> getAutoCleanDefinitionNameList() {
        return autoCleanDefinitionNameList;
    }

    public void setAutoCleanDefinitionNameList(List<String> autoCleanDefinitionNameList) {
        this.autoCleanDefinitionNameList = autoCleanDefinitionNameList;
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
    public void afterInit(String ownSign, String quartzTablePrefix) {
        AnsyJob.initExecutor();
        checkParam();
        //初始化 Timer 节点，set到coreModule里面去
        CoreModule.getInstance().setStateClasses(Timer.class, Delay.class);
        registerQuartz(ownSign, quartzTablePrefix);
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

    /**
     * 因为存在group分组，避免不同应用使用bulbasuar 并使用相同库，而产生trigger 等相互覆盖 只有在整个容器初始化完成后，才有  xxxxx 再初始化quartz
     */
    private void registerQuartz(String ownSign, String quartzTablePrefix) {
        registerJobDetails(ownSign);
        registerTriggers(ownSign);
        registerScheduler(ownSign, quartzTablePrefix);
    }

    private static String lowerFirst(String param) {
        char[] chars = param.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private static String suffixDetail(String param) {
        return lowerFirst(param) + "Detail";
    }

    private static String suffixTrigger(String param) {
        return lowerFirst(param) + "Trigger";
    }

    public final static String BULBASAUR_JOB_EXECUTE = "execute";

    private void registerJobDetails(String ownSign) {

        registerJobDetail(ownSign, lowerFirst(BulbasaurJobProcessor.class.getSimpleName()), BULBASAUR_JOB_EXECUTE,
            suffixDetail(BulbasaurJobProcessor.class.getSimpleName()));

        registerJobDetail(ownSign, lowerFirst(BulbasaurCleanerPrcessor.class.getSimpleName()), BULBASAUR_JOB_EXECUTE,
            suffixDetail(BulbasaurCleanerPrcessor.class.getSimpleName()));

        registerJobDetail(ownSign, lowerFirst(BulbasaurJobCleanerProcessor.class.getSimpleName()),
            BULBASAUR_JOB_EXECUTE,
            suffixDetail(BulbasaurJobCleanerProcessor.class.getSimpleName()));
    }

    private void registerTriggers(String ownSign) {

        registerTrigger(ownSign, "[bulbasaur]job不间断尝试执行", suffixDetail(BulbasaurJobProcessor.class.getSimpleName()),
            "0/30 * * * * ?",
            suffixTrigger(BulbasaurJobProcessor.class.getSimpleName()));

        registerTrigger(ownSign, "[bulbasaur]定时清理执行完的流程和节点",
            suffixDetail(BulbasaurCleanerPrcessor.class.getSimpleName()),
            "0 0 0/6 * * ?",
            suffixTrigger(BulbasaurCleanerPrcessor.class.getSimpleName()));

        registerTrigger(ownSign, "[bulbasaur]定时清理job", suffixDetail(BulbasaurJobCleanerProcessor.class.getSimpleName()),
            "0 0 0/2 * * ?",
            suffixTrigger(BulbasaurJobCleanerProcessor.class.getSimpleName()));
    }

    private void registerScheduler(String ownSign, String quartzTablePrefix) {

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(
            SchedulerFactoryBean.class);

        prepare(beanDefinitionBuilder, quartzTablePrefix);

        org.quartz.Trigger[] triggers = {
            (Trigger)beanFactory.getBean(suffixTrigger(BulbasaurJobProcessor.class.getSimpleName())),
            (Trigger)beanFactory.getBean(suffixTrigger(BulbasaurCleanerPrcessor.class.getSimpleName())),
            (Trigger)beanFactory.getBean(suffixTrigger(BulbasaurJobCleanerProcessor.class.getSimpleName())),

        };

        beanDefinitionBuilder.addPropertyValue("triggers", triggers);

        registry.registerBeanDefinition(ownSign + "BulbasaurScheduler",
            beanDefinitionBuilder.getRawBeanDefinition());

        logger.warn("****** BulbasaurScheduler init over ******");

    }

    protected void prepare(BeanDefinitionBuilder beanDefinitionBuilder,
                           String quartzTablePrefix) {

        beanDefinitionBuilder.addPropertyValue("applicationContextSchedulerContextKey", "applicationContext");

        //设置自动启动
        beanDefinitionBuilder.addPropertyValue("autoStartup", true);

        //需要overwrite已经存在的job，如果需要动态的修改已经存在的job，就需要设置为true，否则会以数据库中已经存在的为准
        beanDefinitionBuilder.addPropertyValue("overwriteExistingJobs", true);

        beanDefinitionBuilder.addPropertyValue("dataSource", PersistModule.getInstance().getDataSource());

        beanDefinitionBuilder.addPropertyValue("transactionManager", PersistHelper.getTransactionManager());

        Properties properties = new Properties();
        // 设置表前缀
        properties.setProperty("org.quartz.jobStore.tablePrefix", quartzTablePrefix);
        properties.setProperty("org.quartz.scheduler.instanceName", "bulbasaurSchedule");
        properties.setProperty("org.quartz.scheduler.instanceId", "AUTO");
        properties.setProperty("org.quartz.scheduler.skipUpdateCheck", "true");

        //Subclass of Quartz's JobStoreCMT class
        properties.setProperty("org.quartz.jobStore.class",
            "org.springframework.scheduling.quartz.LocalDataSourceJobStore");

        //在给定的通行证中，工作区将处理的最大错误次数触发。一次处理很多（超过几十打）可能导致数据库表被锁定得足够长，以致可能会阻碍其他（未失败的）Triggers触发的性能。
        properties.setProperty("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1");

        //在被认为“misfired”之前，调度程序将“tolerate”一个Triggers将其下一个启动时间通过的毫秒数。默认值（如果您在配置中未输入此属性）为60000（60秒）。
        properties.setProperty("org.quartz.jobStore.misfireThreshold", "60000");

        //值“true”告诉Quartz 在JDBC连接上调用setTransactionIsolation（Connection
        // .TRANSACTION_SERIALIZABLE）。这可以有助于防止在高负载下的某些数据库的锁定超时以及“持久”事务。
        properties.setProperty("org.quartz.jobStore.txIsolationLevelSerializable", "true");

        //设置为“true”以打开群集功能。如果您有多个Quartz实例使用同一组数据库表，则此属性必须设置为“true”，否则您将遇到破坏。
        properties.setProperty("org.quartz.jobStore.isClustered", "true");

        //“使用属性”标志指示JDBCJobStore，JobDataMaps中的所有值都将是“字符串”，因此可以将其存储为名称 -
        // 值对，而不是以BLOB列的序列化形式存储更复杂的对象。这可以方便，因为您避免了将非String类序列化为BLOB时可能产生的类版本控制问题。
        properties.setProperty("org.quartz.jobStore.useProperties", "false");

        //设置此实例“检入”*与群集的其他实例的频率（以毫秒为单位）。影响检测失败实例的速度。
        properties.setProperty("org.quartz.jobStore.clusterCheckinInterval", "20000");

        //Configure ThreadPool
        properties.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        properties.setProperty("org.quartz.threadPool.threadCount", "3");
        properties.setProperty("org.quartz.threadPool.threadPriority", "5");
        properties.setProperty("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", "true");

        beanDefinitionBuilder.addPropertyValue("quartzProperties", properties);
    }

    private void registerTrigger(
        String ownSign,
        String name,
        String jobDetailBeanName,
        String cronExpression,
        String triggerName

    ) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(
            CronTriggerFactoryBean.class);
        builder.addPropertyValue("name", name);
        builder.addPropertyValue("group", ownSign);
        builder.addPropertyValue("jobDetail", beanFactory.getBean(jobDetailBeanName));
        builder.addPropertyValue("cronExpression", cronExpression);
        registry.registerBeanDefinition(triggerName, builder.getRawBeanDefinition());

    }

    private void registerJobDetail(
        String ownSign,
        String targetObject,
        String targetMethod,
        String jobDetailBeanName

    ) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(
            JobDetailFactoryBean.class);
        builder.addPropertyValue("jobClass", MyDetailQuartzJobBean.class);
        builder.addPropertyValue("group", ownSign);
        //durability 表示任务完成之后是否依然保留到数据库，默认false
        builder.addPropertyValue("durability", "true");

        Map<String, String> jobDataAsMap = Maps.newHashMap();
        jobDataAsMap.put("targetObject", targetObject);
        jobDataAsMap.put("targetMethod", targetMethod);

        builder.addPropertyValue("jobDataAsMap", jobDataAsMap);
        registry.registerBeanDefinition(jobDetailBeanName, builder.getRawBeanDefinition());

    }

    private ConfigurableListableBeanFactory beanFactory;
    private BeanDefinitionRegistry registry;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        this.registry = registry;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
