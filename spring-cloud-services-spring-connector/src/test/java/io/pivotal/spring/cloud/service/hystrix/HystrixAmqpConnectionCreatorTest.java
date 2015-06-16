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
