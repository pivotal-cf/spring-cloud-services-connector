package org.springframework.cloud;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.cloud.pivotal.service.common.EurekaServiceInfo;
import org.springframework.cloud.pivotal.service.common.HystrixAmqpServiceInfo;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

abstract public class StubCloudConnectorTest {
	private static final String MOCK_CLOUD_BEAN_NAME = "mockCloud";

	protected ApplicationContext getTestApplicationContext(String fileName, ServiceInfo... serviceInfos) {
		final CloudConnector stubCloudConnector = CloudTestUtil.getTestCloudConnector(serviceInfos);

		return new ClassPathXmlApplicationContext(getClass().getPackage().getName().replaceAll("\\.", "/") + "/" + fileName) {
			@Override
			protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
				CloudFactory cloudFactory = new CloudFactory();
				cloudFactory.getCloudConnectors().clear();
				cloudFactory.registerCloudConnector(stubCloudConnector);
				getBeanFactory().registerSingleton(MOCK_CLOUD_BEAN_NAME, cloudFactory);
				super.prepareBeanFactory(beanFactory);
			}
		};
	}

	protected ApplicationContext getTestApplicationContext(Class<?> configClass, ServiceInfo... serviceInfos) {
		final CloudConnector stubCloudConnector = CloudTestUtil.getTestCloudConnector(serviceInfos);

		return new AnnotationConfigApplicationContext(configClass) {
			@Override
			protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
				CloudFactory cloudFactory = new CloudFactory();
				cloudFactory.getCloudConnectors().clear();
				cloudFactory.registerCloudConnector(stubCloudConnector);
				getBeanFactory().registerSingleton(MOCK_CLOUD_BEAN_NAME, cloudFactory);
				super.prepareBeanFactory(beanFactory);
			}
		};
	}

	protected EurekaServiceInfo creatEurekaService(String id) {
		return new EurekaServiceInfo(id, "http://username:password@10.20.30.40:1234");
	}

	protected HystrixAmqpServiceInfo creatHystrixAmqpService(String id) {
		return new HystrixAmqpServiceInfo(id, "amqp://username:password@10.20.30.40:1234/vh");
	}
}