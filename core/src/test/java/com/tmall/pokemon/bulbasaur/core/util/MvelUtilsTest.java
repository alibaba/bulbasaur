package com.tmall.pokemon.bulbasaur.core.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import test.TestBean;

import com.tmall.pokemon.bulbasaur.util.MvelUtils;

/**
 * Created by IntelliJ IDEA. User: guichen - anson Date: 12-2-7
 */

public class MvelUtilsTest {

	@Test
	public void testEvalToBoolean() {
		String expr = "a == 1 && b == 2";
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("a", 1);
		m.put("b", 3);
		boolean result = MvelUtils.evalToBoolean(expr, m);
		System.out.println("result of " + expr + " is " + result);
		Assert.assertFalse(result);

		String expr2 = "o != null";
		m.put("o", new Object());
		Assert.assertTrue(MvelUtils.evalToBoolean(expr2, m));
	}

	@Test
	public void testEval() {
		String expr = "b == 2; a = 1";
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("b", 3);

		Assert.assertEquals(1, MvelUtils.eval(expr, m));
	}

	@Test
	public void testNullParameter() {
		String expr = "testBean.testNullParameter(p)";
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("testBean", new TestBean());
		m.put("p", null);// 因为函数里面是integer，才能传null，int的要传0. (=。=!)，语法错误
		System.out.println(MvelUtils.eval(expr, m));
	}
}