package com.alibaba.pokemon.bulbasaur.persist;

import java.util.List;

import javax.annotation.Resource;

import com.alibaba.pokemon.bulbasaur.core.CoreModule;
import com.alibaba.pokemon.bulbasaur.persist.domain.StateDOExample;
import com.alibaba.pokemon.bulbasaur.persist.domain.StateDOWithBLOBs;
import com.alibaba.pokemon.bulbasaur.persist.mapper.StateDOMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author yunche.ch@taobao.com
 * @since 2012-12-25 下午07:10:42
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/persistMachineTest.xml"})
public class PersistStateTest {

    @Resource
    StateDOMapper stateDOMapper;

    @Test
    public void testdeployDefinition() {
        // 初始化
        StateDOExample stateDOExample = new StateDOExample();
        stateDOExample.setOrderByClause("id asc");
        stateDOExample.createCriteria().andBizIdEqualTo("123").andOwnSignEqualTo(
            CoreModule.getInstance().getOwnSign());

        List<StateDOWithBLOBs> stateList = stateDOMapper.selectByExampleWithBLOBs(stateDOExample);

    }

}
