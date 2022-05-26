package com.alibaba.pokemon.bulbasaur.core.model;

import java.util.Map;

import com.alibaba.pokemon.bulbasaur.exception.CoreModuleException;

/**
 * 路径接口
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-27 下午06:42:21
 */
public interface PathLike {

    String to();

    boolean can(Map<String, Object> vars) throws CoreModuleException;

}
