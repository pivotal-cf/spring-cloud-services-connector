package io.pivotal.spring.cloud.service.eureka;

import java.util.List;
import java.util.Map.Entry;

import lombok.RequiredArgsConstructor;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;

/**
 * 
 * @author Will Tran
 *
 */
@RequiredArgsConstructor
public class ClientFilterAdapter extends ClientFilter {
	private final DiscoveryRequestDecorator decorator;

	@Override
	public ClientResponse handle(ClientRequest cr) throws ClientHandlerException {
		if (decorator.getHeaders() != null) {
			for (Entry<String, List<String>> entry : decorator.getHeaders().entrySet()) {
				for (String value : entry.getValue()) {
					cr.getHeaders().add(entry.getKey(), value);
				}
			}
		}
		return getNext().handle(cr);
	}

}
