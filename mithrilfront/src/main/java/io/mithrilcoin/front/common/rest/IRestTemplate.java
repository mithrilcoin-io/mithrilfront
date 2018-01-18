package io.mithrilcoin.front.common.rest;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

/**
 * 
 * restTemplate interface
 * @author Kei
 *
 */
public interface IRestTemplate { 
	
	public String getCallUrl(String restUrl);
	
	public <T> T get(String url, Object request, ParameterizedTypeReference<T> typeRef);
	
	public <T> T post(String url, Object request, ParameterizedTypeReference<T> typeRef);
	
	public <T> T postForMultipart(String url, Object request, ParameterizedTypeReference<T> typeRef);
	
	@Deprecated
	public <T> T put(String url, Object request, ParameterizedTypeReference<T> typeRef);
	
	@Deprecated
	public <T> T delete(String url, Object request, ParameterizedTypeReference<T> typeRef);
	
	public <T> T send(String url, Object request, ParameterizedTypeReference<T> typeRef, HttpMethod httpMethod);
	
	public <T> T send(String url, Object request, ParameterizedTypeReference<T> typeRef, HttpMethod httpMethod, HttpEntity<Object> entity);
}
