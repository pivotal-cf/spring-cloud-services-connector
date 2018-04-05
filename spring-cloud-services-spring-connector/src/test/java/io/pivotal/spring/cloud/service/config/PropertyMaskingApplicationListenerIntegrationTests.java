package io.pivotal.spring.cloud.service.config;

import javax.swing.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.env.EnvironmentEndpoint;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.config.server.config.ConfigServerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import io.pivotal.spring.cloud.service.config.client.server.ConfigServerTestApplication;
import static org.assertj.core.api.Assertions.assertThat;

public class PropertyMaskingApplicationListenerIntegrationTests {

	@SpringBootApplication
	public static class TestVaultApplication {
		public static void main(String[] args) {
			SpringApplication application = new SpringApplication(TestVaultApplication.class);
			Map<String, Object> vaultProperties = new HashMap<>();
			vaultProperties.put("configService:vault:someTestValue", new Properties());
			application.setDefaultProperties(vaultProperties);
			application.run(args);
		}

	}

	@RunWith(SpringRunner.class)
	@SpringBootTest(classes = {TestVaultApplication.class},
			webEnvironment = WebEnvironment.RANDOM_PORT)
	@ActiveProfiles("integration-test,native")
	public static class TestVaultConfigClientProperties {

		@Autowired
		EnvironmentEndpoint environmentEndpoint;

		@Test
		public void nonVaultPropertyIsNotMasked() {
			EnvironmentEndpoint.PropertySummaryDescriptor vaultEntry = environmentEndpoint
					.environmentEntry("configService:notvault:game").getProperty();

			assertThat(vaultEntry)
					.withFailMessage("notvault property not found in environment")
					.isNotNull();

			assertThat(vaultEntry.getValue().toString())
					.withFailMessage("notvault property is masked when it shouldn't be")
					.isEqualTo("somevalue");
		}

		@Test
		public void passwordPropertyIsMasked() {
			EnvironmentEndpoint.PropertySummaryDescriptor vaultEntry = environmentEndpoint
					.environmentEntry("password").getProperty();

			assertThat(vaultEntry)
					.withFailMessage("Password property not found in environment")
					.isNotNull();

			assertThat(vaultEntry.getValue().toString())
					.withFailMessage("Password property not masked")
					.isEqualTo("******");
		}

		@Test
		public void vaultPropertyIsMasked() {
			EnvironmentEndpoint.PropertySummaryDescriptor vaultEntry = environmentEndpoint
					.environmentEntry("configService:vault:game").getProperty();

			assertThat(vaultEntry)
					.withFailMessage("Vault property not found in environment")
					.isNotNull();

			assertThat(vaultEntry.getValue().toString())
					.withFailMessage("Vault property not masked")
					.isEqualTo("******");
		}

	}
}