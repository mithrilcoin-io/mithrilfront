package io.mithrilcoin.front.config;

import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.util.UrlPathHelper;

import io.mithrilcoin.front.handler.MithrilplayInterceptor;

@EnableWebMvc
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {
	
	 private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
	            "classpath:/META-INF/resources/", "classpath:/resources/",
	            "classpath:/static/", "classpath:/public/"
	    };

	    @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry.addResourceHandler("/**")
	                .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
	    }

//	@Override
//	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
//
//		registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(3600).resourceChain(true)
//				.addResolver(new PathResourceResolver());
//		registry.addResourceHandler("/img/**").addResourceLocations("/img/").setCachePeriod(3600).resourceChain(true)
//				.addResolver(new PathResourceResolver());
//		registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(3600).resourceChain(true)
//				.addResolver(new PathResourceResolver());
//
//		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//	}

	@Bean
	public Filter characterEncodingFilter() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		return characterEncodingFilter;
	}

	@Bean
	public HttpMessageConverter<String> responseBodyConverter() {

		StringHttpMessageConverter con = new StringHttpMessageConverter();
		ArrayList<MediaType> list = new ArrayList<>();
		list.add(MediaType.APPLICATION_FORM_URLENCODED);
		con.setSupportedMediaTypes(list);
		con.setDefaultCharset(Charset.forName("UTF-8"));
		return con;
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Bean
	public ViewResolver getViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}

	/**
	 * Interceptor
	 */
	@Bean
	public MithrilplayInterceptor interceptor() {
		MithrilplayInterceptor interceptor = new MithrilplayInterceptor();
		return interceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor()).addPathPatterns("/**").excludePathPatterns("/health")
				.excludePathPatterns("/certification", "/certification/*", "/certification/*/**")
				.excludePathPatterns("/signin", "/signin/*", "/signin/*/**")
				.excludePathPatterns("/signup", "/signup/*", "/signup/*/**").excludePathPatterns("/error")
		// .addPathPatterns("", "/**/*")
		// .excludePathPatterns("/resourceRevision/*")
		// .excludePathPatterns("/health/*")
		// .excludePathPatterns("/homepage")
		// .excludePathPatterns("/main")
		// .excludePathPatterns("/error")
		// .excludePathPatterns("/homepage/*")
		// .excludePathPatterns("/health/*")
		// .excludePathPatterns("/join/*", "/join/*/**")
		//
		// .excludePathPatterns("/member/auth")
		// .excludePathPatterns("/member/phone/xhr")
		// .excludePathPatterns("/member/phone/send/sms/xhr")
		// .excludePathPatterns("/member/phone/send/sms/re/xhr")
		//
		// .excludePathPatterns("/member/login", "/member/login/*", "/member/login/*/*")
		// .excludePathPatterns("/member/logout")

		;
	}

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		UrlPathHelper urlPathHelper = new UrlPathHelper();
		urlPathHelper.setUrlDecode(false);
		urlPathHelper.setAlwaysUseFullPath(true);
		configurer.setUrlPathHelper(urlPathHelper);
	}

}
