package io.pivotal.spring.cloud.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

/**
 * @author Mike Heath
 */
@ConfigurationProperties("spring.cloud.config.client.oauth2")
public class ConfigClientOAuth2ResourceDetails extends ClientCredentialsResourceDetails{
}
