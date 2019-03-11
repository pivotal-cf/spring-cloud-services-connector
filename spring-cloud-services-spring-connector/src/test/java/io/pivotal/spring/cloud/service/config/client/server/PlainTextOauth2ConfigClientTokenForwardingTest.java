package io.pivotal.spring.cloud.service.config.client.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import io.pivotal.spring.cloud.service.config.ConfigClientOAuth2ResourceDetails;
import io.pivotal.spring.cloud.service.config.PlainTextConfigClient;
import io.pivotal.spring.cloud.service.config.PlainTextConfigClientAutoConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConfigServerTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.profiles.active=vault,plaintext,native", "spring.cloud.config.enabled=true", "eureka.client.enabled=false",
        "spring.cloud.config.client.oauth2.client-id=acme"})
public class PlainTextOauth2ConfigClientTokenForwardingTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ConfigClientOAuth2ResourceDetails resource;

    @Autowired
    private ConfigClientProperties configClientProperties;

    private ConfigurableApplicationContext vaultServer;

    @Before
    public void setup() {
        vaultServer = new SpringApplicationBuilder(VaultServer.class).web(WebApplicationType.SERVLET)
                .run("--server.port=8200", "--eureka.client.enabled=false", "--security.ignored=/**");
        resource.setAccessTokenUri("http://localhost:" + port + "/oauth/token");
        configClientProperties.setName("app");
        configClientProperties.setProfile(null);
        configClientProperties.setUri(new String[]{"http://localhost:" + port});
    }

    @After
    public void teardown() {
        vaultServer.close();
    }

    @Test
    public void requestGoesThroughWhenConfigServerTokenSet() {
        configClientProperties.setToken("vaultToken");
        PlainTextConfigClient configClient = new PlainTextConfigClientAutoConfiguration()
                .plainTextConfigClient(resource, configClientProperties);
        configClient.getConfigFile(null, null, "nginx.conf");
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void badRequestWhenConfigServerTokenNotSet() {
        configClientProperties.setToken(null);
        PlainTextConfigClient configClient = new PlainTextConfigClientAutoConfiguration()
                .plainTextConfigClient(resource, configClientProperties);
        configClient.getConfigFile(null, null, "nginx.conf");
    }

    @Configuration
    @EnableWebSecurity
    @RestController
    @EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class})
    public static class VaultServer extends WebSecurityConfigurerAdapter {
        @GetMapping("v1/secret/{key}")
        public ResponseEntity getKey(@PathVariable("key") String key) {
            return ResponseEntity.notFound().build();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/**").permitAll();
        }
    }
}
