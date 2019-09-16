package io.pivotal.spring.cloud.service.config.refresh;

public class ClientRefreshEvent {

	private String clientName;

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
}