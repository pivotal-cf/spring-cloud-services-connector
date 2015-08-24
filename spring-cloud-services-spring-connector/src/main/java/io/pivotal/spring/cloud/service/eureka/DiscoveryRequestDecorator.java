package io.pivotal.spring.cloud.service.eureka;

import org.springframework.http.HttpHeaders;

/**
 * 
 * @author Will Tran
 *
 */
public interface DiscoveryRequestDecorator {

	HttpHeaders getHeaders();
}
