package io.pivotal.spring.cloud.service.config;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.env.EnvironmentEndpoint;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class VaultPropertyMaskingContextInitializerIntegrationTests {

	private static final String VAULT_TEST_SANITIZE_PROPERTY = "MyHiddenData";
	private static final String GIT_TEST_NON_SANITIZE_PROPERTY = "ReadableProperty";

	@SpringBootApplication
	public static class TestVaultApplication {}

	public static class VaultPropertySourceContextLoader extends SpringBootContextLoader {

		@Override
		protected ConfigurableEnvironment getEnvironment() {
			//Bootstrap properties contain all the Config Server properties
			CompositePropertySource bootstrapPropSource = new CompositePropertySource("bootstrapProperties");

			//Add vault properties that will be masked
			String vaultPropertySourceName = "configService";
			CompositePropertySource compositeProps = new CompositePropertySource(vaultPropertySourceName);
			Map<String, Object> fakeVaultProperties = new HashMap<>();
			fakeVaultProperties.put(VaultPropertyMaskingContextInitializerIntegrationTests.VAULT_TEST_SANITIZE_PROPERTY, "SecretValue");
			compositeProps.addPropertySource(new MapPropertySource("vault:test-data", fakeVaultProperties));

			//Add Git properties that will not be masked (except the my-password which is part of the default sainitze keys)
			Map<String, Object> fakeGitProperties = new HashMap<>();
			fakeGitProperties.put(GIT_TEST_NON_SANITIZE_PROPERTY, "ReadableValue");
			fakeGitProperties.put("my-password", "supersecret");
			compositeProps.addPropertySource(new MapPropertySource("git:test-data", fakeGitProperties));

			bootstrapPropSource.addPropertySource(compositeProps);
			StandardEnvironment environment = new StandardEnvironment();
			environment.getPropertySources().addFirst(bootstrapPropSource);
			return environment;
		}
	}

	@RunWith(SpringRunner.class)
	@SpringBootTest(classes = {VaultPropertyMaskingContextInitializerIntegrationTests.TestVaultApplication.class},
			webEnvironment = WebEnvironment.RANDOM_PORT)
	@ActiveProfiles("integration-test,native")
	@ContextConfiguration(classes = VaultPropertyMaskingContextInitializerIntegrationTests.TestVaultApplication.class,
			loader = VaultPropertyMaskingContextInitializerIntegrationTests.VaultPropertySourceContextLoader.class)
	public static class TestVaultConfigClientProperties {

		@Autowired
		EnvironmentEndpoint environmentEndpoint;

		@Test
		public void vaultPropertyIsIncludedInSantizeEndpoints() {
			EnvironmentEndpoint.PropertySummaryDescriptor sanitizeEndpointsProp = environmentEndpoint
					.environmentEntry(VaultPropertyMaskingContextInitializer.SANITIZE_ENV_KEY).getProperty();

			assertThat(sanitizeEndpointsProp)
					.withFailMessage("Sanitize endpoints property not found in environment")
					.isNotNull();

			assertThat(sanitizeEndpointsProp.getValue().toString())
					.withFailMessage("Sanitize endpoints property not equal to " + sanitizeEndpointsProp.getValue())
					.contains(VAULT_TEST_SANITIZE_PROPERTY);
		}

		@Test
		public void gitPropertyIsNotIncludedInSantizeEndpoints() {
			EnvironmentEndpoint.PropertySummaryDescriptor sanitizeEndpointsProp = environmentEndpoint
					.environmentEntry(VaultPropertyMaskingContextInitializer.SANITIZE_ENV_KEY).getProperty();

			assertThat(sanitizeEndpointsProp)
					.withFailMessage("Sanitize endpoints property not found in environment")
					.isNotNull();

			assertThat(sanitizeEndpointsProp.getValue().toString())
					.withFailMessage("Sanitize endpoints property not equal to " + sanitizeEndpointsProp.getValue())
					.doesNotContain(GIT_TEST_NON_SANITIZE_PROPERTY);
		}

	}
}