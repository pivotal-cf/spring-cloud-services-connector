package io.pivotal.spring.cloud;

import java.util.List;

import org.mockito.Mockito;
import org.springframework.cloud.CloudConnector;
import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.cloud.service.ServiceInfo;

public class MockCloudConnector implements CloudConnector {

	public static final CloudConnector instance = Mockito.mock(CloudConnector.class);

	public static void reset() {
		Mockito.reset(instance);
	}

	public boolean isInMatchingCloud() {
		return instance.isInMatchingCloud();
	}

	public ApplicationInstanceInfo getApplicationInstanceInfo() {
		return instance.getApplicationInstanceInfo();
	}

	public List<ServiceInfo> getServiceInfos() {
		return instance.getServiceInfos();
	}
}