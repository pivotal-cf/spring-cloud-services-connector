/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.pivotal.spring.cloud.cloudfoundry;

import java.util.List;

import io.pivotal.spring.cloud.service.common.HystrixAmqpServiceInfo;
import org.junit.Test;

import org.springframework.cloud.cloudfoundry.AbstractCloudFoundryConnectorTest;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.AmqpServiceInfo;

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
