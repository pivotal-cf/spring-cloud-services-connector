package io.pivotal.spring.cloud.service.common;

import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.UriBasedServiceInfo;

/**
 * Information to access Eureka services
 *
 * @author Chris Schaefer
 */
@ServiceInfo.ServiceLabel("eureka")
public class EurekaServiceInfo extends UriBasedServiceInfo {
	private String clientId;
	private String clientSecret;
	private String accessTokenUri;

	public EurekaServiceInfo(String id, String uriString, String clientId, String clientSecret, String accessTokenUri) {
		super(id, uriString);
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.accessTokenUri = accessTokenUri;
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public String getAccessTokenUri() {
		return accessTokenUri;
	}

}
