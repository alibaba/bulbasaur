package com.tmall.pokemon.bulbasaur.core.model;

import com.tmall.pokemon.bulbasaur.core.Result;
import org.dom4j.Element;

import java.util.Map;

/**
 * State 接口，定义动作模型
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-11 下午9:03:16
 */
public interface StateLike {

    boolean isRunnable();

    /**
     * @return
     */
    String getStateName();

    String getStateAlias();

    String getBizId();

    Result prepare(Map<String, Object> context);

    Result execute(Map<String, Object> context);

    String willGo(Map<String, Object> context);

    StateLike parse(Element elem);

    String getJumpTo();

    void setJumpTo(String jumpTo);

    void setBizId(String bizId);

    /**
     * 这里放入map而不是application，不想跟spring 紧耦合
     *
     * @param map
     */
    void setDAOMap(Map<String, ?> map);

    String getOutGoing();

    void setOutGoing(String outGoing);

    void setDefinitionName(String definitionName);
}
