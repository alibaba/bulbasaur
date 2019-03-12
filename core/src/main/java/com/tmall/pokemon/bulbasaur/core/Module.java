package com.tmall.pokemon.bulbasaur.core;

/**
 * module抽象类，定义模型
 *
 * @author yunche.ch@taobao.com
 * @since 2012-12-3 上午10:41:30
 */
public abstract class Module {

	public Module[] require() {
		return new Module[] {};
	}

	public Module[] optionalRequire() {
		return new Module[] {};
	}

	/* will be true after init */
	boolean initialized = false;

	/**
	 * 不依赖于spring的初始化
	 */
	public final void init(String ownSign, String quartzTablePrefix) {
		afterInit(ownSign, quartzTablePrefix);
		initialized = true;
	}

	public void afterInit(String ownSign, String quartzTablePrefix) {

	}

}
