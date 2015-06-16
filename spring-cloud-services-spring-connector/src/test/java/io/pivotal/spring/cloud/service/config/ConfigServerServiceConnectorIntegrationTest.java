package io.pivotal.spring.cloud.service.config;

import java.util.Collections;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import io.pivotal.spring.cloud.MockCloudConnector;
import io.pivotal.spring.cloud.service.common.ConfigServerServiceInfo;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConfigServerServiceConnectorIntegrationTest.TestConfig.class)
@IntegrationTest()
public class ConfigServerServiceConnectorIntegrationTest {

	private static final String URI = "http://username:password@config-server.mydomain.com";

	@Autowired
	private Environment environment;

	@BeforeClass
	public static void beforeClass() {
		Mockito.when(MockCloudConnector.instance.isInMatchingCloud()).thenReturn(true);
		Mockito.when(MockCloudConnector.instance.getServiceInfos()).thenReturn(Collections.singletonList(
				(ServiceInfo) new ConfigServerServiceInfo("config-server", URI)));
	}

	@AfterClass
	public static void afterClass() {
		MockCloudConnector.reset();
	}

	@Test
	public void propertySourceIsAdded() {
		Assert.assertEquals(URI, environment.getProperty("spring.cloud.config.uri"));
	}

	public static class TestConfig {
	}

}
