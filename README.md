# 轻量级流程引擎    
`读音 Bulbasaur ['bʌlba:sɑu]`    

----

# Bulbasaur QuickStart    

## 简介:      
bulbasuar分为四个模块，`按需加载`使用。分别为：    
> `核心模块 bulbasaur-core`     [提供核心流程]     
> `持久化模块 bulbasaur-persist`   [提供流程的存储和失败回滚]    
> `调度模块 bulbasaur-schedule`     [提供失败重试，定时等调度逻辑]    
> `任务模块 bulbasaur-task`     [提供人工任务和超时自动执行，目前已经支持单人单任务，多人单任务]        


## 概述：    
如果你只是希望使用基于`内存`的流程引擎，那么只要使用`核心模块`即可，流程模板以文件形式维护在业务方系统   
如果希望流程引擎有持久化的流程实例和节点，那么要使用 `核心模块` + `持久化模块`    
如果希望流程引擎有节点失败重试，定时等功能，那么要使用`核心模块` + `持久化模块` + `调度模块`    
如果希望任务审批，多人审批的能力，那么要使用`核心模块` + `持久化模块` + `任务模块` ，至于要不要`调度模块`都可以

其中，除了`核心模块`外，其他模块都需要业务方建表支持。
    
`持久化模块 bulbasaur-persist`  需要表：    

    xx_bulbasaur_d :  模板
    xx_bulbasaur_p ： 流程实例
    xx_bulbasaur_s ： 节点    
    
`调度模块 bulbasaur-schedule`  需要表：    

    xx_bulbasaur_j :  重试/定时 任务 
    
  
`任务模块 bulbasaur-task`  需要表：    

    xx_bulbasaur_t :  审批任务 
    xx_bulbasaur_ptp :  多人审批任务
    
以上表需要建在业务方库中，表名可以业务方指定，流程引擎可以识别，比如：业务方库中表都有统一前缀，那么流程引擎表可以都带上统一前缀    
流程引擎使用quartz做分布式调度，也需要在业务方库中建表。
注意： `建表语句文末提供`    



## 在项目中引入bulbasaur配置，最全配置如下（业务方根据引人模块酌情配置）： 

	
	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xmlns:context="http://www.springframework.org/schema/context"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
	   					http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-2.5.xsd"
	   default-autowire="byName">

	<context:annotation-config/>

	<!--提供核心流程-->
	<bean id="coreModule" class="com.tmall.pokemon.bulbasaur.core.CoreModule">
		<!--用于区分不同环境-->
		<property name="ownSign" value="业务方配置执行，可以区分不同环境隔离"/>
	</bean>
	<!--提供流程的存储和失败回滚-->
	<bean id="persistModule" class="com.tmall.pokemon.bulbasaur.persist.PersistModule">
		<property name="dataSource" ref="dataSource"/>
		<!-- 默认为true走DB，不走DB 加上下面属性,并置为false -->
		<!--<property name="usePersistParser" value="false"/>-->
		<!--业务方可自定义表名，在此配置-->
		<property name="tableNameP" value="xxxx_bulbasaur_p"/>
		<property name="tableNameS" value="xxxx_bulbasaur_s"/>
		<property name="tableNameD" value="xxxx_bulbasaur_d"/>
		<property name="tableNameJ" value="xxxx_bulbasaur_j"/>
		<property name="tableNameT" value="xxxx_bulbasaur_t"/>
		<property name="tableNamePtp" value="xxxx_bulbasaur_ptp"/>

	</bean>
	
    <!--提供人工任务和超时自动执行,提供失败重试，定时等调度逻辑 -->
	<bean id="scheduleModule" class="com.tmall.pokemon.bulbasaur.schedule.ScheduleModule">
		<!-- 默认不删除超过最大重试次数的job ，不设置则为false，这里为 true(删除)-->
		<property name="deleteOverdueJob" value="true"/>
		<!-- 指定quartz的表名前缀，如果不指定则默认为 QRTZ_ -->
		<property name="quartztablePrefix" value="xxxx"/>
	</bean>
	
	<!--提供人工审批-->
    <bean id="taskModule" class="com.tmall.pokemon.bulbasaur.task.TaskModule"/>

	<!-- 引擎主bean，所有模块都用的时候 -->
	<bean id="bulbasaur" class="com.tmall.pokemon.bulbasaur.core.Bulbasaur">
		<property name="requireModule">
			<list>
			    <ref bean="coreModule"/>
				<ref bean="persistModule"/>
				<ref bean="scheduleModule"/>
				<ref bean="taskModule"/>
			</list>
		</property>
	</bean>

	<!--业务方单库数据源-->
	
	<!-- 可以是tddl --> 
	<bean id="dataSource" class="com.taobao.tddl.jdbc.group.TGroupDataSource"
    		init-method="init">
    		<property name="appName" value="xxx" />
    		<property name="dbGroupKey" value="xxx" />
    </bean>
    
     <!-- 也可以其他直连池 --> 
    <!--<bean id="dataSource"-->
          <!--class="org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy">-->
        <!--<property name="targetDataSource">-->
            <!--<ref local="dataSourceImpl"/>-->
        <!--</property>-->
    <!--</bean>-->
    <!--<bean id="dataSourceImpl" class="com.mchange.v2.c3p0.ComboPooledDataSource"-->
          <!--abstract="false" scope="singleton" lazy-init="default" autowire="default"-->
          <!--dependency-check="default" destroy-method="close">-->
        <!--<property name="driverClass" value="com.mysql.jdbc.Driver"/>-->
        <!--<property name="jdbcUrl" value="jdbc:mysql://xxx.xxx.xxx.xxx:3306/xxx?characterEncoding=utf8"/>-->
        <!--<property name="user" value="xxx"/>-->
        <!--<property name="password" value="xxx"/>-->
        <!--<property name="minPoolSize" value="10"/>-->
        <!--<property name="maxIdleTime" value="1800"/>-->
        <!--<property name="initialPoolSize" value="20"/>-->
    <!--</bean>-->

	</beans>


