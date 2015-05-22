package io.pivotal.spring.cloud.cloudfoundry;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.cloud.cloudfoundry.AbstractCloudFoundryConnectorTest;
import io.pivotal.spring.cloud.service.common.GemfireServiceInfo;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GemfireServiceInfoCreatorTest extends AbstractCloudFoundryConnectorTest {

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void testInfoCreator() throws Exception {
		GemfireServiceInfoCreator creator = new GemfireServiceInfoCreator();
		Map services = readServiceData("test-gemfire-service.json");
		Map<String, Object> serviceData = getServiceData(services, "p-gemfire");

		GemfireServiceInfo info = creator.createServiceInfo(serviceData);
		Assert.assertNotNull(info);
	}

	@Test
	public void testAcceptService() throws Exception {
		GemfireServiceInfoCreator creator = new GemfireServiceInfoCreator();
		Map services = readServiceData("test-gemfire-service.json");
		Map<String, Object> serviceData = getServiceData(services, "p-gemfire");
		boolean accepts = creator.accept(serviceData);
		Assert.assertEquals(true, accepts);
	}

	@Test
	public void testInfoCreatorCups() throws Exception {
		GemfireServiceInfoCreator creator = new GemfireServiceInfoCreator();
		Map services = readServiceData("test-gemfire-userprovided.json");
		Map<String, Object> serviceData = getServiceData(services, "user-provided");

		GemfireServiceInfo info = creator.createServiceInfo(serviceData);
		Assert.assertNotNull(info);
	}

	@Test
	public void testAcceptCups() throws Exception {
		GemfireServiceInfoCreator creator = new GemfireServiceInfoCreator();
		Map services = readServiceData("test-gemfire-userprovided.json");
		Map<String, Object> serviceData = getServiceData(services, "user-provided");
		boolean accepts = creator.accept(serviceData);
		Assert.assertEquals(true, accepts);
	}

	private Map readServiceData(String resource) throws java.io.IOException {
		return mapper.readValue(readTestDataFile(resource), Map.class);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getServiceData(Map services, String name) {
		return (Map<String, Object>) ((List) services.get(name)).get(0);
	}
}
