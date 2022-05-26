package com.alibaba.bulbasaur.controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 自检
 * User: yunche.ch ... (ว ˙o˙)ง
 * Date: 13-12-3
 * Time: 下午5:25
 */
public class MockController implements Controller{
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		out.println("success");
		return null;
	}

}