## 代码中使用:    


	 try {	 
	 		//基于内存的流程
			//Machine m = machineFactory.newInstace("业务唯一id","流程名称xxx"); 
			//持久化流程 
			//PersistMachine m = persistMachineFactory.newInstance("业务唯一id","流程名称xxx");
			//可调度流程
			ScheduleMachine m = scheduleMachineFactory.newInstance("业务唯一id", "流程名称xxx");
				
			m.addContext("goto", 2);// 可放入流程中的变量，上文中可用
			m.addContext("_i", 3);// 会被持久化的流程变量，因为带了  "_" 开头
			m.run();
		} catch (Exception e) {
			System.out.println(e);
		}    
	
			
## 功能节点介绍

`start` : 特殊开始节点        
`state` : 直接执行invokes内容，并根据paths中配置执行下一个节点，如果配置了多个path，则可以根据表达式判断走哪条    
`event` ： 先执行pre-invokes，并暂停。需要外部再次触发，继续执行invokes 。其中 pre-invokes 和invokes 按需不配置    
`task`  ： 审批流节点，可以在节点上直接配置审批人列表，或者配置服务从业务方获取审批人列表     

## 流程模板示例（本例子比较全，业务方可以按需使用）:      
	
	<process name="process">
	<start name="i'm start">
		<paths>
			<path to="state1"/>
		</paths>
	</start>
	<state name="state1">
		<paths>
			<path to="state2" expr="goto==2"/>
			<path to="state3" expr="goto==3"/>
		</paths>
	</state>
	 <event name="state2">
            <pre-invokes>
                <script return="info">
                    "some info"
                </script>
            </pre-invokes>
            <invokes>
                 <script return="a" pojos="test.TestBean -> test, test.TestBean2 -> test2">
                   test2.testMethod(test.testMethod(i))
                  </script>
            </invokes>
            <paths>
                <path to="state3" expr="goto==2"/>
                <path to="state4" expr="goto==3"/>
            </paths>
        </event>
	<state name="state3" repeatList="1m 2m 3m 2m 1m">
		<!--重复时间的列表，支持 m分钟，h小时，d天-->
		<invokes>
			<script return="_a" pojos="test.TestBean2 -> test2" expr="test2.judge()">
				test2.testMethod(2)
			</script>
		</invokes>
		<paths>
			<path to="state4"/>
		</paths>
	</state>
	<task name="state4" candidate-users="00001:测试人员">
		<!--candidate-users 可配置列表，: , 分割，例如 00001:测试人员,00002:初审人员-->
		<pre-invokes>
			<script return="_a" beans="notifyPage" async="true">
				<!--业务方接受处理上下文传递出来的task列表，可以做通知-->
				notifyPage.doNotify(taskIds)
			</script>
		</pre-invokes>
		<invokes>
			<script return="_a" pojos="test.TestBean2 -> test2" expr="test2.judge()">
				test2.testState4(6)
			</script>
		</invokes>
		<!--由业务bean，提供审批人列表，格式同candidate-users-->
		<assignment-handler>
			<script return="_a" pojos="test.TestBean2 -> test2" expr="test2.judge()">
				test2.getUsers(2)
			</script>
		</assignment-handler>

		<!--配置超时节点，必须配置超时时间，这里不给xml的配置方式，只由业务方提供接口，并返回特定map -->
		<!--<timeout-handler>-->
		<!--<script return="_a" pojos="test.TestBean2 -> test2" expr="test2.judge()">-->
		<!--test2.getUsers(2)-->
		<!--例如：-->
		<!--map.put("ignoreWeekend", "true");//是否忽略周末-->
		<!--map.put("outGoing", "state3");//超时去向-->
		<!--map.put("period", "2minute");//超时时间-->
		<!--</script>-->
		<!--</timeout-handler>-->

		<paths>
			<path to="end"/>
		</paths>
	</task>
	<state name="state5">
		<paths>
			<path to="end"/>
		</paths>
	</state>
	<state name="end"/>
	</process>
	
	
		
	
