package io.pivotal.spring.cloud.service.gemfire;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import io.pivotal.spring.cloud.service.common.GemfireServiceInfo;
import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;

import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;

public class GemfireClientCacheCreator  extends AbstractServiceConnectorCreator<ClientCache, GemfireServiceInfo>{

	ClientCacheFactory factory;
	
	public GemfireClientCacheCreator(ClientCacheFactory factory) {
		this.factory = factory;
	}

	public GemfireClientCacheCreator() {
		this.factory  = new ClientCacheFactory();
	}
	
	@Override
	public ClientCache create(GemfireServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig) {
		for (URI locator : serviceInfo.getLocators()) {
			factory.addPoolLocator(locator.getHost(), locator.getPort());
		}
		if (serviceInfo.getUsername() != null) {
			factory.set("security-client-auth-init", "io.pivotal.spring.cloud.service.gemfire.UserAuthInitialize.create");
			factory.set("security-username", serviceInfo.getUsername());
		}
		if (serviceInfo.getPassword() != null) {
			factory.set("security-password", serviceInfo.getPassword());
		}
		if (serviceConnectorConfig != null && serviceConnectorConfig.getClass().isAssignableFrom(GemfireServiceConnectorConfig.class)) {
			apply((GemfireServiceConnectorConfig) serviceConnectorConfig);
		}
		return factory.create();
	}
	
	private void apply(GemfireServiceConnectorConfig config){
		BeanUtils.copyProperties(config, factory, getNullPropertyNames(config));
	}

	private static String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<String>();
		for (java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null) emptyNames.add(pd.getName());
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}
}
