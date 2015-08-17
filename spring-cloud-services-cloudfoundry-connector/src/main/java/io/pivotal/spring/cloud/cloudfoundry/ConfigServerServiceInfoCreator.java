package io.pivotal.spring.cloud.cloudfoundry;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;
import io.pivotal.spring.cloud.service.common.ConfigServerServiceInfo;

import java.util.Map;

/**
 * Service info creator for Config Server services
 *
 * @author Chris Schaefer
 */
public class ConfigServerServiceInfoCreator extends CloudFoundryServiceInfoCreator<ConfigServerServiceInfo> {
	private static final String CREDENTIALS_ID_KEY = "name";
	private static final String CONFIG_SERVER_SERVICE_TAG_NAME = "configuration";

	public ConfigServerServiceInfoCreator() {
		super(new Tags(CONFIG_SERVER_SERVICE_TAG_NAME));
	}

	@Override
	public ConfigServerServiceInfo createServiceInfo(Map<String, Object> serviceData) {
		String id = (String) serviceData.get(CREDENTIALS_ID_KEY);
		String uri = getUriFromCredentials(getCredentials(serviceData));

		Map<String, Object> credentials = getCredentials(serviceData);
		String clientId = getStringFromCredentials(credentials, "client_id");
		String clientSecret = getStringFromCredentials(credentials, "client_secret");
		String accessTokenUri = getStringFromCredentials(credentials, "access_token_uri");

		return new ConfigServerServiceInfo(id, uri, clientId, clientSecret, accessTokenUri);
	}
}
