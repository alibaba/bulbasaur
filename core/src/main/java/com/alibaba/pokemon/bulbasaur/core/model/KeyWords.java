package com.alibaba.pokemon.bulbasaur.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 用在引擎上下文里的常量
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-27 下午06:41:49
 */
public class KeyWords {

    public static final String CURRENT_STATE_NAME = "currentStateName";
    public static final String CURRENT_PROCESS_NAME = "currentProcessName";

    private static final List<String> KEYWORDS = new ArrayList<String>();

    public KeyWords() {
        KEYWORDS.add(CURRENT_STATE_NAME);
        KEYWORDS.add(CURRENT_PROCESS_NAME);
    }

}
