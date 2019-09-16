package io.pivotal.spring.cloud.service.config.refresh;

import java.nio.channels.ClosedChannelException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.refresh.ContextRefresher;

public class ClientRefreshEventHandler implements InitializingBean {
	
	private static final Logger log = LoggerFactory.getLogger(ClientRefreshEventHandler.class);

	@Value("${spring.application.name}")
	private String applicationName;
	
	private final ClientRefreshStreamClient client;


	private final ContextRefresher contextRefresher;
	
	public ClientRefreshEventHandler(ClientRefreshStreamClient client, ContextRefresher contextRefresher) {
		this.client = client;
		this.contextRefresher = contextRefresher;
	}

	private void establishRSocketConnectionWithServer() throws Exception {
		client.getDataStream()
			.doOnNext(clientRefreshEvent -> {
				handleClientRefreshEvent(clientRefreshEvent);
			})
			.doOnError(ClosedChannelException.class, e -> {
				log.warn("Lost connection with server");
				// TODO: Figure out how to reestablish connection
			})
			.repeat()
			.subscribe();

	}
	
	private void handleClientRefreshEvent(ClientRefreshEvent clientRefreshEvent) {
		if (clientRefreshEvent.getClientName().equals("*") || clientRefreshEvent.getClientName().equals(applicationName)) {
			log.info("Received a ClientRefreshEvent: " + clientRefreshEvent);
			Set<String> keys = contextRefresher.refresh();
			log.info("   ---- Refreshed keys:  " + keys);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		establishRSocketConnectionWithServer();
	}
	
}
