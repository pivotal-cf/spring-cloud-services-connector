package io.pivotal.spring.cloud.config.java;

import io.pivotal.spring.cloud.MockCloudConnector;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DefaultSpringApplicationNameIntegrationTest.TestConfig.class)
@IntegrationTest({ "vcap.application.name=cloud-app-name" })
public class DefaultSpringApplicationNameIntegrationTest {

	@Autowired
	private Environment environment;

	@Autowired
	private ApplicationContext context;

	@BeforeClass
	public static void beforeClass() {
		Mockito.when(MockCloudConnector.instance.isInMatchingCloud()).thenReturn(true);
	}

	@AfterClass
	public static void afterClass() {
		MockCloudConnector.reset();
	}

	@Test
	public void propertySourceIsAdded() {
		Assert.assertEquals("cloud-app-name", environment.getProperty("spring.application.name"));
	}

	public static class TestConfig {
	}

}
