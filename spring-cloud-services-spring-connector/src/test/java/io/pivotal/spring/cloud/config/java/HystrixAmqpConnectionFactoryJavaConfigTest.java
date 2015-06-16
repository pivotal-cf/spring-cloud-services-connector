package io.pivotal.spring.cloud.config.java;

import org.junit.Test;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.messaging.RabbitConnectionFactoryConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * 
 * @author Ramnivas Laddad
 * @author Scott Frederick
 *
 */
public class HystrixAmqpConnectionFactoryJavaConfigTest extends AbstractServiceJavaConfigTest<ConnectionFactory> {
	public HystrixAmqpConnectionFactoryJavaConfigTest() {
		super(HystrixAmqpConnectionFactoryConfigWithId.class, HystrixAmqpConnectionFactoryConfigWithoutId.class);
	}
	
	protected ServiceInfo createService(String id) {
		return createHystrixAmqpService(id);
	}
	
	protected Class<ConnectionFactory> getConnectorType() {
		return ConnectionFactory.class;
	}
	
	@Test
	public void cloudHystrixAmqpConnectionFactoryWithChannelCacheSize() {
		ApplicationContext testContext = 
			getTestApplicationContext(HystrixAmqpConnectionFactoryConfigWithServiceConfig.class, createService("my-service"));
		
		ConnectionFactory connector = testContext.getBean("channelCacheSize200", getConnectorType());
		assertConfigProperties(connector, 200);
	}

	public static void assertConfigProperties(ConnectionFactory connector, Integer channelCacheSize) {
		assertNotNull(connector);
		assertEquals(channelCacheSize, ReflectionTestUtils.getField(connector, "channelCacheSize"));
	}

}

class HystrixAmqpConnectionFactoryConfigWithId extends CloudConnectorsConfig {
	@Bean(name="my-service")
	public ConnectionFactory testHystrixAmqpConnectionFactory() {
		return connectionFactory().hystrixConnectionFactory("my-service");
	}
}

class HystrixAmqpConnectionFactoryConfigWithoutId extends CloudConnectorsConfig {
	@Bean(name="my-service")
	public ConnectionFactory testHystrixAmqpConnectionFactory() {
		return connectionFactory().hystrixConnectionFactory();
	}
}

class HystrixAmqpConnectionFactoryConfigWithServiceConfig extends CloudConnectorsConfig {
	@Bean
	public ConnectionFactory channelCacheSize200() {
		RabbitConnectionFactoryConfig serviceConfig = new RabbitConnectionFactoryConfig(200);
		return connectionFactory().hystrixConnectionFactory("my-service", serviceConfig);
	}
}