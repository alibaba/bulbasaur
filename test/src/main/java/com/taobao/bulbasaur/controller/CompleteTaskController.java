package com.taobao.bulbasaur.controller;

import com.tmall.pokemon.bulbasaur.persist.domain.InvokeResult;
import com.tmall.pokemon.bulbasaur.persist.domain.QueryResult;
import com.tmall.pokemon.bulbasaur.persist.domain.TaskDO;
import com.tmall.pokemon.bulbasaur.persist.domain.TaskDOExample;
import com.tmall.pokemon.bulbasaur.task.constant.TaskConstant;
import com.tmall.pokemon.bulbasaur.task.constant.TaskStatusEnum;
import com.tmall.pokemon.bulbasaur.task.dto.TaskParam;
import com.tmall.pokemon.bulbasaur.task.dto.TaskQuery;
import com.tmall.pokemon.bulbasaur.task.model.User;
import com.tmall.pokemon.bulbasaur.task.service.TaskAccessor;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicLong;

/**
 * User:  yunche.ch ... (ว ˙o˙)ง
 * Date: 14-12-24
 * Time: 下午5:10
 */

public class CompleteTaskController implements Controller {

    public static final String state3 = "state3";
    @Resource
    TaskAccessor taskAccessor;

    private AtomicLong count = new AtomicLong(0);

    private String getBizId() {
        return String.valueOf(System.currentTimeMillis()) + count.getAndAdd(1L);
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        StringBuilder result = new StringBuilder();
        String userId = request.getParameter("userId");
        if (StringUtils.isNotBlank(userId)) {

            TaskQuery taskQuery = new TaskQuery();
            taskQuery.setUserId(Long.valueOf(userId));
            taskQuery.setTaskStatusEnum(TaskStatusEnum.STATUS_TAKEN);

            QueryResult<TaskDO> queryResult = taskAccessor.queryTasks(taskQuery);
            if (queryResult.isSuccess() && queryResult.getTotal() > 0) {
                for (TaskDO tmp : queryResult.getDataList()) {
                    User user = new User();
                    user.setId(Long.valueOf(userId));
                    TaskParam taskParam = new TaskParam();
                    taskParam.setTaskId(tmp.getId());
                    taskParam.setOutGoing("state3");
                    taskParam.setUser(user);
                    taskParam.setMemo("同意");
                    InvokeResult<Void> re = taskAccessor.completeTask(taskParam);
                    String msg = String.format("审批结果:%s,审批内容:%s", re.isSuccess(), re.getInfo());
                    result.append(msg).append("\n");
                }
            } else {
                result.append("该用户不存在审批记录！");
            }

        } else {
            result.append("未传入userId");
        }

        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println(result);
        return null;
    }

}
