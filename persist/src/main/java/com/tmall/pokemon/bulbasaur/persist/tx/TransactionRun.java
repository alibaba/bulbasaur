package com.tmall.pokemon.bulbasaur.persist.tx;

/**
 * 事务接口
 * Created by IntelliJ IDEA.
 * User: guichen - anson
 * Date: 12-12-21
 */
public interface TransactionRun<T> {
    T run();
}
