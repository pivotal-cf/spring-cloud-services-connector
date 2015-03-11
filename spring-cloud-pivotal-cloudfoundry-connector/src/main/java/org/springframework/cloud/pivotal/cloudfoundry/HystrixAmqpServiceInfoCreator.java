package org.springframework.cloud.pivotal.cloudfoundry;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;
import org.springframework.cloud.pivotal.service.common.HystrixAmqpServiceInfo;

import java.util.Map;

/**
 * Service info creator for Hystrix AMQP services
 *
 * @author Scott Frederick
 */
public class HystrixAmqpServiceInfoCreator extends CloudFoundryServiceInfoCreator<HystrixAmqpServiceInfo> {
	private static final String CREDENTIALS_ID_KEY = "name";
	private static final String TAG_NAME = "hystrix-amqp";
	public static final String AMQP_CREDENTIALS_KEY = "amqp";

	public HystrixAmqpServiceInfoCreator() {
		super(new Tags(TAG_NAME));
	}

	@Override
	public boolean accept(Map<String, Object> serviceData) {
		Map<String, Object> credentials = getCredentials(serviceData);
		return super.accept(serviceData) && credentials.containsKey(AMQP_CREDENTIALS_KEY);
	}

	@Override
	@SuppressWarnings("unchecked")
	public HystrixAmqpServiceInfo createServiceInfo(Map<String, Object> serviceData) {
		String id = (String) serviceData.get(CREDENTIALS_ID_KEY);
		Map<String, Object> credentials = getCredentials(serviceData);
		Map<String, Object> amqpCredentials = (Map<String, Object>) credentials.get(AMQP_CREDENTIALS_KEY);
		String uri = getUriFromCredentials(amqpCredentials);

		return new HystrixAmqpServiceInfo(id, uri);
	}
}
