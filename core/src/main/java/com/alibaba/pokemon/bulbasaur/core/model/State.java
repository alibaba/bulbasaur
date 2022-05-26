package com.alibaba.pokemon.bulbasaur.core.model;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.pokemon.bulbasaur.core.Result;
import com.alibaba.pokemon.bulbasaur.core.annotation.StateMeta;
import com.alibaba.pokemon.bulbasaur.core.invoke.Invokable;
import com.alibaba.pokemon.bulbasaur.core.invoke.InvokableFactory;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import static com.alibaba.pokemon.bulbasaur.core.constants.XmlTagConstants.IGNOREWEEKEND_TAG;
import static com.alibaba.pokemon.bulbasaur.core.constants.XmlTagConstants.INVOKES_TAG;
import static com.alibaba.pokemon.bulbasaur.core.constants.XmlTagConstants.REPEATLIST_TAG;

/**
 * State节点模型，正常执行
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-12 下午12:08:59
 */
@StateMeta(t = "state")
public class State extends RunnableState {
    private final static Logger logger = LoggerFactory.getLogger(State.class);
    private List<Invokable> invokes = new ArrayList<Invokable>();
    private String repeatList;
    private Boolean ignoreWeekend;

    @Override
    public Result execute(Map<String, Object> context) {
        if (invokes == null || invokes.isEmpty()) {
            // 不想污染上下文，只是把操作出的结果返回去，由machine决定是否接收返回的结果或者是否加入context
            Result result = new Result();
            result.setContinue(true);
            return result;
        } else {
            Map<String, Object> middle = new HashMap<String, Object>();
            Map<String, Object> tempContext = new HashMap<String, Object>(context);
            for (Invokable invokable : invokes) {
                logger.debug("run invokable in execute:" + invokable);
                // 调用方法，异步在invoke父类里面做，非异步在子类里面做
                Object o = invokable.invoke(tempContext);
                if (StringUtils.hasText(invokable.getReturnKey())) {
                    middle.put(invokable.getReturnKey(), o);
                    tempContext.put(invokable.getReturnKey(), o);
                    logger.debug(String.format("result: %s  with return key:", o, invokable.getReturnKey()));
                }
            }
            Result result = new Result();
            result.setModels(middle);
            result.setContinue(true);
            return result;
        }
    }

    @Override
    public StateLike parse(Element elem) {
        super.parse(elem);
        if (elem.attributeValue(REPEATLIST_TAG) != null) {
            repeatList = elem.attributeValue(REPEATLIST_TAG);
        }
        if (elem.attributeValue(IGNOREWEEKEND_TAG) != null) {
            ignoreWeekend = Boolean.valueOf(elem.attributeValue(IGNOREWEEKEND_TAG));
        }

        List<Element> invokesPaths = elem.selectNodes(INVOKES_TAG);
        // 拿孩子节点
        for (Element node : invokesPaths) {
            for (Iterator i = node.elementIterator(); i.hasNext(); ) {
                Element child = (Element)i.next();
                try {
                    invokes.add(InvokableFactory.newInstance(child.getName(), child));
                } catch (RuntimeException re) {
                    logger.error(String.format("实例Invokable类型时候出错，类型为： %s , 异常为： %s", child.getName(), re.toString()));
                    throw re;
                } catch (Throwable e) {
                    logger.error(String.format("实例Invokable类型时候出错，类型为： %s , 异常为： %s", child.getName(), e.toString()));
                    throw new UndeclaredThrowableException(e,
                        "error happened when newInstance Invokable class:" + child.getName());
                }

            }
        }
        return this;
    }

    public String getRepeatList() {
        return repeatList;
    }

    public void setRepeatList(String repeatList) {
        this.repeatList = repeatList;
    }

    public Boolean getIgnoreWeekend() {
        return ignoreWeekend;
    }

    public void setIgnoreWeekend(Boolean ignoreWeekend) {
        this.ignoreWeekend = ignoreWeekend;
    }
}
