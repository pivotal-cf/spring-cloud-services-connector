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

package io.pivotal.spring.cloud;

import io.pivotal.spring.cloud.service.common.EurekaServiceInfo;
import io.pivotal.spring.cloud.service.common.HystrixAmqpServiceInfo;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.cloud.CloudConnector;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.CloudTestUtil;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

abstract public class StubCloudConnectorTest {
	private static final String MOCK_CLOUD_BEAN_NAME = "mockCloud";

	protected ApplicationContext getTestApplicationContext(Class<?> configClass, ServiceInfo... serviceInfos) {
		final CloudConnector stubCloudConnector = CloudTestUtil.getTestCloudConnector(serviceInfos);

		return new AnnotationConfigApplicationContext(configClass) {
			@Override
			protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
				CloudFactory cloudFactory = new CloudFactory();
				cloudFactory.registerCloudConnector(stubCloudConnector);
				getBeanFactory().registerSingleton(MOCK_CLOUD_BEAN_NAME, cloudFactory);
				super.prepareBeanFactory(beanFactory);
			}
		};
	}

	protected EurekaServiceInfo createEurekaService(String id) {
		return new EurekaServiceInfo(id, "http://username:password@10.20.30.40:1234", null, null, null);
	}

	protected HystrixAmqpServiceInfo createHystrixAmqpService(String id) {
		return new HystrixAmqpServiceInfo(id, "amqp://username:password@10.20.30.40:1234/vh");
	}
}