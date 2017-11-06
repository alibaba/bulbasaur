package com.tmall.pokemon.bulbasaur;

import com.tmall.pokemon.bulbasaur.persist.DefinitionHelper;
import com.tmall.pokemon.bulbasaur.schedule.ScheduleMachine;
import com.tmall.pokemon.bulbasaur.schedule.ScheduleMachineFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author yunche.ch@taobao.com
 * @since 2013-11-12 下午02:14:30
 */
@RunWith(JUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/taskMachineTest.xml"})
public class TaskTest {
    @Autowired
    ScheduleMachineFactory scheduleMachineFactory;
    private AtomicLong count = new AtomicLong(0);

    private String getBizId() {
        return String.valueOf(System.currentTimeMillis()) + count.getAndAdd(1L);
    }

    @Test
    public void testdeployDefinition() {
        // 初始化

        SAXReader reader = new SAXReader();
        // 拿不到信息
        //URL url = this.getClass().getResource("/multipleTask.xml");
        URL url = this.getClass().getResource("/singleTask.xml");
        Document document = null;
        try {
            document = reader.read(url);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String definitionContent = document.asXML();
        // deploy first time
        DefinitionHelper.getInstance().deployDefinition("singleTask", "测试单人任务流程", definitionContent, true);
        //DefinitionHelper.getInstance().deployDefinition("multipleTask", "测试多人任务流程", definitionContent, true);
    }

    @Test
    public void testMachineRun() {
        String bizId = getBizId();
        System.out.println("bizId:" + bizId);

        ScheduleMachine m = scheduleMachineFactory.newInstance(bizId, "singleTask");
        m.addContext("goto", 2);
        m.addContext("_i", 3);
        m.run();
        //        for(int i = 0; i < 5 ; i++){
        //            System.out.println(m.getCurrentStateName());
        //            m.run(m.getCurrentStateName());
        //            System.out.println(m.getCurrentStateName());
        //
        //        }

    }

}
