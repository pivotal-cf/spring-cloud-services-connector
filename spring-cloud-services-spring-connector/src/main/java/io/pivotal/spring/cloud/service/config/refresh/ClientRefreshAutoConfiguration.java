package io.pivotal.spring.cloud.service.config.refresh;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

import io.rsocket.RSocket;

@Configuration
@ConditionalOnClass({ConfigServicePropertySourceLocator.class, OAuth2RestTemplate.class, RSocket.class})
@ConditionalOnProperty(value = "spring.cloud.config.client.oauth2.client-id")
public class ClientRefreshAutoConfiguration {
	
	@Bean
	public ClientRefreshEventHandler clientRefreshEventHandler(ClientRefreshStreamClient client, ContextRefresher contextRefresher) {
		return new ClientRefreshEventHandler(client, contextRefresher);
	}
	
	@Bean
	public ClientRefreshStreamClient clientRefreshStreamClient(ConfigClientProperties configClientProps, OAuth2ProtectedResourceDetails resource) {
		OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resource);
		String token = oAuth2RestTemplate.getAccessToken().getValue();
		return new ClientRefreshStreamClient(configClientProps, oAuth2RestTemplate.getOAuth2ClientContext());
	}
	
}
