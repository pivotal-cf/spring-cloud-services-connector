/*
 * Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.pivotal.spring.cloud.service.eureka;

import org.junit.Before;
import org.junit.Test;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;

import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Test cases for
 * {@link io.pivotal.spring.cloud.service.eureka.EurekaInstanceAutoConfiguration}
 *
 * @author Chris Schaefer
 * @author Will Tran
 */
public class EurekaInstanceAutoConfigurationTest {
	private static final String ROUTE_REGISTRATION_METHOD = "route";
	private static final String DIRECT_REGISTRATION_METHOD = "direct";
	private static final String INSTANCE_GUID = UUID.randomUUID().toString();
	private static final String INSTANCE_INDEX = "12";
	private static final String HOSTNAME = "www.route.com";
	private static final String IP = "1.2.3.4";
	private static final int PORT = 54321;
	private static final String INSTANCE_ID = UUID.randomUUID().toString();
	private static final String ZONE_URI = "https://eureka-123.west.my-cf.com/eureka/";
	private static final String ZONE = "west.my-cf.com";
	private static final String UNKNOWN_ZONE = "unknown";
	private EurekaInstanceAutoConfiguration eurekaInstanceAutoConfiguration;

	@Before
	public void setup() {
		eurekaInstanceAutoConfiguration = new EurekaInstanceAutoConfiguration();
		eurekaInstanceAutoConfiguration.setHostname(HOSTNAME);
		eurekaInstanceAutoConfiguration.setCfAppGuid(INSTANCE_GUID);
		eurekaInstanceAutoConfiguration.setCfInstanceIndex(INSTANCE_INDEX);
		eurekaInstanceAutoConfiguration.setInstanceId(INSTANCE_ID);
		eurekaInstanceAutoConfiguration.setIp(IP);
		eurekaInstanceAutoConfiguration.setPort(PORT);
		eurekaInstanceAutoConfiguration.setZoneUri(ZONE_URI);
	}

	@Test
	public void testRouteRegistration() {
		eurekaInstanceAutoConfiguration.setRegistrationMethod(ROUTE_REGISTRATION_METHOD);
		testDefaultRegistration();
	}

	@Test
	public void testDefaultRegistration() {
		EurekaInstanceConfigBean eurekaInstanceConfigBean = eurekaInstanceAutoConfiguration.eurekaInstanceConfigBean();
		assertEquals(HOSTNAME + ":" + INSTANCE_ID, eurekaInstanceConfigBean.getInstanceId());
		assertEquals(HOSTNAME, eurekaInstanceConfigBean.getHostname());
		assertEquals(80, eurekaInstanceConfigBean.getNonSecurePort());
		assertEquals(443, eurekaInstanceConfigBean.getSecurePort());
		assertTrue(eurekaInstanceConfigBean.getSecurePortEnabled());

		Map<String, String> metadata = eurekaInstanceConfigBean.getMetadataMap();
		assertEquals(INSTANCE_GUID, metadata.get("cfAppGuid"));
		assertEquals(INSTANCE_INDEX, metadata.get("cfInstanceIndex"));
		assertEquals(INSTANCE_ID, metadata.get("instanceId"));
		assertEquals(ZONE, metadata.get("zone"));
	}

	@Test
	public void testDirectRegistration() {
		eurekaInstanceAutoConfiguration.setRegistrationMethod(DIRECT_REGISTRATION_METHOD);
		EurekaInstanceConfigBean eurekaInstanceConfigBean = eurekaInstanceAutoConfiguration.eurekaInstanceConfigBean();
		assertTrue(eurekaInstanceConfigBean.isPreferIpAddress());
		assertEquals(IP + ":" + INSTANCE_ID, eurekaInstanceConfigBean.getInstanceId());
		assertEquals(IP, eurekaInstanceConfigBean.getHostname());
		assertEquals(PORT, eurekaInstanceConfigBean.getNonSecurePort());
		assertFalse(eurekaInstanceConfigBean.getSecurePortEnabled());
	}

	@Test
	public void testMissingDefaultZoneUri() {
		eurekaInstanceAutoConfiguration.setZoneUri(null);
		EurekaInstanceConfigBean eurekaInstanceConfigBean = eurekaInstanceAutoConfiguration.eurekaInstanceConfigBean();
		assertEquals(UNKNOWN_ZONE, eurekaInstanceConfigBean.getMetadataMap().get("zone"));
	}

	@Test
	public void testEmptyDefaultZoneUri() {
		eurekaInstanceAutoConfiguration.setZoneUri("");
		EurekaInstanceConfigBean eurekaInstanceConfigBean = eurekaInstanceAutoConfiguration.eurekaInstanceConfigBean();
		assertEquals(UNKNOWN_ZONE, eurekaInstanceConfigBean.getMetadataMap().get("zone"));
	}

	@Test
	public void testShortDefaultZoneUri() {
		eurekaInstanceAutoConfiguration.setZoneUri("https://funkylocaldomainname/eureka/");
		EurekaInstanceConfigBean eurekaInstanceConfigBean = eurekaInstanceAutoConfiguration.eurekaInstanceConfigBean();
		assertEquals(UNKNOWN_ZONE, eurekaInstanceConfigBean.getMetadataMap().get("zone"));
	}

	@Test
	public void testMalformedDefaultZoneUri() {
		eurekaInstanceAutoConfiguration.setZoneUri(":" + ZONE_URI);
		EurekaInstanceConfigBean eurekaInstanceConfigBean = eurekaInstanceAutoConfiguration.eurekaInstanceConfigBean();
		assertEquals(UNKNOWN_ZONE, eurekaInstanceConfigBean.getMetadataMap().get("zone"));
	}
}
