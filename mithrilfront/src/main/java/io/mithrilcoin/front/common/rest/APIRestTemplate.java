package io.mithrilcoin.front.common.rest;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.multipart.MultipartFile;

import io.mithril.vo.common.ApiAccessInfo;

/**
 * 
 * ssocioliving api access rest-template
 * 
 * @author Kei
 *
 */
public class APIRestTemplate extends RestOperationImpl implements IRestTemplate {

	private static Logger logger = LoggerFactory.getLogger(APIRestTemplate.class);

	public APIRestTemplate(RestOperations restOperation, String host, String port, ApiAccessInfo accessInfo) {
		this.restOperations = restOperation;
		this.host = host;
		this.port = port;
		this.accessInfo = accessInfo;
	}

	@Override
	public <T> T get(String url, Object request, ParameterizedTypeReference<T> typeRef) {
		return send(getCallUrl(url) + request, null, typeRef, HttpMethod.GET);
	}

	@Override
	public <T> T post(String url, Object request, ParameterizedTypeReference<T> typeRef) {
		return send(getCallUrl(url), request, typeRef, HttpMethod.POST);
	}

	@Override
	public <T> T postForMultipart(String url, Object request, ParameterizedTypeReference<T> typeRef) {
		MultipartFile file = (MultipartFile) request;
		MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
		try {
			ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
				@Override
				public String getFilename() throws IllegalStateException {
					return file.getOriginalFilename();
				}
			};
			multiValueMap.add("upload", resource);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return send(getCallUrlNoAccess(url), multiValueMap, typeRef, HttpMethod.POST,
				getEntity(multiValueMap, MediaType.MULTIPART_FORM_DATA_VALUE));
	}

	@Override
	public <T> T put(String url, Object request, ParameterizedTypeReference<T> typeRef) {
		return send(getCallUrl(url), request, typeRef, HttpMethod.PUT);
	}

	@Override
	public <T> T delete(String url, Object request, ParameterizedTypeReference<T> typeRef) {
		return null;
	}

	@Override
	public <T> T send(String url, Object request, ParameterizedTypeReference<T> typeRef, HttpMethod httpMethod) {
		return restOperation(logger, url, request, typeRef, httpMethod, null);
	}

	@Override
	public <T> T send(String url, Object request, ParameterizedTypeReference<T> typeRef, HttpMethod httpMethod,
			HttpEntity<Object> entity) {
		return restOperation(logger, url, request, typeRef, httpMethod, entity);
	}
}
