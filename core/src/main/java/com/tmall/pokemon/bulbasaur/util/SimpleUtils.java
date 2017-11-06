package com.tmall.pokemon.bulbasaur.util;

/**
 * Created by IntelliJ IDEA.
 * User: guichen - anson
 * Date: 13-1-9
 */
public class SimpleUtils {
	public static void notnull(Object o) {
		if (o == null) {
			throw new NullPointerException("null pointer found");
		}
	}

	public static void notnull(Object o, String message) {
		if (o == null) {
			throw new NullPointerException("null pointer found: " + message);
		}
	}

	public static void require(boolean condition) {
		if (!condition) {
			throw new IllegalArgumentException("requirement failed");
		}
	}

	public static void require(boolean condition, String message) {
		if (!condition) {
			throw new IllegalArgumentException("requirement failed: " + message);
		}
	}
}
