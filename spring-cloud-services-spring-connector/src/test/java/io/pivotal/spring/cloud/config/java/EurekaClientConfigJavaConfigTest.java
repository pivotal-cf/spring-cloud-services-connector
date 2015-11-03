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

package io.pivotal.spring.cloud.config.java;

import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.annotation.Bean;

public class EurekaClientConfigJavaConfigTest extends AbstractServiceJavaConfigTest<EurekaClientConfigBean> {
	public EurekaClientConfigJavaConfigTest() {
		super(EurekaClientConfigWithId.class, EurekaClientConfigWithoutId.class);
	}

	protected ServiceInfo createService(String id) {
		return createEurekaService(id);
	}

	protected Class<EurekaClientConfigBean> getConnectorType() {
		return EurekaClientConfigBean.class;
	}
}

class EurekaClientConfigWithId extends CloudConnectorsConfig {
	@Bean(name="my-service")
	public EurekaClientConfigBean testEurekaClientConfig() {
		return connectionFactory().eurekaClientConfig("my-service");
	}
}

class EurekaClientConfigWithoutId extends CloudConnectorsConfig {
	@Bean(name="my-service")
	public EurekaClientConfigBean testEurekaClientConfig() {
		return connectionFactory().eurekaClientConfig();
	}
}
