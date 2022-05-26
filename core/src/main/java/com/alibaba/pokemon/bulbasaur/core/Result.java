package com.alibaba.pokemon.bulbasaur.core;

import com.alibaba.pokemon.bulbasaur.core.model.StateLike;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于上下文传递
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-24 下午03:13:44
 */
public class Result implements Serializable {

    private static final long serialVersionUID = 6891513493115885205L;
    private Map<String, Object> models = new HashMap<String, Object>(4);
    private StateLike state;
    private boolean _continue = true;

    public Result() {
    }

    public Result(boolean _continue) {
        this._continue = _continue;
    }

    public Result(boolean _continue, StateLike nextState) {
        this(_continue);
        this.state = nextState;
    }

    public Map<String, Object> getModels() {
        return models;
    }

    public StateLike getState() {
        return state;
    }

    public boolean needContinue() {
        return _continue;
    }

    public void setModels(Map<String, Object> models) {
        this.models = models;
    }

    public void setState(StateLike state) {
        this.state = state;
    }

    public void setContinue(boolean _continue) {
        this._continue = _continue;
    }

}
