package org.springframework.cloud.pivotal.service.common;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.cloud.service.ServiceInfo.ServiceLabel;

/**
 * 
 * Information to access Gemfire services
 * 
 * @author Vinicius Carvalho
 *
 */
@ServiceLabel("gemfire")
public class GemfireServiceInfo extends BaseServiceInfo {
	final Pattern p = Pattern.compile("(.*)\\[(\\d*)\\]");
	private URI[] locators;
	private Map<String, Object> credentials;

	public GemfireServiceInfo(String id, Map<String, Object> credentials) {
		super(id);
		this.credentials = credentials;
		List<String> locatorList = (List<String>) credentials.get("locators");
		this.locators = new URI[locatorList.size()];
		for (int i = 0; i < locatorList.size(); i++) {
			locators[i] = parseLocators(locatorList.get(i));
		}
	}

	URI parseLocators(String locator) throws IllegalArgumentException {
		Matcher m = p.matcher(locator);
		URI uri = null;
		if (!m.find()) {
			throw new IllegalArgumentException("Could not parse locator url. Expected format host[port], received: " + locator);
		} else {
			if (m.groupCount() != 2) {
				throw new IllegalArgumentException("Could not parse locator url. Expected format host[port], received: " + locator);
			}
			try {
				uri = new URI("locator://" + m.group(1) + ":" + m.group(2));
			} catch (URISyntaxException e) {
				throw new IllegalArgumentException("Malformed URL " + locator);
			}
		}
		return uri;
	}

	public URI[] getLocators() {
		return locators;
	}

	public String getUsername() {
		return credentials.get("username") == null ? null : String.valueOf(credentials.get("username"));
	}

	public String getPassword() {
		return credentials.get("password") == null ? null : String.valueOf(credentials.get("password"));
	}
}
