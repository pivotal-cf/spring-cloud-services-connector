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

import java.util.Collections;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import io.pivotal.spring.cloud.MockCloudConnector;
import io.pivotal.spring.cloud.service.common.EurekaServiceInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static io.pivotal.spring.cloud.config.java.ServiceInfoPropertySourceAdapter.SPRING_AUTOCONFIGURE_EXCLUDE;
import static org.junit.Assert.assertEquals;

/**
 * 
 * @author Will Tran
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = EurekaServiceConnectorIntegrationTest.TestConfig.class)
public class EurekaServiceConnectorIntegrationTest {

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
				new EurekaServiceInfo("eureka", URI, CLIENT_ID, CLIENT_SECRET, ACCESS_TOKEN_URI)));
	}

	@AfterClass
	public static void afterClass() {
		MockCloudConnector.reset();
	}

	@TestPropertySource(properties = "spring.rabbitmq.host=some_rabbit_host")
	public static class WithRabbitBinding extends EurekaServiceConnectorIntegrationTest {

		@Test
		public void springAutoConfigureExcludeIsNull() {
			assertPropertyEquals(null, SPRING_AUTOCONFIGURE_EXCLUDE);
		}

	}

	public static class WithoutRabbitBinding extends EurekaServiceConnectorIntegrationTest {

		@Test
		public void springAutoConfigureExcludeIsOnlyRabbitAutoConfig() {
			assertPropertyEquals("org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration",
					SPRING_AUTOCONFIGURE_EXCLUDE);
		}

	}

	@TestPropertySource(properties = "spring.autoconfigure.exclude=com.foo.Bar")
	public static class WithoutRabbitBindingButWithExistingAutoConfigExcludes extends EurekaServiceConnectorIntegrationTest {

		@Test
		public void springAutoConfigureExcludePreservesExistingExcludes() {
			assertPropertyEquals("org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration,com.foo.Bar", SPRING_AUTOCONFIGURE_EXCLUDE);
		}

	}

	@Test
	public void propertySourceIsAdded() {
		assertPropertyEquals(URI + "/eureka/", "eureka.client.serviceUrl.defaultZone");
		assertPropertyEquals("default", "eureka.client.region");
		assertPropertyEquals(CLIENT_ID, "eureka.client.oauth2.clientId");
		assertPropertyEquals(CLIENT_SECRET, "eureka.client.oauth2.clientSecret");
		assertPropertyEquals(ACCESS_TOKEN_URI, "eureka.client.oauth2.accessTokenUri");
	}

	void assertPropertyEquals(String expected, String key) {
		assertEquals(expected, environment.getProperty(key));
	}

	static class TestConfig {
	}
}
