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

import io.pivotal.spring.cloud.config.AbstractCloudConfigServiceTest;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;

public abstract class AbstractServiceJavaConfigTest<SC> extends AbstractCloudConfigServiceTest<SC> {
	private Class<?> withServiceIdContextClassName;
	private Class<?> withoutServiceIdContextClassName;

	public AbstractServiceJavaConfigTest(Class<?> withServiceIdContextClassName,
										 Class<?> withoutServiceIdContextClassName) {
		this.withServiceIdContextClassName = withServiceIdContextClassName;
		this.withoutServiceIdContextClassName = withoutServiceIdContextClassName;
	}

	protected Class<?> getWithServiceIdContextClassName() {
		return withServiceIdContextClassName;
	}

	protected Class<?> getWithoutServiceIdContextClassName() {
		return withoutServiceIdContextClassName;
	}

	@Override
	protected ApplicationContext getTestApplicationContextWithServiceId(ServiceInfo... serviceInfos) {
		return getTestApplicationContext(getWithServiceIdContextClassName(), serviceInfos);
	}

	@Override
	protected ApplicationContext getTestApplicationContextWithoutServiceId(ServiceInfo... serviceInfos) {
		return getTestApplicationContext(getWithoutServiceIdContextClassName(), serviceInfos);
	}

}