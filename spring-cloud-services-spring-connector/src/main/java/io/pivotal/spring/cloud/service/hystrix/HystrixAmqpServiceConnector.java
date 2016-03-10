/*
 * Copyright 2016 the original author or authors.
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

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.MapPropertySource;

import io.pivotal.spring.cloud.service.common.HystrixAmqpServiceInfo;

/**
 * Sets properties specific to Spring Cloud Services circuit breaker, overriding defaults in Spring Cloud Netflix
 *
 * @author Will Tran
 */
@Configuration
public class HystrixAmqpServiceConnector implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {
	private static final String HYSTRIX_STREAM_QUEUE_DESTINATION = "hystrix.stream.queue.destination";

	private static final String PROPERTY_SOURCE_NAME = "vcapHystrixAmqp";

	private static final String SPRING_CLOUD_HYSTRIX_STREAM = "spring.cloud.hystrix.stream";

	private Cloud cloud;

	@Override
	public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
		if (cloud != null) {
			return;
		}

		try {
			cloud = new CloudFactory().getCloud();
		} catch (CloudException e) {
			 // not running on a known cloud environment, so nothing to do
			return;
		}

		for (ServiceInfo serviceInfo : cloud.getServiceInfos()) {
			if (serviceInfo instanceof HystrixAmqpServiceInfo) {
				Map<String, Object> properties = new LinkedHashMap<>();
				properties.put(HYSTRIX_STREAM_QUEUE_DESTINATION, SPRING_CLOUD_HYSTRIX_STREAM);
				// set up this property to force spring cloud stream to not use prefixes
				// This may change with https://github.com/spring-cloud/spring-cloud-stream/issues/307
				properties.put("spring.cloud.stream.binder.rabbit.default.prefix", "");
				MapPropertySource mapPropertySource = new MapPropertySource(PROPERTY_SOURCE_NAME, properties);
				event.getEnvironment().getPropertySources().addFirst(mapPropertySource);
			}
		}
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 4;
	}
}
