package com.alibaba.pokemon.bulbasaur.schedule.process;

import java.text.ParseException;
import java.util.List;

import com.alibaba.pokemon.bulbasaur.core.CoreModule;
import com.alibaba.pokemon.bulbasaur.persist.domain.JobDO;
import com.alibaba.pokemon.bulbasaur.persist.domain.JobDOExample;
import com.alibaba.pokemon.bulbasaur.persist.mapper.JobDOMapper;
import com.alibaba.pokemon.bulbasaur.persist.mapper.StateDOMapper;
import com.alibaba.pokemon.bulbasaur.schedule.job.JobConstant;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User:  yunche.ch ... (ว ˙o˙)ง
 * Date: 17/9/28
 * Time: 下午6:11
 */
@Component
public class BulbasaurJobCleanerProcessor extends AbstractBulbasaurProcessor<JobDOExample, JobDO>
    implements org.quartz.Job {
    private static Logger logger = LoggerFactory.getLogger(BulbasaurCleanerPrcessor.class);

    @Autowired
    StateDOMapper stateDOMapper;
    @Autowired
    JobDOMapper jobDOMapper;
    @Autowired
    BulbasaurExecutorHelper bulbasaurExecutorHelper;

    @Override
    protected void shoot(List<JobDO> list) throws ParseException {

        for (JobDO jobDO : list) {
            int deleteJobResult = jobDOMapper.deleteByPrimaryKey(jobDO.getId());
            logger.warn(String.format("已经DONE的job清理，bizId = %s , 记录数 ＝ %s !", jobDO.getBizId(), deleteJobResult));
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
        jobDOExample.createCriteria().andStatusEqualTo(JobConstant.JOB_STATUS_DONE).andOwnSignEqualTo(
            CoreModule.getInstance().getOwnSign());

        try {
            handle(jobDOExample);
        } catch (Exception e) {
            logger.error("已经完成的job清理逻辑出现异常！e＝" + e.getMessage());
        }

    }

}
