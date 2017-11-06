package com.tmall.pokemon.bulbasaur.core.model;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tmall.pokemon.bulbasaur.core.annotation.StateMeta;

import static com.tmall.pokemon.bulbasaur.util.SimpleUtils.require;

/**
 * @author yunche.ch@taobao.com
 * @since 2013-1-6 上午11:23:58
 */
public class StateFactory {
	private final static Logger logger = LoggerFactory.getLogger(StateFactory.class);

	private static Map<String, Class<? extends StateLike>> stateMap = new HashMap<String, Class<? extends StateLike>>();

	/**
	 * @since 2013-1-6 上午11:30:28
	 * @param name
	 * @param elem
	 * @return StateLike
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static StateLike newInstance(String name, Element elem) throws InstantiationException, IllegalAccessException {
		Class clazz = StateFactory.stateMap.get(name);
		require(clazz != null, "StateFactory find no State class for name:" + name);
		return ((StateLike) clazz.newInstance()).parse(elem);
	}

	public static void applyState(Class<? extends StateLike> stateClass) {
		logger.debug("apply state: " + stateClass.getName());
		StateMeta stateMeta = stateClass.getAnnotation(StateMeta.class);
		require(stateMeta != null,
			"State class [\" + stateClass.getName() + \"] should be annotated by annotation @StateMeta");
		stateMap.put(stateMeta.t(), stateClass);
	}

	public static boolean contains(String name) {
		return stateMap.containsKey(name);
	}
}
