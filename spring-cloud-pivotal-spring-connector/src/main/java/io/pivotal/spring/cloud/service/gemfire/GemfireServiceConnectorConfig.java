package io.pivotal.spring.cloud.service.gemfire;

import org.springframework.cloud.service.ServiceConnectorConfig;

import com.gemstone.gemfire.pdx.PdxSerializer;
/**
 * 
 * @author Vinicius Carvalho
 * 
 * Enables specific configuration of the Gemfire ClientCache Factory
 *
 */
public class GemfireServiceConnectorConfig implements ServiceConnectorConfig {

	private Boolean pdxPersistent;
	private String pdxDiskStore;
	private Boolean pdxIgnoreUnreadFields;
	private Boolean pdxReadSerialized;
	private PdxSerializer pdxSerializer;
	private Integer poolFreeConnectionTimeout;
	private Long poolIdleTimeout;
	private Integer poolLoadConditioningInterval;
	private Integer poolReadTimeout;
	private Integer poolRetryAttempts;
	private Integer poolMaxConnections;
	private Integer poolMinConnections;
	private Long poolPingInterval;
	private Boolean poolPRSingleHopEnabled;
	private Integer poolSocketBufferSize;
	private Integer poolStatisticInterval;
	
	private Integer poolSubscriptionAckInterval;
	private Boolean poolSubscriptionEnabled;
	private Integer poolSubscriptionMessageTrackingTimeout;
	private Integer poolSubscriptionRedundancy;
	private Boolean poolThreadLocalConnections;
	private String poolServerGroup;
	
	public Boolean getPdxPersistent() {
		return pdxPersistent;
	}
	public void setPdxPersistent(Boolean pdxPersistent) {
		this.pdxPersistent = pdxPersistent;
	}
	public String getPdxDiskStore() {
		return pdxDiskStore;
	}
	public void setPdxDiskStore(String pdxDiskStore) {
		this.pdxDiskStore = pdxDiskStore;
	}
	public Boolean getPdxIgnoreUnreadFields() {
		return pdxIgnoreUnreadFields;
	}
	public void setPdxIgnoreUnreadFields(Boolean pdxIgnoreUnreadFields) {
		this.pdxIgnoreUnreadFields = pdxIgnoreUnreadFields;
	}
	public Boolean getPdxReadSerialized() {
		return pdxReadSerialized;
	}
	public void setPdxReadSerialized(Boolean pdxReadSerialized) {
		this.pdxReadSerialized = pdxReadSerialized;
	}
	public PdxSerializer getPdxSerializer() {
		return pdxSerializer;
	}
	public void setPdxSerializer(PdxSerializer pdxSerializer) {
		this.pdxSerializer = pdxSerializer;
	}
	public Integer getPoolFreeConnectionTimeout() {
		return poolFreeConnectionTimeout;
	}
	public void setPoolFreeConnectionTimeout(Integer poolFreeConnectionTimeout) {
		this.poolFreeConnectionTimeout = poolFreeConnectionTimeout;
	}
	public Long getPoolIdleTimeout() {
		return poolIdleTimeout;
	}
	public void setPoolIdleTimeout(Long poolIdleTimeout) {
		this.poolIdleTimeout = poolIdleTimeout;
	}
	public Integer getPoolLoadConditioningInterval() {
		return poolLoadConditioningInterval;
	}
	public void setPoolLoadConditioningInterval(Integer poolLoadConditioningInterval) {
		this.poolLoadConditioningInterval = poolLoadConditioningInterval;
	}
	public Integer getPoolReadTimeout() {
		return poolReadTimeout;
	}
	public void setPoolReadTimeout(Integer poolReadTimeout) {
		this.poolReadTimeout = poolReadTimeout;
	}
	public Integer getPoolRetryAttempts() {
		return poolRetryAttempts;
	}
	public void setPoolRetryAttempts(Integer poolRetryAttempts) {
		this.poolRetryAttempts = poolRetryAttempts;
	}
	public String getPoolServerGroup() {
		return poolServerGroup;
	}
	public void setPoolServerGroup(String poolServerGroup) {
		this.poolServerGroup = poolServerGroup;
	}
	public Integer getPoolMaxConnections() {
		return poolMaxConnections;
	}
	public void setPoolMaxConnections(Integer poolMaxConnections) {
		this.poolMaxConnections = poolMaxConnections;
	}
	public Integer getPoolMinConnections() {
		return poolMinConnections;
	}
	public void setPoolMinConnections(Integer poolMinConnections) {
		this.poolMinConnections = poolMinConnections;
	}
	public Long getPoolPingInterval() {
		return poolPingInterval;
	}
	public void setPoolPingInterval(Long poolPingInterval) {
		this.poolPingInterval = poolPingInterval;
	}
	public Boolean getPoolPRSingleHopEnabled() {
		return poolPRSingleHopEnabled;
	}
	public void setPoolPRSingleHopEnabled(Boolean poolPRSingleHopEnabled) {
		this.poolPRSingleHopEnabled = poolPRSingleHopEnabled;
	}
	public Integer getPoolSocketBufferSize() {
		return poolSocketBufferSize;
	}
	public void setPoolSocketBufferSize(Integer poolSocketBufferSize) {
		this.poolSocketBufferSize = poolSocketBufferSize;
	}
	public Integer getPoolStatisticInterval() {
		return poolStatisticInterval;
	}
	public void setPoolStatisticInterval(Integer poolStatisticInterval) {
		this.poolStatisticInterval = poolStatisticInterval;
	}
	public Integer getPoolSubscriptionAckInterval() {
		return poolSubscriptionAckInterval;
	}
	public void setPoolSubscriptionAckInterval(Integer poolSubscritptionAckInterval) {
		this.poolSubscriptionAckInterval = poolSubscritptionAckInterval;
	}
	public Boolean getPoolSubscriptionEnabled() {
		return poolSubscriptionEnabled;
	}
	public void setPoolSubscriptionEnabled(Boolean poolSubscriptionEnabled) {
		this.poolSubscriptionEnabled = poolSubscriptionEnabled;
	}
	public Integer getPoolSubscriptionMessageTrackingTimeout() {
		return poolSubscriptionMessageTrackingTimeout;
	}
	public void setPoolSubscriptionMessageTrackingTimeout(Integer poolSubscriptionMessageTrackingTimeout) {
		this.poolSubscriptionMessageTrackingTimeout = poolSubscriptionMessageTrackingTimeout;
	}
	public Integer getPoolSubscriptionRedundancy() {
		return poolSubscriptionRedundancy;
	}
	public void setPoolSubscriptionRedundancy(Integer poolSubscriptionRedundancy) {
		this.poolSubscriptionRedundancy = poolSubscriptionRedundancy;
	}
	public Boolean getPoolThreadLocalConnections() {
		return poolThreadLocalConnections;
	}
	public void setPoolThreadLocalConnections(Boolean poolThreadLocalConnections) {
		this.poolThreadLocalConnections = poolThreadLocalConnections;
	}

	
	
}
