/*
 * Copyright 2017-2019 the original author or authors.
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

package io.pivotal.spring.cloud.service.config.client.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import io.pivotal.spring.cloud.service.config.ConfigClientOAuth2ResourceDetails;
import io.pivotal.spring.cloud.service.config.ConfigResourceClient;
import io.pivotal.spring.cloud.service.config.ConfigResourceClientAutoConfiguration;

/**
 * @author Anshul Mehra
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConfigServerTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
		"spring.profiles.active=vault,plaintext,native", "spring.cloud.config.enabled=true", "eureka.client.enabled=false",
		"spring.cloud.config.client.oauth2.client-id=acme"})
public class Oauth2ConfigResourceClientTokenForwardingTest {

	@LocalServerPort
	private int port;

	@Autowired
	private ConfigClientOAuth2ResourceDetails resource;

	@Autowired
	private ConfigClientProperties configClientProperties;

	private ConfigurableApplicationContext vaultServer;

	@Before
	public void setup() {
		vaultServer = new SpringApplicationBuilder(VaultServer.class).web(WebApplicationType.SERVLET)
				.run("--server.port=8200", "--eureka.client.enabled=false", "--security.ignored=/**");
		resource.setAccessTokenUri("http://localhost:" + port + "/oauth/token");
		configClientProperties.setName("app");
		configClientProperties.setProfile(null);
		configClientProperties.setUri(new String[]{"http://localhost:" + port});
	}

	@After
	public void teardown() {
		vaultServer.close();
	}

	@Test
	public void requestGoesThroughWhenConfigServerTokenSet() {
		configClientProperties.setToken("vaultToken");
		ConfigResourceClient configClient = new ConfigResourceClientAutoConfiguration()
				.configResourceClient(resource, configClientProperties);
		configClient.getPlainTextResource(null, null, "nginx.conf");
	}

	@Test(expected = HttpClientErrorException.BadRequest.class)
	public void badRequestWhenConfigServerTokenNotSet() {
		configClientProperties.setToken(null);
		ConfigResourceClient configClient = new ConfigResourceClientAutoConfiguration()
				.configResourceClient(resource, configClientProperties);
		configClient.getPlainTextResource(null, null, "nginx.conf");
	}

	@Configuration
	@EnableWebSecurity
	@RestController
	@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class})
	public static class VaultServer extends WebSecurityConfigurerAdapter {
		@GetMapping("v1/secret/{key}")
		public ResponseEntity getKey(@PathVariable("key") String key) {
			return ResponseEntity.notFound().build();
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()
					.antMatchers("/**").permitAll();
		}
	}
}
