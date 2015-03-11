package org.springframework.cloud.pivotal.config.java;

import com.netflix.discovery.EurekaClientConfig;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.pivotal.service.hystrix.HystrixAmqpConnection;
import org.springframework.cloud.service.messaging.RabbitConnectionFactoryConfig;

public class PivotalCloudConfig extends AbstractCloudConfig {
	private ServiceConnectionFactory connectionFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		super.setBeanFactory(beanFactory);
		this.connectionFactory = new ServiceConnectionFactory();
	}

	@Override
	public ServiceConnectionFactory connectionFactory() {
		return connectionFactory;
	}

	public class ServiceConnectionFactory extends AbstractCloudConfig.ServiceConnectionFactory {
		/**
		 * Get the {@link EurekaClientConfig} object associated with the only Eureka service bound to the app.
		 *
		 * @return Eureka client config
		 * @throws CloudException if there are either 0 or more than 1 Hystrix AMQP services.
		 */
		public EurekaClientConfig eurekaClientConfig() {
			return cloud().getSingletonServiceConnector(EurekaClientConfig.class, null);
		}

		/**
		 * Get the {@link EurekaClientConfig} object for the specified Eureka service.
		 *
		 * @param serviceId the name of the service
		 * @return Eureka client config
		 * @throws CloudException if the specified service doesn't exist
		 */
		public EurekaClientConfig eurekaClientConfig(String serviceId) {
			return cloud().getServiceConnector(serviceId, EurekaClientConfig.class, null);
		}

		/**
		 * Get the {@link ConnectionFactory} object associated with the only Hystrix AMQP service bound to the app.
		 *
		 * @return RabbitMQ connection factory
		 * @throws CloudException if there are either 0 or more than 1 Hystrix AMQP services.
		 */
		public ConnectionFactory hystrixConnectionFactory() {
			return hystrixConnectionFactory((RabbitConnectionFactoryConfig) null);
		}

		/**
		 * Get the {@link ConnectionFactory} object associated with the only Hystrix AMQP service bound to the app
		 * configured as specified.
		 *
		 * @param rabbitConnectionFactoryConfig configuration for the RabbitMQ connection factory created
		 * @return RabbitMQ factory
		 * @throws CloudException if there are either 0 or more than 1 Hystrix AMQP services.
		 */
		public ConnectionFactory hystrixConnectionFactory(RabbitConnectionFactoryConfig rabbitConnectionFactoryConfig) {
			return cloud().getSingletonServiceConnector(HystrixAmqpConnection.class, rabbitConnectionFactoryConfig).connectionFactory();
		}

		/**
		 * Get the {@link ConnectionFactory} object for the specified Hystrix AMQP service.
		 *
		 * @param serviceId the name of the service
		 * @return RabbitMQ factory
		 * @throws CloudException if the specified service doesn't exist
		 */
		public ConnectionFactory hystrixConnectionFactory(String serviceId) {
			return hystrixConnectionFactory(serviceId, null);
		}

		/**
		 * Get the {@link ConnectionFactory} object for the specified Hystrix AMQP service configured as specified.
		 *
		 * @param serviceId the name of the service
		 * @param rabbitConnectionFactoryConfig configuration for the {@link ConnectionFactory} created
		 * @return RabbitMQ factory
		 * @throws CloudException if the specified service doesn't exist
		 */
		public ConnectionFactory hystrixConnectionFactory(String serviceId,
														  RabbitConnectionFactoryConfig rabbitConnectionFactoryConfig) {
			return cloud().getServiceConnector(serviceId, HystrixAmqpConnection.class, rabbitConnectionFactoryConfig).connectionFactory();
		}
	}
}
