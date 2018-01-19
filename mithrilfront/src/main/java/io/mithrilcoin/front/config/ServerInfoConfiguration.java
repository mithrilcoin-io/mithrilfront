package io.mithrilcoin.front.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:apiAccess.properties")
public class ServerInfoConfiguration {

	@Autowired
	private Environment env;

	public String getMyhost() {
		return env.getProperty("server.hostname");
	}

	public String getMyPort() {
		return env.getProperty("server.port");
	}

	public String getMyFullUrl() {
		return "http://" + getMyhost() + ":" + getMyPort();
	}
}
