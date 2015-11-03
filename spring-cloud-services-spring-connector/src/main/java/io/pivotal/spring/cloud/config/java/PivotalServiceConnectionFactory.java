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

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.cloud.config.java.ServiceConnectionFactory;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.cloud.service.messaging.RabbitConnectionFactoryConfig;

public interface PivotalServiceConnectionFactory extends ServiceConnectionFactory {

	EurekaClientConfigBean eurekaClientConfig();

	EurekaClientConfigBean eurekaClientConfig(String serviceId);

	ConnectionFactory hystrixConnectionFactory();

	ConnectionFactory hystrixConnectionFactory(RabbitConnectionFactoryConfig rabbitConnectionFactoryConfig);

	ConnectionFactory hystrixConnectionFactory(String serviceId);

	ConnectionFactory hystrixConnectionFactory(String serviceId,
											   RabbitConnectionFactoryConfig rabbitConnectionFactoryConfig);
}
