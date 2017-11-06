package com.tmall.pokemon.bulbasaur.core.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 执行流程模型
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-11 下午12:03:20
 */
public class Definition {

    private String name;
    private String alias;
    private String first;
    private int version;
    private boolean status = true;
    // 可运行节点集合
    private Map<String, StateLike> states = new HashMap<String, StateLike>();
    // 不可运行节点集合
    private Map<Class<? extends StateLike>, StateLike> extNode = new HashMap<Class<? extends StateLike>, StateLike>();

    public Definition(String name, String first, int version, boolean status, String alias) {
        this.name = name;
        this.first = first;
        this.version = version;
        this.status = status;
        this.alias = alias;
    }

    // 由于顶层是接口，所以这里传抽象类
    public void addState(StateLike state) {
        this.states.put(state.getStateName(), state);
    }

    public StateLike getState(String name) {
        return this.states.get(name);
    }

    public void addExtNode(StateLike state) {
        this.extNode.put(state.getClass(), state);
    }

    @SuppressWarnings("unchecked")
    public <T extends StateLike> T getExtNode(Class<T> stateClazz) {
        return (T)extNode.get(stateClazz);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
