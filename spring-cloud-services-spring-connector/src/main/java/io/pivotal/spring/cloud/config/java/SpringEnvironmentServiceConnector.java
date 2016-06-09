package io.pivotal.spring.cloud.config.java;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

public abstract class SpringEnvironmentServiceConnector<T extends ServiceInfo>
		implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {

	private Cloud cloud;

	protected abstract void prepareEnvironment(ConfigurableEnvironment environment, T serviceInfo);

	@Override
	public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
		if (cloud != null) {
			return;
		}

		try {
			cloud = new CloudFactory().getCloud();
		} catch (CloudException e) {
			// not running on a known cloud environment, so nothing to do
			return;
		}

		for (ServiceInfo serviceInfo : cloud.getServiceInfos()) {
			try {
				prepareEnvironment(event.getEnvironment(), (T) serviceInfo);
			} catch (ClassCastException e) {
				// non-matching ServiceInfo
			}
		}
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 4;
	}
}
