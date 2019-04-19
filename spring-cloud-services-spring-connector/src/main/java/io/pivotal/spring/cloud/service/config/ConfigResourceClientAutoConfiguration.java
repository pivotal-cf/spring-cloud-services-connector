/*
 * Copyright 2017 the original author or authors.
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

package io.pivotal.spring.cloud.service.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.config.client.ConfigClientAutoConfiguration;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

/**
 * Auto configures a {@link OAuth2ConfigResourceClient} when a
 * {@link ConfigClientOAuth2ResourceDetails} and a {@link PlainTextConfigClient} are
 * available in the container.
 *
 * @author Daniel Lavoie
 * @author Roy Clarkson
 */
@Configuration
@ConditionalOnClass({ OAuth2ProtectedResourceDetails.class,
		ConfigClientProperties.class })
@AutoConfigureAfter({ ConfigClientAutoConfiguration.class,
		ConfigClientOAuth2BootstrapConfiguration.class })
public class ConfigResourceClientAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(PlainTextConfigClient.class)
	@ConditionalOnProperty(prefix = "spring.cloud.config.client.oauth2", name = {
			"client-id", "client-secret" })
	public ConfigResourceClient configResourceClient(
			ConfigClientOAuth2ResourceDetails resource,
			ConfigClientProperties configClientProperties) {
		return new OAuth2ConfigResourceClient(resource, configClientProperties);
	}
}
