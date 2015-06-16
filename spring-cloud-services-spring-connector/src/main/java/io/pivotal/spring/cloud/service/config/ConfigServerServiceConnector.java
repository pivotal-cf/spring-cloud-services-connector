package io.pivotal.spring.cloud.service.config;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.MapPropertySource;
import io.pivotal.spring.cloud.service.common.ConfigServerServiceInfo;

import java.util.Collections;

/**
 * Connector to Config Server service
 *
 * @author Chris Schaefer
 */
@Configuration
public class ConfigServerServiceConnector implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {
	private static final String PROPERTY_SOURCE_NAME = "vcapConfigServerUri";
	private static final String SPRING_CLOUD_CONFIG_URI = "spring.cloud.config.uri";

	private Cloud cloud;

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
			if (serviceInfo instanceof ConfigServerServiceInfo) {
				String uri = ((ConfigServerServiceInfo) serviceInfo).getUri();

				MapPropertySource mapPropertySource = new MapPropertySource(PROPERTY_SOURCE_NAME,
						Collections.<String, Object>singletonMap(SPRING_CLOUD_CONFIG_URI, uri));

				event.getEnvironment().getPropertySources().addFirst(mapPropertySource);
			}
		}
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 4;
	}
}
