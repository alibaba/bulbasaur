package com.tmall.pokemon.bulbasaur.core.model;

import java.util.Map;

/**
 * 暂时没用
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-12 上午11:20:59
 */
public interface StateAround {

    Map<String, Object> beforePrepare(Map<String, Object> context, StateLike state);

    Map<String, Object> afterPrepare(Map<String, Object> context, StateLike state);

    Map<String, Object> beforeExecute(Map<String, Object> context, StateLike state);

    Map<String, Object> afterExecute(Map<String, Object> context, StateLike state);

}
