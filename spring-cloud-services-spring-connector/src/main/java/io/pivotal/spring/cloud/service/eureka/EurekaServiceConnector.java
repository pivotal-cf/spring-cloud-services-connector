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

package io.pivotal.spring.cloud.service.eureka;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import io.pivotal.spring.cloud.config.java.ServiceInfoPropertySourceAdapter;
import io.pivotal.spring.cloud.service.common.EurekaServiceInfo;

/**
 * Provides eureka.client.* properties and enables {@link EurekaOAuth2AutoConfiguration}
 *
 * @author Will Tran
 */
public class EurekaServiceConnector extends ServiceInfoPropertySourceAdapter<EurekaServiceInfo> {
	private static final String EUREKA_CLIENT = "eureka.client.";
	private static final String EUREKA_CLIENT_OAUTH2 = EUREKA_CLIENT + "oauth2.";
	private static final String EUREKA_API_PREFIX = "/eureka/";
	private static final String DEFAULT_REGION = "default";
	private static final String PROPERTY_SOURCE_NAME = "springCloudServicesServiceRegistry";

	@Override
	protected PropertySource<?> toPropertySource(EurekaServiceInfo eurekaServiceInfo) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put(EUREKA_CLIENT + "serviceUrl.defaultZone", eurekaServiceInfo.getUri() + EUREKA_API_PREFIX);
		map.put(EUREKA_CLIENT + "region", DEFAULT_REGION);
		map.put(EUREKA_CLIENT_OAUTH2 + "clientId", eurekaServiceInfo.getClientId());
		map.put(EUREKA_CLIENT_OAUTH2 + "clientSecret", eurekaServiceInfo.getClientSecret());
		map.put(EUREKA_CLIENT_OAUTH2 + "accessTokenUri", eurekaServiceInfo.getAccessTokenUri());
		return new MapPropertySource(PROPERTY_SOURCE_NAME, map);
	}

}
