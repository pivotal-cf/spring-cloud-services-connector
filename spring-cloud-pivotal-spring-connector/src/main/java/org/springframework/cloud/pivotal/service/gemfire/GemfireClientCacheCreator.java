package org.springframework.cloud.pivotal.service.gemfire;

import java.net.URI;

import org.springframework.cloud.pivotal.service.common.GemfireServiceInfo;
import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;

import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;

public class GemfireClientCacheCreator  extends AbstractServiceConnectorCreator<ClientCache, GemfireServiceInfo>{

	final ClientCacheFactory factory;
	
	public GemfireClientCacheCreator(ClientCacheFactory factory) {
		this.factory = factory;
	}

	public GemfireClientCacheCreator() {
		this.factory  = new ClientCacheFactory();
	}
	
	@Override
	public ClientCache create(GemfireServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig) {
		
		for(URI locator: serviceInfo.getLocators()){
			factory.addPoolLocator(locator.getHost(), locator.getPort());
		}
		if(serviceInfo.getUsername() != null){
			factory.set("security-client-auth-init", "org.springframework.cloud.pivotal.services.gemfire.UserAuthInitialize.create");
			factory.set("security-username",serviceInfo.getUsername());
		}
		if(serviceInfo.getPassword() != null){
			factory.set("security-password",serviceInfo.getPassword());
		}
		return factory.create();
		 
	}
}
