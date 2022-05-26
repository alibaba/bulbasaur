package com.alibaba.pokemon.bulbasaur.schedule.process;

import com.alibaba.pokemon.bulbasaur.core.CoreModule;
import com.alibaba.pokemon.bulbasaur.core.model.State;
import com.alibaba.pokemon.bulbasaur.core.model.StateLike;
import com.alibaba.pokemon.bulbasaur.persist.domain.JobDO;
import com.alibaba.pokemon.bulbasaur.persist.domain.JobDOExample;
import com.alibaba.pokemon.bulbasaur.persist.mapper.JobDOMapper;
import com.alibaba.pokemon.bulbasaur.schedule.job.DayUtils;
import com.alibaba.pokemon.bulbasaur.schedule.job.JobConstant;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class JobHelper {
    public static final int MOD_NUM_COUNT = 1000;
    private static Logger logger = LoggerFactory.getLogger(JobHelper.class);
    @Autowired
    private JobDOMapper jobDOMapper;

    /**
     * hash 到指定范围内,0-999
     * 31 131 1313 13131 131313 etc..
     *
     * @param str
     * @return
     */
    public static long BKDRHash(String str) {
        int seed = 1313;
        int hash = 0;

        for (int i = 0; i < str.length(); i++) {
            hash = hash * seed + (str.charAt(i));
        }
        hash = hash % MOD_NUM_COUNT;
        return Math.abs(hash);
    }

    /**
     * 转换repeatList中的时间描述为以前的时间描述
     *
     * @param originStr 原始字串
     * @return 转换后的字串
     */
    public static String transformRepeatStr(String originStr) {
        switch (originStr.charAt(originStr.length() - 1)) {
            case 's':
                return originStr.substring(0, originStr.length() - 1) + " seconds";
            case 'm':
                return originStr.substring(0, originStr.length() - 1) + " minutes";
            case 'h':
                return originStr.substring(0, originStr.length() - 1) + " hours";
            case 'd':
                return originStr.substring(0, originStr.length() - 1) + " days";
            default:
                throw new IllegalArgumentException("unknown time unit");
        }
    }

    /**
     * 获取job的执行剩余时间
     *
     * @param job job
     * @return 毫秒数
     */
    public static long getRemainingTime(JobDO job) {
        // endTime作为上一次操作的时间
        Date lastInvokeTime = job.getEndTime();
        if (lastInvokeTime == null) {
            lastInvokeTime = job.getGmtCreate();
        }
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(lastInvokeTime);
        Calendar calEnd = Calendar.getInstance();

        long repeatTime = 1000L * getRepeatSecond(job);

        long remainingTime;
        if (job.getIgnoreWeekend() != null && job.getIgnoreWeekend()) {
            if (!DayUtils.initialized) { DayUtils.init(); }
            remainingTime = repeatTime - DayUtils.workTime(calStart, calEnd);
        } else {
            remainingTime = repeatTime - (calEnd.getTimeInMillis() - calStart.getTimeInMillis());
        }
        return (remainingTime < 0L) ? 0L : remainingTime;
    }

    /**
     * 本方法仅支持到分
     *
     * @param job
     * @return
     */
    @Deprecated
    public static int getRepeatMinutes(JobDO job) {
        String repeat = job.getRepetition();
        int minutes = 0;
        int index;
        if ((index = StringUtils.indexOf(repeat, "day")) != -1) {
            String timeNum = StringUtils.substring(repeat, 0, index);
            timeNum = StringUtils.strip(timeNum);
            int repeatDay = Integer.parseInt(timeNum);
            minutes = repeatDay * 60 * 24;
        } else if ((index = StringUtils.indexOf(repeat, "hour")) != -1) {
            String timeNum = StringUtils.substring(repeat, 0, index);
            timeNum = StringUtils.strip(timeNum);
            int repeatHour = Integer.parseInt(timeNum);
            minutes = repeatHour * 60;
        } else if ((index = StringUtils.indexOf(repeat, "minute")) != -1) {
            String timeNum = StringUtils.substring(repeat, 0, index);
            timeNum = StringUtils.strip(timeNum);
            minutes = Integer.parseInt(timeNum);
        }
        return minutes;
    }

    /**
     * 本方法支持到秒级别
     *
     * @param job
     * @return
     */
    public static int getRepeatSecond(JobDO job) {
        String repeat = job.getRepetition();
        int seconds = 0;
        int index;
        if ((index = StringUtils.indexOf(repeat, "day")) != -1) {
            String timeNum = StringUtils.substring(repeat, 0, index);
            timeNum = StringUtils.strip(timeNum);
            int repeatDay = Integer.parseInt(timeNum);
            seconds = repeatDay * 60 * 24 * 60;
        } else if ((index = StringUtils.indexOf(repeat, "hour")) != -1) {
            String timeNum = StringUtils.substring(repeat, 0, index);
            timeNum = StringUtils.strip(timeNum);
            int repeatHour = Integer.parseInt(timeNum);
            seconds = repeatHour * 60 * 60;
        } else if ((index = StringUtils.indexOf(repeat, "minute")) != -1) {
            String timeNum = StringUtils.substring(repeat, 0, index);
            timeNum = StringUtils.strip(timeNum);
            seconds = Integer.parseInt(timeNum) * 60;
        } else if ((index = StringUtils.indexOf(repeat, "second")) != -1) {
            String timeNum = StringUtils.substring(repeat, 0, index);
            timeNum = StringUtils.strip(timeNum);
            seconds = Integer.parseInt(timeNum);
        }
        return seconds;
    }

    public void add(String bizId, String definitionName, StateLike currentState, Throwable e) {

        JobDO record = new JobDO();
        record.setBizId(bizId);
        record.setOwnSign(CoreModule.getInstance().getOwnSign());
        record.setDefinitionName(definitionName);
        record.setStateName(currentState.getStateName());
        record.setGmtCreate(new Date());
        record.setGmtModified(new Date());
        record.setEndTime(new Date());
        record.setStatus(JobConstant.JOB_STATUS_INIT);
        record.setModNum(BKDRHash(bizId));
        record.setEventType(JobConstant.JOB_ENVENT_TYPE_FAILEDRETRY);
        record.setOwnSign(CoreModule.getInstance().getOwnSign());
        if (e != null) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(os));
            record.setLastExceptionStack(os.size() > 2500 ? os.toString().substring(0, 2500) : os.toString());
        }
        if (StringUtils.isNotBlank(((State)currentState).getRepeatList())) {
            // 校验下，只允许 [秒，分，时 ，天]
            String repeatList = ((State)currentState).getRepeatList().trim();
            if (!repeatList.matches("(\\d+[smhd])(\\s\\d+[smhd])*")) {
                throw new IllegalArgumentException(String
                    .format("repeatList格式错误:[%s],仅支持秒(s)，分(m)，时(h)，天(d)。", ((State)currentState).getRepeatList()));
            }

            // repeatList放在deal strategy里
            record.setDealStrategy(repeatList);
            String[] repeatArray = repeatList.split("\\s");
            record.setRepeatTimes(Long.valueOf(repeatArray.length));
            record.setRepetition(JobHelper.transformRepeatStr(repeatArray[0]));
            if (((State)currentState).getIgnoreWeekend() != null) {
                record.setIgnoreWeekend(((State)currentState).getIgnoreWeekend());
            }
        }

        try {
            jobDOMapper.insert(record);
        } catch (Exception e1) {
            logger.error(String.format("插入新job失败，e = [%s]", ExceptionUtils.getStackTrace(e1)));
        }

        logger.info(String.format("=====将需要分布式执行的流程[%s]的节点[%s]放入队列===", bizId, currentState.getStateName()));
    }

    /**
     * job 就不再set ownsign
     *
     * @param bizId
     * @return
     */
    public JobDO getJob(String bizId) {
        JobDO jobDO = new JobDO();
        jobDO.setBizId(bizId);

        JobDOExample jobDOExample = new JobDOExample();
        jobDOExample.createCriteria().andBizIdEqualTo(bizId).andOwnSignEqualTo(CoreModule.getInstance().getOwnSign());

        List<JobDO> jobDOList = jobDOMapper.selectByExample(jobDOExample);
        return jobDOList != null && !jobDOList.isEmpty() ? jobDOList.get(0) : null;
    }

    public void updateJobStack(JobDO jobDO, Throwable e) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(os));
        jobDO.setLastExceptionStack(os.size() > 2500 ? os.toString().substring(0, 2500) : os.toString());
        jobDO.setGmtModified(new Date());

        jobDOMapper.updateByPrimaryKeySelective(jobDO);

    }

}
