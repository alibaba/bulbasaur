package com.alibaba.pokemon.bulbasaur.schedule;

import static com.alibaba.pokemon.bulbasaur.util.SimpleUtils.notnull;
import static com.alibaba.pokemon.bulbasaur.util.SimpleUtils.require;

import com.alibaba.pokemon.bulbasaur.persist.PersistHelper;
import com.alibaba.pokemon.bulbasaur.persist.PersistMachine;
import com.alibaba.pokemon.bulbasaur.persist.PersistMachineFactory;
import com.alibaba.pokemon.bulbasaur.persist.mapper.*;
import com.alibaba.pokemon.bulbasaur.util.SimpleUtils;
import com.alibaba.pokemon.bulbasaur.persist.mapper.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.pokemon.bulbasaur.schedule.process.JobHelper;

/**
 * 增加了调度能力
 * User: yunche.ch - - - - (ว ˙o˙)ง
 * Date: 13-11-1
 * Time: 下午5:55
 */
@Component
public class ScheduleMachineFactory extends PersistMachineFactory {
    @Autowired
    private ProcessDOMapper processDOMapper;
    @Autowired
    private StateDOMapper stateDOMapper;
    @Autowired
    private JobDOMapper jobDOMapper;
    @Autowired
    private JobHelper jobHelper;
    @Autowired
    private PersistHelper persistHelper;
    @Autowired
    private TaskDOMapper taskDOMapper;
    @Autowired
    private ParticipationDOMapper participationDOMapper;

    @Override
    public ScheduleMachine newInstance(String bizId) {
        return newInstance(bizId, null);
    }

    @Override
    public ScheduleMachine newInstance(String bizId, String processName) {
        return newInstance(bizId, processName, 0);
    }

    @Override
    public ScheduleMachine newInstance(String bizId, String processName, int processVersion) {

        SimpleUtils.notnull(bizId, "parameter bizId should not null");
        SimpleUtils.require(bizId.matches("\\w+"), "bizId not match regex `\\w+`");

        PersistMachine persistMachine = super.newInstance(bizId, processName, processVersion);

        if(StringUtils.isBlank(processName)){
            processName = persistMachine.getProcessName();
        }

        return new ScheduleMachine(bizId, processName, processVersion, persistMachine.getProcessDO()
            , processDOMapper, stateDOMapper, persistHelper, jobDOMapper, jobHelper, taskDOMapper, participationDOMapper);
    }

}
