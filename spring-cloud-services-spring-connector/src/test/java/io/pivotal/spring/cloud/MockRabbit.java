package io.pivotal.spring.cloud;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.mockito.Mockito;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rabbitmq.client.Channel;

@Configuration
public class MockRabbit {
	
	public static final Channel mockChannel = mock(Channel.class);
	private static final ConnectionFactory mockConnectionFactory = mock(ConnectionFactory.class);
	
	@Bean
	public ConnectionFactory mockConnectionFactory() throws IOException {
		Connection connection = mock(Connection.class);
		when(mockConnectionFactory.createConnection()).thenReturn(connection);
		when(connection.createChannel(anyBoolean())).thenReturn(mockChannel);
		return mockConnectionFactory;
	}
	
	public static void reset() {
		Mockito.reset(mockChannel);
	}


}
