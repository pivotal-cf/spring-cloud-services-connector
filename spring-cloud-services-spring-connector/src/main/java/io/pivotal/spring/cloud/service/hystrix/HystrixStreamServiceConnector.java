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

import io.pivotal.spring.cloud.config.java.ServiceInfoPropertySourceAdapter;
import io.pivotal.spring.cloud.service.common.HystrixAmqpServiceInfo;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

/**
 * Sets properties specific to Spring Cloud Services circuit breaker, overriding defaults
 * in Spring Cloud Netflix.
 *
 * @author Will Tran
 * @author Scott Frederick
 * @author Roy Clarkson
 */
@Configuration
public class HystrixStreamServiceConnector
		extends ServiceInfoPropertySourceAdapter<HystrixAmqpServiceInfo> {

	public static final String PROPERTY_SOURCE_NAME = "springCloudServicesCircuitBreaker";

	public static final String SPRING_CLOUD_HYSTRIX_STREAM = "spring.cloud.hystrix.stream";

	public static final String SPRING_CLOUD_STREAM = "spring.cloud.stream.";

	public static final String SPRING_CLOUD_STREAM_BINDINGS_HYSTRIXSTREAMOUTPUT = SPRING_CLOUD_STREAM
			+ "bindings.hystrixStreamOutput.";

	public static final String SPRING_CLOUD_STREAM_BINDERS_HYSTRIX = SPRING_CLOUD_STREAM
			+ "binders.hystrix.";

	public static final String SPRING_CLOUD_STREAM_BINDERS_HYSTRIX_ENVIRONMENT_SPRING_RABBITMQ = SPRING_CLOUD_STREAM_BINDERS_HYSTRIX
			+ "environment.spring.rabbitmq.";

	@Override
	protected PropertySource<?> toPropertySource(HystrixAmqpServiceInfo serviceInfo) {
		Map<String, Object> properties = new LinkedHashMap<>();

		properties.put(SPRING_CLOUD_STREAM_BINDINGS_HYSTRIXSTREAMOUTPUT + "destination",
				SPRING_CLOUD_HYSTRIX_STREAM);
		properties.put(SPRING_CLOUD_STREAM_BINDINGS_HYSTRIXSTREAMOUTPUT + "binder",
				"hystrix");
		properties.put(SPRING_CLOUD_STREAM_BINDERS_HYSTRIX + "type", "rabbit");
		properties.put(SPRING_CLOUD_STREAM_BINDERS_HYSTRIX + "inheritEnvironment", false);
		properties.put(SPRING_CLOUD_STREAM_BINDERS_HYSTRIX + "defaultCandidate", false);
		properties.put(
				SPRING_CLOUD_STREAM_BINDERS_HYSTRIX
						+ "environment.spring.cloud.stream.overrideCloudConnectors",
				true);
		properties.put(SPRING_CLOUD_STREAM_BINDERS_HYSTRIX_ENVIRONMENT_SPRING_RABBITMQ
				+ "addresses", serviceInfo.getAddresses());
		properties.put(SPRING_CLOUD_STREAM_BINDERS_HYSTRIX_ENVIRONMENT_SPRING_RABBITMQ
				+ "username", serviceInfo.getUserName());
		properties.put(SPRING_CLOUD_STREAM_BINDERS_HYSTRIX_ENVIRONMENT_SPRING_RABBITMQ
				+ "password", serviceInfo.getPassword());
		properties.put(SPRING_CLOUD_STREAM_BINDERS_HYSTRIX_ENVIRONMENT_SPRING_RABBITMQ
				+ "virtualHost", serviceInfo.getVirtualHost());
		properties.put(SPRING_CLOUD_STREAM_BINDERS_HYSTRIX_ENVIRONMENT_SPRING_RABBITMQ
				+ "ssl.enabled", serviceInfo.getSslEnabled());
		properties.put(SPRING_CLOUD_STREAM_BINDERS_HYSTRIX + "default.prefix", "");

		return new MapPropertySource(PROPERTY_SOURCE_NAME, properties);
	}
}
