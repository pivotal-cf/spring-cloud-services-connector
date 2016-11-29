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

import org.junit.After;
import org.junit.Test;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.EnvironmentTestUtils;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test cases for
 * {@link SanitizingEurekaInstanceConfigBean}
 */
public class SanitizingEurekaInstanceConfigBeanTest {

    AnnotationConfigApplicationContext ctx;

    @After
    public void tearDown() {
        this.ctx.close();
    }

    @Test
    public void testProvidedVirtualHostName() {
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("eureka.instance.virtualHostName:vhn");
        assertEquals("vhn", bean.getVirtualHostName());
    }

   @Test
    public void testProvidedVirtualHostNameDashed() {
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("eureka.instance.virtual-host-name:vhn");
        assertEquals("vhn", bean.getVirtualHostName());
    }

  @Test
    public void testProvidedVirtualHostNameUnderscored() {
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("eureka.instance.virtual_host_name:vhn");
        assertEquals("vhn", bean.getVirtualHostName());
    }

    @Test
    public void testProvidedSecureVirtualHostName() {
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("eureka.instance.secureVirtualHostName:svhn");
        assertEquals("svhn", bean.getSecureVirtualHostName());
    }

    @Test
    public void testProvidedSecureVirtualHostNameDashed() {
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("eureka.instance.secure-virtual-host-name:svhn");
        assertEquals("svhn", bean.getSecureVirtualHostName());
    }

   @Test
    public void testProvidedSecureVirtualHostNameUnderscored() {
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("eureka.instance.secure_virtual_host_name:svhn");
        assertEquals("svhn", bean.getSecureVirtualHostName());
    }

    @Test
    public void testProvidedVirtualHostNameIsRespected() {
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("eureka.instance.virtualHostName:vhn", "spring.application.name:san");
        assertEquals("vhn", bean.getVirtualHostName());
    }

    @Test
    public void testProvidedVirtualHostNameDashedIsRespected() {
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("eureka.instance.virtual-host-name:vhn", "spring.application.name:san");
        assertEquals("vhn", bean.getVirtualHostName());
    }

    @Test
    public void testProvidedVirtualHostNameUnderscoredIsRespected() {
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("eureka.instance.virtual_host_name:vhn", "spring.application.name:san");
        assertEquals("vhn", bean.getVirtualHostName());
    }

    @Test
    public void testProvidedSecureVirtualHostNameIsRespected() {
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("eureka.instance.secureVirtualHostName:svhn", "spring.application.name:san");
        assertEquals("svhn", bean.getSecureVirtualHostName());
    }

    @Test
    public void testProvidedSecureVirtualHostNameDashedIsRespected() {
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("eureka.instance.secure-virtual-host-name:svhn", "spring.application.name:san");
        assertEquals("svhn", bean.getSecureVirtualHostName());
    }

    @Test
    public void testProvidedSecureVirtualHostNameUnderscoredIsRespected() {
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("eureka.instance.secure_virtual_host_name:svhn", "spring.application.name:san");
        assertEquals("svhn", bean.getSecureVirtualHostName());
    }

    @Test
    public void testProvidedVirtualHostNameIsNotSanitised() {
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("eureka.instance.virtualHostName:v_hn");
        assertEquals("v_hn", bean.getVirtualHostName());
    }

   @Test
    public void testProvidedVirtualHostNameDashedIsNotSanitised() {
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("eureka.instance.virtual-host-name:v_hn");
        assertEquals("v_hn", bean.getVirtualHostName());
    }

   @Test
    public void testProvidedVirtualHostNameUnderscoredIsNotSanitised() {
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("eureka.instance.virtual_host_name:v_hn");
        assertEquals("v_hn", bean.getVirtualHostName());
    }

    @Test
    public void testProvidedSecureVirtualHostNameIsNotSanitised() {
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("eureka.instance.secureVirtualHostName:sv_hn");
        assertEquals("sv_hn", bean.getSecureVirtualHostName());
    }

    @Test
    public void testProvidedSecureVirtualHostNameDashedIsNotSanitised() {
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("eureka.instance.secure-virtual-host-name:sv_hn");
        assertEquals("sv_hn", bean.getSecureVirtualHostName());
    }

