package com.alibaba.pokemon.bulbasaur.core;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.pokemon.bulbasaur.core.model.Definition;

/**
 * 处理解析器中完成的模板和节点
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-11 上午11:41:50
 */
public class Place {
    private final static Logger logger = LoggerFactory.getLogger(Place.class);

    // 使用PersistModule时可以被覆盖为PersistParser，从DB读取模板
    private Parser parser;

    public void setParser(Parser parser) {
        this.parser = parser;
    }

    public Map<String, Definition> definitions = new HashMap<String, Definition>();

    /**
     * the key of process will be
     * 1. process's name + "$" + process's version for specifically version |
     * example: orderProcess$1
     * 2. process's name for default version | example: orderProcess
     *
     * 0 for default version
     *
     * @param processName
     * @param processVersion
     * @return
     */
    public Definition getDefinition(String processName, int processVersion) {
        Definition returnDefinition;
        String key;

        if (processVersion == 0) {
            key = processName;
        } else {
            key = processName + "$" + processVersion;
        }

        if (definitions.containsKey(key) && !parser.needRefresh(processName, processVersion, definitions.get(key))) {
            returnDefinition = definitions.get(key);
        } else {
            logger.info(String
                .format("process: %s$%s can't find or need refresh, try to parse it", processName, processVersion));
            Definition newDefinition = parser.parse(processName, processVersion);
            putIn(newDefinition);

            returnDefinition = newDefinition;
        }

        return returnDefinition;

    }

    /**
     * @return void
     * @author: yunche.ch@taobao.com
     * @date 2012-12-21 下午05:02:33
     */
    public void putIn(Definition newDefinition) {
        if (newDefinition.getStatus()) {
            Definition oldDefinition = definitions.get(newDefinition.getName());
            if (oldDefinition != null) { oldDefinition.setStatus(false); }
            definitions.put(newDefinition.getName(), newDefinition);

        }
        definitions.put(newDefinition.getName() + "$" + newDefinition.getVersion(), newDefinition);
    }

}
