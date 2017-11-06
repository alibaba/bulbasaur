package com.tmall.pokemon.bulbasaur.core.model;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tmall.pokemon.bulbasaur.core.Place;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/coreModule.xml" })
public class PlaceTest {
	@Autowired
	Place place;

	@Test
	public void testPlace() {
		Definition definition1 = new Definition("testProcess1", "test", 1, true ,"test");
		Definition definition2 = new Definition("testProcess2", "test", 1, false,"test");
		Definition definition1_2 = new Definition("testProcess2", "test", 2, false,"test");
		Definition definition1_3 = new Definition("testProcess2", "test", 3, true,"test");
		// Place place = new Place();
		place.putIn(definition1);
		place.putIn(definition2);
		place.putIn(definition1_2);

		// find default
		Definition r = place.getDefinition("testProcess1", 0);
		Assert.assertEquals("testProcess1", r.getName());

		try {
			Assert.assertNull((place.getDefinition("testProcess2", 0)));
			// never run to here
		} catch (NullPointerException ne) {
			System.out.println("come in");
		}
		// find with version
		Assert.assertEquals("testProcess2", place.getDefinition("testProcess2", 1).getName());
		Assert.assertEquals(2, place.getDefinition("testProcess2", 2).getVersion());

		// cover default version
		place.putIn(definition1_3);

		// find new default
		Assert.assertEquals(3, place.getDefinition("testProcess2", 0).getVersion());
	}
}