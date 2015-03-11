package org.springframework.cloud.pivotal.config.java;

import com.netflix.discovery.EurekaClientConfig;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.annotation.Bean;

public class EurekaClientConfigJavaConfigTest extends AbstractServiceJavaConfigTest<EurekaClientConfig> {
	public EurekaClientConfigJavaConfigTest() {
		super(EurekaClientConfigWithId.class, EurekaClientConfigWithoutId.class);
	}

	protected ServiceInfo createService(String id) {
		return creatEurekaService(id);
	}

	protected Class<EurekaClientConfig> getConnectorType() {
		return EurekaClientConfig.class;
	}
}

class EurekaClientConfigWithId extends PivotalCloudConfig {
	@Bean(name="my-service")
	public EurekaClientConfig testEurekaClientConfig() {
		return connectionFactory().eurekaClientConfig("my-service");
	}
}

class EurekaClientConfigWithoutId extends PivotalCloudConfig {
	@Bean(name="my-service")
	public EurekaClientConfig testEurekaClientConfig() {
		return connectionFactory().eurekaClientConfig();
	}
}
