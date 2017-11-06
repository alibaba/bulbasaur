package com.tmall.pokemon.bulbasaur.schedule;

import com.tmall.pokemon.bulbasaur.schedule.process.BulbasaurJobProcessor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Resource;

/**
 * @author yunche.ch@taobao.com
 * @since 2012-12-25 下午07:10:06
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/scheduleMachineTest.xml"})
public class ScheduleMachineTest {
    @Autowired
    ScheduleMachineFactory scheduleMachineFactory;
    @Resource
    BulbasaurJobProcessor bulbasaurJobProcessor;
    private AtomicLong count = new AtomicLong(0);

    private String getBizId() {
        return String.valueOf(System.currentTimeMillis()) + count.getAndAdd(1L);
    }

    @Test
    public void testMachineRun() {
        String bizId = getBizId();
        System.out.println("bizId:" + bizId);

        ScheduleMachine m = scheduleMachineFactory.newInstance(bizId, "process");
        m.addContext("goto", 3);
        m.addContext("_i", 3);
        m.run();

        Assert.assertEquals("timer1", m.getCurrentStateName());
        bulbasaurJobProcessor.execute();
        ScheduleMachine end = scheduleMachineFactory.newInstance(bizId, "process");

        Assert.assertEquals("end", end.getCurrentStateName());
    }

}
