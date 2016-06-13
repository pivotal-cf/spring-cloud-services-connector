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

import io.pivotal.spring.cloud.config.java.SpringEnvironmentServiceConnector;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import io.pivotal.spring.cloud.service.common.HystrixAmqpServiceInfo;

/**
 * Sets properties specific to Spring Cloud Services circuit breaker, overriding defaults in Spring Cloud Netflix.
 *
 * @author Will Tran
 * @author Scott Frederick
 */
@Configuration
public class HystrixStreamServiceConnector extends SpringEnvironmentServiceConnector<HystrixAmqpServiceInfo> {
	public static final String PROPERTY_SOURCE_NAME = "springCloudHystrixStream";

	public static final String SPRING_CLOUD_HYSTRIX_STREAM = "spring.cloud.hystrix.stream";

	public static final String HYSTRIX_STREAM_PREFIX = "spring.cloud.stream.";
	public static final String HYSTRIX_STREAM_BINDING_PREFIX = HYSTRIX_STREAM_PREFIX + "bindings.hystrixStreamOutput.";
	public static final String HYSTRIX_STREAM_BINDER_PREFIX = HYSTRIX_STREAM_PREFIX + "binders.hystrix.";

	protected void prepareEnvironment(ConfigurableEnvironment environment, HystrixAmqpServiceInfo serviceInfo) {
		Map<String, Object> properties = new LinkedHashMap<>();

		properties.put(HYSTRIX_STREAM_BINDING_PREFIX + "destination", SPRING_CLOUD_HYSTRIX_STREAM);
		properties.put(HYSTRIX_STREAM_BINDING_PREFIX + "binder", "hystrix");

		properties.put(HYSTRIX_STREAM_BINDER_PREFIX + "type", "rabbit");
		properties.put(HYSTRIX_STREAM_BINDER_PREFIX + "inheritEnvironment", false);
		properties.put(HYSTRIX_STREAM_BINDER_PREFIX + "defaultCandidate", false);
		properties.put(HYSTRIX_STREAM_BINDER_PREFIX + "environment.spring.cloud.stream.overrideCloudConnectors", true);
		properties.put(HYSTRIX_STREAM_BINDER_PREFIX + "environment.spring.rabbitmq.addresses",
				serviceInfo.getAmqpInfo().getUri());
		properties.put(HYSTRIX_STREAM_BINDER_PREFIX + "default.prefix", "");

		MapPropertySource mapPropertySource = new MapPropertySource(PROPERTY_SOURCE_NAME, properties);
		environment.getPropertySources().addFirst(mapPropertySource);
	}
}
