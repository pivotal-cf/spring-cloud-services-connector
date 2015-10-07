package io.pivotal.spring.cloud.config.java;

import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.annotation.Bean;

public class EurekaClientConfigJavaConfigTest extends AbstractServiceJavaConfigTest<EurekaClientConfigBean> {
	public EurekaClientConfigJavaConfigTest() {
		super(EurekaClientConfigWithId.class, EurekaClientConfigWithoutId.class);
	}

	protected ServiceInfo createService(String id) {
		return createEurekaService(id);
	}

	protected Class<EurekaClientConfigBean> getConnectorType() {
		return EurekaClientConfigBean.class;
	}
}

class EurekaClientConfigWithId extends CloudConnectorsConfig {
	@Bean(name="my-service")
	public EurekaClientConfigBean testEurekaClientConfig() {
		return connectionFactory().eurekaClientConfig("my-service");
	}
}

class EurekaClientConfigWithoutId extends CloudConnectorsConfig {
	@Bean(name="my-service")
	public EurekaClientConfigBean testEurekaClientConfig() {
		return connectionFactory().eurekaClientConfig();
	}
}
