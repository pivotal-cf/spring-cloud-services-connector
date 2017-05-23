package io.pivotal.spring.cloud.service.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(ConfigServicePropertySourceLocator.class)
public class PropertySourceMaskingConfiguration {

	@Bean
	public PropertySourceMaskingEnvironmentEndpoint environmentEndpoint() {
		PropertySourceMaskingEnvironmentEndpoint environmentEndpoint = new PropertySourceMaskingEnvironmentEndpoint();
		environmentEndpoint.setSourceNamePatterns("configService:vault:*");
		return environmentEndpoint;
	}
	
}
