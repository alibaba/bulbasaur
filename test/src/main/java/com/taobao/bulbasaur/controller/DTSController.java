package com.taobao.bulbasaur.controller;

import com.tmall.pokemon.bulbasaur.schedule.ScheduleMachine;
import com.tmall.pokemon.bulbasaur.schedule.ScheduleMachineFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * User:  yunche.ch ... (ว ˙o˙)ง
 * Date: 14-12-25
 * Time: 下午3:52
 */

public class DTSController implements Controller {

	@Resource
	private ScheduleMachineFactory scheduleMachineFactory;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		StringBuilder result = new StringBuilder();
		ScheduleMachine m = scheduleMachineFactory.newInstance(String.valueOf(System.currentTimeMillis()), "dtstest");
		m.addContext("goto", 2);
		m.addContext("_i", 3);
		m.run();

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(result);
		return null;
	}
}
