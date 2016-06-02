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

import org.junit.Before;
import org.junit.Test;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Test cases for
 * {@link io.pivotal.spring.cloud.service.eureka.EurekaInstanceConfigBeanOverride}
 */
public class EurekaInstanceConfigBeanOverrideTest {

    public static final String VIRTUAL_HOSTNAME = "virtual-hostname";
    public static final String SECURE_VIRTUAL_HOSTNAME = "secure-virtual-hostname";
    public static final String NEW_VIRTUAL_HOSTNAME = "new-virtual-hostname";

    private InetUtils mockInetutils;
    private EurekaInstanceConfigBeanOverride override;

    @Before
    public void setup() {
        this.mockInetutils = new InetUtils(mock(InetUtilsProperties.class));
        this.override = new EurekaInstanceConfigBeanOverride(this.mockInetutils, VIRTUAL_HOSTNAME, SECURE_VIRTUAL_HOSTNAME);
    }

    @Test
    public void testConstructor() {
        assertEquals(VIRTUAL_HOSTNAME, override.getVirtualHostName());
        assertEquals(SECURE_VIRTUAL_HOSTNAME, override.getSecureVirtualHostName());
        assertEquals("unknown", override.getAppname());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetVirtualHostname() {
        override.setVirtualHostName(NEW_VIRTUAL_HOSTNAME);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetSecureVirtualHostname() {
        override.setSecureVirtualHostName(NEW_VIRTUAL_HOSTNAME);
    }
}
