package io.pivotal.spring.cloud.service.eureka;

import io.pivotal.spring.cloud.TestApplication;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author Roy Clarkson
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplication.class)
@IntegrationTest("server.port=0")
public class EurekaAutoConfigLocalIntegrationTest {

	@Autowired
	private ApplicationContext context;

	@Test
	public void eurekaConfigBean() throws Exception {
		final EurekaInstanceConfigBean config = context
				.getBean(EurekaInstanceConfigBean.class);
		assertEquals(0, config.getNonSecurePort());
	}

}
