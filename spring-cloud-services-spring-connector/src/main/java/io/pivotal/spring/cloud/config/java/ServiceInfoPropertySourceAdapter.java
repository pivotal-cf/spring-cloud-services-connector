package io.pivotal.spring.cloud.config.java;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

/**
 * Transforms a {@link ServiceInfo} into a {@link PropertySource} with highest precedence
 * 
 * @author Scott Frederick
 * @author Will Tran
 * 
 * @see <a href=
 *      "http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config">
 *      http://docs.spring.io/spring-boot/docs/current/reference/html/boot-
 *      features-external-config.html#boot-features-external-config</a>
 * @param <T>
 *            the {@link ServiceInfo} type
 */
public abstract class ServiceInfoPropertySourceAdapter<T extends ServiceInfo>
		implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {

	private Cloud cloud;

	protected abstract PropertySource<?> toPropertySource(T serviceInfo);

	@SuppressWarnings("unchecked")
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
				PropertySource<?> propertySource = toPropertySource((T) serviceInfo);
				event.getEnvironment().getPropertySources().addFirst(propertySource);
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
