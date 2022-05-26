package com.alibaba.pokemon.bulbasaur.core.model;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.pokemon.bulbasaur.core.Result;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.alibaba.pokemon.bulbasaur.core.annotation.StateMeta;
import com.alibaba.pokemon.bulbasaur.core.invoke.Invokable;
import com.alibaba.pokemon.bulbasaur.core.invoke.InvokableFactory;

import static com.alibaba.pokemon.bulbasaur.core.constants.XmlTagConstants.PRE_INVOKES_TAG;

/**
 * Event节点类型，当引擎执行到该节点，会停止
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-27 下午06:41:30
 */
@StateMeta(t = "event")
public class Event extends State {
    private final static Logger logger = LoggerFactory.getLogger(Event.class);

    private List<Invokable> preInvokes = new ArrayList<Invokable>();

    @Override
    public Result prepare(Map<String, Object> context) {
        if (preInvokes == null || preInvokes.isEmpty()) {
            // 不想污染上下文，只是把操作出的结果返回去，由machine决定是否接收返回的结果或者是否加入context
            Result result = new Result();
            result.setContinue(false);
            return result;
        } else {
            Map<String, Object> middle = new HashMap<String, Object>();
            Map<String, Object> tempContext = new HashMap<String, Object>(context);
            for (Invokable invokable : preInvokes) {
                logger.debug("run invokable in pre:" + invokable);
                //调用方法，异步在invoke父类里面做，非异步在子类里面做
                Object o = invokable.invoke(tempContext);
                if (StringUtils.hasText(invokable.getReturnKey())) {
                    middle.put(invokable.getReturnKey(), o);
                    tempContext.put(invokable.getReturnKey(), o);
                    logger.debug(String.format("result: %s with return key: %s", o, invokable.getReturnKey()));
                }
            }
            Result result = new Result();
            result.setModels(middle);
            result.setContinue(false);
            return result;
        }

    }

    @Override
    public StateLike parse(Element elem) {
        super.parse(elem);
        List<Element> pre_invokesPaths = elem.selectNodes(PRE_INVOKES_TAG);
        for (Element node : pre_invokesPaths) {// 拿 孩子节点
            for (Iterator i = node.elementIterator(); i.hasNext(); ) {
                Element child = (Element)i.next();
                try {
                    preInvokes.add(InvokableFactory.newInstance(child.getName(), child));
                } catch (RuntimeException re) {
                    logger.error(String.format("实例Invokable类型时候出错，类型为：%s , 异常为： %s", child.getName(), re.toString()));
                    throw re;
                } catch (Throwable e) {
                    logger.error(String.format("实例Invokable类型时候出错，类型为： %s , 异常为：", child.getName(), e.toString()));
                    throw new UndeclaredThrowableException(e,
                        "error happened when newInstance Invokable class:" + child.getName());
                }
            }
        }
        return this;
    }

}
