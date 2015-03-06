package org.springframework.cloud.pivotal.cloudfoundry;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;
import org.springframework.cloud.pivotal.service.common.EurekaServiceInfo;

import java.util.Map;

/**
 * Service info creator for Eureka services
 *
 * @author Chris Schaefer
 */
public class EurekaServiceInfoCreator extends CloudFoundryServiceInfoCreator<EurekaServiceInfo> {
	private static final String CREDENTIALS_ID_KEY = "name";
	private static final String EUREKA_SERVICE_TAG_NAME = "eureka";

	public EurekaServiceInfoCreator() {
		super(new Tags(EUREKA_SERVICE_TAG_NAME));
	}

	@Override
	public EurekaServiceInfo createServiceInfo(Map<String, Object> serviceData) {
		String id = (String) serviceData.get(CREDENTIALS_ID_KEY);
		String uri = getUriFromCredentials(getCredentials(serviceData));

		return new EurekaServiceInfo(id, uri);
	}
}
