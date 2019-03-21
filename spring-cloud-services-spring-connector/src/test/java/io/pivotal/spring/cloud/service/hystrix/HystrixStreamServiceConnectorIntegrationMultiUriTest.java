/*
 * Copyright 2016 the original author or authors.
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

package io.pivotal.spring.cloud.service.hystrix;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import io.pivotal.spring.cloud.MockCloudConnector;
import io.pivotal.spring.cloud.service.common.EurekaServiceInfo;
import io.pivotal.spring.cloud.service.common.HystrixAmqpServiceInfo;
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

import static io.pivotal.spring.cloud.service.hystrix.HystrixStreamServiceConnector.SPRING_CLOUD_HYSTRIX_STREAM;
import static io.pivotal.spring.cloud.service.hystrix.HystrixStreamServiceConnector.SPRING_CLOUD_STREAM_BINDERS_HYSTRIX;
import static io.pivotal.spring.cloud.service.hystrix.HystrixStreamServiceConnector.SPRING_CLOUD_STREAM_BINDERS_HYSTRIX_ENVIRONMENT_SPRING_RABBITMQ;
import static io.pivotal.spring.cloud.service.hystrix.HystrixStreamServiceConnector.SPRING_CLOUD_STREAM_BINDINGS_HYSTRIXSTREAMOUTPUT;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {
		HystrixStreamServiceConnectorIntegrationMultiUriTest.TestConfig.class
})
@IntegrationTest()
public class HystrixStreamServiceConnectorIntegrationMultiUriTest {

	private static final String URI = "amqps://username:password@p-rabbitmq1.mydomain.com/testvhost";

	private static final List<String> URIS = Arrays.asList(
			"amqps://username:password@p-rabbitmq1.mydomain.com/testvhost",
			"amqps://username:password@p-rabbitmq2.mydomain.com/testvhost");

	@Autowired
	private Environment environment;

	@BeforeClass
	@SuppressWarnings("unchecked")
	public static void beforeClass() throws IOException {
		when(MockCloudConnector.instance.isInMatchingCloud()).thenReturn(true);
		when(MockCloudConnector.instance.getServiceInfos()).thenReturn(
				Arrays.asList(
						(ServiceInfo) new HystrixAmqpServiceInfo("circuit-breaker", URI, URIS, true),
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
		assertPropertyEquals("hystrix", SPRING_CLOUD_STREAM_BINDINGS_HYSTRIXSTREAMOUTPUT + "binder");
		assertPropertyEquals(SPRING_CLOUD_HYSTRIX_STREAM, SPRING_CLOUD_STREAM_BINDINGS_HYSTRIXSTREAMOUTPUT + "destination");
		assertPropertyEquals("rabbit", SPRING_CLOUD_STREAM_BINDERS_HYSTRIX + "type");
		assertPropertyEquals("false", SPRING_CLOUD_STREAM_BINDERS_HYSTRIX + "inheritEnvironment");
		assertPropertyEquals("false", SPRING_CLOUD_STREAM_BINDERS_HYSTRIX + "defaultCandidate");
		assertPropertyEquals("p-rabbitmq1.mydomain.com:5671,p-rabbitmq2.mydomain.com:5671", SPRING_CLOUD_STREAM_BINDERS_HYSTRIX_ENVIRONMENT_SPRING_RABBITMQ + "addresses");
		assertPropertyEquals("username", SPRING_CLOUD_STREAM_BINDERS_HYSTRIX_ENVIRONMENT_SPRING_RABBITMQ + "username");
		assertPropertyEquals("password", SPRING_CLOUD_STREAM_BINDERS_HYSTRIX_ENVIRONMENT_SPRING_RABBITMQ + "password");
		assertPropertyEquals("testvhost", SPRING_CLOUD_STREAM_BINDERS_HYSTRIX_ENVIRONMENT_SPRING_RABBITMQ + "virtualHost");
		assertPropertyEquals("true", SPRING_CLOUD_STREAM_BINDERS_HYSTRIX_ENVIRONMENT_SPRING_RABBITMQ + "ssl.enabled");
		assertPropertyEquals("true", SPRING_CLOUD_STREAM_BINDERS_HYSTRIX + "environment.spring.cloud.stream.overrideCloudConnectors");
		assertPropertyEquals("", SPRING_CLOUD_STREAM_BINDERS_HYSTRIX + "default.prefix");
	}

	private void assertPropertyEquals(String expected, String key) {
		assertEquals(expected, environment.getProperty(key));
	}

	@EnableCircuitBreaker
	public static class TestConfig {
	}
}
