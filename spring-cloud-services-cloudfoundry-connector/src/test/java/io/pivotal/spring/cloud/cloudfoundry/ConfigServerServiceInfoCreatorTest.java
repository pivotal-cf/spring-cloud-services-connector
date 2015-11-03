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

import org.junit.Assert;
import org.junit.Test;
import org.springframework.cloud.cloudfoundry.AbstractCloudFoundryConnectorTest;
import org.springframework.cloud.service.ServiceInfo;
import io.pivotal.spring.cloud.service.common.ConfigServerServiceInfo;

import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Connector tests for Config Server services
 *
 * @author Chris Schaefer
 */
public class ConfigServerServiceInfoCreatorTest extends AbstractCloudFoundryConnectorTest {
	private static final String CONFIG_SERVER_SERVICE_TAG_NAME = "myConfigServerService";
	private static final String VCAP_SERVICES_ENV_KEY = "VCAP_SERVICES";
	private static final String PAYLOAD_FILE_NAME = "test-config-server-info.json";
	private static final String PAYLOAD_TEMPLATE_SERVICE_NAME = "$serviceName";
	private static final String PAYLOAD_TEMPLATE_HOSTNAME = "$hostname";
	private static final String PAYLOAD_TEMPLATE_PORT = "$port";
	private static final String PAYLOAD_TEMPLATE_USER = "$user";
	private static final String PAYLOAD_TEMPLATE_PASS = "$pass";

	@Test
	public void configServerServiceCreationWithTags() {
		when(mockEnvironment.getEnvValue(VCAP_SERVICES_ENV_KEY))
				.thenReturn(getServicesPayload(getConfigServerServicePayload(CONFIG_SERVER_SERVICE_TAG_NAME,
						hostname, port, username, password)));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertServiceFoundOfType(serviceInfos, CONFIG_SERVER_SERVICE_TAG_NAME, ConfigServerServiceInfo.class);
		ConfigServerServiceInfo configServiceInfo = (ConfigServerServiceInfo) serviceInfos.stream()
				.filter(serviceInfo -> serviceInfo instanceof ConfigServerServiceInfo)
				.findFirst()
				.orElseThrow(() -> new AssertionError("A ConfigServiceInfo should exist"));
		Assert.assertEquals("config_client_id", configServiceInfo.getClientId());
		Assert.assertEquals("its_a_secret_dont_tell", configServiceInfo.getClientSecret());
		Assert.assertEquals("https://p-spring-cloud-services.uaa.my-cf.com/oauth/token", configServiceInfo.getAccessTokenUri());

	}

	private String getConfigServerServicePayload(String serviceName, String hostname, int port, String user, String password) {
		String payload = readTestDataFile(PAYLOAD_FILE_NAME);
		payload = payload.replace(PAYLOAD_TEMPLATE_SERVICE_NAME, serviceName);
		payload = payload.replace(PAYLOAD_TEMPLATE_HOSTNAME, hostname);
		payload = payload.replace(PAYLOAD_TEMPLATE_PORT, Integer.toString(port));
		payload = payload.replace(PAYLOAD_TEMPLATE_USER, user);
		payload = payload.replace(PAYLOAD_TEMPLATE_PASS, password);

		return payload;
	}
}