------	

# 相关重点说明：     

------  

## 关于调度依赖分布式quartz
流程引擎使用了quartz，通过数据库完成分布式调度。配置：quartz.xml      

系统内存在的调度处理逻辑：

1. `bulbasaurJobProcessor `   
任务读取的频率为每30s一次，那么流程引擎中 "失败重试"，"定时"，"超时"等逻辑，最小时间单位就是30s    
使用人员可以根据业务情况自行修改减小频率    

2. `bulbasaurCleanerTrigger`    
清理已经完成的流程和其所关联的节点以及job，如果不需要，可以直接在配置中删除触发    

3. `bulbasaurJobCleanerTrigger`    
单独清理已经完成的job数据，如果不需要，可以直接在配置中删除触发    


## 关于重试:      

repeatList="1m 2m 3m 2m 1m" ，支持分(m)，小时(h)和天(d)，该示例意思为:重试5次，每次间隔分别为1分钟，2分钟，3分钟...      
  
## task节点:   

1. pre-invokes 该配置，业务方可以配置bean，接收参数为string，逗号分割的taskId，业务可以处理该task，给用户发旺旺消息，邮件等审核链接        

2. candidate-users  可配置列表，: , 分割，例如 00001:测试人员,00002:初审人员     

3. assignment-handler 配置获得处理人，业务bean返回的格式同 candidate-users ，这里bulbasaur只关注id 和name，业务系统自行对接hecla或uic或subaccount。其中 `assignment-handler`  获取的内容优先级高于 `candidate-users` 。

4. 如果 `candidate-users `没有配置，并且`assignment-handler` 返回null，则任务该任务节点不需要审核，直接往下走

5. timeout-handler 配置超时的策略，业务bean返回给bulbasaur一个Map<String,Object>，key分别为：period[超时时间]，outGoing[超时之后去到的节点名]，ignoreWeekend[是否忽略周末]。


## 任务处理接口    

> com.tmall.pokemon.bulbasaur.task.service.TaskAccessor.hasTaskTakenPermission 判断用户是否有权限申领指定的任务    

> com.tmall.pokemon.bulbasaur.task.service.TaskAccessor.takenTask 申领一个任务    
   
> com.tmall.pokemon.bulbasaur.task.service.TaskAccessor.releaseTask  释放一个任务，任务状态转为 created    

> com.tmall.pokemon.bulbasaur.task.service.TaskAccessor.hasTaskDealPermission 判断用户是否有权限处理给定的任务    

> com.tmall.pokemon.bulbasaur.task.service.TaskAccessor.completeTask(java.lang.Long, java.lang.String, com.tmall.pokemon.bulbasaur.task.model.User, java.lang.String) 完成一个任务，内部再次run    

