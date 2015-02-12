package org.springframework.cloud.pivotal.service.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class GemfireServiceInfoTest {

	@Test
	public void testValidLocatorIP(){
		Map<String,Object> credentials = new HashMap<String, Object>();
		credentials.put("locators", Collections.singletonList("10.0.0.1[1044]"));
		GemfireServiceInfo info = new GemfireServiceInfo("gemfire", credentials);
		Assert.assertEquals(1, info.getLocators().length);
		Assert.assertEquals("10.0.0.1", info.getLocators()[0].getHost());
		Assert.assertEquals(1044, info.getLocators()[0].getPort());
	}
	
	@Test
	public void testValidLocatorHost(){
		Map<String,Object> credentials = new HashMap<String, Object>();
		credentials.put("locators", Collections.singletonList("localhost[1044]"));
		
		GemfireServiceInfo info = new GemfireServiceInfo("gemfire", credentials);
		Assert.assertEquals(1, info.getLocators().length);
		Assert.assertEquals("localhost", info.getLocators()[0].getHost());
		Assert.assertEquals(1044, info.getLocators()[0].getPort());
	}
	
	@Test
	public void testValidLocators(){
		Map<String,Object> credentials = new HashMap<String, Object>();
		List<String> locators = new ArrayList<String>();
		locators.add("localhost[1044]");
		locators.add("10.0.0.1[1044]");
		credentials.put("locators", locators);
		
		GemfireServiceInfo info = new GemfireServiceInfo("gemfire", credentials);
		Assert.assertEquals(2, info.getLocators().length);
		Assert.assertEquals("localhost", info.getLocators()[0].getHost());
		Assert.assertEquals(1044, info.getLocators()[0].getPort());
		Assert.assertEquals("10.0.0.1", info.getLocators()[1].getHost());
		Assert.assertEquals(1044, info.getLocators()[1].getPort());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidLocators(){
		Map<String,Object> credentials = new HashMap<String, Object>();
		credentials.put("locators", Collections.singletonList("10.0.0.1[1024],10.0.0.1[1234]"));
		
		GemfireServiceInfo info = new GemfireServiceInfo("gemfire", credentials);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidFormat(){
		Map<String,Object> credentials = new HashMap<String, Object>();
		credentials.put("locators", Collections.singletonList("10.0.0.1:1044"));
		GemfireServiceInfo info = new GemfireServiceInfo("gemfire", credentials);
	}
}
