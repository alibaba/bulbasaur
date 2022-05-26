/**
 * Filename:    ModuleFactory.java
 * Description:
 *
 * @author: yunche.ch@taobao.com
 * @version: 1.0
 * @date 2012-12-17 下午12:18:16
 */
package com.alibaba.pokemon.bulbasaur.core;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yunche.ch@taobao.com
 * @Description:
 * @since 2012-12-17 下午12:18:16
 */
public class MachineFactory {

    @Autowired
    private Place place;

    public Machine newInstance(String processName) {
        return newInstance(processName, 0);// default
    }

    public Machine newInstance(String processName, int processVersion) {
        return new Machine(processName, processVersion, place);// default
    }

}
