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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.DiscoveryManager;
import com.netflix.discovery.EurekaClientConfig;
import com.sun.jersey.client.apache4.ApacheHttpClient4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.eureka.DiscoveryManagerInitializer;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.util.ReflectionUtils;

/**
 * 
 * @author Will Tran
 *
 */
@Configuration
@ConditionalOnClass({EurekaClientConfig.class, OAuth2RestTemplate.class})
@ConditionalOnProperty(value = "eureka.client.oauth2.clientId")
@AutoConfigureBefore(EurekaClientAutoConfiguration.class)
public class EurekaOAuth2ReflectionShim {
	
	private static final String INIT_SCHEDULED_TASKS = "initScheduledTasks";
	private static final String FETCH_REGISTRY_FROM_BACKUP = "fetchRegistryFromBackup";
	private static final String FETCH_REGISTRY = "fetchRegistry";
	private static final String DISCOVERY_APACHE_CLIENT = "discoveryApacheClient";
	
	@Autowired(required = false)
	private Collection<DiscoveryRequestDecorator> requestDecorators;
	
	@Autowired
	private EurekaClientConfigBean clientConfig;
	
	@Bean
	public DiscoveryManagerInitializer discoveryManagerInitializer() {
		return new DiscoveryManagerInitializer() {
			@Override
			public synchronized void init() {
				if (requestDecorators == null || requestDecorators.isEmpty()) {
					super.init();
				} else {
					boolean shouldFetchRegistry = clientConfig.shouldFetchRegistry();
					boolean shouldRegisterWithEureka = clientConfig.shouldRegisterWithEureka();
					// prevent DiscoveryClient from making requests to the Eureka server
					// until after we add DiscoveryRequestDecorators 
					clientConfig.setFetchRegistry(false);
					clientConfig.setRegisterWithEureka(false);
					super.init();
					DiscoveryClient discoveryClient = DiscoveryManager.getInstance().getDiscoveryClient();
					ApacheHttpClient4 discoveryApacheClient = getDiscoveryApacheClient(discoveryClient);
					for (DiscoveryRequestDecorator requestDecorator : requestDecorators) {
						discoveryApacheClient.addFilter(new ClientFilterAdapter(requestDecorator));
					}
					if (shouldFetchRegistry || shouldRegisterWithEureka) {
						clientConfig.setFetchRegistry(shouldFetchRegistry);
						clientConfig.setRegisterWithEureka(shouldRegisterWithEureka);
						// replay https://github.com/Netflix/eureka/blob/v1.1.147/eureka-client/src/main/java/com/netflix/discovery/DiscoveryClient.java#L320-L323
						// this was initially skipped because we set flags to false
						if (clientConfig.shouldFetchRegistry() && !fetchRegistry(discoveryClient, false)) {
							fetchRegistryFromBackup(discoveryClient);
						}
						initScheduledTasks(discoveryClient);
					}
				}

			}

			private ApacheHttpClient4 getDiscoveryApacheClient(DiscoveryClient discoveryClient) {
				Field field = ReflectionUtils.findField(DiscoveryClient.class, DISCOVERY_APACHE_CLIENT);
				ReflectionUtils.makeAccessible(field);
				ApacheHttpClient4 discoveryApacheClient = (ApacheHttpClient4) ReflectionUtils.getField(field, discoveryClient);
				return discoveryApacheClient;
			}

			private boolean fetchRegistry(DiscoveryClient discoveryClient, boolean forceFullRegistryFetch) {
				Method fetchRegistry = ReflectionUtils.findMethod(DiscoveryClient.class, FETCH_REGISTRY, boolean.class);
				ReflectionUtils.makeAccessible(fetchRegistry);
				return (boolean) ReflectionUtils.invokeMethod(fetchRegistry, discoveryClient, forceFullRegistryFetch);
			}

			private void fetchRegistryFromBackup(DiscoveryClient discoveryClient) {
				Method fetchRegistryFromBackup = ReflectionUtils.findMethod(DiscoveryClient.class, FETCH_REGISTRY_FROM_BACKUP);
				ReflectionUtils.makeAccessible(fetchRegistryFromBackup);
				ReflectionUtils.invokeMethod(fetchRegistryFromBackup, discoveryClient);
			}

			private void initScheduledTasks(DiscoveryClient discoveryClient) {
				Method initScheduledTasks = ReflectionUtils.findMethod(DiscoveryClient.class, INIT_SCHEDULED_TASKS);
				ReflectionUtils.makeAccessible(initScheduledTasks);
				ReflectionUtils.invokeMethod(initScheduledTasks, discoveryClient);
			}
		};
	}
	

}
