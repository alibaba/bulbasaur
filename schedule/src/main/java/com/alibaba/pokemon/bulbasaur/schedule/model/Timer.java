package com.alibaba.pokemon.bulbasaur.schedule.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.pokemon.bulbasaur.core.CoreModule;
import com.alibaba.pokemon.bulbasaur.core.Result;
import com.alibaba.pokemon.bulbasaur.core.annotation.NeedDAOMeta;
import com.alibaba.pokemon.bulbasaur.core.annotation.StateMeta;
import com.alibaba.pokemon.bulbasaur.core.constants.XmlTagConstants;
import com.alibaba.pokemon.bulbasaur.core.model.Event;
import com.alibaba.pokemon.bulbasaur.core.model.StateLike;
import com.alibaba.pokemon.bulbasaur.persist.domain.JobDO;
import com.alibaba.pokemon.bulbasaur.persist.mapper.JobDOMapper;
import com.alibaba.pokemon.bulbasaur.schedule.job.JobConstant;
import com.alibaba.pokemon.bulbasaur.schedule.vo.Hour;
import com.alibaba.pokemon.bulbasaur.schedule.process.JobHelper;
import org.dom4j.Element;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 只能在某个时间段执行
 * User:  yunche.ch ... (ว ˙o˙)ง
 * Date: 17/4/13
 * Time: 下午3:08
 */
@StateMeta(t = "timer")
@NeedDAOMeta(need = true)
public class Timer extends Event {
    private final static Logger logger = LoggerFactory.getLogger(Timer.class);

    private Map<String, Object> dAOMap;
    private Hour hour;

    @Override
    public Result prepare(Map<String, Object> context) {

        Result result = new Result();
        logger.info("<========= Timer preInvoke  start [Timer:" + this.getStateName() + "] =========>");

        JobDOMapper jobDOMapper = (JobDOMapper)dAOMap.get("jobDOMapper");

        // 创建定时任务
        JobDO jobDO = new JobDO();
        jobDO.setBizId(getBizId());
        jobDO.setStateName(getStateName());
        jobDO.setDefinitionName(getDefinitionName());
        jobDO.setGmtCreate(new Date());
        jobDO.setGmtModified(new Date());
        jobDO.setEndTime(new Date());

        jobDO.setStatus("INIT");
        jobDO.setModNum(JobHelper.BKDRHash(getBizId()));
        jobDO.setEventType(JobConstant.JOB_ENVENT_TYPE_TIMER);
        jobDO.setOwnSign(CoreModule.getInstance().getOwnSign());

        int hourNow = new DateTime().getHourOfDay();
        String delay = hour.calcDelay(hourNow) + "h";

        jobDO.setDealStrategy(delay);
        jobDO.setRepeatTimes(1L);
        jobDO.setRepetition(JobHelper.transformRepeatStr(delay));
        jobDO.setIgnoreWeekend(true);

        jobDOMapper.insert(jobDO);

        logger.info("<========= Timer preInvoke  end [ Timer:" + this.getStateName() + "] =========>");
        //执行pre-invokes
        super.prepare(context);
        result.setContinue(false);
        return result;

    }

    @Override
    public StateLike parse(Element elem) {
        super.parse(elem);
        List<Element> intervalElement = elem.selectNodes(XmlTagConstants.INTERVAL_TAG);
        // 拿 孩子节点
        for (Element intervalNode : intervalElement) {

            List<Element> hourElement = intervalNode.selectNodes(XmlTagConstants.HOUR_TAG);

            //如果配置了多个以最后一个为准
            for (Element hourNode : hourElement) {
                Hour hour = new Hour();
                if (hourNode.attributeValue(XmlTagConstants.START_TAG) != null) {
                    hour.setStart(Integer.valueOf(hourNode.attributeValue(XmlTagConstants.START_TAG)));
                }
                if (hourNode.attributeValue(XmlTagConstants.END_TAG) != null) {
                    hour.setEnd(Integer.valueOf(hourNode.attributeValue(XmlTagConstants.END_TAG)));
                }
                this.hour = hour;
            }

            // 其他类型暂时没有用到

        }
        return this;
    }

    @Override
    public void setDAOMap(Map<String, ?> map) {
        this.dAOMap = (Map<String, Object>)map;
    }

}
