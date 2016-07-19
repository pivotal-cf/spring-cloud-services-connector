/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.pivotal.spring.cloud.service.common;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;

public class HystrixAmqpServiceInfoTest {

	private static final String ID = "circuit-breaker";

	private static final String URI = "amqp://username:password@p-rabbitmq1.mydomain.com/testvhost";

	private static final List<String> URIS = Arrays.asList(
			"amqps://username:password@p-rabbitmq1.mydomain.com/testvhost",
			"amqps://username:password@p-rabbitmq2.mydomain.com/testvhost");

	private HystrixAmqpServiceInfo serviceInfo;

	@Before
	public void setup() {
		this.serviceInfo = new HystrixAmqpServiceInfo(ID, URI);
	}

	@Test
	public void oneUri() {
		String addresses = ReflectionTestUtils.invokeMethod(this.serviceInfo,
				"assembleAddresses", Arrays.asList(URI));
		assertEquals("p-rabbitmq1.mydomain.com:5672", addresses);
	}

	@Test
	public void multipleUris() {
		String addresses = ReflectionTestUtils.invokeMethod(this.serviceInfo,
				"assembleAddresses", URIS);
		assertEquals("p-rabbitmq1.mydomain.com:5671,p-rabbitmq2.mydomain.com:5671",
				addresses);
	}

	@Test
	public void configuredPort() {
		int port = ReflectionTestUtils.invokeMethod(this.serviceInfo,
				"getSchemeBasedPort", 10009, "amqps");
		assertEquals(10009, port);
	}

	@Test
	public void amqpPort() {
		int port = ReflectionTestUtils.invokeMethod(this.serviceInfo,
				"getSchemeBasedPort", -1, "amqp");
		assertEquals(5672, port);
	}

	@Test
	public void amqpsPort() {
		int port = ReflectionTestUtils.invokeMethod(this.serviceInfo,
				"getSchemeBasedPort", -1, "amqps");
		assertEquals(5671, port);
	}
}
