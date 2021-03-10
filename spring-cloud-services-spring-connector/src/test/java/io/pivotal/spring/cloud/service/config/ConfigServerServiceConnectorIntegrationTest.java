/*
 * Copyright 2015 the original author or authors.
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

package io.pivotal.spring.cloud.service.config;

import java.util.Collections;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.pivotal.spring.cloud.MockCloudConnector;
import io.pivotal.spring.cloud.service.common.ConfigServerServiceInfo;

import static io.pivotal.spring.cloud.config.java.ServiceInfoPropertySourceAdapter.SPRING_AUTOCONFIGURE_EXCLUDE;
import static io.pivotal.spring.cloud.service.config.ConfigServerServiceConnector.SPRING_CLOUD_CONFIG_OAUTH2_CLIENT_ACCESS_TOKEN_URI;
import static io.pivotal.spring.cloud.service.config.ConfigServerServiceConnector.SPRING_CLOUD_CONFIG_OAUTH2_CLIENT_CLIENT_ID;
import static io.pivotal.spring.cloud.service.config.ConfigServerServiceConnector.SPRING_CLOUD_CONFIG_OAUTH2_CLIENT_CLIENT_SECRET;
import static io.pivotal.spring.cloud.service.config.ConfigServerServiceConnector.SPRING_CLOUD_CONFIG_URI;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
		classes = {
				ConfigServerServiceConnectorIntegrationTest.TestConfig.class
		},
		properties = "spring.cloud.config.enabled=true")
public class ConfigServerServiceConnectorIntegrationTest {

	private static final String CLIENT_ID = "client-id";

	private static final String CLIENT_SECRET = "secret";

	private static final String ACCESS_TOKEN_URI = "https://your-identity-zone.uaa.my-cf.com/oauth/token";

	private static final String URI = "https://username:password@config-server.mydomain.com";

	@Autowired
	private Environment environment;

	@Autowired
	private ApplicationContext context;

	@BeforeClass
	public static void beforeClass() {
		Mockito.when(MockCloudConnector.instance.isInMatchingCloud()).thenReturn(true);
		Mockito.when(MockCloudConnector.instance.getServiceInfos()).thenReturn(Collections.singletonList(
				new ConfigServerServiceInfo("config-server",
						URI,
						CLIENT_ID,
						CLIENT_SECRET,
						ACCESS_TOKEN_URI)));
	}

	@AfterClass
	public static void afterClass() {
		MockCloudConnector.reset();
	}

	@TestPropertySource(properties = "spring.rabbitmq.host=some_rabbit_host")
	public static class WithRabbitBinding extends ConfigServerServiceConnectorIntegrationTest {

		@Test
		public void springAutoConfigureExcludeIsNull() {
			assertPropertyEquals(null, SPRING_AUTOCONFIGURE_EXCLUDE);
		}

	}

	public static class WithoutRabbitBinding extends ConfigServerServiceConnectorIntegrationTest {

		@Test
		public void springAutoConfigureExcludeIsOnlyRabbitAutoConfig() {
			assertPropertyEquals("org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration",
					SPRING_AUTOCONFIGURE_EXCLUDE);
		}

	}

	@TestPropertySource(properties = "spring.autoconfigure.exclude=com.foo.Bar")
	public static class WithoutRabbitBindingButWithExistingAutoConfigExcludes extends ConfigServerServiceConnectorIntegrationTest {

		@Test
		public void springAutoConfigureExcludePreservesExistingExcludes() {
			assertPropertyEquals("org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration,com.foo.Bar", SPRING_AUTOCONFIGURE_EXCLUDE);
		}

	}

	@Test
	public void propertySourceIsAdded() {
		assertPropertyEquals(URI, SPRING_CLOUD_CONFIG_URI);
		assertPropertyEquals(CLIENT_ID, SPRING_CLOUD_CONFIG_OAUTH2_CLIENT_CLIENT_ID);
		assertPropertyEquals(CLIENT_SECRET, SPRING_CLOUD_CONFIG_OAUTH2_CLIENT_CLIENT_SECRET);
		assertPropertyEquals(ACCESS_TOKEN_URI, SPRING_CLOUD_CONFIG_OAUTH2_CLIENT_ACCESS_TOKEN_URI);
	}

	@Test
	public void hasConfigClientOAuth2PropertiesBean() {
		final ConfigClientOAuth2Properties resourceDetails = context.getBean(ConfigClientOAuth2Properties.class);
		Assert.assertEquals(CLIENT_ID, resourceDetails.getClientId());
		Assert.assertEquals(CLIENT_SECRET, resourceDetails.getClientSecret());
		Assert.assertEquals(ACCESS_TOKEN_URI, resourceDetails.getAccessTokenUri());
	}

	void assertPropertyEquals(String expected, String key) {
		assertEquals(expected, environment.getProperty(key));
	}

	static class TestConfig {
	}
}
