package io.mithrilcoin.front;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@EnableCaching
@EnableAutoConfiguration
@ComponentScan("io.mithrilcoin.*")
@SpringBootApplication
public class MithrilfrontApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		System.setProperty("org.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH", "true");
		new SpringApplicationBuilder(MithrilfrontApplication.class)
				.properties("spring.config.name=application,apiAccess").run(args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MithrilfrontApplication.class);
	}
}
