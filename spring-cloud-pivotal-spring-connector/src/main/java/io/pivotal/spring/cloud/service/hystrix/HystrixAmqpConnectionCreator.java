package io.pivotal.spring.cloud.service.hystrix;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import io.pivotal.spring.cloud.service.common.HystrixAmqpServiceInfo;
import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.messaging.RabbitConnectionFactoryCreator;

/**
 * Connector creator for Hystrix AMQP client services
 *
 * @author Scott Frederick
 */
public class HystrixAmqpConnectionCreator extends AbstractServiceConnectorCreator<HystrixAmqpConnectionFactory, HystrixAmqpServiceInfo> {
	private RabbitConnectionFactoryCreator delegate = new RabbitConnectionFactoryCreator();

	@Override
	public HystrixAmqpConnectionFactory create(HystrixAmqpServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig) {
		ConnectionFactory connectionFactory = delegate.create(serviceInfo.getAmqpInfo(), serviceConnectorConfig);
		return new HystrixAmqpConnectionFactory(connectionFactory);
	}
}
