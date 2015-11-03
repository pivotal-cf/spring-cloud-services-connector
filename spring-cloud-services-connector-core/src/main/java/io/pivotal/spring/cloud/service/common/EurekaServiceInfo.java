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

package io.pivotal.spring.cloud.service.common;

import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.UriBasedServiceInfo;

/**
 * Information to access Eureka services
 *
 * @author Chris Schaefer
 */
@ServiceInfo.ServiceLabel("eureka")
public class EurekaServiceInfo extends UriBasedServiceInfo {
	private String clientId;
	private String clientSecret;
	private String accessTokenUri;

	public EurekaServiceInfo(String id, String uriString, String clientId, String clientSecret, String accessTokenUri) {
		super(id, uriString);
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.accessTokenUri = accessTokenUri;
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public String getAccessTokenUri() {
		return accessTokenUri;
	}

}
