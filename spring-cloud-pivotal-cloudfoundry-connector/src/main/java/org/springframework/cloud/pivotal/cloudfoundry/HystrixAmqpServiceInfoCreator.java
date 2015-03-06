package org.springframework.cloud.pivotal.cloudfoundry;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;
import org.springframework.cloud.service.common.AmqpServiceInfo;

import java.util.Map;

/**
 * Service info creator for Hystrix AMQP services
 *
 * @author Scott Frederick
 */
public class HystrixAmqpServiceInfoCreator extends CloudFoundryServiceInfoCreator<AmqpServiceInfo> {
	private static final String CREDENTIALS_ID_KEY = "name";
	private static final String TAG_NAME = "hystrix-amqp";

	public HystrixAmqpServiceInfoCreator() {
		super(new Tags(TAG_NAME));
	}

	@Override
	@SuppressWarnings("unchecked")
	public AmqpServiceInfo createServiceInfo(Map<String, Object> serviceData) {
		String id = (String) serviceData.get(CREDENTIALS_ID_KEY);
		Map<String, Object> credentials = getCredentials(serviceData);
		Map<String, Object> amqpCredentials = (Map<String, Object>) credentials.get("amqp");
		String uri = getUriFromCredentials(amqpCredentials);

		return new AmqpServiceInfo(id, uri);
	}
}
