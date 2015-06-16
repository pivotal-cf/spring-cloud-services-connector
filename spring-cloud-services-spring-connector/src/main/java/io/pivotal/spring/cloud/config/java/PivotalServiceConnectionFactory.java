package io.pivotal.spring.cloud.config.java;

import com.netflix.discovery.EurekaClientConfig;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.cloud.config.java.ServiceConnectionFactory;
import org.springframework.cloud.service.messaging.RabbitConnectionFactoryConfig;

public interface PivotalServiceConnectionFactory extends ServiceConnectionFactory {

	EurekaClientConfig eurekaClientConfig();

	EurekaClientConfig eurekaClientConfig(String serviceId);

	ConnectionFactory hystrixConnectionFactory();

	ConnectionFactory hystrixConnectionFactory(RabbitConnectionFactoryConfig rabbitConnectionFactoryConfig);

	ConnectionFactory hystrixConnectionFactory(String serviceId);

	ConnectionFactory hystrixConnectionFactory(String serviceId,
											   RabbitConnectionFactoryConfig rabbitConnectionFactoryConfig);
}
