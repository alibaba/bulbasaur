package com.tmall.pokemon.bulbasaur.task;

import com.tmall.pokemon.bulbasaur.core.CoreModule;
import com.tmall.pokemon.bulbasaur.core.Module;
import com.tmall.pokemon.bulbasaur.persist.PersistModule;
import com.tmall.pokemon.bulbasaur.task.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * User: yunche.ch - - - - (ว ˙o˙)ง
 * Date: 13-11-6
 * Time: 下午4:47
 */
public class TaskModule extends Module implements InitializingBean {
    private final static Logger logger = LoggerFactory.getLogger(TaskModule.class);
    public static Module taskModule;

    public static Module getInstance() {
        if (taskModule == null) {
            taskModule = new TaskModule();
        }
        return taskModule;
    }

    @Override
    public Module[] require() {
        return new Module[] {CoreModule.getInstance(),
            PersistModule.getInstance()};
    }

    @Override
    public void afterInit() {
        //初始化task 节点，set到coreModule里面去
        CoreModule.getInstance().setStateClasses(Task.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        taskModule = this;
    }
}
