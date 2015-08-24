package io.pivotal.spring.cloud.service.eureka;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

/**
 * 
 * @author Will Tran
 *
 */
@ConfigurationProperties("eureka.client.oauth2")
public class EurekaOAuth2ResourceDetails extends ClientCredentialsResourceDetails {

}
