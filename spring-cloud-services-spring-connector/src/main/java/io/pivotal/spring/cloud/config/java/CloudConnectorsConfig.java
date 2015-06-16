package io.pivotal.spring.cloud.config.java;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cloud.config.java.AbstractCloudConfig;

public class CloudConnectorsConfig extends AbstractCloudConfig {
	private PivotalServiceConnectionFactory connectionFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		super.setBeanFactory(beanFactory);
		this.connectionFactory = new PivotalCloudServiceConnectionFactory(this, cloud());
	}

	@Override
	public PivotalServiceConnectionFactory connectionFactory() {
		return connectionFactory;
	}
}
