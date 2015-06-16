package io.pivotal.spring.cloud.config.java;

import com.netflix.discovery.EurekaClientConfig;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.annotation.Bean;

public class EurekaClientConfigJavaConfigTest extends AbstractServiceJavaConfigTest<EurekaClientConfig> {
	public EurekaClientConfigJavaConfigTest() {
		super(EurekaClientConfigWithId.class, EurekaClientConfigWithoutId.class);
	}

	protected ServiceInfo createService(String id) {
		return createEurekaService(id);
	}

	protected Class<EurekaClientConfig> getConnectorType() {
		return EurekaClientConfig.class;
	}
}

class EurekaClientConfigWithId extends CloudConnectorsConfig {
	@Bean(name="my-service")
	public EurekaClientConfig testEurekaClientConfig() {
		return connectionFactory().eurekaClientConfig("my-service");
	}
}

class EurekaClientConfigWithoutId extends CloudConnectorsConfig {
	@Bean(name="my-service")
	public EurekaClientConfig testEurekaClientConfig() {
		return connectionFactory().eurekaClientConfig();
	}
}
