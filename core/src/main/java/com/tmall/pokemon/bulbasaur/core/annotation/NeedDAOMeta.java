package com.tmall.pokemon.bulbasaur.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 流程引擎中的自定义注解,需要DAO引用
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-27 下午05:42:22
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NeedDAOMeta {
    boolean need();
}
