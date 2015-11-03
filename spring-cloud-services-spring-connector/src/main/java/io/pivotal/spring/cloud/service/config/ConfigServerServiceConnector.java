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

import io.pivotal.spring.cloud.service.common.ConfigServerServiceInfo;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.MapPropertySource;

/**
 * Connector to Config Server service
 *
 * @author Chris Schaefer
 */
@Configuration
public class ConfigServerServiceConnector implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {
	private static final String PROPERTY_SOURCE_NAME = "vcapConfigServerUri";

	private static final String SPRING_CLOUD_CONFIG_URI = "spring.cloud.config.uri";

	private static final String SPRING_CLOUD_CONFIG_OAUTH2_CLIENT_CLIENT_ID = "spring.cloud.config.client.oauth2.clientId";

	private static final String SPRING_CLOUD_CONFIG_OAUTH2_CLIENT_CLIENT_SECRET = "spring.cloud.config.client.oauth2.clientSecret";

	private static final String SPRING_CLOUD_CONFIG_OAUTH2_CLIENT_ACCESS_TOKEN_URI = "spring.cloud.config.client.oauth2.accessTokenUri";

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
			if (serviceInfo instanceof ConfigServerServiceInfo) {
				final ConfigServerServiceInfo configServerServiceInfo = (ConfigServerServiceInfo) serviceInfo;
				Map<String, Object> properties = new LinkedHashMap<>();
				properties.put(SPRING_CLOUD_CONFIG_URI, configServerServiceInfo.getUri());
				properties.put(SPRING_CLOUD_CONFIG_OAUTH2_CLIENT_CLIENT_ID, configServerServiceInfo.getClientId());
				properties.put(SPRING_CLOUD_CONFIG_OAUTH2_CLIENT_CLIENT_SECRET, configServerServiceInfo.getClientSecret());
				properties.put(SPRING_CLOUD_CONFIG_OAUTH2_CLIENT_ACCESS_TOKEN_URI, configServerServiceInfo.getAccessTokenUri());
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
