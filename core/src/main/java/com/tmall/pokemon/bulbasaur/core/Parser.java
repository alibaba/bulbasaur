package com.tmall.pokemon.bulbasaur.core;

import java.lang.reflect.UndeclaredThrowableException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tmall.pokemon.bulbasaur.core.model.Definition;
import com.tmall.pokemon.bulbasaur.core.model.StateAround;
import com.tmall.pokemon.bulbasaur.core.model.StateFactory;
import com.tmall.pokemon.bulbasaur.core.model.StateLike;

import static com.tmall.pokemon.bulbasaur.util.SimpleUtils.require;

/**
 * 解析器，解析xml，生成一次流程，里面初始化好xml中的一堆state
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-11 上午11:55:36
 */
public class Parser implements BaseParser {
    private final static Logger logger = LoggerFactory.getLogger(Parser.class);

    public static Map<String, StateAround> stateLifeMap = new HashMap<String, StateAround>();

    @Override
    public boolean needRefresh(String processName, int processVersion, Definition oldDefinition) {
        return false;
    }

    @Override
    public Definition parse(String processName, int processVersion) {
        Document processXML = getXML(processName, processVersion);
        return parser0(processName, 1, processXML, true);
    }

    public Definition parser0(String name, int version, Document processXML, boolean status) {
        Element root = processXML.getRootElement();
        String innerName = root.attributeValue("name");
        require(innerName != null, "attribute name in process is required");
        require(name.equals(innerName),
            String.format("name in process not equals given one\n" + "Inner: %s \n  Given: %s", innerName, name));

        String alias = root.attributeValue("alias");
        // 模板
        Definition definition = new Definition(name, "start", version, status, alias);

        // 解析xml
        for (Iterator i = root.elementIterator(); i.hasNext(); ) {
            Element tmp = (Element)i.next();
            StateLike state;
            try {
                state = StateFactory.newInstance(tmp.getName(), tmp);
            } catch (RuntimeException re) {
                logger.error(String.format("实例节点类型时候出错，节点类型为：%s , 异常为：%s", tmp.getName(), re.toString()));
                throw re;
            } catch (Throwable e) {
                logger.error(String.format("实例节点类型时候出错，节点类型为：%s , 异常为：", tmp.getName(), e.toString()));
                throw new UndeclaredThrowableException(e, "error happened when newInstance class:" + tmp.getName());
            }
            if (!state.isRunnable()) {
                definition.addExtNode(state);
            } else {
                definition.addState(state);
                if (!StringUtils.isBlank(tmp.getName()) && (tmp.getName()).contains("start")) {
                    definition.setFirst(state.getStateName());
                }
            }
        }

        return definition;

    }

    /**
     * 从文件读取XML，输入文件名，返回XML文档
     *
     * @param processName
     * @param processVersion
     * @return Document
     * @since 2012-12-27 下午06:52:23
     */
    public Document getXML(String processName, @SuppressWarnings("UnusedParameters") int processVersion) {

        SAXReader reader = new SAXReader();
        URL url = this.getClass().getResource("/" + processName.replaceAll("\\.", "/") + ".xml");
        Document document;
        try {
            document = reader.read(url);
        } catch (Exception e) {
            logger.error("xml流程文件读取失败!模板名：" + processName);
            throw new NullPointerException("xml流程文件读取失败!模板名：" + processName + "\n" + e);
        }

        return document;
    }

}
