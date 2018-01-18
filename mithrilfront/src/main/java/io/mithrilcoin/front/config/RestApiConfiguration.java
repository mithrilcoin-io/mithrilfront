package io.mithrilcoin.front.config;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import io.mithril.vo.common.ApiAccessInfo;
import io.mithril.vo.common.FrontAccessInfo;
import io.mithrilcoin.front.common.rest.APIRestTemplate;
import io.mithrilcoin.front.common.rest.IRestTemplate;


/**
 * 인터페이스 통신 rest api configuration class
 */
@Configuration
@PropertySource("classpath:apiAccess.properties")
public class RestApiConfiguration {
	
	@Autowired
	Environment env;
	
	@Autowired
	private ApiAccessInfo accessInfo;
	
	private RestOperations getRestOperation(int readTimeout){
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setConnectTimeout(1000);
		factory.setReadTimeout(readTimeout);
		RestTemplate restTemplate = new RestTemplate(factory);
		return restTemplate;
	}
	
	@Bean(name="mithrilAPIRestTemplate", autowire = Autowire.BY_NAME)
	public IRestTemplate cashchargeAPIRestTemplate(){
		return new APIRestTemplate(
				getRestOperation(env.getProperty("mithrilapi.timeout", int.class, 15000))
				, env.getProperty("mithrilapi.host")
				, env.getProperty("mithrilapi.port")
				, accessInfo
				);
	}
	
	
	@Bean(name="bizMailAPIRestTemplate", autowire = Autowire.BY_NAME)
	public IRestTemplate bizMailAPIRestTemplate(){
//		biz.mail.host=http://www.bizmailer.co.kr
//		biz.mail.port=80
//		biz.mail.time=15000
		return new APIRestTemplate(
				getRestOperation(env.getProperty("biz.mail.host.timeout	", int.class, 15000))
				, env.getProperty("biz.mail.host")
				, env.getProperty("biz.mail.port")
				, null
				);
	}
	@Bean
	public ApiAccessInfo frontApiAccessInfo()
	{
		return new FrontAccessInfo();
	}
	
}
