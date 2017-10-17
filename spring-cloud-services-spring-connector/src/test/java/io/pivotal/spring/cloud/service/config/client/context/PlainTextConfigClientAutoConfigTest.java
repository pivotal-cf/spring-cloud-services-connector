/*
 * Copyright 2017 the original author or authors.
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

package io.pivotal.spring.cloud.service.config.client.context;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import io.pivotal.spring.cloud.service.config.ConfigClientOAuth2ResourceDetails;
import io.pivotal.spring.cloud.service.config.PlainTextConfigClient;
import io.pivotal.spring.cloud.service.config.PlainTextConfigClientAutoConfiguration;

/**
 * @author Daniel Lavoie
 */
@SpringBootApplication
@RunWith(SpringRunner.class)
@Import({ ConfigClientOAuth2ResourceDetails.class,
		PlainTextConfigClientAutoConfiguration.class })
@SpringBootTest(classes = PlainTextConfigClientAutoConfigTest.class)
public class PlainTextConfigClientAutoConfigTest {
	@Autowired
	private PlainTextConfigClient plainTextConfigClient;

	@Test
	public void contextLoads() {
		Assert.assertNotNull(plainTextConfigClient);
	}
}
