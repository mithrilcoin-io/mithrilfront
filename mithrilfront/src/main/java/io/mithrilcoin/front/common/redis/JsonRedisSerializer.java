package io.mithrilcoin.front.common.redis;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;

/**
 * REDIS 저장용 JSON <-> Object 직렬화처리 클래스 
 * @author jongkuni@danalssocio.com
 *
 */
public class JsonRedisSerializer implements RedisSerializer<Object> {

		private final ObjectMapper om;

		public JsonRedisSerializer() {
			// 분석 필요함.
			this.om = new ObjectMapper().enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
		}
		
		@Override
		public byte[] serialize(Object t) throws SerializationException {
			try {
				return om.writeValueAsBytes(t);
			} catch (JsonProcessingException e) {
				throw new SerializationException(e.getMessage(), e);
			}
		}

		@Override
		public Object deserialize(byte[] bytes) throws SerializationException {
			
			if(bytes == null){
				return null;
			}
			
			try {
				return om.readValue(bytes, Object.class);
			} catch (Exception e) {
				throw new SerializationException(e.getMessage(), e);
			}
		}
	}