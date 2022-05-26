package com.alibaba.pokemon.bulbasaur.core.model;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/coreModule.xml" })
public class StateTest {
	@Test
	public void testWillGo() {
		State state = new State();
		String xml = "<state name=\"state\">\n" + "                <paths>\n" + "                  <path to=\"1\" expr=\"i==1\"/>\n" + "                  <path to=\"2\" expr=\"i==2\"/>\n" + "                </paths>\n" + "              </state>";
		Document document = null;
		try {
			document = DocumentHelper.parseText(xml);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StateLike s = state.parse(document.getRootElement());
		Map m = new HashMap();
		m.put("i", 2);
		String result = null;
		try {
			result = s.willGo(m);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertEquals("2", result);
	}
}
