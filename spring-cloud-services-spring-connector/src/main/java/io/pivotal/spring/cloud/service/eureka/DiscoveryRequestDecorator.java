package io.pivotal.spring.cloud.service.eureka;

import org.springframework.http.HttpHeaders;

public interface DiscoveryRequestDecorator {

	HttpHeaders getHeaders();
}
