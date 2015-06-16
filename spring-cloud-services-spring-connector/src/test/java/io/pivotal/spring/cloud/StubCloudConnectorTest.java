package io.pivotal.spring.cloud;

import io.pivotal.spring.cloud.service.common.EurekaServiceInfo;
import io.pivotal.spring.cloud.service.common.HystrixAmqpServiceInfo;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.cloud.CloudConnector;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.CloudTestUtil;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

abstract public class StubCloudConnectorTest {
	private static final String MOCK_CLOUD_BEAN_NAME = "mockCloud";

	protected ApplicationContext getTestApplicationContext(Class<?> configClass, ServiceInfo... serviceInfos) {
		final CloudConnector stubCloudConnector = CloudTestUtil.getTestCloudConnector(serviceInfos);

		return new AnnotationConfigApplicationContext(configClass) {
			@Override
			protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
				CloudFactory cloudFactory = new CloudFactory();
				cloudFactory.registerCloudConnector(stubCloudConnector);
				getBeanFactory().registerSingleton(MOCK_CLOUD_BEAN_NAME, cloudFactory);
				super.prepareBeanFactory(beanFactory);
			}
		};
	}

	protected EurekaServiceInfo createEurekaService(String id) {
		return new EurekaServiceInfo(id, "http://username:password@10.20.30.40:1234");
	}

	protected HystrixAmqpServiceInfo createHystrixAmqpService(String id) {
		return new HystrixAmqpServiceInfo(id, "amqp://username:password@10.20.30.40:1234/vh");
	}
}