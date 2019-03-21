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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.pivotal.spring.cloud.MockCloudConnector;
import io.pivotal.spring.cloud.service.common.EurekaServiceInfo;
import io.pivotal.spring.cloud.service.common.HystrixAmqpServiceInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.AmqpServiceInfo;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static io.pivotal.spring.cloud.service.hystrix.HystrixStreamServiceConnector.SPRING_AUTOCONFIGURE_EXCLUDE;
import static io.pivotal.spring.cloud.service.hystrix.HystrixStreamServiceConnector.SPRING_CLOUD_HYSTRIX_STREAM;
import static io.pivotal.spring.cloud.service.hystrix.HystrixStreamServiceConnector.SPRING_CLOUD_STREAM_BINDERS_HYSTRIX;
import static io.pivotal.spring.cloud.service.hystrix.HystrixStreamServiceConnector.SPRING_CLOUD_STREAM_BINDERS_HYSTRIX_ENVIRONMENT_SPRING_RABBITMQ;
import static io.pivotal.spring.cloud.service.hystrix.HystrixStreamServiceConnector.SPRING_CLOUD_STREAM_BINDINGS_HYSTRIXSTREAMOUTPUT;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class HystrixStreamServiceConnectorIntegrationTest {
	
	public static class WithoutRabbitBinding extends AbstractHystrixStreamServiceConnectorIntegrationTest {

		@Test
		public void propertySourceIsAdded() {
			assertPropertyEquals("hystrix", SPRING_CLOUD_STREAM_BINDINGS_HYSTRIXSTREAMOUTPUT + "binder");
			assertPropertyEquals(SPRING_CLOUD_HYSTRIX_STREAM, SPRING_CLOUD_STREAM_BINDINGS_HYSTRIXSTREAMOUTPUT + "destination");
			assertPropertyEquals("rabbit", SPRING_CLOUD_STREAM_BINDERS_HYSTRIX + "type");
			assertPropertyEquals("false", SPRING_CLOUD_STREAM_BINDERS_HYSTRIX + "inheritEnvironment");
			assertPropertyEquals("false", SPRING_CLOUD_STREAM_BINDERS_HYSTRIX + "defaultCandidate");
			assertPropertyEquals("p-rabbitmq.mydomain.com:5672", SPRING_CLOUD_STREAM_BINDERS_HYSTRIX_ENVIRONMENT_SPRING_RABBITMQ + "addresses");
			assertPropertyEquals("username", SPRING_CLOUD_STREAM_BINDERS_HYSTRIX_ENVIRONMENT_SPRING_RABBITMQ + "username");
			assertPropertyEquals("password", SPRING_CLOUD_STREAM_BINDERS_HYSTRIX_ENVIRONMENT_SPRING_RABBITMQ + "password");
			assertPropertyEquals("testvhost", SPRING_CLOUD_STREAM_BINDERS_HYSTRIX_ENVIRONMENT_SPRING_RABBITMQ + "virtualHost");
			assertPropertyEquals("false", SPRING_CLOUD_STREAM_BINDERS_HYSTRIX_ENVIRONMENT_SPRING_RABBITMQ + "ssl.enabled");
			assertPropertyEquals("true", SPRING_CLOUD_STREAM_BINDERS_HYSTRIX + "environment.spring.cloud.stream.overrideCloudConnectors");
			assertPropertyEquals("", SPRING_CLOUD_STREAM_BINDERS_HYSTRIX + "default.prefix");
			assertPropertyEquals("org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration", SPRING_AUTOCONFIGURE_EXCLUDE);
		}
		
	}
	
	@TestPropertySource(properties="spring.rabbitmq.host=some_rabbit_host")
	public static class WithRabbitBinding extends AbstractHystrixStreamServiceConnectorIntegrationTest {

		@Test
		public void springAutoConfigureExcludeIsNull() {
			assertPropertyEquals(null, SPRING_AUTOCONFIGURE_EXCLUDE);
		}
		
	}

	public static class WithRabbitCloudBinding extends AbstractHystrixStreamServiceConnectorIntegrationTest {
		@BeforeClass
		public static void beforeClass() {
			List<ServiceInfo> serviceInfosWithAddedRabbitCloudBinding = new ArrayList<>(MockCloudConnector.instance.getServiceInfos());
			serviceInfosWithAddedRabbitCloudBinding.add(
					new AmqpServiceInfo("rabbitmq", "example.com", 5672, "username", "password", "virtualHost")
			);
			when(MockCloudConnector.instance.getServiceInfos()).thenReturn(serviceInfosWithAddedRabbitCloudBinding);
		}

		@Test
		public void springAutoConfigureExcludeIsNull() {
			assertPropertyEquals(null, SPRING_AUTOCONFIGURE_EXCLUDE);
		}
	}
	
	@TestPropertySource(properties="spring.autoconfigure.exclude=com.foo.Bar")
	public static class WithExistingAutoConfigExcludes extends AbstractHystrixStreamServiceConnectorIntegrationTest {

		@Test
		public void springAutoConfigureExcludePreservesExistingExcludes() {
			assertPropertyEquals("org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration,com.foo.Bar", SPRING_AUTOCONFIGURE_EXCLUDE);
		}
		
	}

	@DirtiesContext
	@RunWith(SpringJUnit4ClassRunner.class)
	@SpringBootTest(classes = AbstractHystrixStreamServiceConnectorIntegrationTest.TestConfig.class,
		properties = "eureka.client.enabled=false")
	public static abstract class AbstractHystrixStreamServiceConnectorIntegrationTest {

		private static final String URI = "amqp://username:password@p-rabbitmq.mydomain.com/testvhost";

		@Autowired
		private Environment environment;
	 
		@BeforeClass
		public static void beforeClass() {
			when(MockCloudConnector.instance.isInMatchingCloud()).thenReturn(true);
			when(MockCloudConnector.instance.getServiceInfos()).thenReturn(
					Arrays.asList(
							new HystrixAmqpServiceInfo("circuit-breaker", URI),
							new EurekaServiceInfo("service-registry", "https://example.com", "clientId", "clientSecret", "https://example.com/token")
					)
			);
		}

		@AfterClass
		public static void afterClass() {
			MockCloudConnector.reset();
		}

		void assertPropertyEquals(String expected, String key) {
			assertEquals(expected, environment.getProperty(key));
		}

		@EnableCircuitBreaker
		static class TestConfig {
		}
	}

}
