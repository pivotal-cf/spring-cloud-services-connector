package io.pivotal.spring.cloud.service.config;

import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;


/**
 * Ensure client applications have `keys-to-sanitize` set so boot will automatically mask sensitive properties.
 * If client has manually set this property, merge it with any SCS specific keys that need to be sanitized
 *
 * @see <a href="https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html">
 * Spring Boot Common application properties</a>
 *
 * @author Ollie Hughes
 */
@ConditionalOnClass(ConfigServicePropertySourceLocator.class)
public class PropertyMaskingApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

	private static final String SANITIZE_ENV_KEY = "management.endpoint.env.keys-to-sanitize";
	private static final String SANITIZE_PROPS_KEY = "management.endpoint.configprops.keys-to-sanitize";
	private static final String VAULT_PROPERTY_MASKING_PATTERN = "configService.vault.*";

	@Override
	public void onApplicationEvent(@NonNull ApplicationEnvironmentPreparedEvent event) {
		ConfigurableEnvironment environment = event.getEnvironment();
		MutablePropertySources propertySources = environment.getPropertySources();

		String[] defaultKeys = {"password", "secret", "key", "token", ".*credentials.*", "vcap_services"};
		Set<String> propertiesToSanitize = Stream.of(defaultKeys)
												 .collect(Collectors.toSet());

		PropertiesPropertySource envKeysToSanitize =
				new PropertiesPropertySource(
						SANITIZE_ENV_KEY, mergeClientProperties(propertySources, propertiesToSanitize, SANITIZE_ENV_KEY));

		PropertiesPropertySource configPropsToSanitize =
				new PropertiesPropertySource(
						SANITIZE_PROPS_KEY, mergeClientProperties(propertySources, propertiesToSanitize, SANITIZE_PROPS_KEY));

		propertySources.addFirst(envKeysToSanitize);
		propertySources.addFirst(configPropsToSanitize);
	}

	private Properties mergeClientProperties(MutablePropertySources propertySources, Set<String> propertiesToSanitize, String propertyKey) {
		Properties props = new Properties();
		if(propertySources.contains(propertyKey)){
			String clientProperties = Objects.requireNonNull(propertySources.get(propertyKey)).toString();
			propertiesToSanitize.addAll(Stream.of(clientProperties.split(",")).collect(Collectors.toSet()));
		}
		propertiesToSanitize.add(VAULT_PROPERTY_MASKING_PATTERN);
		props.setProperty(propertyKey, StringUtils.arrayToCommaDelimitedString(propertiesToSanitize.toArray()));
		return props;
	}
}
