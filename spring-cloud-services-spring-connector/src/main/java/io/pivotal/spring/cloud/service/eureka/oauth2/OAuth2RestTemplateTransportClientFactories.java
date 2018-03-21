package io.pivotal.spring.cloud.service.eureka.oauth2;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.util.Collection;
import java.util.Optional;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClientConfig;
import com.netflix.discovery.shared.transport.TransportClientFactory;
import com.netflix.discovery.shared.transport.jersey.EurekaJerseyClient;
import com.netflix.discovery.shared.transport.jersey.TransportClientFactories;

import io.pivotal.spring.cloud.service.eureka.EurekaOAuth2ResourceDetails;

public class OAuth2RestTemplateTransportClientFactories	implements TransportClientFactories<Void> {
	private final EurekaOAuth2ResourceDetails eurekaOAuth2ResourceDetails;

	public OAuth2RestTemplateTransportClientFactories(
			final EurekaOAuth2ResourceDetails eurekaOAuth2ResourceDetails) {
		this.eurekaOAuth2ResourceDetails = eurekaOAuth2ResourceDetails;
	}

	@Override
	public TransportClientFactory newTransportClientFactory(
			Collection<Void> additionalFilters, EurekaJerseyClient providedJerseyClient) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TransportClientFactory newTransportClientFactory(
			EurekaClientConfig clientConfig, Collection<Void> additionalFilters,
			InstanceInfo myInstanceInfo) {
		return new OAuth2RestTemplateTransportClientFactory(
				this.eurekaOAuth2ResourceDetails);
	}

	@Override
	public TransportClientFactory newTransportClientFactory(EurekaClientConfig clientConfig,
															Collection<Void> additionalFilters,
															InstanceInfo myInstanceInfo,
															Optional<SSLContext> sslContext,
															Optional<HostnameVerifier> hostnameVerifier) {

		return new OAuth2RestTemplateTransportClientFactory(
				this.eurekaOAuth2ResourceDetails);
	}

}
