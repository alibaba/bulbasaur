package com.alibaba.pokemon.bulbasaur.core;

import java.util.*;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 启动类，负责计算，初始化 modules.
 * 读音 Bulbasaur ['bʌlba:sɑu]
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-3 上午11:58:47
 */
public class Bulbasaur implements InitializingBean, BeanFactoryPostProcessor, ApplicationContextAware {
    private final static Logger logger = LoggerFactory.getLogger(Bulbasaur.class);

    // bulbasaur 自身容器
    private static BeanFactory innerBeanFactory;
    // 外部容器的ctx
    private static ApplicationContext applicationContext;
    private static boolean initialized = false;
    private static Map<Module, Integer> dependencies = new HashMap<Module, Integer>();
    private static boolean springContextShare = true;
    /**
     * 与CoreModule一样的，为了ScheduleModule的quartz
     */
    private String ownSign = "default";

    // 默认是 QRTZ_
    private String quartzTablePrefix = "QRTZ_";
    // 由spring中配置
    private Module[] requireModule;

    public String getQuartzTablePrefix() {
        return quartzTablePrefix;
    }

    public void setQuartzTablePrefix(String quartzTablePrefix) {
        this.quartzTablePrefix = quartzTablePrefix;
    }

    public String getOwnSign() {
        return ownSign;
    }

    public void setOwnSign(String ownSign) {
        this.ownSign = ownSign;
    }

    public static void require(String ownSign, String quartzTablePrefix) {
        require(new Module[] {CoreModule.getInstance()}, ownSign, quartzTablePrefix);
    }

    /**
     * 留给接口，可以不依赖spring配置
     *
     * @param modules
     * @since 2012-12-27 下午06:45:14
     */
    public synchronized static void require(Module[] modules, String ownSign, String quartzTablePrefix) {
        if (initialized) {
            logger.warn("Bulbasaur already initialized!");
        } else {
            if (modules == null || modules.length < 1) {
                modules = new Module[] {CoreModule.getInstance()};
            }
            // 解析modules
            // 得到需要加载的列表
            for (Module module : modules) {
                List<Module> list = new ArrayList<Module>();
                checkRequire(module, list, dependencies);
            }
            // 计算依赖顺序
            for (Module module : dependencies.keySet()) {
                calcRequire(module, dependencies);
            }
            dependencies = sortByValue(dependencies);
            // init spring first
            innerBeanFactory = initSpring(dependencies.keySet());
            // then init module object themselves
            for (Module toInit : dependencies.keySet()) {
                logger.warn("initialize " + toInit.getClass().getSimpleName());
                toInit.init(ownSign, quartzTablePrefix);
                logger.warn(toInit.getClass().getSimpleName() + " initialized");
            }
        }
        initialized = true;
    }

    /**
     * 按值排序
     *
     * @param <K>
     * @param <V>
     * @param map
     * @return Map<K
                       *       ,
                       *       V>
     * @since 2012-12-5 下午2:19:38
     */
    public static <K, V extends Comparable<V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Entry<K, V>> list = new LinkedList<Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Entry<K, V>>() {
            @Override
            public int compare(Entry<K, V> o2, Entry<K, V> o1) {

                Comparable<V> v1 = o1.getValue();
                V v2 = o2.getValue();
                if (v1 == null) {
                    if (v2 == null) {
                        return 0;
                    } else {
                        return -1;
                    }
                } else {
                    if (v2 == null) {
                        return 1;
                    } else {
                        return v1.compareTo(v2);
                    }
                }
            }
        });
        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * 每次依赖到，计数加一
     *
     * @return void
     * @author: yunche.ch@taobao.com
     * @since 2012-12-4 上午11:37:39
     */
    private static void calcRequire(Module module, Map<Module, Integer> dependencies) {
        Module[] requireList = module.require();
        List<Module> requireListAsList = java.util.Arrays.asList(requireList);// 不可变的必须list
        List<Module> optionalRequireList = new ArrayList<Module>();
        for (Module in : module.optionalRequire()) {
            // 因为可选，所以在必须列表中没用，则不加入计算列表；如果在必须列表中有，则加到必须列表中，计算时候优先加一次
            if (dependencies.containsKey(in)) {
                optionalRequireList.add(in);
            }

        }

        optionalRequireList.addAll(requireListAsList);// 合并两个list
        for (Module requireModule : optionalRequireList) {
            int requirePriorityCount = dependencies.get(requireModule);
            // calc priorityCount
            dependencies.put(requireModule, requirePriorityCount + 1);
            calcRequire(requireModule, dependencies);
        }

    }

    /**
     * 得到所有依赖的module列表
     *
     * @return void
     * @author: yunche.ch@taobao.com
     * @date 2012-12-3 上午11:37:57
     */
    private static void checkRequire(Module module, List<Module> checkModuleList, Map<Module, Integer> dependencies) {
        // check cycle dependency
        // 传进来的module已经在checkModuleList里面，则为循环依赖，挂掉
        if (checkModuleList.contains(module)) {
            logger.info("find cycle dependency at " + module.getClass().getSimpleName());
            return;// 阻止继续进行

        }
        if (!dependencies.containsKey(module)) {
            dependencies.put(module, 0);
        }
        // 遍历依赖的module列表
        for (Module requireModule : module.require()) {
            checkModuleList.add(module);
            checkRequire(requireModule, checkModuleList, dependencies);
        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        if (springContextShare) {
            if (beanFactory.getParentBeanFactory() == null) {
                beanFactory.setParentBeanFactory(innerBeanFactory);
            } else {
                logger.warn("Had set Parent applicationContext!");
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.warn("require module from spring:" + Arrays.toString(requireModule));
        if (requireModule == null || requireModule.length == 0) {
            require(ownSign, quartzTablePrefix);
        } else {
            require(requireModule, ownSign, quartzTablePrefix);
        }

        logger.warn("afterPropertiesSet initialized");
    }

    /**
     * 初始化容器，读取classpath下的给定名称的流程模板，并初始化。
     *
     * @return ApplicationContext
     * @author: yunche.ch@taobao.com
     * @since 2012-12-20 下午05:00:08
     */
    private static BeanFactory initSpring(Set<Module> requireModuleList) {
        long start = System.currentTimeMillis();
        String[] config = new String[dependencies.keySet().size()];
        int i = 0;
        for (Module m : requireModuleList) {
            config[i] = "classpath:" + m.getClass().getSimpleName() + ".xml";
            i++;
        }

        /**
         * 本来是可以返回 ApplicationContext 的，但是业务方是 pandora boot 的时候，加载 handlerExceptionResolver 的时候
         * 会报错 org.springframework.context.support.ClassPathXmlApplicationContext  不是实例化来自
         * class org.springframework .beans.factory.support.DefaultListableBeanFactory
         */
        ApplicationContext temp = new ClassPathXmlApplicationContext(config);

        //将applicationContext转换为ConfigurableApplicationContext
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext)temp;

        // 获取bean工厂并转换为DefaultListableBeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory
            = (DefaultListableBeanFactory)configurableApplicationContext.getBeanFactory();

        logger.warn(
            "******Bulbasaur inner spring context initialized, spendTime:" + (System.currentTimeMillis() - start)
                + " ms");
        return defaultListableBeanFactory;
    }

    public static boolean isSpringContextShare() {
        return springContextShare;
    }

    public void setSpringContextShare(boolean springContextShare) {
        Bulbasaur.springContextShare = springContextShare;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Bulbasaur.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static BeanFactory getInnerBeanFactory() {
        return innerBeanFactory;
    }

    public static void setSpringContext(ApplicationContext applicationContext) {
        Bulbasaur.applicationContext = applicationContext;
    }

    public Module[] getRequireModule() {
        return requireModule;
    }

    public void setRequireModule(Module[] requireModule) {
        this.requireModule = requireModule;
    }
}
