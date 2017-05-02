package io.pivotal.spring.cloud.config.java;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

/**
 * Transforms a {@link ServiceInfo} into a {@link PropertySource} with highest precedence
 *
 * @param <T> the {@link ServiceInfo} type
 * @author Scott Frederick
 * @author Will Tran
 * @see <a href=
 * "http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config">
 * http://docs.spring.io/spring-boot/docs/current/reference/html/boot-
 * features-external-config.html#boot-features-external-config</a>
 */
public abstract class ServiceInfoPropertySourceAdapter<T extends ServiceInfo>
		implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {

	public static final String SPRING_AUTOCONFIGURE_EXCLUDE = "spring.autoconfigure.exclude";

	private static final String RABBIT_AUTOCONFIG_CLASS = "org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration";

	private ConfigurableEnvironment environment;

	private Cloud cloud;

	protected abstract PropertySource<?> toPropertySource(T serviceInfo);

	@SuppressWarnings("unchecked")
	@Override
	public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
		environment = event.getEnvironment();

		if (cloud != null) {
			return;
		}

		try {
			cloud = new CloudFactory().getCloud();
		} catch (CloudException e) {
			// not running on a known cloud environment, so nothing to do
			return;
		}

		for (ServiceInfo serviceInfo : cloud.getServiceInfos()) {
			try {
				PropertySource<?> propertySource = toPropertySource((T) serviceInfo);
				event.getEnvironment().getPropertySources().addFirst(propertySource);
			} catch (ClassCastException e) {
				// non-matching ServiceInfo
			}
		}

		conditionallyExcludeRabbitAutoConfiguration();
	}

	private void conditionallyExcludeRabbitAutoConfiguration() {
		if (appIsBoundToRabbitMQ()) {
			return;
		}

		Map<String, Object> properties = new LinkedHashMap<>();
		String existingExcludes = environment.getProperty(SPRING_AUTOCONFIGURE_EXCLUDE);
		if (existingExcludes == null) {
			properties.put(SPRING_AUTOCONFIGURE_EXCLUDE, RABBIT_AUTOCONFIG_CLASS);
		} else if (!existingExcludes.contains(RABBIT_AUTOCONFIG_CLASS)) {
			properties.put(SPRING_AUTOCONFIGURE_EXCLUDE, RABBIT_AUTOCONFIG_CLASS + "," + existingExcludes);
		}

		PropertySource<?> propertySource = new MapPropertySource("springCloudServicesRabbitAutoconfigExcluder",
				properties);
		environment.getPropertySources().addFirst(propertySource);
	}

	private boolean appIsBoundToRabbitMQ() {
		return environment.containsProperty("spring.rabbitmq.host") &&
				!environment.getProperty("spring.rabbitmq.host").equals("localhost");
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 4;
	}
}
