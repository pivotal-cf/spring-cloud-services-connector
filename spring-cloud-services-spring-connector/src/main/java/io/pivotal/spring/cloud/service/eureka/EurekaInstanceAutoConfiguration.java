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

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.cloud.util.InetUtils;
import org.springframework.cloud.util.InetUtilsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * Configuration class to configure a Eureka instance's settings based on the
 * value of <code>spring.cloud.services.registrationMethod</code>. "route" will
 * register vcap.application.uris[0] while "direct" will register CF_INSTANCE_IP
 * and CF_INSTANCE_PORT registration methods. The default behaviour is "route" <br>
 * <br>
 * Any defined eureka.instance.* property will override those set by this
 * auto-configuration.
 *
 * @author Chris Schaefer
 * @author Will Tran
 */
@Configuration
@ConditionalOnClass(EurekaInstanceConfigBean.class)
@ConditionalOnExpression("'${vcap.application.uris[0]:}'!='' || '${cf.instance.ip:}'!=''")
public class EurekaInstanceAutoConfiguration {
	private static Logger LOGGER = Logger.getLogger(EurekaInstanceAutoConfiguration.class.getName());

	private static final String ROUTE_REGISTRATION_METHOD = "route";
	private static final String DIRECT_REGISTRATION_METHOD = "direct";

	@Value("${vcap.application.uris[0]:}")
	private String hostname;

	@Value("${spring.application.name:unknown}")
	private String appname = "unknown";

	@Value("${cf.instance.ip:}")
	private String ip;

	@Value("${cf.instance.port:-1}")
	private int port;

	@Value("${vcap.application.instance_id:${random.value}}")
	private String instanceId;

	@Value("${spring.cloud.services.registrationMethod:route}")
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
		}

		return getDefaultRegistration();
	}

	private EurekaInstanceConfigBean getRouteRegistration() {
		EurekaInstanceConfigBean eurekaInstanceConfigBean = getDefaults();
		eurekaInstanceConfigBean.setSecurePortEnabled(true);
		eurekaInstanceConfigBean.setSecureVirtualHostName(appname);
		eurekaInstanceConfigBean.setInstanceId(hostname+":"+instanceId);
		return eurekaInstanceConfigBean;
	}

	private EurekaInstanceConfigBean getDirectRegistration() {
		EurekaInstanceConfigBean eurekaInstanceConfigBean = getDefaults();
		eurekaInstanceConfigBean.setNonSecurePort(port);
		eurekaInstanceConfigBean.setPreferIpAddress(true);
		eurekaInstanceConfigBean.setInstanceId(ip+":"+instanceId);
		return eurekaInstanceConfigBean;
	}

	private EurekaInstanceConfigBean getDefaults() {
		InetUtilsProperties inetUtilsProperties = new InetUtilsProperties();
		inetUtilsProperties.setDefaultHostname(hostname);
		inetUtilsProperties.setDefaultIpAddress(ip);
		EurekaInstanceConfigBean eurekaInstanceConfigBean = new EurekaInstanceConfigBean(new InetUtils(inetUtilsProperties));
		eurekaInstanceConfigBean.setHostname(hostname);
		eurekaInstanceConfigBean.setIpAddress(ip);
		return eurekaInstanceConfigBean;
	}

	private EurekaInstanceConfigBean getDefaultRegistration() {
		LOGGER.info("Eureka registration method not provided, defaulting to route");
		return getRouteRegistration();
	}

	void setHostname(String hostname) {
		this.hostname = hostname;
	}

	void setIp(String ip) {
		this.ip = ip;
	}

	void setPort(int port) {
		this.port = port;
	}

	void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	void setRegistrationMethod(String registrationMethod) {
		this.registrationMethod = registrationMethod;
	}

}
