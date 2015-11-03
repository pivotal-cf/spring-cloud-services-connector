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

package io.pivotal.spring.cloud.cloudfoundry;

import java.util.Map;

import io.pivotal.spring.cloud.service.common.ConfigServerServiceInfo;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;

/**
 * Service info creator for Config Server services
 *
 * @author Chris Schaefer
 * @author Will Tran
 */
public class ConfigServerServiceInfoCreator extends CloudFoundryServiceInfoCreator<ConfigServerServiceInfo> {
	private static final String CREDENTIALS_ID_KEY = "name";
	private static final String CONFIG_SERVER_SERVICE_TAG_NAME = "configuration";

	public ConfigServerServiceInfoCreator() {
		super(new Tags(CONFIG_SERVER_SERVICE_TAG_NAME));
	}

	@Override
	public ConfigServerServiceInfo createServiceInfo(Map<String, Object> serviceData) {
		String id = (String) serviceData.get(CREDENTIALS_ID_KEY);
		String uri = getUriFromCredentials(getCredentials(serviceData));

		Map<String, Object> credentials = getCredentials(serviceData);
		String clientId = getStringFromCredentials(credentials, "client_id");
		String clientSecret = getStringFromCredentials(credentials, "client_secret");
		String accessTokenUri = getStringFromCredentials(credentials, "access_token_uri");

		return new ConfigServerServiceInfo(id, uri, clientId, clientSecret, accessTokenUri);
	}
}
