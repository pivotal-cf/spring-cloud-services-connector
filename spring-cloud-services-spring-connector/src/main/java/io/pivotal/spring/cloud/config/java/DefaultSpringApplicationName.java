package io.pivotal.spring.cloud.config.java;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.CloudFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

/**
 * Sets the spring.application.name to vcap.application.name in a property
 * source with lower precedence than any explicit configuration.
 *
 * @author Will Tran
 */
@Configuration
public class DefaultSpringApplicationName implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {

	private Cloud cloud;

	@Override
	public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
		if (cloud != null) {
			return;
		}

		try {
			cloud = new CloudFactory().getCloud();
		} catch (CloudException e) {
			// not running on a known cloud environment, so nothing to do
			return;
		}

		ConfigurableEnvironment environment = event.getEnvironment();
		String vcapApplicationName = environment.getProperty("vcap.application.name");
		if (vcapApplicationName != null) {
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put("spring.application.name", vcapApplicationName);
			MapPropertySource propertySource = new MapPropertySource("defaultSpringApplicationName", properties);
			MutablePropertySources propertySources = environment.getPropertySources();
			propertySources.addLast(propertySource);

			PropertySource<?> defaultProperties = environment.getPropertySources()
					.remove("defaultProperties");
			if (defaultProperties != null) {
				environment.getPropertySources().addLast(defaultProperties);
			}
		}
	}

	@Override
	public int getOrder() {
		// run this after VcapApplicationListener and ConfigFileApplicationListener.
		// We want to add this property source with precedence lower than those 
		// added by ConfigFileApplicationListener
		return ConfigFileApplicationListener.DEFAULT_ORDER + 1;
	}
}
