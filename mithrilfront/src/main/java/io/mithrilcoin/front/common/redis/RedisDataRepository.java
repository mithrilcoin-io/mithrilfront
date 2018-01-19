package io.mithrilcoin.front.common.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author  Kei
 * 레디스 Nosql 사용 API Class 
 * @param <K> Generic Key (anyType)
 * @param <T> Generic Value (anyType)
 */
@Repository
public class RedisDataRepository<K, T> {
	
	
	private static final Logger logger = LoggerFactory.getLogger(RedisDataRepository.class);
	
	@Autowired
	private RedisTemplate<K, T> masterRedisTemplate;
	
	/**
	 * 데이터 저장 함수 
	 * @param <K> Generic Key (anyType)
	 * @param <T> Generic Value (anyType)
	 */
	public void setData(K key, T value)
	{
		logger.info("RedisDataRepository set key : " + key.toString());
		logger.info("RedisDataRepository set vale :" + value.toString());
		masterRedisTemplate.opsForValue().set(key, value);
	}
	/**
	 * 데이터 저장 함수 
	 * @param <K> Generic Key (anyType)
	 * @param <T> Generic Value (anyType)
	 * @param exipireTime TimeUnit 만료 시간 설정 
	 */
	public void setData(K key, T value, long expireTime, TimeUnit timeUnit)
	{
		masterRedisTemplate.opsForValue().set(key, value, expireTime, timeUnit);
	}
	/**
	 * redis에 저장된 데이터 불러오기 함수 
	 * @param  <K> Generic Key (anyType)
	 * @return <T> Generic Value (anyType)- null 이 리턴될 수 있음.
	 */
	public T getData(K key)
	{
		logger.info("RedisDataRepository get key : " + key.toString());
		return masterRedisTemplate.opsForValue().get(key);
	}
	/**
	 * redis에 저장된 데이터 삭제 함수
	 * @param <K> Generic Key (anyType)
	 */
	public void deleteData(K key)
	{
		masterRedisTemplate.delete(key);
	}
	/**
	 * 파마매터로 전달되는 List 안에 Key를 가진 모든 데이터 삭제
	 * @param ArrayList<K> Generic Key list(anyType)
	 */
	public void deleteData(ArrayList<K> keys)
	{
		masterRedisTemplate.delete(keys);
	}
	/**
	 * 파라매터로 전달되는 Key 가 존재하는 지 확인
	 * @param <K> Generic Key (anyType)
	 * @return 키가 존재하면 True, 아니면 False
	 */
	public boolean hasContainKey(K key)
	{
		return masterRedisTemplate.hasKey(key);
	}
	/**
	 * HashMap 객체 전체를 REDIS에 저장하는 함수
	 * @param multiDataMap Redis에 저장할 맵 데이터 
	 */
	public void setMultiData(HashMap<K, T> multiDataMap)
	{
		masterRedisTemplate.opsForValue().multiSet(multiDataMap);
	}
	/**
	 * 파마매터로 전달되는 List안에 Key를 가진 모든 데이터 가져오기
	 * @param  ArrayList<K> Generic Key (anyType)
	 * @return HashMap<K, T> (anyType)- 빈 MAP이 리턴될 수 있음.
	 */
	public HashMap<K, T> getMultiData(ArrayList<K> keys)
	{
		List<T> resultList = masterRedisTemplate.opsForValue().multiGet(keys);
		HashMap<K, T> resultMap = new HashMap<>();

		int index = 0;
		for (T result : resultList) {
			resultMap.put(keys.get(index), result);
			index++;
		}

		return resultMap;
	}
}
