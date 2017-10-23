package io.pivotal.spring.cloud.service.eureka.oauth2;

import org.springframework.cloud.netflix.eureka.http.RestTemplateEurekaHttpClient;
import org.springframework.cloud.netflix.eureka.http.RestTemplateTransportClientFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import com.netflix.discovery.shared.resolver.EurekaEndpoint;
import com.netflix.discovery.shared.transport.EurekaHttpClient;
import com.netflix.discovery.shared.transport.TransportClientFactory;

import io.pivotal.spring.cloud.service.eureka.EurekaOAuth2ResourceDetails;

public class OAuth2RestTemplateTransportClientFactory implements TransportClientFactory {
	private final EurekaOAuth2ResourceDetails eurekaOAuth2ResourceDetails;
	private final MappingJackson2HttpMessageConverter jacksonConverter;

	public OAuth2RestTemplateTransportClientFactory(
			final EurekaOAuth2ResourceDetails eurekaOAuth2ResourceDetails) {
		this.eurekaOAuth2ResourceDetails = eurekaOAuth2ResourceDetails;

		// Eureka JSON is incompatible with the default format expected by Jackson.
		this.jacksonConverter = new RestTemplateTransportClientFactory()
				.mappingJacksonHttpMessageConverter();
	}

	@Override
	public EurekaHttpClient newClient(EurekaEndpoint serviceUrl) {
		return new RestTemplateEurekaHttpClient(oauth2RestTemplate(),
				serviceUrl.getServiceUrl());
	}

	@Override
	public void shutdown() {
		// Do nothing
	}

	private OAuth2RestTemplate oauth2RestTemplate() {
		OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(
				eurekaOAuth2ResourceDetails);

		restTemplate.getMessageConverters().add(0, this.jacksonConverter);

		return restTemplate;
	}
}
