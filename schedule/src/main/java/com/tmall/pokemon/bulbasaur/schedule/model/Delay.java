package com.tmall.pokemon.bulbasaur.schedule.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.tmall.pokemon.bulbasaur.core.CoreModule;
import com.tmall.pokemon.bulbasaur.core.Result;
import com.tmall.pokemon.bulbasaur.core.annotation.NeedDAOMeta;
import com.tmall.pokemon.bulbasaur.core.annotation.StateMeta;
import com.tmall.pokemon.bulbasaur.core.model.Event;
import com.tmall.pokemon.bulbasaur.core.model.StateLike;
import com.tmall.pokemon.bulbasaur.persist.domain.JobDO;
import com.tmall.pokemon.bulbasaur.persist.mapper.JobDOMapper;
import com.tmall.pokemon.bulbasaur.schedule.process.JobHelper;
import com.tmall.pokemon.bulbasaur.schedule.job.JobConstant;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.tmall.pokemon.bulbasaur.core.constants.XmlTagConstants.TIME_TAG;

/**
 * 延时一段时间再执行
 * User:  yunche.ch ... (ว ˙o˙)ง
 * Date: 17/4/13
 * Time: 下午3:08
 */
@StateMeta(t = "delay")
@NeedDAOMeta(need = true)
public class Delay extends Event {
    private final static Logger logger = LoggerFactory.getLogger(Delay.class);

    private Map<String, Object> dAOMap;
    private String time;

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

        jobDO.setDealStrategy(time);
        jobDO.setRepeatTimes(1L);
        jobDO.setRepetition(JobHelper.transformRepeatStr(time));
        jobDO.setIgnoreWeekend(true);

        jobDOMapper.insert(jobDO);

        logger.info("<========= Delay preInvoke  end [ Delay:" + this.getStateName() + "] =========>");
        //执行pre-invokes
        super.prepare(context);
        result.setContinue(false);
        return result;

    }

    @Override
    public StateLike parse(Element elem) {
        super.parse(elem);
        List<Element> intervalElement = elem.selectNodes(TIME_TAG);
        // 拿 孩子节点
        for (Element intervalNode : intervalElement) {
            this.time = intervalNode.getStringValue();
        }
        return this;
    }

    @Override
    public void setDAOMap(Map<String, ?> map) {
        this.dAOMap = (Map<String, Object>)map;
    }

}
