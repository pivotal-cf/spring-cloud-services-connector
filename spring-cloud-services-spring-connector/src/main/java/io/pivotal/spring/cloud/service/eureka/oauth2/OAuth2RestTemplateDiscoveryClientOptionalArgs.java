package io.pivotal.spring.cloud.service.eureka.oauth2;

import com.netflix.discovery.AbstractDiscoveryClientOptionalArgs;

import io.pivotal.spring.cloud.service.eureka.EurekaOAuth2ResourceDetails;

public class OAuth2RestTemplateDiscoveryClientOptionalArgs
		extends AbstractDiscoveryClientOptionalArgs<Void> {

	public OAuth2RestTemplateDiscoveryClientOptionalArgs(
			EurekaOAuth2ResourceDetails eurekaOAuth2ResourceDetails) {
		setTransportClientFactories(new OAuth2RestTemplateTransportClientFactories(
				eurekaOAuth2ResourceDetails));
	}
}
