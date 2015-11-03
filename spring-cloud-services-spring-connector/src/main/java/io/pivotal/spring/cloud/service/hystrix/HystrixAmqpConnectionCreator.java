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

package io.pivotal.spring.cloud.service.hystrix;

import io.pivotal.spring.cloud.service.common.HystrixAmqpServiceInfo;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
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
