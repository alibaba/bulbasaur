package com.tmall.pokemon.bulbasaur.core.model;

import java.util.Map;

import org.dom4j.Element;

import com.tmall.pokemon.bulbasaur.core.Result;

/**
 * 非可执行业务类型，主要做信息透传
 *
 * Created by IntelliJ IDEA.
 * User: guichen - anson
 * Date: 13-1-7
 */
public abstract class NotRunnableState implements StateLike {
    @Override
    public boolean isRunnable() {
        return false;
    }

    @Override
    public String getStateName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStateAlias() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getBizId() {
        return null;
    }

    @Override
    public Result prepare(Map<String, Object> context) {
        // do nothing
        return null;
    }

    @Override
    public Result execute(Map<String, Object> context) {
        // do nothing
        return null;
    }

    @Override
    public String willGo(Map<String, Object> context) {
        // do nothing
        return null;
    }

    /* (non-Javadoc)
     * @see com.tmall.pokemon.bulbasaur.core.model.StateLike#setDAOMap(java.util.Map)
     */
    @Override
    public void setDAOMap(Map<String, ?> map) {
        // TODO Auto-generated method stub

    }
}
