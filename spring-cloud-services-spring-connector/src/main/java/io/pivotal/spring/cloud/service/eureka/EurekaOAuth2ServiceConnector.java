package io.pivotal.spring.cloud.service.eureka;

import io.pivotal.spring.cloud.service.common.EurekaServiceInfo;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.MapPropertySource;

/**
 * Provides properties to enable {@link EurekaOAuth2AutoConfiguration}
 *
 * @author Will Tran
 */
public class EurekaOAuth2ServiceConnector implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {
	private static final String EUREKA_OAUTH2_CLIENT_ACCESS_TOKEN_URI = "eureka.client.oauth2.accessTokenUri";

	private static final String EUREKA_OAUTH2_CLIENT_CLIENT_SECRET = "eureka.client.oauth2.clientSecret";

	private static final String EUREKA_OAUTH2_CLIENT_CLIENT_ID = "eureka.client.oauth2.clientId";

	private static final String PROPERTY_SOURCE_NAME = "vcapEurekaOAuth2";

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
			if (serviceInfo instanceof EurekaServiceInfo) {
				EurekaServiceInfo eurekaServiceInfo = (EurekaServiceInfo) serviceInfo;

				Map<String, Object> map = new LinkedHashMap<>();
				map.put(EUREKA_OAUTH2_CLIENT_CLIENT_ID, eurekaServiceInfo.getClientId());
				map.put(EUREKA_OAUTH2_CLIENT_CLIENT_SECRET, eurekaServiceInfo.getClientSecret());
				map.put(EUREKA_OAUTH2_CLIENT_ACCESS_TOKEN_URI, eurekaServiceInfo.getAccessTokenUri());
				MapPropertySource mapPropertySource = new MapPropertySource(PROPERTY_SOURCE_NAME, map);

				event.getEnvironment().getPropertySources().addFirst(mapPropertySource);
			}
		}
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 4;
	}
}
