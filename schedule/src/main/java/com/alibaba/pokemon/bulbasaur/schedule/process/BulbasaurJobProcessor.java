package com.alibaba.pokemon.bulbasaur.schedule.process;

import com.alibaba.pokemon.bulbasaur.core.CoreModule;
import com.alibaba.pokemon.bulbasaur.persist.constant.StateConstant;
import com.alibaba.pokemon.bulbasaur.persist.domain.JobDO;
import com.alibaba.pokemon.bulbasaur.persist.domain.JobDOExample;
import com.alibaba.pokemon.bulbasaur.persist.domain.StateDO;
import com.alibaba.pokemon.bulbasaur.persist.domain.StateDOExample;
import com.alibaba.pokemon.bulbasaur.persist.mapper.JobDOMapper;
import com.alibaba.pokemon.bulbasaur.persist.mapper.StateDOMapper;
import com.alibaba.pokemon.bulbasaur.schedule.ScheduleMachineFactory;
import com.alibaba.pokemon.bulbasaur.schedule.ScheduleModule;
import com.alibaba.pokemon.bulbasaur.schedule.job.FailedRetryJob;
import com.alibaba.pokemon.bulbasaur.schedule.job.Job;
import com.alibaba.pokemon.bulbasaur.schedule.job.JobConstant;
import com.alibaba.pokemon.bulbasaur.schedule.job.TimeOutJob;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class BulbasaurJobProcessor extends AbstractBulbasaurProcessor<JobDOExample, JobDO> implements org.quartz.Job {
    private static Logger logger = LoggerFactory.getLogger(BulbasaurJobProcessor.class);
    private static final int pageSize = 200;

    @Autowired
    ScheduleMachineFactory scheduleMachineFactory;
    @Autowired
    JobDOMapper jobDOMapper;
    @Autowired
    JobHelper jobHelper;
    @Autowired
    StateDOMapper stateDOMapper;
    @Autowired
    BulbasaurExecutorHelper bulbasaurExecutorHelper;

    @Override
    protected void shoot(List<JobDO> list) {

        boolean deleteOverdueJob = ScheduleModule.getInstance().getDeleteOverdueJob();

        try {
            for (Object beef : list) {
                if (beef instanceof JobDO) {
                    JobDO jobDO = (JobDO)beef;

                    // 判断该job在state表中是否是complete
                    StateDOExample stateDOExample = new StateDOExample();
                    stateDOExample.setOrderByClause("id asc");
                    stateDOExample.createCriteria().andBizIdEqualTo(jobDO.getBizId()).andStateNameEqualTo(
                        jobDO.getStateName()).andOwnSignEqualTo(CoreModule.getInstance().getOwnSign());
                    List<StateDO> stateDOList = stateDOMapper.selectByExample(stateDOExample);

                    StateDO stateDO = stateDOList != null && !stateDOList.isEmpty() ? stateDOList.get(0) : null;
                    if (stateDO == null || StateConstant.STATE_COMPLETE.equals(stateDO.getStatus())) {
                        jobDOMapper.deleteByPrimaryKey(jobDO.getId());
                        continue;
                    }

                    if (StringUtils.isNotBlank(jobDO.getBizId())) {

                        // 判断是否到时间重试
                        long remaining = JobHelper.getRemainingTime(jobDO);

                        if (remaining <= 0) {
                            long start = System.currentTimeMillis();

                            Job job;

                            if (JobConstant.JOB_ENVENT_TYPE_FAILEDRETRY.equals(jobDO.getEventType())) {
                                job = new FailedRetryJob(jobDO, scheduleMachineFactory);
                            } else if (JobConstant.JOB_ENVENT_TYPE_TIMEOUT.equals(jobDO.getEventType())) {
                                job = new TimeOutJob(jobDO, scheduleMachineFactory);
                            } else if (JobConstant.JOB_ENVENT_TYPE_TIMER.equals(jobDO.getEventType())) {
                                job = new TimeOutJob(jobDO, scheduleMachineFactory);
                            } else {
                                logger.error("未找到JOB类型，拒绝执行！job :" + jobDO.toString());
                                throw new RuntimeException("未找到JOB类型，拒绝执行！job :" + jobDO.toString());
                            }

                            try {
                                job.doJob();
                            } catch (Exception e) {
                                logger.error(String
                                    .format("流程[%s]在节点[%s]调度出错:[%s]", jobDO.getBizId(), jobDO.getStateName(),
                                        e.getMessage()));
                                logger.error(String.format("详细错误堆栈 \n %s", ExceptionUtils.getStackTrace(e)));
                            } finally {

                                if (jobDO.getRepeatTimes() == null || jobDO.getRepeatTimes() < 1) {

                                    if (deleteOverdueJob) {
                                        // 直接删除
                                        logger.error(String
                                            .format("bulbasaur job id = %s , bizId = %s , 已经超过最大重拾次数,被删除.....",
                                                jobDO.getId(), jobDO.getBizId()));

                                        jobDOMapper.deleteByPrimaryKey(jobDO.getId());

                                    } else {
                                        // 重试次数没有了，认为是DONE
                                        Date now = new Date();
                                        jobDO.setGmtModified(now);
                                        jobDO.setStatus(JobConstant.JOB_STATUS_DONE);
                                        jobDOMapper.updateByPrimaryKeySelective(jobDO);
                                    }
                                } else {
                                    Date now = new Date();
                                    jobDO.setGmtModified(now);
                                    jobDO.setEndTime(now);
                                    jobDO.setStatus(JobConstant.JOB_STATUS_RUNNING);

                                    /**
                                     * repeatTimes 剩余的执行次数，当到0的时候，没有下一次，也就不需要计算 repetition
                                     */
                                    jobDO.setRepeatTimes(jobDO.getRepeatTimes() - 1);
                                    if (StringUtils.isNotBlank(jobDO.getDealStrategy())) {
                                        if (jobDO.getRepeatTimes() != 0) {
                                            String[] repeatArray = jobDO.getDealStrategy().split("\\s");
                                            jobDO.setRepetition(JobHelper.transformRepeatStr(
                                                repeatArray[repeatArray.length - jobDO.getRepeatTimes().intValue()]));
                                        } else {
                                            jobDO.setRepetition("0");
                                        }
                                    }

                                    jobDOMapper.updateByPrimaryKeySelective(jobDO);
                                }
                            }
                            logger.info("执行调度节点花费时间:" + (System.currentTimeMillis() - start) / 1000 + " s");
                        }
                    }

                }
            }
        } catch (Exception e) {
            logger.error(String.format("bulbasaur调度业务组件任务异常: \n [%s]", ExceptionUtils.getStackTrace(e)));
        }
    }

    @Override
    public List<JobDO> query(int pageNo, JobDOExample example) {
        example.setOffset(PageSizeHelper.calcOffset(pageNo, querySupportPageSize()));
        return jobDOMapper.selectByExample(example);
    }

    @Override
    protected int querySupportPageSize() {
        return PAGE_SIZE;
    }

    @Override
    public int queryTotalCount(JobDOExample example) {
        return jobDOMapper.countByExample(example);
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDOExample jobDOExample = new JobDOExample();
        jobDOExample.setLimit(querySupportPageSize());

        JobDOExample.Criteria criteria = jobDOExample.createCriteria();
        /* 查出非DONE 的job*/
        criteria.andOwnSignEqualTo(CoreModule.getInstance().getOwnSign()).andStatusNotEqualTo(
            JobConstant.JOB_STATUS_DONE);

        try {
            handle(jobDOExample);
        } catch (Exception e) {
            logger.error("清理流程和节点逻辑出现异常！e＝" + e.getMessage());
        }

    }

}
