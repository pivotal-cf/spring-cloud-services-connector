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

package io.pivotal.spring.cloud.service.config;

import java.util.LinkedHashMap;
import java.util.Map;

import io.pivotal.spring.cloud.config.java.SpringEnvironmentServiceConnector;
import io.pivotal.spring.cloud.service.common.ConfigServerServiceInfo;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

/**
 * Connector to Config Server service
 *
 * @author Chris Schaefer
 * @author Scott Frederick
 */
public class ConfigServerServiceConnector extends SpringEnvironmentServiceConnector<ConfigServerServiceInfo> {
	private static final String PROPERTY_SOURCE_NAME = "springCloudConfigServer";

	public static final String SPRING_CLOUD_CONFIG_URI = "spring.cloud.config.uri";
	public static final String SPRING_CLOUD_CONFIG_OAUTH2_CLIENT_CLIENT_ID = "spring.cloud.config.client.oauth2.clientId";
	public static final String SPRING_CLOUD_CONFIG_OAUTH2_CLIENT_CLIENT_SECRET = "spring.cloud.config.client.oauth2.clientSecret";
	public static final String SPRING_CLOUD_CONFIG_OAUTH2_CLIENT_ACCESS_TOKEN_URI = "spring.cloud.config.client.oauth2.accessTokenUri";

	@Override
	protected void prepareEnvironment(ConfigurableEnvironment environment, ConfigServerServiceInfo serviceInfo) {
		Map<String, Object> properties = new LinkedHashMap<>();

		properties.put(SPRING_CLOUD_CONFIG_URI, serviceInfo.getUri());
		properties.put(SPRING_CLOUD_CONFIG_OAUTH2_CLIENT_CLIENT_ID, serviceInfo.getClientId());
		properties.put(SPRING_CLOUD_CONFIG_OAUTH2_CLIENT_CLIENT_SECRET, serviceInfo.getClientSecret());
		properties.put(SPRING_CLOUD_CONFIG_OAUTH2_CLIENT_ACCESS_TOKEN_URI, serviceInfo.getAccessTokenUri());

		MapPropertySource mapPropertySource = new MapPropertySource(PROPERTY_SOURCE_NAME, properties);
		environment.getPropertySources().addFirst(mapPropertySource);
	}
}
