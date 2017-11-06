package com.taobao.bulbasaur.controller;

import com.tmall.pokemon.bulbasaur.persist.domain.QueryResult;
import com.tmall.pokemon.bulbasaur.persist.domain.TaskDO;
import com.tmall.pokemon.bulbasaur.persist.domain.TaskDOExample;
import com.tmall.pokemon.bulbasaur.task.dto.TaskQuery;
import com.tmall.pokemon.bulbasaur.task.model.User;
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

public class AssignTaskController implements Controller {

    @Resource
    private com.tmall.pokemon.bulbasaur.task.service.TaskAccessor taskAccessor;

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        StringBuilder result = new StringBuilder();
        String userId = request.getParameter("userid");

        User current = new User();
        current.setId(Long.valueOf(userId));

        User assignUser = new User();
        assignUser.setId(90L);
        assignUser.setName("机械键盘");

        TaskQuery taskQuery = new TaskQuery();
        taskQuery.setUserId(Long.valueOf(userId));
        QueryResult<TaskDO> queryResult = taskAccessor.queryTasks(taskQuery);
        if (queryResult.isSuccess() && queryResult.getTotal() > 0) {
            for (TaskDO tmp : queryResult.getDataList()) {
                taskAccessor.assignTaskWithResult(tmp.getId(), current, assignUser, "转移了");
            }
        } else {
            result.append("该用户不存在审批记录！");
        }

        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println(result);
        return null;
    }
}
