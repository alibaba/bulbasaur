package com.tmall.pokemon.bulbasaur.persist;

import java.net.URL;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.junit.Assert;
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
public class PersistDefinationTest {

    @Test
    public void testdeployDefinition() {
        // 初始化

        SAXReader reader = new SAXReader();
        // 拿不到信息
        URL url = this.getClass().getResource("/process12.xml");
        Document document = null;
        try {
            document = reader.read(url);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String definitionContent = document.asXML();
        // deploy first time
        DefinitionHelper.getInstance().deployDefinition("process", "测试流程", definitionContent, true);
    }

    //@Test
    //    public void testDefinition() {
    //        // 初始化
    //
    //        SAXReader reader = new SAXReader();
    //        // 拿不到信息
    //        URL url = this.getClass().getResource("/process12.xml");
    //        Document document = null;
    //        try {
    //            document = reader.read(url);
    //        } catch (DocumentException e) {
    //            e.printStackTrace();
    //        }
    ////
    //		DefinitionDAO definitionDAO = (DefinitionDAO)Bulbasaur.getInnerApplicationContext().getBean
    // ("definitionDAO",DefinitionDAO.class);
    //
    //		String definitionContent = document.asXML(); // deploy first time
    //		DefinitionHelper.getInstance().deployDefinition("test-definition", definitionContent, true);
    //
    //		DefinitionDO d = definitionDAO.selectByNameAndVersion("test-definition", 1);
    ////
    //		Assert.assertNotNull(d);// 非空
    //		Assert.assertEquals(definitionContent, d.getContent());
    //        //
    //         deploy second time, the first one should be not default
    //		DefinitionHelper.getInstance().deployDefinition("test-definition", definitionContent, true);
    //		DefinitionDO d1 = definitionDAO.selectByNameAndVersion("test-definition", 1);
    //		Assert.assertFalse(d1.getStatus());
    //		DefinitionDO d2 = definitionDAO.selectByNameAndVersion("test-definition", 2);
    //		Assert.assertTrue(d2.getStatus());

    //		// set default to version 1
    //		DefinitionHelper.getInstance().setDefaultDefinition("test-definition", 1);
    //		DefinitionDO d3 = definitionDAO.selectByNameAndVersion("test-definition", 1);
    //		Assert.assertTrue(d3.getStatus());
    //		DefinitionDO d4 = definitionDAO.selectByNameAndVersion("test-definition", 2);
    //		Assert.assertFalse(d4.getStatus());

    //}

}
