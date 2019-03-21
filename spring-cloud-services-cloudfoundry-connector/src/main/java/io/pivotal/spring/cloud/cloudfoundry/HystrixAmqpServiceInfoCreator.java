/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.pivotal.spring.cloud.cloudfoundry;

import java.util.List;
import java.util.Map;

import io.pivotal.spring.cloud.service.common.HystrixAmqpServiceInfo;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;

/**
 * Service info creator for Hystrix AMQP services
 *
 * @author Scott Frederick
 * @author Roy Clarkson
 */
public class HystrixAmqpServiceInfoCreator extends CloudFoundryServiceInfoCreator<HystrixAmqpServiceInfo> {

	private static final String TAG_NAME = "hystrix-amqp";

	private static final String CREDENTIALS_ID_KEY = "name";

	private static final String URIS_ID_KEY = "uris";

	private static final String SSL_ID_KEY = "ssl";

	public static final String AMQP_CREDENTIALS_KEY = "amqp";

	public HystrixAmqpServiceInfoCreator() {
		super(new Tags(TAG_NAME));
	}

	@Override
	public boolean accept(Map<String, Object> serviceData) {
		Map<String, Object> credentials = getCredentials(serviceData);
		return super.accept(serviceData) && credentials.containsKey(AMQP_CREDENTIALS_KEY);
	}

	@Override
	@SuppressWarnings("unchecked")
	public HystrixAmqpServiceInfo createServiceInfo(Map<String, Object> serviceData) {
		String id = (String) serviceData.get(CREDENTIALS_ID_KEY);
		Map<String, Object> credentials = getCredentials(serviceData);
		Map<String, Object> amqpCredentials = (Map<String, Object>) credentials.get(AMQP_CREDENTIALS_KEY);
		String uri = getUriFromCredentials(amqpCredentials);
		List<String> uris = (List<String>) amqpCredentials.get(URIS_ID_KEY);
		boolean sslEnabled = Boolean.valueOf(amqpCredentials.get(SSL_ID_KEY).toString());
		return new HystrixAmqpServiceInfo(id, uri, uris, sslEnabled);
	}
}
