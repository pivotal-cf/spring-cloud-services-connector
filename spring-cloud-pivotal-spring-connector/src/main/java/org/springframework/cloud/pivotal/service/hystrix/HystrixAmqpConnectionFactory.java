package org.springframework.cloud.pivotal.service.hystrix;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author Scott Frederick
 */
public class HystrixAmqpConnectionFactory implements FactoryBean<ConnectionFactory> {
	private ConnectionFactory connectionFactory;

	public HystrixAmqpConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public ConnectionFactory connectionFactory() {
		return connectionFactory;
	}

	@Override
	public ConnectionFactory getObject() throws Exception {
		return connectionFactory;
	}

	@Override
	public Class<?> getObjectType() {
		return connectionFactory.getClass();
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
