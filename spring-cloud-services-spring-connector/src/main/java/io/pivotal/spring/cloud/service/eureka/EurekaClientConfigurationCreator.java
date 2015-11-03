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

import java.util.Collections;

import io.pivotal.spring.cloud.service.common.EurekaServiceInfo;

import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;

/**
 * Connector creator for Eureka client services
 *
 * @author Chris Schaefer
 */
public class EurekaClientConfigurationCreator extends AbstractServiceConnectorCreator<EurekaClientConfigBean, EurekaServiceInfo> {
	private static final String DEFAULT_REGION = "default";
	private static final String DEFAULT_ZONE = "defaultZone";
	private static final int REGISTRY_FETCH_INTERVAL_SECS = 5;
	private static final String EUREKA_API_PREFIX = "/eureka/";

	@Override
	public EurekaClientConfigBean create(EurekaServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig) {
		return getEurekaClientConfig(serviceInfo);
	}

	protected EurekaClientConfigBean getEurekaClientConfig(EurekaServiceInfo serviceInfo) {
		EurekaClientConfigBean eurekaClientConfigBean = new EurekaClientConfigBean();
		eurekaClientConfigBean.setServiceUrl(Collections.singletonMap(DEFAULT_ZONE, serviceInfo.getUri() + EUREKA_API_PREFIX));
		eurekaClientConfigBean.setRegion(DEFAULT_REGION);
		eurekaClientConfigBean.setRegistryFetchIntervalSeconds(REGISTRY_FETCH_INTERVAL_SECS);

		return eurekaClientConfigBean;
	}
}
