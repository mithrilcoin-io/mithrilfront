package io.mithrilcoin.front.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class ParameterChanger {

	private static final String UTF_8 = "UTF-8";
	/**
	 * 객체로부터 필드명과 필드에 들어있는 값을 추출합니다.
	 * HTTP Method가 GET방식일 때 파라미터 생성을 위해 사용합니다.
	 * 
	 * 사용예)
	 * Student student = new Student("hong gil dong", 17700102, true);
	 * HttpUtils.urlEncodeUTF8(HttpUtils.extractFieldNameValueMap(Student.class, student));
	 * 결과: ?name=hong%2Bgil%2Bdong&birthday=17700102&sex=true
	 * 
	 * @param clazz
	 * @param type
	 * @return
	 */
	public static <T> Map<String, String> extractFieldNameValueMap(Class<?> clazz, T type) {
		Map<String, String> fieldNameValueMap = new LinkedHashMap<>();

		for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
			
			Field[] declaredFields = c.getDeclaredFields();
			for (Field f : declaredFields) {
				f.setAccessible(true);
				String fieldName = f.getName();
				if ("serialVersionUID".equals(fieldName)) {
					continue;
				}

				Object value = null;
				try {
					value = f.get(type);
				} catch (Exception e) {
					//ignore
				}
				if(value == null){
					continue;
				}
				if(value instanceof String[]){
					String [] stringParameterArray = (String [])value;
					for(int i = 0, length = stringParameterArray.length ; i < length ; i++){
						fieldNameValueMap.put(fieldName + "[" + i + "]", stringParameterArray[i]);
					}
					continue;
				}
				
				fieldNameValueMap.put(fieldName, "" + value);
			}
		}
		return fieldNameValueMap;
	}
	
	/**
	 * URL인코딩을 합니다.
	 * 
	 * 필드명과 값 사이에 = 을 넣어서 세팅합니다.
	 * 
	 * @param parameterMap
	 * @return
	 */
	public static String urlEncodeUTF8(Map<?, ?> parameterMap) {
		StringBuilder sb = new StringBuilder();
		sb.append("?");
		for (Map.Entry<?, ?> entry : parameterMap.entrySet()) {
			if (sb.length() > 0) {
				sb.append("&");
			}
			sb.append(String.format("%s=%s", entry.getKey().toString(), urlEncodeUTF8(entry.getValue().toString())));
		}
		return sb.toString();
	}
	/**
	 * URL인코딩을 합니다.
	 * 
	 * @param param
	 * @return
	 */
	private static String urlEncodeUTF8(String param) {
		try {
			return java.net.URLEncoder.encode(param, UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	
}
