package com.tmall.pokemon.bulbasaur.schedule.quartz;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 重写 quartz 的 QuartzJobBean 类
 * 原因是在使用 quartz+spring 把 quartz 的 task 实例化进入数据库时，会产生： serializable 的错误，原因在于：
 *
 * 这个 MethodInvokingJobDetailFactoryBean 类中的 methodInvoking 方法，是不支持序列化的，因此在把 QUARTZ 的 TASK 序列化进入数据库时就会抛错。网上有说把 SPRING
 * 源码拿来，修改一下这个方案，然后再打包成 SPRING.jar 发布，这些都是不好的方法，是不安全的。
 * 必须根据 QuartzJobBean 来重写一个自己的类，然后使用 SPRING 把这个重写的类（我们就名命它为： MyDetailQuartzJobBean ）注入 appContext 中后，再使用 AOP 技术反射出原有的
 * quartzJobx( 就是开发人员原来已经做好的用于执行 QUARTZ 的 JOB 的执行类 ) 。我们这个主要是通过反射机制调用实际要执行的操作方法，所以，调用的时候要符合反射机制的规范。
 *
 * User:  yunche.ch ... (ว ˙o˙)ง
 * Date: 17/4/21
 * Time: 下午10:21
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution // 不允许并发执行
public class MyDetailQuartzJobBean extends QuartzJobBean {
    protected final Log logger = LogFactory.getLog(getClass());
    private String targetObject;
    private String targetMethod;
    private ApplicationContext ctx;

    @Override
    protected void executeInternal(JobExecutionContext context)
        throws JobExecutionException {
        try {
            logger.info("execute [" + targetObject + "] at once>>>>>>");
            Object otargetObject = ctx.getBean(targetObject);
            Method m = null;

            try {
                m = otargetObject.getClass().getMethod(targetMethod, new Class[] {JobExecutionContext.class});
                m.invoke(otargetObject, new Object[] {context});
            } catch (SecurityException e) {
                logger.error(e);
            } catch (NoSuchMethodException e) {
                logger.error(e);
            }
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }



    public void setApplicationContext(ApplicationContext applicationContext) {
        this.ctx = applicationContext;
    }

    public void setTargetObject(String targetObject) {
        this.targetObject = targetObject;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }
}
