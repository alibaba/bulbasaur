package com.tmall.pokemon.bulbasaur.persist;

import com.tmall.pokemon.bulbasaur.core.CoreModule;
import com.tmall.pokemon.bulbasaur.persist.domain.ProcessDO;
import com.tmall.pokemon.bulbasaur.persist.domain.ProcessDOExample;
import com.tmall.pokemon.bulbasaur.persist.domain.ProcessDOExample.Criteria;
import com.tmall.pokemon.bulbasaur.persist.mapper.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.tmall.pokemon.bulbasaur.persist.exception.ProcessAlreadyExistException;
import com.tmall.pokemon.bulbasaur.persist.exception.ProcessNotFoundException;

import java.util.List;

import static com.tmall.pokemon.bulbasaur.util.SimpleUtils.notnull;
import static com.tmall.pokemon.bulbasaur.util.SimpleUtils.require;

/**
 * 按给定的bizId加载或创建流程
 * User: yunche.ch - - - - (ว ˙o˙)ง
 * Date: 13-11-1
 * Time: 下午5:55
 */
public class PersistMachineFactory {
    @Autowired
    private ProcessDOMapper processDOMapper;
    @Autowired
    private StateDOMapper stateDOMapper;
    @Autowired
    private TaskDOMapper taskDOMapper;
    @Autowired
    private ParticipationDOMapper participationDOMapper;
    @Autowired
    private PersistHelper persistHelper;
    @Autowired
    private JobDOMapper jobDOMapper;

    public PersistMachine newInstance(String bizId) {
        return newInstance(bizId, null);
    }

    public PersistMachine newInstance(String bizId, String processName) {
        return newInstance(bizId, processName, 0);
    }

    public PersistMachine newInstance(String bizId, String processName, int processVersion) {
        notnull(bizId, "parameter bizId should not null");
        require(bizId.matches("\\w+"), "bizId not match regex `\\w+`");

        ProcessDO processDO = findProcess(bizId, processName, processVersion);

        if (processDO != null) {
            processName = processDO.getDefinitionName();
            processVersion = processDO.getDefinitionVersion();
        }

        return new PersistMachine(bizId, processName, processVersion, processDO
            , processDOMapper, stateDOMapper, persistHelper, taskDOMapper, participationDOMapper, jobDOMapper);
    }

    /**
     * 1. 只传入了bizId，必须查到记录，否则挂
     * 2. bizId + processName ，可以是新建流程
     * 3. bizId + processName + processVersion 指定加载某个版本的流程, processVersion 在processName存在才有效
     *
     * @param bizId
     * @param processName
     * @param processVersion
     */
    private ProcessDO findProcess(String bizId, String processName, int processVersion) {

        ProcessDO processDO;
        ProcessDOExample processDOExample = new ProcessDOExample();
        Criteria criteria = processDOExample.createCriteria();
        if (StringUtils.isNotBlank(processName)) {

            criteria.andBizIdEqualTo(bizId)
                .andDefinitionNameEqualTo(processName)
                .andOwnSignEqualTo(CoreModule.getInstance().getOwnSign());

            if (processVersion > 0) {
                criteria.andDefinitionVersionEqualTo(processVersion);
            }

            List<ProcessDO> processDOList = processDOMapper.selectByExample(processDOExample);
            processDO = processDOList != null && !processDOList.isEmpty() ? processDOList.get(0) : null;

        } else {

            //根据bizId直接查数据库，默认bizId业务隔离唯一
            criteria.andBizIdEqualTo(bizId).andOwnSignEqualTo(
                CoreModule.getInstance().getOwnSign());
            List<ProcessDO> processDOList = processDOMapper.selectByExample(processDOExample);

            // 直接返回第一个
            processDO = processDOList != null && !processDOList.isEmpty() ? processDOList.get(0) : null;

            if (processDO == null) {
                throw new ProcessNotFoundException("no process instance find for bizId: " + bizId);
            }

        }

        return processDO;

    }
}
