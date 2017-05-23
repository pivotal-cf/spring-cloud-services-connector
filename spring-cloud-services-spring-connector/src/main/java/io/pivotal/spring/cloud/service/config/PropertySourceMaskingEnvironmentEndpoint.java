package io.pivotal.spring.cloud.service.config;

import java.util.Map;

import org.springframework.boot.actuate.endpoint.EnvironmentEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.PatternMatchUtils;

/**
 * Custom environment endpoint (Actuator) that masks property values for given
 * property source name patterns. By default, no masking is performed.
 * 
 * The property source pattern names can be specified either through the
 * {@link #setSourceNamePatterns(String...)} method or by setting a
 * <code>endpoints.env.mask.source-name-patterns</code> property.
 * 
 * @author cwalls
 */
@ConfigurationProperties(prefix="endpoints.env.mask")
public class PropertySourceMaskingEnvironmentEndpoint extends EnvironmentEndpoint {

	private String[] sourceNamePatterns = {};
	
	/**
	 * Set patterns to apply against property source names that should be masked.
	 * Wildcards (*) can be used to apply masking to many similarly named property sources.
	 * For example, <code>"configService:*"</code> will mask all properties for any
	 * Spring Cloud Config Service property source. Similarly, <code>"*gi*ub*"</code>
	 * will mask all property sources that have "gi" and "ub" (including "github") in
	 * their name.
	 * 
	 * @param sourceNamePatterns One or more property source name patterns.
	 */
	public void setSourceNamePatterns(String... sourceNamePatterns) {
		this.sourceNamePatterns = sourceNamePatterns;
	}
	
	public String[] getSourceNamePatterns() {
		return sourceNamePatterns;
	}
	
	@Override
	protected Map<String, Object> postProcessSourceProperties(String sourceName, Map<String, Object> properties) {
		if(PatternMatchUtils.simpleMatch(sourceNamePatterns, sourceName)) {
			properties.replaceAll((key, value) -> "******");
		}
		return super.postProcessSourceProperties(sourceName, properties);
	}
	
}
