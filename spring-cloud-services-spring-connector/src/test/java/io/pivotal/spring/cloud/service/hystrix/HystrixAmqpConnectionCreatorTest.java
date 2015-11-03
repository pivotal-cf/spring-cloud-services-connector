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

import org.junit.Test;
import io.pivotal.spring.cloud.service.common.HystrixAmqpServiceInfo;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test cases for the Hystrix AMQP connection factory creator
 *
 * @author Scott Frederick
 */
public class HystrixAmqpConnectionCreatorTest {
	private static final String TEST_URI = "amqp://myuser:mypass@10.20.30.40:1234/vhost1";

	private HystrixAmqpConnectionCreator creator = new HystrixAmqpConnectionCreator();

	@Test
	public void cloudRabbitCreationNoConfig() throws Exception {
		HystrixAmqpServiceInfo serviceInfo = createServiceInfo();

		HystrixAmqpConnectionFactory connector = creator.create(serviceInfo, null);

		assertConnectorProperties(serviceInfo, connector.connectionFactory());
	}

	public HystrixAmqpServiceInfo createServiceInfo() {
		return new HystrixAmqpServiceInfo("id", TEST_URI);
	}

	private void assertConnectorProperties(HystrixAmqpServiceInfo serviceInfo, ConnectionFactory connector) {
		assertNotNull(connector);

		assertEquals(serviceInfo.getHost(), connector.getHost());
		assertEquals(serviceInfo.getPort(), connector.getPort());
		com.rabbitmq.client.ConnectionFactory underlying =
				(com.rabbitmq.client.ConnectionFactory) ReflectionTestUtils.getField(connector, "rabbitConnectionFactory");
		assertEquals(serviceInfo.getUserName(), ReflectionTestUtils.getField(underlying, "username"));
		assertEquals(serviceInfo.getPassword(), ReflectionTestUtils.getField(underlying, "password"));
		assertEquals(serviceInfo.getVirtualHost(), connector.getVirtualHost());
	}
}
