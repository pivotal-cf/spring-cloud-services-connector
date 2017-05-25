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
		Map<String, Object> processedProperties = envEndpoint.postProcessSourceProperties("configService:vault:game", properties);
		
		assertEquals(1, processedProperties.size());
		assertEquals(PropertySourceMaskingEnvironmentEndpoint.DEFAULT_MESSAGE, processedProperties.get("******"));
	}
	
	@Test
	public void shouldMaskPropertiesThatMatchPatternWithCustomMessage() {
		PropertySourceMaskingEnvironmentEndpoint envEndpoint = new PropertySourceMaskingEnvironmentEndpoint();
		envEndpoint.setSourceNamePatterns("configService:vault:*");
		envEndpoint.setMessage("No properties for you");
		
		Map<String, Object> properties = new HashMap<>();
		properties.put("abc", "123456");
		properties.put("def", "987654");
		Map<String, Object> processedProperties = envEndpoint.postProcessSourceProperties("configService:vault:game", properties);
		
		assertEquals(1, processedProperties.size());
		assertEquals("No properties for you", processedProperties.get("******"));
	}
	
	@Test
	public void shouldNotMaskPropertiesThatDoNotMatchPattern() {
		PropertySourceMaskingEnvironmentEndpoint envEndpoint = new PropertySourceMaskingEnvironmentEndpoint();
		envEndpoint.setSourceNamePatterns("configService:vault:*");
		
		Map<String, Object> properties = new HashMap<>();
		properties.put("abc", "123456");
		properties.put("def", "987654");
		Map<String, Object> processedProperties = envEndpoint.postProcessSourceProperties("configService:https://github.com/test-org/test-config/application.yml", properties);
		
		assertEquals("123456", processedProperties.get("abc"));
		assertEquals("987654", processedProperties.get("def"));
	}
}
