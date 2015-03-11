package org.springframework.cloud.pivotal.cloudfoundry;

import org.junit.Test;
import org.springframework.cloud.cloudfoundry.AbstractCloudFoundryConnectorTest;
import org.springframework.cloud.pivotal.service.common.HystrixAmqpServiceInfo;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.AmqpServiceInfo;

import java.util.List;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * Connector tests for Hystrix AMQP services
 *
 * @author Scott Frederick
 */
public class HystrixAmqpServiceInfoCreatorTest extends AbstractCloudFoundryConnectorTest {
	private static final String SERVICE_TAG_NAME = "myHystrixAmqpInstance";
	private static final String VCAP_SERVICES_ENV_KEY = "VCAP_SERVICES";
	private static final String PAYLOAD_FILE_NAME = "test-hystrix-amqp-info.json";
	private static final String PAYLOAD_TEMPLATE_SERVICE_NAME = "$serviceName";
	private static final String PAYLOAD_TEMPLATE_HOSTNAME = "$hostname";
	private static final String PAYLOAD_TEMPLATE_PORT = "$port";
	private static final String PAYLOAD_TEMPLATE_USER = "$user";
	private static final String PAYLOAD_TEMPLATE_PASS = "$pass";
	private static final String PAYLOAD_TEMPLATE_VHOST = "$vhost";

	@Test
	public void hystrixAmqpServiceCreation() {
		when(mockEnvironment.getEnvValue(VCAP_SERVICES_ENV_KEY))
				.thenReturn(getServicesPayload(getServicePayload(SERVICE_TAG_NAME, hostname, port, username, password, "vhost1")));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertServiceFoundOfType(serviceInfos, SERVICE_TAG_NAME, HystrixAmqpServiceInfo.class);

		// ensure this type will not match on AmqpServiceInfos
		assertServiceNotFoundOfType(serviceInfos, AmqpServiceInfo.class);
	}

	private void assertServiceNotFoundOfType(List<ServiceInfo> serviceInfos, Class<? extends ServiceInfo> expected) {
		for (ServiceInfo serviceInfo : serviceInfos) {
			if (expected.isAssignableFrom(serviceInfo.getClass())) {
				fail("ServiceInfo of type " + expected.getName() + " should not have been found");
			}
		}
	}

	private String getServicePayload(String serviceName, String hostname, int port, String user, String password, String vhost) {
		String payload = readTestDataFile(PAYLOAD_FILE_NAME);
		payload = payload.replace(PAYLOAD_TEMPLATE_SERVICE_NAME, serviceName);
		payload = payload.replace(PAYLOAD_TEMPLATE_HOSTNAME, hostname);
		payload = payload.replace(PAYLOAD_TEMPLATE_PORT, Integer.toString(port));
		payload = payload.replace(PAYLOAD_TEMPLATE_USER, user);
		payload = payload.replace(PAYLOAD_TEMPLATE_PASS, password);
		payload = payload.replace(PAYLOAD_TEMPLATE_VHOST, vhost);

		return payload;
	}
}
