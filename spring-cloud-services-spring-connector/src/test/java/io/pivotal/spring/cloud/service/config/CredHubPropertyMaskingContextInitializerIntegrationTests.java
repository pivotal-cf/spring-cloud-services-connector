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

public class CredHubPropertyMaskingContextInitializerIntegrationTests {

	private static final String CREDHUB_TEST_SANITIZE_PROPERTY = "MyHiddenData";
	private static final String GIT_TEST_NON_SANITIZE_PROPERTY = "ReadableProperty";

	@SpringBootApplication
	public static class TestCredhubApplication {}

	public static class CredHubPropertySourceContextLoader extends SpringBootContextLoader {

		@Override
		protected ConfigurableEnvironment getEnvironment() {
			//Bootstrap properties contain all the Config Server properties
			CompositePropertySource bootstrapPropSource = new CompositePropertySource("bootstrapProperties");

			//Add credhub properties that will be masked
			String credhubPropertySourceName = "configService";
			CompositePropertySource compositeProps = new CompositePropertySource(credhubPropertySourceName);
			Map<String, Object> fakeCredhubProperties = new HashMap<>();
			fakeCredhubProperties.put(CredHubPropertyMaskingContextInitializerIntegrationTests.CREDHUB_TEST_SANITIZE_PROPERTY, "SecretValue");
			compositeProps.addPropertySource(new MapPropertySource("credhub-test-data", fakeCredhubProperties));

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
	@SpringBootTest(classes = {CredHubPropertyMaskingContextInitializerIntegrationTests.TestCredhubApplication.class},
			webEnvironment = WebEnvironment.RANDOM_PORT)
	@ActiveProfiles("integration-test,native")
	@ContextConfiguration(classes = CredHubPropertyMaskingContextInitializerIntegrationTests.TestCredhubApplication.class,
			loader = CredHubPropertyMaskingContextInitializerIntegrationTests.CredHubPropertySourceContextLoader.class)
	public static class TestCredhubConfigClientProperties {

		@Autowired
		EnvironmentEndpoint environmentEndpoint;

		@Test
		public void credhubPropertyIsIncludedInSantizeEndpoints() {
			EnvironmentEndpoint.PropertySummaryDescriptor sanitizeEndpointsProp = environmentEndpoint
					.environmentEntry(PropertyMaskingContextInitializer.SANITIZE_ENV_KEY).getProperty();

			assertThat(sanitizeEndpointsProp)
					.withFailMessage("Sanitize endpoints property not found in environment")
					.isNotNull();

			assertThat(sanitizeEndpointsProp.getValue().toString())
					.withFailMessage("Sanitize endpoints property not equal to " + sanitizeEndpointsProp.getValue())
					.contains(CREDHUB_TEST_SANITIZE_PROPERTY);
		}

		@Test
		public void gitPropertyIsNotIncludedInSantizeEndpoints() {
			EnvironmentEndpoint.PropertySummaryDescriptor sanitizeEndpointsProp = environmentEndpoint
					.environmentEntry(PropertyMaskingContextInitializer.SANITIZE_ENV_KEY).getProperty();

			assertThat(sanitizeEndpointsProp)
					.withFailMessage("Sanitize endpoints property not found in environment")
					.isNotNull();

			assertThat(sanitizeEndpointsProp.getValue().toString())
					.withFailMessage("Sanitize endpoints property not equal to " + sanitizeEndpointsProp.getValue())
					.doesNotContain(GIT_TEST_NON_SANITIZE_PROPERTY);
		}

	}
}