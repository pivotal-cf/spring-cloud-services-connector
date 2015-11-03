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
