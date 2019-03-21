/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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