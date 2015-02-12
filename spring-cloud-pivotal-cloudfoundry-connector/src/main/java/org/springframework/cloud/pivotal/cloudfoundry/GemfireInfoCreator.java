package org.springframework.cloud.pivotal.cloudfoundry;

import java.util.Map;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;
import org.springframework.cloud.pivotal.service.common.GemfireServiceInfo;

/**
 *
 * Service info creator for Eureka services
 *
 * @author Vinicius Carvalho
 */
public class GemfireInfoCreator extends CloudFoundryServiceInfoCreator<GemfireServiceInfo> {

	public GemfireInfoCreator() {
		super(new Tags("gemfire"));
	}
	

	public GemfireInfoCreator(Tags tags) {
		super(new Tags("gemfire"));
	}

	
	
	@Override
	public GemfireServiceInfo createServiceInfo(Map<String, Object> serviceData) {
		String id = (String) serviceData.get("name");
		Map<String, Object> credentials = (Map<String, Object>) serviceData.get("credentials");
		return new GemfireServiceInfo(id, credentials);
	}


	@Override
	public boolean accept(Map<String, Object> serviceData) {
		return containsLocators(serviceData) || super.accept(serviceData);
	}
	
	protected boolean containsLocators(Map<String,Object> serviceData){
		Object locators = getCredentials(serviceData).get("locators");
		if(locators == null)
			return false;
		return true;
	}

}
