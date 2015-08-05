package io.pivotal.spring.cloud.cloudfoundry;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;

import io.pivotal.spring.cloud.service.common.EurekaServiceInfo;

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
		Map<String, Object> credentials = getCredentials(serviceData);
		String uri = getUriFromCredentials(credentials);
		
		String clientId = getStringFromCredentials(credentials, "client_id");
		String clientSecret = getStringFromCredentials(credentials, "client_secret");
		String accessTokenUri = getStringFromCredentials(credentials, "access_token_uri");
		
		return new EurekaServiceInfo(id, uri, clientId, clientSecret, accessTokenUri);
	}
}
