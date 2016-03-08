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

import java.util.Collections;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.cloud.netflix.hystrix.stream.HystrixStreamProperties;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.pivotal.spring.cloud.MockCloudConnector;
import io.pivotal.spring.cloud.service.common.HystrixAmqpServiceInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {
		HystrixAmqpServiceConnectorIntegrationTest.TestConfig.class
})
@IntegrationTest()
public class HystrixAmqpServiceConnectorIntegrationTest {

	private static final String URI = "amqp://username:password@p-rabbitmq.mydomain.com";

	@Autowired
	private Environment environment;
	
	@Autowired
	private HystrixStreamProperties properties;


	@BeforeClass
	public static void beforeClass() {
		Mockito.when(MockCloudConnector.instance.isInMatchingCloud()).thenReturn(true);
		Mockito.when(MockCloudConnector.instance.getServiceInfos()).thenReturn(Collections.singletonList(
				(ServiceInfo) new HystrixAmqpServiceInfo("circuit-breaker",URI)));
	}

	@AfterClass
	public static void afterClass() {
		MockCloudConnector.reset();
	}

	@Test
	public void propertySourceIsAdded() {
		Assert.assertEquals("spring.cloud.hystrix.stream", environment.getProperty("hystrix.stream.queue.destination"));
	}
	
	@Test
	public void destinationPropertyIsSet() {
		Assert.assertEquals("spring.cloud.hystrix.stream", properties.getDestination());
	}


	@EnableConfigurationProperties
	public static class TestConfig {
		
		@Bean
		public HystrixStreamProperties hystrixStreamProperties() {
			return new HystrixStreamProperties();
		}
	}

}
