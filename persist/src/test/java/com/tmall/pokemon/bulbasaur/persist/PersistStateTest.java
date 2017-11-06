package com.tmall.pokemon.bulbasaur.persist;

import java.net.URL;
import java.util.List;

import javax.annotation.Resource;

import com.tmall.pokemon.bulbasaur.core.CoreModule;
import com.tmall.pokemon.bulbasaur.persist.domain.StateDOExample;
import com.tmall.pokemon.bulbasaur.persist.domain.StateDOWithBLOBs;
import com.tmall.pokemon.bulbasaur.persist.mapper.StateDOMapper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
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
