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

package io.pivotal.spring.cloud.service.eureka;

import java.util.List;

import com.netflix.discovery.EurekaClientConfig;
import io.pivotal.spring.cloud.service.common.EurekaServiceInfo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test cases around the Eureka client configuration creator
 *
 * @author Chris Schaefer
 */
public class EurekaClientConfigurationCreatorTest {
	private static final String REGION = "default";
	private static final String SERVICE_INFO_ID = "id";
	private static final int REGISTRY_FETCH_INTERVAL_SECS = 5;
	private static final String AVAILABLITY_ZONE = "defaultZone";
	private static final String URI = "http://user:pass@192.168.23.4:1234";
	private static final String EUREKA_API_PREFIX = "/eureka/";

	private EurekaClientConfigurationCreator eurekaClientConfigurationCreator = new EurekaClientConfigurationCreator();

	@Test
	public void testClientConfiguration() {
		EurekaServiceInfo eurekaServiceInfo = new EurekaServiceInfo(SERVICE_INFO_ID, URI, null, null, null);

		EurekaClientConfig eurekaClientConfig = eurekaClientConfigurationCreator.create(eurekaServiceInfo, null);
		List<String> serviceUrls = eurekaClientConfig.getEurekaServerServiceUrls(null);

		assertEquals(1, serviceUrls.size());
		assertEquals(URI + EUREKA_API_PREFIX, serviceUrls.get(0));
		assertEquals(REGION, eurekaClientConfig.getRegion());
		assertEquals(REGISTRY_FETCH_INTERVAL_SECS, eurekaClientConfig.getRegistryFetchIntervalSeconds());

		String[] availablityZones = eurekaClientConfig.getAvailabilityZones(null);

		assertEquals(1, availablityZones.length);
		assertEquals(AVAILABLITY_ZONE, availablityZones[0]);
	}
}
