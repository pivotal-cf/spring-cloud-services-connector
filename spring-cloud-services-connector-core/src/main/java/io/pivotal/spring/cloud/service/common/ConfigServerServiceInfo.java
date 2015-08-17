package io.pivotal.spring.cloud.service.common;

import org.springframework.cloud.service.UriBasedServiceInfo;

/**
 * Service info to access Config Server services
 *
 * @author Chris Schaefer
 */
public class ConfigServerServiceInfo extends UriBasedServiceInfo {
	private String clientId;
	private String clientSecret;
	private String accessTokenUri;

	public ConfigServerServiceInfo(String id, String uriString, String clientId, String clientSecret, String accessTokenUri) {
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
