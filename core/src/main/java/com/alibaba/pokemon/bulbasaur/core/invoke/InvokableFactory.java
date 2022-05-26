package com.alibaba.pokemon.bulbasaur.core.invoke;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.pokemon.bulbasaur.core.annotation.InvokableMeta;
import com.alibaba.pokemon.bulbasaur.util.SimpleUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.alibaba.pokemon.bulbasaur.util.SimpleUtils.require;

/**
 * @author yunche.ch@taobao.com
 * @since 2013-1-6 上午11:36:15
 */
public class InvokableFactory {
	private final static Logger logger = LoggerFactory.getLogger(InvokableFactory.class);

	private static Map<String, Class<? extends Invokable>> invokableMap = new HashMap<String, Class<? extends Invokable>>();

	/**
	 * @since 2013-1-6 上午11:58:30
	 * @param name
	 * @return Invokable
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static Invokable newInstance(String name, Element elem) throws InstantiationException, IllegalAccessException {
		Class clazz = InvokableFactory.invokableMap.get(name);
		SimpleUtils.require(clazz != null,
				"InvokableFactory find no Invokable class for name:" + name);
		return ((Invokable) clazz.newInstance()).init(elem);
	}

	public static void applyInvokable(Class<? extends Invokable> invokeClass) {
		logger.debug("apply invokable: " + invokeClass.getName());
		InvokableMeta invokableMeta = invokeClass.getAnnotation(InvokableMeta.class);
		SimpleUtils.require(invokableMeta != null,
				"State class [" + invokeClass.getName() + "] should be annotated by annotation @InvokableMeta");
		invokableMap.put(invokableMeta.t(), invokeClass);
	}

	public static boolean contains(String name) {
		return invokableMap.containsKey(name);
	}
}
