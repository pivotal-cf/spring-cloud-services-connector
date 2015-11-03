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

package io.pivotal.spring.cloud.service.config;

import java.util.Collections;

import io.pivotal.spring.cloud.MockCloudConnector;
import io.pivotal.spring.cloud.service.common.ConfigServerServiceInfo;
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
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {
		ConfigServerServiceConnectorIntegrationTest.TestConfig.class,
		ConfigClientOAuth2BootstrapConfiguration.class
})
@IntegrationTest()
public class ConfigServerServiceConnectorIntegrationTest {

	private static final String CLIENT_ID = "client-id";

	private static final String CLIENT_SECRET = "secret";

	private static final String ACCESS_TOKEN_URI = "https://your-identity-zone.uaa.my-cf.com/oauth/token";

	private static final String URI = "http://username:password@config-server.mydomain.com";

	@Autowired
	private Environment environment;

	@Autowired
	private ApplicationContext context;

	@BeforeClass
	public static void beforeClass() {
		Mockito.when(MockCloudConnector.instance.isInMatchingCloud()).thenReturn(true);
		Mockito.when(MockCloudConnector.instance.getServiceInfos()).thenReturn(Collections.singletonList(
				(ServiceInfo) new ConfigServerServiceInfo("config-server", URI, CLIENT_ID, CLIENT_SECRET, ACCESS_TOKEN_URI)));
	}

	@AfterClass
	public static void afterClass() {
		MockCloudConnector.reset();
	}

	@Test
	public void propertySourceIsAdded() {
		Assert.assertEquals(URI, environment.getProperty("spring.cloud.config.uri"));
		Assert.assertEquals(CLIENT_ID, environment.getProperty("spring.cloud.config.client.oauth2.clientId"));
		Assert.assertEquals(CLIENT_SECRET, environment.getProperty("spring.cloud.config.client.oauth2.clientSecret"));
		Assert.assertEquals(ACCESS_TOKEN_URI, environment.getProperty("spring.cloud.config.client.oauth2.accessTokenUri"));
	}

	@Test
	public void hasConfigClientOAuth2ResourceDetailsBean() {
		final ConfigClientOAuth2ResourceDetails resourceDetails = context.getBean(ConfigClientOAuth2ResourceDetails.class);
		Assert.assertEquals(CLIENT_ID, resourceDetails.getClientId());
		Assert.assertEquals(CLIENT_SECRET, resourceDetails.getClientSecret());
		Assert.assertEquals(ACCESS_TOKEN_URI, resourceDetails.getAccessTokenUri());
	}

	public static class TestConfig {
	}

}
