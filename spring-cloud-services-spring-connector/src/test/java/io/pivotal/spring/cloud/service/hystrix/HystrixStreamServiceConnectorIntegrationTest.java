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

package io.pivotal.spring.cloud.service.hystrix;

import static io.pivotal.spring.cloud.service.hystrix.HystrixStreamServiceConnector.HYSTRIX_STREAM_BINDER_PREFIX;
import static io.pivotal.spring.cloud.service.hystrix.HystrixStreamServiceConnector.HYSTRIX_STREAM_BINDING_PREFIX;
import static io.pivotal.spring.cloud.service.hystrix.HystrixStreamServiceConnector.SPRING_CLOUD_HYSTRIX_STREAM;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;

import io.pivotal.spring.cloud.service.common.EurekaServiceInfo;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.pivotal.spring.cloud.MockCloudConnector;
import io.pivotal.spring.cloud.service.common.HystrixAmqpServiceInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {
		HystrixStreamServiceConnectorIntegrationTest.TestConfig.class
})
@IntegrationTest()
public class HystrixStreamServiceConnectorIntegrationTest {

	private static final String URI = "amqp://username:password@p-rabbitmq.mydomain.com";

	@Autowired
	private Environment environment;

	@BeforeClass
	@SuppressWarnings("unchecked")
	public static void beforeClass() throws IOException {
		when(MockCloudConnector.instance.isInMatchingCloud()).thenReturn(true);
		when(MockCloudConnector.instance.getServiceInfos()).thenReturn(
				Arrays.asList(
						(ServiceInfo) new HystrixAmqpServiceInfo("circuit-breaker", URI),
						(ServiceInfo) new EurekaServiceInfo("service-registry", "http://example.com", "clientId", "clientSecret", "http://example.com/token")
				)
		);
	}

	@AfterClass
	public static void afterClass() {
		MockCloudConnector.reset();
	}

	@Test
	public void propertySourceIsAdded() {
		assertPropertyEquals("hystrix", HYSTRIX_STREAM_BINDING_PREFIX + "binder");
		assertPropertyEquals(SPRING_CLOUD_HYSTRIX_STREAM, HYSTRIX_STREAM_BINDING_PREFIX + "destination");

		assertPropertyEquals("rabbit", HYSTRIX_STREAM_BINDER_PREFIX + "type");
		assertPropertyEquals("false", HYSTRIX_STREAM_BINDER_PREFIX + "inheritEnvironment");
		assertPropertyEquals("false", HYSTRIX_STREAM_BINDER_PREFIX + "defaultCandidate");
		assertPropertyEquals(URI, HYSTRIX_STREAM_BINDER_PREFIX + "environment.spring.rabbitmq.addresses");
		assertPropertyEquals("true", HYSTRIX_STREAM_BINDER_PREFIX + "environment.spring.cloud.stream.overrideCloudConnectors");
		assertPropertyEquals("", HYSTRIX_STREAM_BINDER_PREFIX + "default.prefix");
	}

	private void assertPropertyEquals(String expected, String key) {
		assertEquals(expected, environment.getProperty(key));
	}

	@EnableCircuitBreaker
	public static class TestConfig {
	}
}
