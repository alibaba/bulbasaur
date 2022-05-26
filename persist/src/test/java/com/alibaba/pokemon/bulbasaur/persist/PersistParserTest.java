package com.alibaba.pokemon.bulbasaur.persist;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.pokemon.bulbasaur.core.model.Definition;
import junit.framework.Assert;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/persistMachineTest.xml"})
public class PersistParserTest {

    @Autowired
    PersistParser persistParser;
    @Autowired
    PersistMachineFactory persistMachineFactory;

    @Test
    public void testPersistParser() {

        // deploy 2 version of definition first

        SAXReader reader = new SAXReader();
        // 拿不到信息
        URL url = this.getClass().getResource("/persist_definition.xml");
        Document document = null;
        try {
            document = reader.read(url);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String definitionContent = document.asXML();

        DefinitionHelper.getInstance().deployDefinition("persist_definition", "测试", definitionContent, true);
        DefinitionHelper.getInstance().deployDefinition("persist_definition", "测试", definitionContent, true);

        Definition definition = persistParser.parse("persist_definition", 0);
        Assert.assertEquals("persist_definition", definition.getName());
        Assert.assertEquals(2, definition.getVersion());
        Assert.assertNotNull(definition.getState("i'm start"));
        Definition definition1 = persistParser.parse("persist_definition", 1);
        Assert.assertEquals(1, definition1.getVersion());
    }

    @Test
    public void testMachineRunWithPersistParser() {

        SAXReader reader = new SAXReader();
        // 拿不到信息
        URL url = this.getClass().getResource("/persist_definition.xml");
        Document document = null;
        try {
            document = reader.read(url);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String definitionContent = document.asXML();
        int definitionVersion = DefinitionHelper.getInstance().deployDefinition("persist_definition","测试", definitionContent,
            true).getDefinitionVersion();

        PersistMachine p = persistMachineFactory.newInstance(String.valueOf(System.currentTimeMillis()),
            "persist_definition");
        Assert.assertEquals(definitionVersion, p.getProcessVersion());
        Map m = new HashMap();
        m.put("goto", 2);
        m.put("_i", 3);
        p.addContext(m);
        p.run();
        Assert.assertEquals(6, p.getContext("_a"));
    }
}
