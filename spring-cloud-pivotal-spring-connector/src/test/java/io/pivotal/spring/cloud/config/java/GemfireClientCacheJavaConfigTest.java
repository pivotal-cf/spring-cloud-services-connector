package io.pivotal.spring.cloud.config.java;

import com.gemstone.gemfire.cache.client.ClientCache;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.annotation.Bean;

public class GemfireClientCacheJavaConfigTest extends AbstractServiceJavaConfigTest<ClientCache> {
	public GemfireClientCacheJavaConfigTest() {
		super(GemfireClientCacheConfigWithId.class, GemfireClientCacheConfigWithoutId.class);
	}

	protected ServiceInfo createService(String id) {
		return createGemfireService(id);
	}

	protected Class<ClientCache> getConnectorType() {
		return ClientCache.class;
	}
}

class GemfireClientCacheConfigWithId extends CloudConnectorsConfig {
	@Bean(name="my-service")
	public ClientCache testGemfireClientCache() {
		return connectionFactory().gemfireClientCache("my-service");
	}
}

class GemfireClientCacheConfigWithoutId extends CloudConnectorsConfig {
	@Bean(name="my-service")
	public ClientCache testGemfireClientCache() {
		return connectionFactory().gemfireClientCache();
	}
}
