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

package io.pivotal.spring.cloud.config.java;

import io.pivotal.spring.cloud.service.hystrix.HystrixAmqpConnectionFactory;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.config.java.CloudServiceConnectionFactory;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.cloud.service.messaging.RabbitConnectionFactoryConfig;

public class PivotalCloudServiceConnectionFactory extends CloudServiceConnectionFactory implements PivotalServiceConnectionFactory {
	private CloudConnectorsConfig cloudConnectorsConfig;

	public PivotalCloudServiceConnectionFactory(CloudConnectorsConfig cloudConnectorsConfig, Cloud cloud) {
		super(cloud);
		this.cloudConnectorsConfig = cloudConnectorsConfig;
	}

	/**
	 * Get the {@link EurekaClientConfigBean} object associated with the only Eureka service bound to the app.
	 *
	 * @return Eureka client config
	 * @throws CloudException if there are either 0 or more than 1 Eureka services.
	 */
	@Override
	public EurekaClientConfigBean eurekaClientConfig() {
		return cloudConnectorsConfig.cloud().getSingletonServiceConnector(EurekaClientConfigBean.class, null);
	}

	/**
	 * Get the {@link EurekaClientConfigBean} object for the specified Eureka service.
	 *
	 * @param serviceId the name of the service
	 * @return Eureka client config
	 * @throws CloudException if the specified service doesn't exist
	 */
	@Override
	public EurekaClientConfigBean eurekaClientConfig(String serviceId) {
		return cloudConnectorsConfig.cloud().getServiceConnector(serviceId, EurekaClientConfigBean.class, null);
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
