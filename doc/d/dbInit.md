
# DB Init SQL


## 模板表


    CREATE TABLE `bulbasaur_d` (
      `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
      `definition_name` varchar(64) NOT NULL COMMENT 'definition name',
      `definition_version` int(11) NOT NULL DEFAULT '1' COMMENT 'definition version',
      `own_sign` varchar(64) NOT NULL COMMENT 'sign to separate between different runtime or app',
      `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'status',
      `content` text COMMENT 'definition content',
      `gmt_create` datetime NOT NULL COMMENT 'gmt_create',
      `gmt_modified` datetime DEFAULT NULL COMMENT 'gmt_modified',
      PRIMARY KEY (`id`),
      KEY `idx_name_ownsign_version` (`definition_name`,`own_sign`,`definition_version`)
    ) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='流程模板'
    ;


## 流程表

 
    CREATE TABLE `bulbasaur_p` (
      `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id\n',
      `biz_id` varchar(64) NOT NULL COMMENT 'biz id from biz system',
      `definition_name` varchar(64) NOT NULL COMMENT 'definition name, see pikachu_d',
      `definition_version` int(11) DEFAULT NULL COMMENT 'definition version, see pikachu_d',
      `own_sign` varchar(64) NOT NULL COMMENT 'sign to separate between different runtime or app',
      `current_state_name` varchar(64) DEFAULT NULL COMMENT 'this process''''s current state''''s name',
      `status` varchar(64) DEFAULT NULL COMMENT 'this process''''s current state',
      `gmt_create` datetime NOT NULL COMMENT 'gmt_create',
      `gmt_modified` datetime DEFAULT NULL COMMENT 'gmt_modified',
      PRIMARY KEY (`id`),
      UNIQUE KEY `uk_biz_id_own_sign` (`biz_id`,`own_sign`),
      KEY `idx_bizid_ownsign` (`biz_id`,`own_sign`),
      KEY `idx_status_ownsign` (`status`,`own_sign`),
      KEY `idx_bizid` (`biz_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8 COMMENT='process instance of bulbasaur'
    ;


## 节点表

 
     CREATE TABLE `bulbasaur_s` (
       `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
       `biz_id` varchar(64) NOT NULL COMMENT 'biz id, see biz_id in pikachu_p',
       `state_name` varchar(64) NOT NULL COMMENT 'state name',
       `own_sign` varchar(45) NOT NULL COMMENT '标识',
       `pre_biz_info` text COMMENT '业务信息',
       `biz_info` text COMMENT 'current biz context info',
       `status` varchar(20) NOT NULL COMMENT 'state''''s status\\n  1. ready - ready to execute\\n  2. complete - completed\\n  3. rollback - rollbacked',
       `gmt_create` datetime NOT NULL COMMENT 'gmt_create',
       `gmt_modified` datetime DEFAULT NULL COMMENT 'gmt_modified',
       PRIMARY KEY (`id`),
       KEY `idx_bizid_ownsign_statename` (`biz_id`,`own_sign`,`state_name`)
     ) ENGINE=InnoDB AUTO_INCREMENT=281 DEFAULT CHARSET=utf8 COMMENT='节点表'
     ;


## 活动表

 
    CREATE TABLE `bulbasaur_j` (
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


## 任务表    

     CREATE TABLE `bulbasaur_t` (
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
       PRIMARY KEY (`id`),
       KEY `idx_bizid_status` (`biz_id`,`status`),
       KEY `idx_userid_status` (`user_id`,`status`)
     ) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='流程引擎任务表'
     ;

        
## 参与者表    

    CREATE TABLE `bulbasaur_ptp` (
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


            


