package io.pivotal.spring.cloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 *
 * @author Roy Clarkson
 */
@SpringBootApplication
public class TestApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(TestApplication.class).run(args);
	}

}
