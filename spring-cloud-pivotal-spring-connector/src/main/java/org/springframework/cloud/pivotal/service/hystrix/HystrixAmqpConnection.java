package org.springframework.cloud.pivotal.service.hystrix;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;

/**
 * @author Scott Frederick
 */
public class HystrixAmqpConnection {
	private ConnectionFactory connectionFactory;

	public HystrixAmqpConnection(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public ConnectionFactory connectionFactory() {
		return connectionFactory;
	}
}
