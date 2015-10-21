package io.pivotal.spring.cloud.service.eureka;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;

/**
 * Test cases for {@link io.pivotal.spring.cloud.service.eureka.EurekaInstanceAutoConfiguration}
 *
 * @author Chris Schaefer
 * @author Will Tran
 */
public class EurekaInstanceAutoConfigurationTest {
	private static final String ROUTE_REGISTRATION_METHOD = "route";
	private static final String DIRECT_REGISTRATION_METHOD = "direct";
	private static final String HOSTNAME = "www.route.com";
	private static final String IP = "1.2.3.4";
	private static final int PORT = 54321;
	private static final String INSTANCE_ID = UUID.randomUUID().toString();
	private EurekaInstanceAutoConfiguration eurekaInstanceAutoConfiguration;

	@Before
	public void setup() {
		eurekaInstanceAutoConfiguration = new EurekaInstanceAutoConfiguration();
		eurekaInstanceAutoConfiguration.setHostname(HOSTNAME);
		eurekaInstanceAutoConfiguration.setInstanceId(INSTANCE_ID);
		eurekaInstanceAutoConfiguration.setIp(IP);
		eurekaInstanceAutoConfiguration.setPort(PORT);
	}

	@Test
	public void testRouteRegistration() {
		eurekaInstanceAutoConfiguration.setRegistrationMethod(ROUTE_REGISTRATION_METHOD);
		testDefaultRegistration();
	}

	@Test
	public void testDefaultRegistration() {
		EurekaInstanceConfigBean eurekaInstanceConfigBean = eurekaInstanceAutoConfiguration.eurekaInstanceConfigBean();
		assertEquals(INSTANCE_ID, eurekaInstanceConfigBean.getMetadataMap().get("instanceId"));
		assertEquals(HOSTNAME, eurekaInstanceConfigBean.getHostname());
		assertEquals(80, eurekaInstanceConfigBean.getNonSecurePort());
		assertEquals(443, eurekaInstanceConfigBean.getSecurePort());
		assertTrue(eurekaInstanceConfigBean.getSecurePortEnabled());
	}

	@Test
	public void testDirectRegistration() {
		eurekaInstanceAutoConfiguration.setRegistrationMethod(DIRECT_REGISTRATION_METHOD);
		EurekaInstanceConfigBean eurekaInstanceConfigBean = eurekaInstanceAutoConfiguration.eurekaInstanceConfigBean();
		assertEquals(INSTANCE_ID, eurekaInstanceConfigBean.getMetadataMap().get("instanceId"));
		assertEquals(IP, eurekaInstanceConfigBean.getHostname());
		assertEquals(PORT, eurekaInstanceConfigBean.getNonSecurePort());
		assertFalse(eurekaInstanceConfigBean.getSecurePortEnabled());
	}
}
