package io.mithrilcoin.front.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import io.mithrilcoin.front.common.redis.JsonRedisSerializer;


@Configuration
public class RedisConfig {

	@Autowired
	private JedisConnectionFactory jedisConnectionFactory;

	@Bean(name = "masterRedisTemplate")
	public <K, T> RedisTemplate<K, T> masterRedisTemplate() {
		RedisTemplate<K, T> redistemp = new RedisTemplate<>();
		redistemp.setConnectionFactory(jedisConnectionFactory);
		// serialize 방식 - JSON 방식으로 저장 및 처리하도록 주입
		redistemp.setValueSerializer(jsonRedisSerializer());
		redistemp.setKeySerializer(new StringRedisSerializer());
		return redistemp;
	}

	@Bean
	public JsonRedisSerializer jsonRedisSerializer() {
		JsonRedisSerializer jsonSerializer = new JsonRedisSerializer();
		return jsonSerializer;
	}

	// nosql cache bean 선언.
	@Bean(name = "redisTemplate")
	@Qualifier(value = "redisTemplate")
	public RedisTemplate<Object, Object> redisTemplate() {
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory);
		return redisTemplate;
	}

	// cache bean 선언.
	@Bean
	public CacheManager redisCacheManager() {
		RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate());
		redisCacheManager.setUsePrefix(true);
		return redisCacheManager;
	}
}
