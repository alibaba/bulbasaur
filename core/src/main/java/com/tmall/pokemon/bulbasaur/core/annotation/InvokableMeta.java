package com.tmall.pokemon.bulbasaur.core.annotation;

import java.lang.annotation.*;

/**
 * 流程引擎中的自定义注解
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-27 下午05:42:22
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InvokableMeta {
    String t();
}
