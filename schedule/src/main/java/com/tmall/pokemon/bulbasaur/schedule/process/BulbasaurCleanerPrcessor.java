package com.tmall.pokemon.bulbasaur.schedule.process;

import com.tmall.pokemon.bulbasaur.core.CoreModule;
import com.tmall.pokemon.bulbasaur.persist.domain.JobDOExample;
import com.tmall.pokemon.bulbasaur.persist.domain.ProcessDO;
import com.tmall.pokemon.bulbasaur.persist.domain.ProcessDOExample;
import com.tmall.pokemon.bulbasaur.persist.domain.StateDOExample;
import com.tmall.pokemon.bulbasaur.persist.mapper.JobDOMapper;
import com.tmall.pokemon.bulbasaur.persist.mapper.ProcessDOMapper;
import com.tmall.pokemon.bulbasaur.persist.mapper.StateDOMapper;
import com.tmall.pokemon.bulbasaur.schedule.ScheduleModule;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BulbasaurCleanerPrcessor extends AbstractBulbasaurProcessor<ProcessDOExample, ProcessDO>
    implements Job {
    private static Logger logger = LoggerFactory.getLogger(BulbasaurCleanerPrcessor.class);

    @Autowired
    ProcessDOMapper processDOMapper;
    @Autowired
    StateDOMapper stateDOMapper;
    @Autowired
    JobDOMapper jobDOMapper;
    @Autowired
    BulbasaurExecutorHelper bulbasaurExecutorHelper;

    @Override
    protected void shoot(List<ProcessDO> list) {

        for (ProcessDO pr : list) {

            // 1. 清理 jobs
            JobDOExample jobDOExample = new JobDOExample();
            jobDOExample.createCriteria().andBizIdEqualTo(pr.getBizId())
                .andOwnSignEqualTo(CoreModule.getInstance().getOwnSign());

            int deleteJobResult = jobDOMapper.deleteByExample(jobDOExample);
            logger.warn(String.format("清理流程逻辑，将job一起清理成功，bizId = %s , 记录数 ＝ %s !", pr.getBizId(), deleteJobResult));

            // 2. 清理 states
            StateDOExample stateDOExample = new StateDOExample();
            stateDOExample.createCriteria().andBizIdEqualTo(pr.getBizId())
                .andOwnSignEqualTo(CoreModule.getInstance().getOwnSign());

            int deleteStateResult = stateDOMapper.deleteByExample(stateDOExample);
            logger.warn(String.format("清理流程逻辑 state 表 成功，bizId = %s , 记录数 ＝ %s !", pr.getBizId(), deleteStateResult));

            // 2. 清理 states
            int deleteProcess = processDOMapper.deleteByPrimaryKey(pr.getId());
            logger.warn(String.format("清理流程逻辑 process 表 成功，bizId = %s , 记录数 = %s !", pr.getBizId(), deleteProcess));

        }
    }

    @Override
    public List<ProcessDO> query(int pageNo, ProcessDOExample example) {
        example.setOffset(PageSizeHelper.calcOffset(pageNo, querySupportPageSize()));
        return processDOMapper.selectByExample(example);
    }

    @Override
    protected int querySupportPageSize() {
        return PAGE_SIZE;
    }

    @Override
    public int queryTotalCount(ProcessDOExample example) {
        return processDOMapper.countByExample(example);
    }

    @Override
    public void execute(JobExecutionContext context) {

        List<String> autoCleanDefinitionNameList = ScheduleModule.getInstance().getAutoCleanDefinitionNameList();

        if (autoCleanDefinitionNameList != null && !autoCleanDefinitionNameList.isEmpty()) {

            ProcessDOExample processDOExample = new ProcessDOExample();
            processDOExample.setLimit(querySupportPageSize());
            processDOExample.createCriteria()
                .andStatusEqualTo("complete")
                .andOwnSignEqualTo(CoreModule.getInstance().getOwnSign())
                .andDefinitionNameIn(autoCleanDefinitionNameList)
            ;

            try {
                handle(processDOExample);
            } catch (Exception e) {
                logger.error("清理流程和节点逻辑出现异常！e＝" + e.getMessage());
            }
        }

    }

}
