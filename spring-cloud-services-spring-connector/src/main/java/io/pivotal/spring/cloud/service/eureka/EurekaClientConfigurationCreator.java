package io.pivotal.spring.cloud.service.eureka;

import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import io.pivotal.spring.cloud.service.common.EurekaServiceInfo;

import java.util.Collections;

/**
 * Connector creator for Eureka client services
 *
 * @author Chris Schaefer
 */
public class EurekaClientConfigurationCreator extends AbstractServiceConnectorCreator<EurekaClientConfigBean, EurekaServiceInfo> {
	private static final String DEFAULT_REGION = "default";
	private static final String DEFAULT_ZONE = "defaultZone";
	private static final int REGISTRY_FETCH_INTERVAL_SECS = 5;
	private static final String EUREKA_API_PREFIX = "/eureka/";

	@Override
	public EurekaClientConfigBean create(EurekaServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig) {
		return getEurekaClientConfig(serviceInfo);
	}

	protected EurekaClientConfigBean getEurekaClientConfig(EurekaServiceInfo serviceInfo) {
		EurekaClientConfigBean eurekaClientConfigBean = new EurekaClientConfigBean();
		eurekaClientConfigBean.setServiceUrl(Collections.singletonMap(DEFAULT_ZONE, serviceInfo.getUri() + EUREKA_API_PREFIX));
		eurekaClientConfigBean.setRegion(DEFAULT_REGION);
		eurekaClientConfigBean.setRegistryFetchIntervalSeconds(REGISTRY_FETCH_INTERVAL_SECS);

		return eurekaClientConfigBean;
	}
}
