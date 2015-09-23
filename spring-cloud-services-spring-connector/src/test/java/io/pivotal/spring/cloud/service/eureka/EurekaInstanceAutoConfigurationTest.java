package io.pivotal.spring.cloud.service.eureka;

import org.junit.Test;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;

import static org.junit.Assert.assertEquals;

/**
 * Test cases for {@link io.pivotal.spring.cloud.service.eureka.EurekaInstanceAutoConfiguration}
 *
 * @author Chris Schaefer
 */
public class EurekaInstanceAutoConfigurationTest {
	private static final int NON_SECURE_PORT = 80;
	private static final String ROUTE_REGISTRATION_METHOD = "route";
	private static final String DIRECT_REGISTRATION_METHOD = "direct";
	private static final String ROUTE_HOSTNAME = "http://www.route.com";
	private static final String DIRECT_HOSTNAME = "http://www.direct.com";
	private static final String DEFAULT_HOSTNAME = ROUTE_HOSTNAME;

	@Test
	public void testRouteRegistration() {
		EurekaInstanceAutoConfiguration eurekaInstanceAutoConfiguration = new EurekaInstanceAutoConfiguration();
		eurekaInstanceAutoConfiguration.setRegistrationMethod(ROUTE_REGISTRATION_METHOD);
		eurekaInstanceAutoConfiguration.setRouteHostName(ROUTE_HOSTNAME);

		EurekaInstanceConfigBean eurekaInstanceConfigBean = eurekaInstanceAutoConfiguration.eurekaInstanceConfigBean();
		assertEquals(ROUTE_HOSTNAME, eurekaInstanceConfigBean.getHostname());
		assertEquals(NON_SECURE_PORT, eurekaInstanceConfigBean.getNonSecurePort());
	}

	@Test
	public void testDirectRegistration() {
		EurekaInstanceAutoConfiguration eurekaInstanceAutoConfiguration = new EurekaInstanceAutoConfiguration();
		eurekaInstanceAutoConfiguration.setRegistrationMethod(DIRECT_REGISTRATION_METHOD);
		eurekaInstanceAutoConfiguration.setDirectHostName(DIRECT_HOSTNAME);
		eurekaInstanceAutoConfiguration.setDirectPort(NON_SECURE_PORT);

		EurekaInstanceConfigBean eurekaInstanceConfigBean = eurekaInstanceAutoConfiguration.eurekaInstanceConfigBean();
		assertEquals(DIRECT_HOSTNAME, eurekaInstanceConfigBean.getHostname());
		assertEquals(NON_SECURE_PORT, eurekaInstanceConfigBean.getNonSecurePort());
	}

	@Test
	public void testDefaultRegistration() {
		EurekaInstanceAutoConfiguration eurekaInstanceAutoConfiguration = new EurekaInstanceAutoConfiguration();
		eurekaInstanceAutoConfiguration.setRouteHostName(DEFAULT_HOSTNAME);

		EurekaInstanceConfigBean eurekaInstanceConfigBean = eurekaInstanceAutoConfiguration.eurekaInstanceConfigBean();
		assertEquals(DEFAULT_HOSTNAME, eurekaInstanceConfigBean.getHostname());
		assertEquals(NON_SECURE_PORT, eurekaInstanceConfigBean.getNonSecurePort());
	}
}
