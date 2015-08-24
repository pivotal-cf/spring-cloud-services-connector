package io.pivotal.spring.cloud.service.eureka;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import com.netflix.discovery.EurekaClientConfig;

/**
 * 
 * @author Will Tran
 *
 */
@Configuration
@EnableConfigurationProperties
@ConditionalOnClass({EurekaClientConfig.class, OAuth2RestTemplate.class})
@ConditionalOnProperty(value = "eureka.client.oauth2.clientId")
@AutoConfigureBefore(EurekaClientAutoConfiguration.class)
public class EurekaOAuth2AutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean(EurekaOAuth2RequestDecorator.class)
    public EurekaOAuth2RequestDecorator eurekaOauth2RequestDecorator() {
        return new EurekaOAuth2RequestDecorator(eurekaOAuth2ResourceDetails());
    }
    
    @Bean
    @ConditionalOnMissingBean(EurekaOAuth2ResourceDetails.class)
    public EurekaOAuth2ResourceDetails eurekaOAuth2ResourceDetails() {
        return new EurekaOAuth2ResourceDetails();
    }
    
    

}
