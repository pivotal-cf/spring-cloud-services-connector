package io.pivotal.spring.cloud.cloudfoundry;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;
import io.pivotal.spring.cloud.service.common.GemfireServiceInfo;

/**
 *
 * Service info creator for Eureka services
 *
 * @author Vinicius Carvalho
 */
public class GemfireServiceInfoCreator extends CloudFoundryServiceInfoCreator<GemfireServiceInfo> {

	public GemfireServiceInfoCreator() {
		super(new Tags("gemfire"));
	}

	@Override
	@SuppressWarnings("unchecked")
	public GemfireServiceInfo createServiceInfo(Map<String, Object> serviceData) {
		String id = (String) serviceData.get("name");

		Map<String, Object> credentials = getCredentials(serviceData);

		String username = getStringFromCredentials(credentials, "username");
		String password = getStringFromCredentials(credentials, "password");
		List<String> locators = (List<String>) credentials.get("locators");

		return new GemfireServiceInfo(id, locators, username, password);
	}

	@Override
	public boolean accept(Map<String, Object> serviceData) {
		return containsLocators(serviceData) || super.accept(serviceData);
	}

	protected boolean containsLocators(Map<String,Object> serviceData){
		Object locators = getCredentials(serviceData).get("locators");
		return locators != null;
	}

}
