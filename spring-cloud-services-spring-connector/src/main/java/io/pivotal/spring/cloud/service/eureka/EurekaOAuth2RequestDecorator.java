package io.pivotal.spring.cloud.service.eureka;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.AbstractClientHttpRequest;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.DefaultOAuth2RequestAuthenticator;
import org.springframework.security.oauth2.client.OAuth2RequestAuthenticator;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

public class EurekaOAuth2RequestDecorator implements DiscoveryRequestDecorator {

	private final OAuth2RestTemplate oauth2RestTemplate;
	private final OAuth2RequestAuthenticator requestAuthenticator;

	public EurekaOAuth2RequestDecorator(ClientCredentialsResourceDetails resourceDetails) {
		oauth2RestTemplate = new OAuth2RestTemplate(resourceDetails);
		requestAuthenticator = new DefaultOAuth2RequestAuthenticator();
	}

	@Override
	public HttpHeaders getHeaders() {
		// do what org.springframework.security.oauth2.client.OAuth2RestTemplate.createRequest(URI, HttpMethod) does
		// to generate the header
		oauth2RestTemplate.getAccessToken();
		ClientHttpRequest requestHeaderExtrator = new AbstractClientHttpRequest() {

			@Override
			public URI getURI() {
				return null;
			}

			@Override
			public HttpMethod getMethod() {
				return null;
			}

			@Override
			protected OutputStream getBodyInternal(HttpHeaders headers) throws IOException {
				return null;
			}

			@Override
			protected ClientHttpResponse executeInternal(HttpHeaders headers) throws IOException {
				return null;
			}
		};
		requestAuthenticator.authenticate(oauth2RestTemplate.getResource(),
				oauth2RestTemplate.getOAuth2ClientContext(), requestHeaderExtrator);
		return requestHeaderExtrator.getHeaders();
	}

}
