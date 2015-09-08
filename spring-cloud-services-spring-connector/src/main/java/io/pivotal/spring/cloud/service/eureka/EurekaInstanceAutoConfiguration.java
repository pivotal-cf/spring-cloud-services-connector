package io.pivotal.spring.cloud.service.eureka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.logging.Logger;

/**
 * Configuration class to configure a Eureka instance's settings based
 * on route, direct or custom registration methods. Route and direct
 * use predefined CloudFoundry variables for configuration, custom allows
 * the use of any eureka.instance.* properties. If no configuration is
 * present, the default behavior is route.
 *
 * @author Chris Schaefer
 */
@Configuration
@ConfigurationProperties("spring.cloud.services")
@ConditionalOnClass(EurekaInstanceConfigBean.class)
public class EurekaInstanceAutoConfiguration {
	private static Logger LOGGER = Logger.getLogger(EurekaInstanceAutoConfiguration.class.getName());

	private static final int ROUTE_NON_SECURE_PORT = 80;
	private static final String ROUTE_REGISTRATION_METHOD = "route";
	private static final String DIRECT_REGISTRATION_METHOD = "direct";
	private static final String CUSTOM_REGISTRATION_METHOD = "custom";

	@Value("${vcap.application.uris[0]:localhost}")
	private String routeHostName;

	@Value("${cf.instance.ip:localhost}")
	private String directHostName;

	@Value("${cf.instance.port:80}")
	private int directPort;

	@Value("${spring.application.name:unknown}")
	private String appname = "unknown";

	@Value("${spring.application.name:unknown}")
	private String virtualHostName;

	private String registrationMethod;

	@Bean
	public EurekaInstanceConfigBean eurekaInstanceConfigBean() {
		if(!StringUtils.isEmpty(registrationMethod)) {
			LOGGER.info("Eureka registration method: " + registrationMethod);

			if(ROUTE_REGISTRATION_METHOD.equals(registrationMethod)) {
				return getRouteRegistration();
			}

			if(DIRECT_REGISTRATION_METHOD.equals(registrationMethod)) {
				return getDirectRegistration();
			}

			if(CUSTOM_REGISTRATION_METHOD.equals(registrationMethod)) {
				return getCustomRegistration();
			}
		}

		return getDefaultRegistration();
	}

	public void setRegistrationMethod(String registrationMethod) {
		this.registrationMethod = registrationMethod;
	}

	protected void setRouteHostName(String routeHostName) {
		this.routeHostName = routeHostName;
	}

	protected void setDirectHostName(String directHostName) {
		this.directHostName = directHostName;
	}

	protected void setDirectPort(int directPort) {
		this.directPort = directPort;
	}

	private EurekaInstanceConfigBean getRouteRegistration() {
		return getEurekaInstanceConfigBean(routeHostName, ROUTE_NON_SECURE_PORT);
	}

	private EurekaInstanceConfigBean getDirectRegistration() {
		return getEurekaInstanceConfigBean(directHostName, directPort);
	}

	private EurekaInstanceConfigBean getCustomRegistration() {
		return new EurekaInstanceConfigBean();
	}

	private EurekaInstanceConfigBean getDefaultRegistration() {
		LOGGER.info("Eureka registration method not provided, defaulting to route");
		return getRouteRegistration();
	}

	private EurekaInstanceConfigBean getEurekaInstanceConfigBean(String hostname, int port) {
		EurekaInstanceConfigBean eurekaInstanceConfigBean = new EurekaInstanceConfigBean();
		eurekaInstanceConfigBean.setHostname(hostname);
		eurekaInstanceConfigBean.setNonSecurePort(port);
		eurekaInstanceConfigBean.setAppname(appname);
		eurekaInstanceConfigBean.setVirtualHostName(virtualHostName);

		return eurekaInstanceConfigBean;
	}
}
