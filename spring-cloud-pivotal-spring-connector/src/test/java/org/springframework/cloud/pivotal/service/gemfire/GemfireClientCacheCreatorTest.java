package org.springframework.cloud.pivotal.service.gemfire;

import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.cloud.pivotal.service.common.GemfireServiceInfo;

import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.pdx.PdxSerializer;
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
	
	
	@Test
	public void testcacheCreationWithConfig() throws Exception {
		ClientCacheFactory factory = mock(ClientCacheFactory.class);
		ClientCache cache = mock(ClientCache.class);
		Map<String,Object> credentials = new HashMap<String, Object>();
		credentials.put("locators", Collections.singletonList("localhost[1234]"));
		GemfireServiceInfo info = new GemfireServiceInfo("gemfire", credentials);
		when(factory.create()).thenReturn(cache);
		GemfireClientCacheCreator creator = new GemfireClientCacheCreator(factory);
		
		GemfireServiceConnectorConfig config = new GemfireServiceConnectorConfig();
		config.setPdxIgnoreUnreadFields(true);
		config.setPdxPersistent(false);
		
		
		ClientCache cretedCache = creator.create(info, config);
		
		verify(factory, atLeastOnce()).addPoolLocator("localhost", 1234);
		verify(factory, atLeastOnce()).setPdxIgnoreUnreadFields(true);
		verify(factory, atLeastOnce()).setPdxPersistent(false);
		verify(factory, never()).setPdxDiskStore(anyString());
		
	}
	
	@Test
	public void testcacheCreationWithConfigAll() throws Exception {
		ClientCacheFactory factory = mock(ClientCacheFactory.class);
		ClientCache cache = mock(ClientCache.class);
		PdxSerializer serializer = mock(PdxSerializer.class);
		
		Map<String,Object> credentials = new HashMap<String, Object>();
		credentials.put("locators", Collections.singletonList("localhost[1234]"));
		GemfireServiceInfo info = new GemfireServiceInfo("gemfire", credentials);
		when(factory.create()).thenReturn(cache);
		GemfireClientCacheCreator creator = new GemfireClientCacheCreator(factory);
		
		GemfireServiceConnectorConfig config = new GemfireServiceConnectorConfig();
		config.setPdxDiskStore("foo");
		config.setPdxIgnoreUnreadFields(true);
		config.setPdxPersistent(false);
		config.setPdxReadSerialized(true);
		config.setPdxSerializer(serializer);
		config.setPoolFreeConnectionTimeout(10);
		config.setPoolIdleTimeout(11L);
		config.setPoolLoadConditioningInterval(12);
		config.setPoolMaxConnections(13);
		config.setPoolMinConnections(14);
		config.setPoolPingInterval(15L);
		config.setPoolPRSingleHopEnabled(true);
		config.setPoolReadTimeout(16);
		config.setPoolRetryAttempts(17);
		config.setPoolServerGroup("group");
		config.setPoolSocketBufferSize(18);
		config.setPoolStatisticInterval(19);
		config.setPoolSubscriptionEnabled(true);
		config.setPoolSubscriptionMessageTrackingTimeout(20);
		config.setPoolSubscriptionRedundancy(21);
		config.setPoolSubscriptionAckInterval(22);
		config.setPoolThreadLocalConnections(true);
		ClientCache createdCache = creator.create(info, config);
		
		verify(factory, atLeastOnce()).addPoolLocator("localhost", 1234);
		verify(factory, atLeastOnce()).setPdxIgnoreUnreadFields(config.getPdxIgnoreUnreadFields());
		verify(factory, atLeastOnce()).setPdxPersistent(config.getPdxPersistent());
		verify(factory, atLeastOnce()).setPdxReadSerialized(config.getPdxReadSerialized());
		verify(factory, atLeastOnce()).setPdxSerializer(serializer);
		verify(factory, atLeastOnce()).setPoolFreeConnectionTimeout(config.getPoolFreeConnectionTimeout());
		verify(factory, atLeastOnce()).setPoolLoadConditioningInterval(config.getPoolLoadConditioningInterval());
		verify(factory, atLeastOnce()).setPoolIdleTimeout(config.getPoolIdleTimeout());
		verify(factory, atLeastOnce()).setPoolMaxConnections(config.getPoolMaxConnections());
		verify(factory, atLeastOnce()).setPoolMinConnections(config.getPoolMinConnections());
		verify(factory, atLeastOnce()).setPoolPingInterval(config.getPoolPingInterval());
		verify(factory, atLeastOnce()).setPoolPRSingleHopEnabled(config.getPoolPRSingleHopEnabled());
		verify(factory, atLeastOnce()).setPoolReadTimeout(config.getPoolReadTimeout());
		verify(factory, atLeastOnce()).setPoolRetryAttempts(config.getPoolRetryAttempts());
		verify(factory, atLeastOnce()).setPoolServerGroup(config.getPoolServerGroup());
		verify(factory, atLeastOnce()).setPoolSocketBufferSize(config.getPoolSocketBufferSize());
		verify(factory, atLeastOnce()).setPoolStatisticInterval(config.getPoolStatisticInterval());
		verify(factory, atLeastOnce()).setPoolSubscriptionAckInterval(config.getPoolSubscriptionAckInterval());
		verify(factory, atLeastOnce()).setPoolSubscriptionEnabled(config.getPoolSubscriptionEnabled());
		verify(factory, atLeastOnce()).setPoolSubscriptionMessageTrackingTimeout(config.getPoolSubscriptionMessageTrackingTimeout());
		verify(factory, atLeastOnce()).setPoolSubscriptionRedundancy(config.getPoolSubscriptionRedundancy());
		verify(factory, atLeastOnce()).setPoolThreadLocalConnections(config.getPoolThreadLocalConnections());
		
	}
}
