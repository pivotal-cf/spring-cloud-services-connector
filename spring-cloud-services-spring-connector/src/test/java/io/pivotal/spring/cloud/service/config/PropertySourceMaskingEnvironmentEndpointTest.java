package io.pivotal.spring.cloud.service.config;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class PropertySourceMaskingEnvironmentEndpointTest {

	@Test
	public void shouldMaskPropertiesThatMatchPattern() {
		PropertySourceMaskingEnvironmentEndpoint envEndpoint = new PropertySourceMaskingEnvironmentEndpoint();
		envEndpoint.setSourceNamePatterns("configService:vault:*");
		
		Map<String, Object> properties = new HashMap<>();
		properties.put("abc", "123456");
		properties.put("def", "987654");
		envEndpoint.postProcessSourceProperties("configService:vault:game", properties);
		
		assertEquals("******", properties.get("abc"));
		assertEquals("******", properties.get("def"));
	}
	
	@Test
	public void shouldMaskPropertiesThatDontMatchPattern() {
		PropertySourceMaskingEnvironmentEndpoint envEndpoint = new PropertySourceMaskingEnvironmentEndpoint();
		envEndpoint.setSourceNamePatterns("configService:vault:*");
		
		Map<String, Object> properties = new HashMap<>();
		properties.put("abc", "123456");
		properties.put("def", "987654");
		envEndpoint.postProcessSourceProperties("configService:https://github.com/test-org/test-config/application.yml", properties);
		
		assertEquals("123456", properties.get("abc"));
		assertEquals("987654", properties.get("def"));
	}
}
