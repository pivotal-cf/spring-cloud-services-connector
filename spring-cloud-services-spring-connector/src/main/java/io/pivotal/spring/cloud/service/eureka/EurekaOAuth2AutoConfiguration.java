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

import java.util.ArrayList;
import java.util.List;

import com.netflix.discovery.DiscoveryClient.DiscoveryClientOptionalArgs;
import com.netflix.discovery.EurekaClientConfig;
import com.sun.jersey.api.client.filter.ClientFilter;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

/**
 * 
 * @author Will Tran
 *
 */
@Configuration
@EnableConfigurationProperties
@ConditionalOnClass({ EurekaClientConfig.class, OAuth2RestTemplate.class })
@ConditionalOnProperty(value = "eureka.client.oauth2.clientId")
@AutoConfigureBefore(EurekaClientAutoConfiguration.class)
public class EurekaOAuth2AutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(EurekaOAuth2RequestDecorator.class)
	public EurekaOAuth2RequestDecorator eurekaOauth2RequestDecorator() {
		return new EurekaOAuth2RequestDecorator(eurekaOAuth2ResourceDetails());
	}

	@Bean
	@ConditionalOnMissingBean(EurekaOAuth2ResourceDetails.class)
	public EurekaOAuth2ResourceDetails eurekaOAuth2ResourceDetails() {
		return new EurekaOAuth2ResourceDetails();
	}


	@Bean
	@ConditionalOnMissingBean(DiscoveryClientOptionalArgs.class)
	public DiscoveryClientOptionalArgs discoveryClientOptionalArgs() {
		List<ClientFilter> filters = new ArrayList<>();
		filters.add(new ClientFilterAdapter(eurekaOauth2RequestDecorator()));

		DiscoveryClientOptionalArgs args = new DiscoveryClientOptionalArgs();
		args.setAdditionalFilters(filters);

		return args;
	}
}