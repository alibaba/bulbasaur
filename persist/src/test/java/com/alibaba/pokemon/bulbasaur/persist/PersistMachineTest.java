package com.alibaba.pokemon.bulbasaur.persist;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.pokemon.bulbasaur.persist.constant.ProcessConstant;
import com.alibaba.pokemon.bulbasaur.persist.domain.ProcessDO;
import com.alibaba.pokemon.bulbasaur.persist.domain.StateDOWithBLOBs;
import com.alibaba.pokemon.bulbasaur.persist.exception.ProcessNotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author yunche.ch@taobao.com
 * @since 2012-12-25 下午07:10:06
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/persistMachineTest.xml"})
public class PersistMachineTest {
    @Autowired
    PersistMachineFactory persistMachineFactory;

    private AtomicLong count = new AtomicLong(0);

    private String getBizId() {
        return String.valueOf(System.currentTimeMillis()) + count.getAndAdd(1L);
    }

    @Test
    public void testMachineRun() {
        String bizId = getBizId();
        System.out.println("bizId:" + bizId);
        try {
            persistMachineFactory.newInstance(bizId);
            // noinspection ConstantConditions
            assert (false);
        } catch (ProcessNotFoundException e) {
            System.out.println("process not found, bingo");
        } catch (Exception e) {
            assert (false);
        }
        PersistMachine m = persistMachineFactory.newInstance(bizId, "process");

        m.addContext("goto", 2);
        m.addContext("_i", 3);
        m.run();
        assert (m.getContext().get("_a").equals(6));
        ProcessDO processDO = m.getProcessDO();
        List<StateDOWithBLOBs> stateDOList = m.getStateDOList();
        System.out.println(processDO);
        System.out.println(m.getStateDOList());
        assert (processDO.getStatus().equals(ProcessConstant.STATE_COMPLETE));
        assert (stateDOList.get(stateDOList.size() - 1).getStateName().equals("end"));
    }

    @Test
    public void testMachineRunPerf() throws InterruptedException {
        final int count = 500;
        int threadCount = 5;
        long startTime = System.currentTimeMillis();
        Thread[] threadList = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threadList[i] = new Thread("perf-thread-" + i) {
                @Override
                public void run() {
                    for (int i = 0; i < count; i++) {
                        String bizId = getBizId();
                        PersistMachine m = persistMachineFactory.newInstance(bizId, "process");
                        m.addContext("goto", 2);
                        m.addContext("_i", 3);
                        m.run();
                        System.out.println("thread:" + this.getName() + " count:" + i + " bizId:" + bizId);
                    }
                }
            };
        }
        for (Thread t : threadList) {
            t.start();
        }
        for (Thread t : threadList) {
            t.join();
        }
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("total time:" + duration + "ms");
        System.out.println("average time:" + (duration / (count * threadCount)) + "ms");
    }

    @Test
    public void PersistMachineTest_run_not_DB() {
        // 不走DB
        // 在配置文件中配置 <property name="usePersistParser" value="false"/>
        String bizId = getBizId();
        PersistMachine p = persistMachineFactory.newInstance(bizId, "process");
        p.addContext("goto", 2);// 2
        p.addContext("i", 3);
        p.run();
    }

    @Test
    public void testMachineRunWithException() {

        PersistMachine p = persistMachineFactory.newInstance(getBizId(), "process_with_biz_exception");
        p.addContext("goto", 2);
        p.addContext("_i", 1);

        try {
            p.run();
            Assert.fail("Should get exception here!");
        } catch (Exception e) {
            System.out.println("get a exception:" + e.getMessage() + "\n");
            e.printStackTrace();
        }
        PersistMachine p2 = persistMachineFactory.newInstance(p.getBizId());
        Assert.assertEquals("state2", p2.getCurrentStateName());
        p2.addContext("_i", 0);
        p2.run();
        Assert.assertEquals("end", p2.getCurrentStateName());
    }

    @Test
    public void testMachineRunWithEvent() {

        PersistMachine p = persistMachineFactory.newInstance(getBizId(), "process_with_event_p");

        p.run();
        System.out.println(p.getCurrentStateName());
        Assert.assertEquals("some info", p.getContext("_info"));

        PersistMachine p2 = persistMachineFactory.newInstance(p.getBizId());
        p2.addContext("goto", 2);
        p2.addContext("_i", 3);
        p2.addContext("info", "some info1");
        p2.run();
        Assert.assertEquals("wok", p2.getContext("_errorInfo"));
        System.out.println(p2.getContext());
        Assert.assertEquals(6, p2.getContext("_a"));
    }

    @Test
    public void testPersistMachineRunRollback() {
        // 初始化
        // 创建一个state-machine并run
        PersistMachine p = persistMachineFactory.newInstance(getBizId(), "process_test_rollback");
        p.run();
        // 到达第一个会暂停的状态state1 并返回了_user
        Assert.assertEquals("state1", p.getCurrentStateName());
        Assert.assertEquals("user1", p.getContext().get("_user"));

        // 创建一个state-machine并run 用同一个bizId

        PersistMachine p2 = persistMachineFactory.newInstance(p.getBizId());
        p2.addContext("goto", 2);// 2
        p2.addContext("_i", 3);
        p2.run();
        // 从第一个状态按照goto2到达了第二个状态state2并返回了state2的_user
        Assert.assertEquals("state2", p2.getCurrentStateName());
        Assert.assertEquals("user2", p2.getContext().get("_user"));

        // 同样的bizId 再次获取到state-machine
        PersistMachine p3 = persistMachineFactory.newInstance(p.getBizId());
        System.out.println(p3.getContext());
        Assert.assertEquals(3, p3.getContext().get("_i"));

        // 回滚一下 state2 -> state1
        p3.rollback();
        // 回到了state1的状态
        Assert.assertEquals("state1", p3.getCurrentStateName());
        Assert.assertEquals("user1", p3.getContext().get("_user"));
        Assert.assertNull(p3.getContext().get("_i"));

        // 　run到结束
        p3.addContext("goto", 2);
        p3.run();// 遇到state2 又stop了
        p3.run();
        Assert.assertEquals("end", p3.getCurrentStateName());
        System.out.println(p3.getStatePathString());
    }

    @Test
    public void testRetry() {

        String bizId = "14891527747920";
        PersistMachine m = persistMachineFactory.newInstance(bizId, "process");

        m.addContext("goto", 2);
        m.addContext("_i", 3);
        m.run();
    }
}
