package io.pivotal.spring.cloud.service.eureka;

import io.pivotal.spring.cloud.MockCloudConnector;
import io.pivotal.spring.cloud.service.common.EurekaServiceInfo;

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
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EurekaOAuth2ServiceConnectorIntegrationTest.TestConfig.class)
@IntegrationTest()
public class EurekaOAuth2ServiceConnectorIntegrationTest {

	private static final String ACCESS_TOKEN_URI = "https://p-spring-cloud-services.uaa.my-cf.com/oauth/token";

	private static final String CLIENT_SECRET = "theClientSecret";

	private static final String CLIENT_ID = "theClientId";

	private static final String URI = "https://eureka-12345.mydomain.com";

	@Autowired
	private Environment environment;

	@BeforeClass
	public static void beforeClass() {
		Mockito.when(MockCloudConnector.instance.isInMatchingCloud()).thenReturn(true);
		Mockito.when(MockCloudConnector.instance.getServiceInfos()).thenReturn(Collections.singletonList(
				(ServiceInfo) new EurekaServiceInfo("eureka", URI, CLIENT_ID, CLIENT_SECRET, ACCESS_TOKEN_URI)));
	}

	@AfterClass
	public static void afterClass() {
		MockCloudConnector.reset();
	}

	@Test
	public void propertySourceIsAdded() {
		Assert.assertEquals(CLIENT_ID, environment.getProperty("eureka.client.oauth2.clientId"));
		Assert.assertEquals(CLIENT_SECRET, environment.getProperty("eureka.client.oauth2.clientSecret"));
		Assert.assertEquals(ACCESS_TOKEN_URI, environment.getProperty("eureka.client.oauth2.accessTokenUri"));
	}

	public static class TestConfig {
	}

}
