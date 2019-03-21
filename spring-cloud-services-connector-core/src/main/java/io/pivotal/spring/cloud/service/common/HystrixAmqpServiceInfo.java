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

package io.pivotal.spring.cloud.service.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.cloud.CloudException;
import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.cloud.service.common.AmqpServiceInfo;
import org.springframework.cloud.util.UriInfo;
import org.springframework.util.StringUtils;

/**
 * @author Roy Clarkson
 */
public class HystrixAmqpServiceInfo extends BaseServiceInfo {

	private static final int AMQP_PORT = 5672;

	private static final int AMQPS_PORT = 5671;

	private AmqpServiceInfo delegate;

	private boolean sslEnabled;

	public HystrixAmqpServiceInfo(String id, String uri) throws CloudException {
		super(id);
		delegate = new AmqpServiceInfo(id, uri);
	}

	public HystrixAmqpServiceInfo(String id, String uri, List<String> uris,
			boolean sslEnabled) {
		super(id);
		delegate = new AmqpServiceInfo(id, uri, null, uris, null);
		this.sslEnabled = sslEnabled;
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

	public boolean getSslEnabled() {
		return this.sslEnabled;
	}

	public String getAddresses() {
		if (this.delegate.getUris() != null && !this.delegate.getUris().isEmpty()) {
			return assembleAddresses(this.delegate.getUris());
		}
		else {
			return assembleAddresses(Arrays.asList(this.delegate.getUri()));
		}
	}

	private String assembleAddresses(List<String> uris) {
		List<String> addresses = new ArrayList<>();
		for (String uri : uris) {
			UriInfo uriInfo = new UriInfo(uri);
			int port = getSchemeBasedPort(uriInfo.getPort(), uriInfo.getScheme());
			addresses.add(uriInfo.getHost() + ":" + port);
		}
		return StringUtils.arrayToCommaDelimitedString(addresses.toArray());
	}

	private int getSchemeBasedPort(final int currentPort, String scheme) {
		int port = currentPort;
		if (currentPort == -1) {
			if (AmqpServiceInfo.AMQP_SCHEME.equals(scheme)) {
				port = AMQP_PORT;
			}
			else if (AmqpServiceInfo.AMQPS_SCHEME.equals(scheme)) {
				port = AMQPS_PORT;
			}
		}
		return port;
	}

}
