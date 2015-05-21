package org.springframework.cloud.pivotal.config.java;

import com.gemstone.gemfire.cache.client.ClientCache;
import com.netflix.discovery.EurekaClientConfig;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.cloud.config.java.ServiceConnectionFactory;
import org.springframework.cloud.pivotal.service.gemfire.GemfireServiceConnectorConfig;
import org.springframework.cloud.service.messaging.RabbitConnectionFactoryConfig;

public interface PivotalServiceConnectionFactory extends ServiceConnectionFactory {
	ClientCache gemfireClientCache();

	ClientCache gemfireClientCache(GemfireServiceConnectorConfig config);

	ClientCache gemfireClientCache(String serviceId);

	ClientCache gemfireClientCache(String serviceId, GemfireServiceConnectorConfig config);

	EurekaClientConfig eurekaClientConfig();

	EurekaClientConfig eurekaClientConfig(String serviceId);

	ConnectionFactory hystrixConnectionFactory();

	ConnectionFactory hystrixConnectionFactory(RabbitConnectionFactoryConfig rabbitConnectionFactoryConfig);

	ConnectionFactory hystrixConnectionFactory(String serviceId);

	ConnectionFactory hystrixConnectionFactory(String serviceId,
											   RabbitConnectionFactoryConfig rabbitConnectionFactoryConfig);
}
