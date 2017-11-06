package com.taobao.bulbasaur.service;

/**
 * User:  yunche.ch ... (ว ˙o˙)ง
 * Date: 14-12-24
 * Time: 下午3:43
 */

public class NotifyPage {

    public void doNotify(String taskIds) {
        System.out.println("将taskId传递给业务bean，可以再来调用 complete 接口！");
        System.out.println(taskIds);
    }
}
