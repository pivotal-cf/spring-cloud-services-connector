package io.pivotal.spring.cloud.service.eureka;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequestFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SurgicalRoutingRequestTransformerIntegrationTest.TestConfig.class, properties = {
		"vcap.application.uris[0]=www.route.local"
})
public class SurgicalRoutingRequestTransformerIntegrationTest {
	@Mock
	private HttpRequest originalRequest;
	@Mock
	private ClientHttpRequestExecution execution;
	@Mock
	private ServiceInstance instance;

	private byte[] body = new byte[] {};
	
	@Autowired
	private LoadBalancerRequestFactory lbReqFactory;

	@Test
	public void transformer() throws Exception {
		Map<String, String> metadata = new HashMap<>();
		metadata.put("cfAppGuid", "123");
		metadata.put("cfInstanceIndex", "4");
		HttpHeaders originalHeaders = new HttpHeaders();
		originalHeaders.add("foo", "bar");
		originalHeaders.add("foo", "baz");
		Mockito.when(instance.getMetadata()).thenReturn(metadata);
		Mockito.when(originalRequest.getHeaders()).thenReturn(originalHeaders);

		lbReqFactory.createRequest(originalRequest, body, execution).apply(instance);

		ArgumentCaptor<HttpRequest> httpRequestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
		verify(execution).execute(httpRequestCaptor.capture(), eq(body));
		HttpRequest transformedRequest = httpRequestCaptor.getValue();
		assertThat(transformedRequest.getHeaders().get("foo"), contains("bar", "baz"));
		assertEquals("123:4", transformedRequest.getHeaders().getFirst("X-CF-APP-INSTANCE"));
	}

	@SpringBootApplication
	@EnableDiscoveryClient
	static class TestConfig {

		@LoadBalanced
		@Bean
		public RestTemplate restTemplate() {
			return new RestTemplate();
		}
	}

}