    @Test
    public void testProvidedSecureVirtualHostNameUnderscoredIsNotSanitised() {
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("eureka.instance.secure_virtual_host_name:sv_hn");
        assertEquals("sv_hn", bean.getSecureVirtualHostName());
    }

    @Test
    public void testProvidedVirtualHostNameIsNotSanitisedWhenEqualToApplicationName() {
        // Test for an implementation which attempts to check for a provided virtual hostname by comparing the virtual hostname with the application name.
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("eureka.instance.virtualHostName:some_name", "spring.application.name:some_name",
                "eureka.instance.secureVirtualHostName:svhn"); // prevent confusing warning log
        assertEquals("some_name", bean.getVirtualHostName());
    }

    @Test
    public void testProvidedSecureVirtualHostNameIsNotSanitisedWhenEqualToApplicationName() {
        // Test for an implementation which attempts to check for a provided secure virtual hostname by comparing the secure virtual hostname with the application name.
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("eureka.instance.secureVirtualHostName:some_name", "spring.application.name:some_name",
                "eureka.instance.virtualHostName:vhn"); // prevent confusing warning log
        assertEquals("some_name", bean.getSecureVirtualHostName());
    }

    @Test
    public void testVirtualHostNameDefaultsToApplicationName() {
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("spring.application.name:san");
        assertEquals("san", bean.getVirtualHostName());
    }

    @Test
    public void testVirtualHostNameDefaultsToEurekaApplicationName() {
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("spring.application.name:san", "eureka.instance.appname:ean");
        assertEquals("ean", bean.getVirtualHostName());
    }

    @Test
    public void testSecureVirtualHostNameDefaultsToApplicationName() {
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("spring.application.name:san");
        assertEquals("san", bean.getSecureVirtualHostName());
    }

    @Test
    public void testSecureVirtualHostNameDefaultsToEurekaApplicationName() {
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("spring.application.name:san", "eureka.instance.appname:ean");
        assertEquals("ean", bean.getSecureVirtualHostName());
    }

    @Test
    public void testDefaultVirtualHostNameIsSanitised() {
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("spring.application.name:s_an");
        assertEquals("s-an", bean.getVirtualHostName());
    }

    @Test
    public void testDefaultSecureVirtualHostNameIsSanitised() {
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("spring.application.name:s_an");
        assertEquals("s-an", bean.getSecureVirtualHostName());
    }

    @Test
    public void testApplicationNameIsNotSanitised() {
        // Virtual hostnames should be sanitised without affecting the application name.
        SanitizingEurekaInstanceConfigBean bean = createBeanWithProps("spring.application.name:s_an");
        assertEquals("s_an", bean.getAppname());
    }

    private SanitizingEurekaInstanceConfigBean createBeanWithProps(String... pairs) {
        this.ctx = new AnnotationConfigApplicationContext();

        ArrayList<String> pairs1 = new ArrayList();

        for (String pair : pairs) {
            pairs1.add(pair);
        }
        pairs1.add("sanitizingEurekaInstanceConfigBean.integration.test:true");
        EnvironmentTestUtils.addEnvironment(ctx, pairs1.toArray(new String[pairs1.size()]));
        this.ctx.register(Context.class);
        this.ctx.refresh();

        return this.ctx.getBean(SanitizingEurekaInstanceConfigBean.class);
    }

    @Configuration
    @ComponentScan
    @ConditionalOnProperty(value = "sanitizingEurekaInstanceConfigBean.integration.test")
    @EnableConfigurationProperties
    public static class Context {

        @Bean
        public static PropertySourcesPlaceholderConfigurer getPropertySourcesPlaceholderConfigurer() {
            return new PropertySourcesPlaceholderConfigurer();
        }

        @Bean
	    public VirtualHostNamesBean getVirtualHostNamesBean() {
		    return new VirtualHostNamesBean();
	    }

        @Bean
        public SanitizingEurekaInstanceConfigBean getSanitizingEurekaInstanceConfigBean() {
            return new SanitizingEurekaInstanceConfigBean(getInetUtils());
        }

        private InetUtils getInetUtils() {

            InetUtils.HostInfo hostInfo = new InetUtils(new InetUtilsProperties()).findFirstNonLoopbackHostInfo();

            InetUtils inetUtils = mock(InetUtils.class);
            when(inetUtils.findFirstNonLoopbackHostInfo()).thenReturn(hostInfo);

            return inetUtils;
        }

    }
}
