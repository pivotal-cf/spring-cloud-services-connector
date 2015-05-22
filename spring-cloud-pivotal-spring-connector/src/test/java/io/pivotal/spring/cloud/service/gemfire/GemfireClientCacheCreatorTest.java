package io.pivotal.spring.cloud.service.gemfire;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import io.pivotal.spring.cloud.service.common.GemfireServiceInfo;

import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.pdx.PdxSerializer;

@RunWith(MockitoJUnitRunner.class)
public class GemfireClientCacheCreatorTest {
	@Mock
	private ClientCacheFactory factory;

	@Mock
	private ClientCache cache;

	@Mock
	private PdxSerializer serializer;

	private GemfireClientCacheCreator creator;
	private GemfireServiceInfo info;

	@Before
	public void setUp() {
		when(factory.create()).thenReturn(cache);

		info = new GemfireServiceInfo("gemfire", Collections.singletonList("localhost[1234]"));
		creator = new GemfireClientCacheCreator(factory);
	}

	@Test
	public void cacheCreation() throws Exception {
		ClientCache createdCache = creator.create(info, null);

		assertNotNull(createdCache);

		verify(factory, atLeastOnce()).addPoolLocator("localhost", 1234);
	}

	@Test
	public void cacheCreationWithConfig() throws Exception {
		GemfireServiceConnectorConfig config = new GemfireServiceConnectorConfig();
		config.setPdxIgnoreUnreadFields(true);
		config.setPdxPersistent(false);

		ClientCache createdCache = creator.create(info, config);

		assertNotNull(createdCache);

		verify(factory, atLeastOnce()).addPoolLocator("localhost", 1234);
		verify(factory, atLeastOnce()).setPdxIgnoreUnreadFields(true);
		verify(factory, atLeastOnce()).setPdxPersistent(false);
		verify(factory, never()).setPdxDiskStore(anyString());
	}
	
	@Test
	public void cacheCreationWithConfigAll() throws Exception {
		GemfireServiceConnectorConfig config = new GemfireServiceConnectorConfig();
		config.setPdxDiskStore("foo");
		config.setPdxIgnoreUnreadFields(true);
		config.setPdxPersistent(true);
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

		assertNotNull(createdCache);

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
