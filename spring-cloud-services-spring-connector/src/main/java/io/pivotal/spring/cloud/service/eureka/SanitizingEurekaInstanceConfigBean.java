/*
 * Copyright 2016 the original author or authors.
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

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;

import java.util.logging.Logger;

final class SanitizingEurekaInstanceConfigBean extends EurekaInstanceConfigBean implements InitializingBean {

	private static Logger LOGGER = Logger.getLogger(SanitizingEurekaInstanceConfigBean.class.getName());

	@Autowired
	private VirtualHostNamesBean virtualHostNamesBean;

	public SanitizingEurekaInstanceConfigBean(InetUtils inetUtils) {
		super(inetUtils);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		String providedEurekaAppname = this.getAppname();
		super.afterPropertiesSet();
		setVirtualHostName(determineVirtualHostName(this.virtualHostNamesBean.getVirtualHostName(), "", providedEurekaAppname));
		setSecureVirtualHostName(determineVirtualHostName(this.virtualHostNamesBean.getSecureVirtualHostName(), "secure ", providedEurekaAppname));
		if (providedEurekaAppname != "unknown") {
			setAppname(providedEurekaAppname);
		}
	}

	private String determineVirtualHostName(String providedVirtualHostname, String type, String providedEurekaAppname) {
		String result = providedVirtualHostname == null ? virtualHostnameFromSanitizedAppNames(type, providedEurekaAppname) : providedVirtualHostname;
		LOGGER.info("Determined " + type + "virtual hostname '" + result + "' from provided value '" + providedVirtualHostname + "' and provided Eureka appname'" + providedEurekaAppname + "'");
		return result;
	}

	private String virtualHostnameFromSanitizedAppNames(String type, String providedEurekaAppname) {
		String appName = providedEurekaAppname == "unknown" ? this.getAppname() : providedEurekaAppname;

		// RFC 952 defines the valid character set for hostnames.
		final String virtualHostname = appName.replaceAll("[^0-9a-zA-Z\\-\\.]", "-");

		if (!appName.equals(virtualHostname)) {
			// Log a warning since this sanitised virtual hostname could clash with the virtual hostname of other applications.
			LOGGER.warning("Application name '" + appName + "' was sanitised to produce " + type + "virtual hostname '" + virtualHostname + "'");
		}

		return virtualHostname;
	}

}
