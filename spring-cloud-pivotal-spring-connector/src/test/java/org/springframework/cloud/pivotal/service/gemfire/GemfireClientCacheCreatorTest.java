package org.springframework.cloud.pivotal.service.gemfire;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.cloud.pivotal.service.common.GemfireServiceInfo;

import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
public class GemfireClientCacheCreatorTest {
	
	
	@Test
	public void testcacheCreation() throws Exception {
		ClientCacheFactory factory = mock(ClientCacheFactory.class);
		ClientCache cache = mock(ClientCache.class);
		Map<String,Object> credentials = new HashMap<String, Object>();
		credentials.put("locators", Collections.singletonList("localhost[1234]"));
		GemfireServiceInfo info = new GemfireServiceInfo("gemfire", credentials);
		when(factory.create()).thenReturn(cache);
		GemfireClientCacheCreator creator = new GemfireClientCacheCreator(factory);
		ClientCache cretedCache = creator.create(info, null);
		verify(factory, atLeastOnce()).addPoolLocator("localhost", 1234);
		
	}
	
}
