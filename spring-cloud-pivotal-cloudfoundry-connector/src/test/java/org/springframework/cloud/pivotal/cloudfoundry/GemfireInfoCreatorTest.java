package org.springframework.cloud.pivotal.cloudfoundry;


import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.cloud.pivotal.service.common.GemfireServiceInfo;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GemfireInfoCreatorTest {
	
	
	private ObjectMapper mapper = new ObjectMapper();
	private Map<String, Object> serviceData;
	
	@Before
	public void loadServiceData() throws Exception{
		Map services = mapper.readValue(GemfireInfoCreatorTest.class.getClassLoader().getSystemResourceAsStream("test-gemfire-service.json"), Map.class);
		serviceData = (Map<String, Object>) ((List)services.get("p-gemfire")).get(0);
	}
	
	@Test
	public void testInfoCreator(){
		GemfireInfoCreator creator = new GemfireInfoCreator();
		GemfireServiceInfo info = creator.createServiceInfo(serviceData);
		Assert.assertNotNull(info);
	}
	
}
