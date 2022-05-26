package com.alibaba.bulbasaur.controller;

import com.alibaba.pokemon.bulbasaur.schedule.ScheduleMachineFactory;
import com.alibaba.pokemon.bulbasaur.schedule.process.BulbasaurCleanerPrcessor;
import com.alibaba.pokemon.bulbasaur.task.service.TaskAccessor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicLong;


/**
 * 自检
 * User: yunche.ch ... (ว ˙o˙)ง
 * Date: 13-12-3
 * Time: 下午5:25
 */
public class CreateTaskController implements Controller {
    @Resource
    private ScheduleMachineFactory scheduleMachineFactory;
    @Resource
    TaskAccessor taskAccessor;
    @Resource
    BulbasaurCleanerPrcessor clearProcessStatePrcessor;

    private AtomicLong count = new AtomicLong(0);

    private String getBizId() {
        return String.valueOf(System.currentTimeMillis()) + count.getAndAdd(1L);
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String bizId = getBizId();
        System.out.println("bizId:" + bizId);

        clearProcessStatePrcessor.execute(null);

        //ScheduleMachine m = scheduleMachineFactory.newInstance(bizId, "process");
        //m.addContext("goto", 2);
        //m.addContext("_i", 3);
        //m.run();

        //taskAccessor.getTask()
        PrintWriter out = response.getWriter();
        out.println("success");
        return null;
    }

}
