package org.springframework.cloud.pivotal.service.common;

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
