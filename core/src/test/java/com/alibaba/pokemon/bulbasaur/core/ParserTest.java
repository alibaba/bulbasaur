/**
 * Filename:    ParserTest.java
 * Description:
 *
 * @author: yunche.ch@taobao.com
 * @version: 1.0
 * @date 2012-12-12 上午10:32:18
 */
package com.alibaba.pokemon.bulbasaur.core;

import com.alibaba.pokemon.bulbasaur.core.invoke.InvokableFactory;
import com.alibaba.pokemon.bulbasaur.core.invoke.MvelScriptInvokable;
import com.alibaba.pokemon.bulbasaur.core.model.*;
import com.alibaba.pokemon.bulbasaur.core.model.*;
import com.alibaba.pokemon.bulbasaur.core.model.Definition;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description:
 * @author yunche.ch@taobao.com
 * @date 2012-12-12 上午10:32:18
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/coreModule.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class ParserTest {

    @Autowired
    Parser parser;

    @Test
    public void testGetXML() {
        System.out.println(parser.getXML("process", 0));
    }

    @Test
    public void testParse() {
        StateFactory.applyState(Start.class);
        StateFactory.applyState(State.class);
        StateFactory.applyState(Event.class);
        StateFactory.applyState(BizInfo.class);
        InvokableFactory.applyInvokable(MvelScriptInvokable.class);
        Definition definition = parser.parse("process", 0);
        BizInfo bizInfo = definition.getExtNode(BizInfo.class);
        Assert.assertNotNull(bizInfo);
        for (BizInfo.BizInfoElement e : bizInfo.getBizInfoList("r")) {
            Assert.assertEquals("r", e.getAttribute("key"));
            Assert.assertTrue(e.getAttribute("value").startsWith("r"));
            System.out.println(e.getAttribute("key") + " -> " + e.getAttribute("value"));
        }
    }
}