> com.tmall.pokemon.bulbasaur.task.service.TaskAccessor.assignTaskWithResult 当前所有人将任务转给指定用户    

> com.tmall.pokemon.bulbasaur.task.service.TaskAccessor.queryTasksByUser 业务方可以使用该接口，获取审批人的任务列表，支持单人单任务，多人单任务    

> com.tmall.pokemon.bulbasaur.task.service.TaskAccessor.queryTasks 查询任务    

> com.tmall.pokemon.bulbasaur.task.service.TaskAccessor.queryTaskByBizId 查询任务
    
> com.tmall.pokemon.bulbasaur.task.service.TaskAccessor.update(java.lang.Long, java.lang.String, java.lang.String) 更新处理结果和意见

> com.tmall.pokemon.bulbasaur.task.service.TaskAccessor.update(java.lang.Long, java.lang.Object) 按taskId，全量覆盖bizInfo    




## 建表DDL    



#### 模板表


    CREATE TABLE `xxxx_bulbasaur_d` (
      `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
      `definition_name` varchar(64) NOT NULL COMMENT 'definition name',
      `definition_version` int(11) NOT NULL DEFAULT '1' COMMENT 'definition version',
      `own_sign` varchar(64) NOT NULL COMMENT 'sign to separate between different runtime or app',
      `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'status',
      `content` text COMMENT 'definition content',
      `gmt_create` datetime NOT NULL COMMENT 'gmt_create',
      `gmt_modified` datetime DEFAULT NULL COMMENT 'gmt_modified',
      `definition_alias` varchar(64) DEFAULT NULL COMMENT '别名，主要维护中文名称',
      PRIMARY KEY (`id`),
      KEY `idx_name_ownsign_version` (`definition_name`,`own_sign`,`definition_version`)
    ) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='流程模板'
    ;


