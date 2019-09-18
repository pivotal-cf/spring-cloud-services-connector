package io.pivotal.spring.cloud.service.config.refresh;

public class ClientRefreshEvent {

	private String clientName;
	
	private String requester;

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getRequester() {
		return requester;
	}

	public void setRequester(String requester) {
		this.requester = requester;
	}
	
}