package io.pivotal.spring.cloud.service.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class GemfireServiceInfoTest {

	@Test
	public void testValidLocatorIP(){
		GemfireServiceInfo info = new GemfireServiceInfo("gemfire", Collections.singletonList("10.0.0.1[1044]"));
		Assert.assertEquals(1, info.getLocators().length);
		Assert.assertEquals("10.0.0.1", info.getLocators()[0].getHost());
		Assert.assertEquals(1044, info.getLocators()[0].getPort());
	}
	
	@Test
	public void testValidLocatorHost(){
		GemfireServiceInfo info = new GemfireServiceInfo("gemfire", Collections.singletonList("localhost[1044]"));
		Assert.assertEquals(1, info.getLocators().length);
		Assert.assertEquals("localhost", info.getLocators()[0].getHost());
		Assert.assertEquals(1044, info.getLocators()[0].getPort());
	}
	
	@Test
	public void testValidLocators(){
		List<String> locators = new ArrayList<String>();
		locators.add("localhost[1044]");
		locators.add("10.0.0.1[1044]");

		GemfireServiceInfo info = new GemfireServiceInfo("gemfire", locators);
		Assert.assertEquals(2, info.getLocators().length);
		Assert.assertEquals("localhost", info.getLocators()[0].getHost());
		Assert.assertEquals(1044, info.getLocators()[0].getPort());
		Assert.assertEquals("10.0.0.1", info.getLocators()[1].getHost());
		Assert.assertEquals(1044, info.getLocators()[1].getPort());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidLocators(){
		new GemfireServiceInfo("gemfire", Collections.singletonList("10.0.0.1[1024],10.0.0.1[1234]"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidFormat(){
		new GemfireServiceInfo("gemfire", Collections.singletonList("10.0.0.1:1044"));
	}
}