#### 流程表

 
    CREATE TABLE `xxxx_bulbasaur_p` (
      `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id\n',
      `biz_id` varchar(64) NOT NULL COMMENT 'biz id from biz system',
      `definition_name` varchar(64) NOT NULL COMMENT 'definition name, see pikachu_d',
      `definition_version` int(11) DEFAULT NULL COMMENT 'definition version, see pikachu_d',
      `own_sign` varchar(64) NOT NULL COMMENT 'sign to separate between different runtime or app',
      `current_state_name` varchar(64) DEFAULT NULL COMMENT 'this process''''s current state''''s name',
      `status` varchar(64) DEFAULT NULL COMMENT 'this process''''s current state',
      `gmt_create` datetime NOT NULL COMMENT 'gmt_create',
      `gmt_modified` datetime DEFAULT NULL COMMENT 'gmt_modified',
      `alias` varchar(64) DEFAULT NULL COMMENT '别名，主要维护中文名',
      PRIMARY KEY (`id`),
      UNIQUE KEY `uk_biz_id_own_sign` (`biz_id`,`own_sign`),
      KEY `idx_bizid_ownsign` (`biz_id`,`own_sign`),
      KEY `idx_status_ownsign` (`status`,`own_sign`),
      KEY `idx_bizid` (`biz_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8 COMMENT='process instance of bulbasaur'
    ;


#### 节点表

 
     CREATE TABLE `xxxx_bulbasaur_s` (
       `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
       `biz_id` varchar(64) NOT NULL COMMENT 'biz id, see biz_id in pikachu_p',
       `state_name` varchar(64) NOT NULL COMMENT 'state name',
       `own_sign` varchar(45) NOT NULL COMMENT '标识',
       `pre_biz_info` text COMMENT '业务信息',
       `biz_info` text COMMENT 'current biz context info',
       `status` varchar(20) NOT NULL COMMENT 'state''''s status\\n  1. ready - ready to execute\\n  2. complete - completed\\n  3. rollback - rollbacked',
       `gmt_create` datetime NOT NULL COMMENT 'gmt_create',
       `gmt_modified` datetime DEFAULT NULL COMMENT 'gmt_modified',
       `alias` varchar(64) DEFAULT NULL COMMENT '别名，主要维护中文名',
       PRIMARY KEY (`id`),
       KEY `idx_bizid_ownsign_statename` (`biz_id`,`own_sign`,`state_name`)
     ) ENGINE=InnoDB AUTO_INCREMENT=281 DEFAULT CHARSET=utf8 COMMENT='节点表'
     ;


#### 活动表

 
    CREATE TABLE `xxxx_bulbasaur_j` (
      `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
      `gmt_create` datetime NOT NULL COMMENT '创建时间',
      `gmt_modified` datetime NOT NULL COMMENT '修改时间',
      `biz_id` varchar(64) NOT NULL COMMENT 'biz id from biz system',
      `definition_name` varchar(64) DEFAULT NULL COMMENT 'definition name',
      `state_name` varchar(64) NOT NULL COMMENT 'state_name',
      `event_type` varchar(20) NOT NULL COMMENT '任务类型，FailedRetry，TimeOut',
      `status` varchar(20) NOT NULL COMMENT 'INIT，RUNNING，DONE，FAILED',
      `repetition` varchar(64) DEFAULT NULL COMMENT '重复类型',
      `ignore_weekend` tinyint(4) DEFAULT NULL COMMENT 'ignore_weekend',
      `deal_strategy` varchar(128) DEFAULT NULL COMMENT '处理策略',
      `repeat_times` bigint(20) DEFAULT NULL COMMENT '重复测试',
      `task_id` bigint(20) DEFAULT NULL COMMENT 'task_id',
      `mod_num` bigint(20) NOT NULL COMMENT 'tbschedule 计算因子',
      `end_time` datetime DEFAULT NULL COMMENT '结束时间',
      `last_exception_stack` varchar(4000) DEFAULT NULL COMMENT '异常栈',
      `out_going` varchar(128) DEFAULT NULL COMMENT '超时跳转目标节点',
      `own_sign` varchar(64) NOT NULL COMMENT 'own_sign',
      PRIMARY KEY (`id`),
      KEY `idx_bizid_ownsign_repeattimes` (`biz_id`,`own_sign`,`repeat_times`)
    ) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='流程引擎job'
    ;


#### 任务表    

     CREATE TABLE `xxxx_bulbasaur_t` (
       `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
       `gmt_create` datetime NOT NULL COMMENT '创建时间',
       `gmt_modified` datetime NOT NULL COMMENT '修改时间',
       `name` varchar(64) NOT NULL COMMENT '任务名称',
       `end_time` datetime DEFAULT NULL COMMENT '结束时间',
       `biz_id` varchar(128) NOT NULL COMMENT '业务id',
       `user_id` bigint(20) DEFAULT NULL COMMENT '审批人id',
       `user_name` varchar(64) DEFAULT NULL COMMENT '审批人名称',
       `memo` varchar(500) DEFAULT NULL COMMENT '处理备注',
       `assign_user_id` bigint(20) DEFAULT NULL COMMENT '转交审批人id',
       `assign_user_name` varchar(20) DEFAULT NULL COMMENT '转交审批人名称',
       `assign_time` datetime DEFAULT NULL COMMENT '转交时间',
       `type` varchar(20) DEFAULT NULL COMMENT '类型',
       `status` varchar(20) DEFAULT NULL COMMENT '状态',
       `definition_name` varchar(64) DEFAULT NULL COMMENT '冗余',
       `biz_info` varchar(1000) DEFAULT NULL COMMENT '冗余',
       `creator_id` bigint(20) DEFAULT NULL COMMENT 'creator_id',
       `creator_name` varchar(64) DEFAULT NULL COMMENT 'creator_name',
       `deal_result` varchar(128) DEFAULT NULL COMMENT '处理结果，比如 ok ，fail 等，业务方自己存，自己查询',
       PRIMARY KEY (`id`),
       KEY `idx_bizid_status` (`biz_id`,`status`),
       KEY `idx_userid_status` (`user_id`,`status`),
       KEY `idx_dealresult` (`deal_result`)
     ) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 COMMENT='流程引擎任务表'
     ;

        
#### 参与者表    

    CREATE TABLE `xxxx_bulbasaur_ptp` (
      `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
      `gmt_create` datetime NOT NULL COMMENT '创建时间',
      `gmt_modified` datetime NOT NULL COMMENT '修改时间',
      `user_id` bigint(20) DEFAULT NULL COMMENT '审批人id',
      `user_name` varchar(20) DEFAULT NULL COMMENT '审批人名称',
      `type` varchar(20) DEFAULT NULL COMMENT '暂时无用。\\n参与类型（任务参与人的类型）\\nAgentUser：代理人\\nOriUser：本人',
      `task_id` bigint(20) NOT NULL COMMENT '任务id',
      `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否有效。1有效，0无效',
      `definition_name` varchar(64) DEFAULT NULL COMMENT '冗余',
      PRIMARY KEY (`id`),
      KEY `idx_taskid_status` (`task_id`,`status`),
      KEY `idx_userid_status` (`user_id`,`status`)
    ) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='流程引擎参与表'
    ;
    
    
#### 基于数据库的分布式quartz所需表    

这里的 `QRTZ_` 为表前缀，可以改成业务方统一的前缀，比如像 xxxx_bulbasaur_p 一样的 xxxx_FIRED_TRIGGERS。    
在 scheduleModule 中配置，如果不指定，默认为 QRTZ_    
    
    DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS;
    DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS;
    DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE;
    DROP TABLE IF EXISTS QRTZ_LOCKS;
    DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS;
    DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS;
    DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS;
    DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS;
    DROP TABLE IF EXISTS QRTZ_TRIGGERS;
    DROP TABLE IF EXISTS QRTZ_JOB_DETAILS;
    DROP TABLE IF EXISTS QRTZ_CALENDARS;

    
    CREATE TABLE QRTZ_JOB_DETAILS
      (
        SCHED_NAME VARCHAR(120) NOT NULL,
        JOB_NAME  VARCHAR(200) NOT NULL,
        JOB_GROUP VARCHAR(200) NOT NULL,
        DESCRIPTION VARCHAR(250) NULL,
        JOB_CLASS_NAME   VARCHAR(250) NOT NULL,
        IS_DURABLE VARCHAR(1) NOT NULL,
        IS_NONCONCURRENT VARCHAR(1) NOT NULL,
        IS_UPDATE_DATA VARCHAR(1) NOT NULL,
        REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
        JOB_DATA BLOB NULL,
        PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
    );
    
    CREATE TABLE QRTZ_TRIGGERS
      (
        SCHED_NAME VARCHAR(120) NOT NULL,
        TRIGGER_NAME VARCHAR(200) NOT NULL,
        TRIGGER_GROUP VARCHAR(200) NOT NULL,
        JOB_NAME  VARCHAR(200) NOT NULL,
        JOB_GROUP VARCHAR(200) NOT NULL,
        DESCRIPTION VARCHAR(250) NULL,
        NEXT_FIRE_TIME BIGINT(13) NULL,
        PREV_FIRE_TIME BIGINT(13) NULL,
        PRIORITY INTEGER NULL,
        TRIGGER_STATE VARCHAR(16) NOT NULL,
        TRIGGER_TYPE VARCHAR(8) NOT NULL,
        START_TIME BIGINT(13) NOT NULL,
        END_TIME BIGINT(13) NULL,
        CALENDAR_NAME VARCHAR(200) NULL,
        MISFIRE_INSTR SMALLINT(2) NULL,
        JOB_DATA BLOB NULL,
        PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
        FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
            REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP)
    );
    
    CREATE TABLE QRTZ_SIMPLE_TRIGGERS
      (
        SCHED_NAME VARCHAR(120) NOT NULL,
        TRIGGER_NAME VARCHAR(200) NOT NULL,
        TRIGGER_GROUP VARCHAR(200) NOT NULL,
        REPEAT_COUNT BIGINT(7) NOT NULL,
        REPEAT_INTERVAL BIGINT(12) NOT NULL,
        TIMES_TRIGGERED BIGINT(10) NOT NULL,
        PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
        FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
            REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
    );
    
    CREATE TABLE QRTZ_CRON_TRIGGERS
      (
        SCHED_NAME VARCHAR(120) NOT NULL,
        TRIGGER_NAME VARCHAR(200) NOT NULL,
        TRIGGER_GROUP VARCHAR(200) NOT NULL,
        CRON_EXPRESSION VARCHAR(200) NOT NULL,
        TIME_ZONE_ID VARCHAR(80),
        PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
        FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
            REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
    );
    
    CREATE TABLE QRTZ_SIMPROP_TRIGGERS
      (          
        SCHED_NAME VARCHAR(120) NOT NULL,
        TRIGGER_NAME VARCHAR(200) NOT NULL,
        TRIGGER_GROUP VARCHAR(200) NOT NULL,
        STR_PROP_1 VARCHAR(512) NULL,
        STR_PROP_2 VARCHAR(512) NULL,
        STR_PROP_3 VARCHAR(512) NULL,
        INT_PROP_1 INT NULL,
        INT_PROP_2 INT NULL,
        LONG_PROP_1 BIGINT NULL,
        LONG_PROP_2 BIGINT NULL,
        DEC_PROP_1 NUMERIC(13,4) NULL,
        DEC_PROP_2 NUMERIC(13,4) NULL,
        BOOL_PROP_1 VARCHAR(1) NULL,
        BOOL_PROP_2 VARCHAR(1) NULL,
        PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
        FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) 
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
    );
    
    CREATE TABLE QRTZ_BLOB_TRIGGERS
      (
        SCHED_NAME VARCHAR(120) NOT NULL,
        TRIGGER_NAME VARCHAR(200) NOT NULL,
        TRIGGER_GROUP VARCHAR(200) NOT NULL,
        BLOB_DATA BLOB NULL,
        PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
        FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
            REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
    );
    
    CREATE TABLE QRTZ_CALENDARS
      (
        SCHED_NAME VARCHAR(120) NOT NULL,
        CALENDAR_NAME  VARCHAR(200) NOT NULL,
        CALENDAR BLOB NOT NULL,
        PRIMARY KEY (SCHED_NAME,CALENDAR_NAME)
    );
    
    CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS
      (
        SCHED_NAME VARCHAR(120) NOT NULL,
        TRIGGER_GROUP  VARCHAR(200) NOT NULL, 
        PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP)
    );
    
    CREATE TABLE QRTZ_FIRED_TRIGGERS
      (
        SCHED_NAME VARCHAR(120) NOT NULL,
        ENTRY_ID VARCHAR(95) NOT NULL,
        TRIGGER_NAME VARCHAR(200) NOT NULL,
        TRIGGER_GROUP VARCHAR(200) NOT NULL,
        INSTANCE_NAME VARCHAR(200) NOT NULL,
        FIRED_TIME BIGINT(13) NOT NULL,
        SCHED_TIME BIGINT(13) NOT NULL,
        PRIORITY INTEGER NOT NULL,
        STATE VARCHAR(16) NOT NULL,
        JOB_NAME VARCHAR(200) NULL,
        JOB_GROUP VARCHAR(200) NULL,
        IS_NONCONCURRENT VARCHAR(1) NULL,
        REQUESTS_RECOVERY VARCHAR(1) NULL,
        PRIMARY KEY (SCHED_NAME,ENTRY_ID)
    );
    
    CREATE TABLE QRTZ_SCHEDULER_STATE
      (
        SCHED_NAME VARCHAR(120) NOT NULL,
        INSTANCE_NAME VARCHAR(200) NOT NULL,
        LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
        CHECKIN_INTERVAL BIGINT(13) NOT NULL,
        PRIMARY KEY (SCHED_NAME,INSTANCE_NAME)
    );
    
    CREATE TABLE QRTZ_LOCKS
      (
        SCHED_NAME VARCHAR(120) NOT NULL,
        LOCK_NAME  VARCHAR(40) NOT NULL, 
        PRIMARY KEY (SCHED_NAME,LOCK_NAME)
    );
    
    
------

备注：    

### 在xml中，可以通过表达式循环执行一个节点

        <state name="state2" alias="节点2">
            <invokes>
                <script return="_a" pojos="test.TestBean -> test">
                    test.testMethod(_a)
                </script>
            </invokes>
            <paths>
                <path to="state2"     expr="_a &lt;= 10"   />
                <path to="end"  expr="_a  &gt; 10 " />
    
            </paths>
        </state>
        
       
        
|   |   |   |
|---|---|---|
| &lt;  | <  |  小于号 |
|  &gt; | >  |  大于号 |
|  &amp; | &  |  和 |
|  &apos; | ‘  | 单引号  |
|  &quot; |  " | 双引号  |

		
		
		
		
  
	



