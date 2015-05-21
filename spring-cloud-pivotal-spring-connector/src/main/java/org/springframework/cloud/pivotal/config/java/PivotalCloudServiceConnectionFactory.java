package org.springframework.cloud.pivotal.config.java;

import com.gemstone.gemfire.cache.client.ClientCache;
import com.netflix.discovery.EurekaClientConfig;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.config.java.CloudServiceConnectionFactory;
import org.springframework.cloud.pivotal.service.gemfire.GemfireServiceConnectorConfig;
import org.springframework.cloud.pivotal.service.hystrix.HystrixAmqpConnectionFactory;
import org.springframework.cloud.service.messaging.RabbitConnectionFactoryConfig;

public class PivotalCloudServiceConnectionFactory extends CloudServiceConnectionFactory implements PivotalServiceConnectionFactory {
	private CloudConnectorsConfig cloudConnectorsConfig;

	public PivotalCloudServiceConnectionFactory(CloudConnectorsConfig cloudConnectorsConfig, Cloud cloud) {
		super(cloud);
		this.cloudConnectorsConfig = cloudConnectorsConfig;
	}

	/**
	 * Get the GemFire {@link ClientCache} object associated with the only GemFire service bound to the app.
	 *
	 * @return GemFire client
	 * @throws CloudException if there are either 0 or more than 1 GemFire services.
	 */
	@Override
	public ClientCache gemfireClientCache() {
		return gemfireClientCache((GemfireServiceConnectorConfig) null);
	}

	/**
	 * Get the GemFire {@link ClientCache} object associated with the only GemFire service bound to the app.
	 *
	 * @return GemFire client
	 * @param config configuration for the created {@link ClientCache}
	 * @throws CloudException if there are either 0 or more than 1 GemFire services.
	 */
	@Override
	public ClientCache gemfireClientCache(GemfireServiceConnectorConfig config) {
		return cloudConnectorsConfig.cloud().getSingletonServiceConnector(ClientCache.class, config);
	}

	/**
	 * Get the GemFire {@link ClientCache} object associated with the specified GemFire service.
	 *
	 * @param serviceId the name of the service
	 * @return GemFire client
	 * @throws CloudException if the specified service doesn't exist
	 */
	@Override
	public ClientCache gemfireClientCache(String serviceId) {
		return gemfireClientCache(serviceId, null);
	}

	/**
	 * Get the GemFire {@link ClientCache} object associated with the specified GemFire service.
	 *
	 * @param serviceId the name of the service
	 * @param config configuration for the created {@link ClientCache}
	 * @return GemFire client
	 * @throws CloudException if the specified service doesn't exist
	 */
	@Override
	public ClientCache gemfireClientCache(String serviceId, GemfireServiceConnectorConfig config) {
		return cloudConnectorsConfig.cloud().getServiceConnector(serviceId, ClientCache.class, config);
	}

	/**
	 * Get the {@link EurekaClientConfig} object associated with the only Eureka service bound to the app.
	 *
	 * @return Eureka client config
	 * @throws CloudException if there are either 0 or more than 1 Eureka services.
	 */
	@Override
	public EurekaClientConfig eurekaClientConfig() {
		return cloudConnectorsConfig.cloud().getSingletonServiceConnector(EurekaClientConfig.class, null);
	}

	/**
	 * Get the {@link EurekaClientConfig} object for the specified Eureka service.
	 *
	 * @param serviceId the name of the service
	 * @return Eureka client config
	 * @throws CloudException if the specified service doesn't exist
	 */
	@Override
	public EurekaClientConfig eurekaClientConfig(String serviceId) {
		return cloudConnectorsConfig.cloud().getServiceConnector(serviceId, EurekaClientConfig.class, null);
	}

	/**
	 * Get the {@link ConnectionFactory} object associated with the only Hystrix AMQP service bound to the app.
	 *
	 * @return RabbitMQ connection factory
	 * @throws CloudException if there are either 0 or more than 1 Hystrix AMQP services.
	 */
	@Override
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
	@Override
	public ConnectionFactory hystrixConnectionFactory(RabbitConnectionFactoryConfig rabbitConnectionFactoryConfig) {
		return cloudConnectorsConfig.cloud().getSingletonServiceConnector(HystrixAmqpConnectionFactory.class, rabbitConnectionFactoryConfig).connectionFactory();
	}

	/**
	 * Get the {@link ConnectionFactory} object for the specified Hystrix AMQP service.
	 *
	 * @param serviceId the name of the service
	 * @return RabbitMQ factory
	 * @throws CloudException if the specified service doesn't exist
	 */
	@Override
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
	@Override
	public ConnectionFactory hystrixConnectionFactory(String serviceId,
													  RabbitConnectionFactoryConfig rabbitConnectionFactoryConfig) {
		return cloudConnectorsConfig.cloud().getServiceConnector(serviceId, HystrixAmqpConnectionFactory.class, rabbitConnectionFactoryConfig).connectionFactory();
	}
}
