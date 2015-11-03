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

import org.springframework.cloud.CloudException;
import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.cloud.service.common.AmqpServiceInfo;

public class HystrixAmqpServiceInfo extends BaseServiceInfo {
	private AmqpServiceInfo delegate;

	public HystrixAmqpServiceInfo(String id, String uri) throws CloudException {
		super(id);
		delegate = new AmqpServiceInfo(id, uri);
	}

	public AmqpServiceInfo getAmqpInfo() {
		return delegate;
	}

	public String getHost() {
		return delegate.getHost();
	}

	public int getPort() {
		return delegate.getPort();
	}

	public String getUserName() {
		return delegate.getUserName();
	}

	public String getPassword() {
		return delegate.getPassword();
	}

	public String getVirtualHost() {
		return delegate.getVirtualHost();
	}
}
