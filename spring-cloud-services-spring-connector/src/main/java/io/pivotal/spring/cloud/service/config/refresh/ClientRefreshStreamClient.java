package io.pivotal.spring.cloud.service.config.refresh;

import java.io.IOException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.security.oauth2.client.OAuth2ClientContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.WebsocketClientTransport;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

public class ClientRefreshStreamClient {

	private static final Logger log = LoggerFactory.getLogger(ClientRefreshStreamClient.class);
	
	private final String DATA_STREAM_NAME = "clientRefresh";

	private final RSocket socket;

	private final ObjectMapper om = new ObjectMapper();
	private Flux<ClientRefreshEvent> responseFlux;

	public ClientRefreshStreamClient(ConfigClientProperties configClientProps, OAuth2ClientContext oauth2Context) {
		String configServerUri = configClientProps.getUri()[0];
		log.info("Creating RSocket connection (using Websocket Transport) with config server at " + configServerUri);

		HttpClient httpClient = HttpClient.create().baseUrl(configServerUri).keepAlive(true);

		WebsocketClientTransport websocketTransport = WebsocketClientTransport.create(httpClient, "/_refreshproxy");
		websocketTransport.setTransportHeaders(() -> {
			String accessToken = oauth2Context.getAccessToken().getValue();
			HashMap<String, String> headers = new HashMap<>();
			headers.put("Authorization", "Bearer " + accessToken);
			return headers;
		});
		this.socket = RSocketFactory.connect().transport(websocketTransport).start().block();

		try {
			this.responseFlux = this.socket.requestStream(DefaultPayload.create(DATA_STREAM_NAME)).map(it -> {
				try {
					return om.readValue(it.getDataUtf8(), ClientRefreshEvent.class);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}).onErrorReturn(null);
			log.info("Config client ready to receive refresh events from server");
		} catch (Exception e) {
			log.error("Error connecting with refresh server: ", e);
		}

	}

	public Flux<ClientRefreshEvent> getDataStream() {
		return this.responseFlux;
	}

	public void dispose() {
		this.socket.dispose();
	}
}
