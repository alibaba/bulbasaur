package com.tmall.pokemon.bulbasaur.core.model;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tmall.pokemon.bulbasaur.exception.CoreModuleException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/coreModule.xml" })
public class PathTest {
	@Test
	public void testCan() {
		// true
		Path path = new Path("to", "true");
		try {
			Assert.assertTrue(path.can(null));
		} catch (CoreModuleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Int judge
		Path path1 = new Path("to", "param == 1");
		Map m = new HashMap();
		m.put("param", 1);
		try {
			Assert.assertTrue(path1.can(m));
		} catch (CoreModuleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// String judge
		Path path2 = new Path("to", "if (param == \"123\") {true} else {false}");
		Map m1 = new HashMap();
		m1.put("param", "123");
		try {
			Assert.assertTrue(path2.can(m1));
		} catch (CoreModuleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// blank expr
		Path path3 = new Path("to", null);
		try {
			Assert.assertTrue(path3.can(null));
		} catch (CoreModuleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// null judge
		Path path4 = new Path("to", "null");
		try {
			Assert.assertFalse(path4.can(null));
		} catch (CoreModuleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}