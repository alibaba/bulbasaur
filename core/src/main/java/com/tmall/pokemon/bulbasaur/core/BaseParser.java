package com.tmall.pokemon.bulbasaur.core;

import com.tmall.pokemon.bulbasaur.core.model.Definition;

/**
 * Parser模型
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-11 上午11:56:40
 */
public interface BaseParser {

    boolean needRefresh(String processName, int processVersion, Definition oldDefinition);

    Definition parse(String processName, int processVersion) throws Throwable;

}
