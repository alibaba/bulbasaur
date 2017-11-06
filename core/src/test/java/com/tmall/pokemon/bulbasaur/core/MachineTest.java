package com.tmall.pokemon.bulbasaur.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tmall.pokemon.bulbasaur.core.invoke.InvokableFactory;
import junit.framework.Assert;

/**
 * 测试Machine，其中覆盖了普通执行，带Event的执行
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-25 下午07:02:02
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/machineTest.xml", "classpath:/coreModule.xml"})
public class MachineTest {
    @Autowired
    private MachineFactory machineFactory;

    @Test
    public void testMachineRun() {
        Machine m = machineFactory.newInstance("process");
        m.addContext("goto", 2);// 2
        m.addContext("i", 3);
        m.run();
        Assert.assertEquals(6, m.getContext("a"));
    }

    @Test
    public void testMachineRunPerformance() {
        // parse first
        machineFactory.newInstance("process");
        // run
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            Machine mt = machineFactory.newInstance("process");
            mt.addContext("goto", 2);
            mt.addContext("i", 3);
            mt.run();

            Assert.assertEquals(6, mt.getContext("a"));
        }
        long end = System.currentTimeMillis();
        System.out.println("run machine 1000 times, it spend:" + (end - start) + "ms");

    }

    @Test
    public void testMachineWithEvent() {
        // _从I_am_start开始_到event结束
        Machine m = machineFactory.newInstance("process_with_event");
        m.run();
        System.out.println("当前节点" + m.getCurrentStateName());
        System.out.println(m.getContext());
        Assert.assertEquals("some info", m.getContext("info"));

        Machine m2 = machineFactory.newInstance("process_with_event");
        m2.addContext("goto", 2);
        m2.addContext("i", 3);
        m2.run(m.getCurrentStateName());
        System.out.println(m2.getContext());
        Assert.assertEquals(6, m2.getContext("a"));
    }

    @Test
    public void testMachineWithEvent1() {
        // _从state1开始到计算完毕
        Machine m = machineFactory.newInstance("process_with_event");
        m.addContext("goto", 2);
        m.addContext("i", 3);
        m.run("state1");
    }

    @Test
    public void testMachineWithException() {
        Machine m = machineFactory.newInstance("process_with_exception");
        m.addContext("goto", 2);
        try {
            m.run();
            // assert(false)
            Assert.fail("Should get exception here!");
        } catch (Exception e) {
            System.out.println("get a exception:" + e.getMessage() + "\n");
            e.printStackTrace();
        }
        Assert.assertEquals("state2", m.getCurrentStateName());
        Machine m2 = machineFactory.newInstance("process_with_exception");
        m2.addContext("goto", 3);
        try {
            m2.run();
            Assert.fail("Should get exception here!");
        } catch (Exception e) {
            System.out.println("get a exception:" + e.getMessage() + "\n");
            e.printStackTrace();
        }

        Assert.assertEquals("state1", m2.getCurrentStateName());
    }

    @Test
    public void testInvokableClassInject() {
        Assert.assertTrue(InvokableFactory.contains("testInvoke"));
    }

    @Test
    public void test() {
        Machine m = machineFactory.newInstance("repeatState");
        m.addContext("goto", 2);// 2
        m.addContext("_a", 3);
        m.run();
    }
}
