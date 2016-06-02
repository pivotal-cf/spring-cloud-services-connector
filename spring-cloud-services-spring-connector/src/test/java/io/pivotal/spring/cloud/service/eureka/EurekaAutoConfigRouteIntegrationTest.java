/*
 * Copyright 2015 the original author or authors.
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

package io.pivotal.spring.cloud.service.eureka;

import io.pivotal.spring.cloud.TestApplication;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Roy Clarkson
 * @author Will Tran
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplication.class)
@IntegrationTest({ "server.port=0", "vcap.application.uris[0]=www.route.local",
		"cf.instance.ip=1.2.3.4",
		"cf.instance.port=54321",
		"vcap.application.instance_id=instance-id",
		"spring.application.name=app-name_",
		"spring.cloud.services.registrationMethod=route" })
public class EurekaAutoConfigRouteIntegrationTest {

	@Autowired
	private ApplicationContext context;

	@Test
	public void eurekaConfigBean() throws Exception {
		final EurekaInstanceConfigBean config = context
				.getBean(EurekaInstanceConfigBean.class);
		assertEquals("www.route.local:instance-id", config.getInstanceId());
		assertEquals("app-name_", config.getAppname());
		assertEquals("app-name-", config.getVirtualHostName());
		assertEquals("app-name-", config.getSecureVirtualHostName());
		assertEquals("www.route.local", config.getHostname());
		assertEquals(80, config.getNonSecurePort());
		assertEquals(443, config.getSecurePort());
		assertTrue(config.getSecurePortEnabled());
	}

}
