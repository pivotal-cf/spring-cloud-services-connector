package io.pivotal.spring.cloud.service.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

/**
 * @author Mike Heath
 */
@Configuration
@EnableConfigurationProperties
@ConditionalOnClass({ConfigServicePropertySourceLocator.class, OAuth2RestTemplate.class})
public class ConfigClientOAuth2AutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(ConfigClientOAuth2ResourceDetails.class)
	public ConfigClientOAuth2ResourceDetails configClientOAuth2ResourceDetails() {
		return new ConfigClientOAuth2ResourceDetails();
	}

}
