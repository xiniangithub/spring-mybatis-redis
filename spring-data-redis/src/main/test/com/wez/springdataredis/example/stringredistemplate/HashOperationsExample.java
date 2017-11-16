package com.wez.springdataredis.example.stringredistemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * hash操作类
 * Redis hash是一个键值对集合，是一个String类型的field和value的映射表，hash特别适合用与存储对象；
 * @author Administrator
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-resource.xml")
public class HashOperationsExample {
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	private HashOperations<String, String, String> hashOps;
	
	@Before
	public void init() {
		hashOps = stringRedisTemplate.opsForHash();
	}

	/**
	 * put(String key, String hashKey, String value)
	 *     设置散列hashKey的值。
	 */
	@Test
	public void testPut() {
		hashOps.put("hash01", "id", "100001");
		hashOps.put("hash01", "name", "Tom");
		hashOps.put("hash01", "age", "20");
	}
	
	/**
	 * putAll(String key, Map<? extends String, ? extends String> m)
	 *     使用m中提供的多个散列字段设置到key对应的散列表中。
	 */
	@Test
	public void testPutAll() {
		Map<String, String> person = new HashMap<String, String>(); 
		person.put("id", "100002");
		person.put("name", "Tony");
		person.put("age", "21");
		hashOps.putAll("hash02", person);
	}
	
	/**
	 * putIfAbsent(String key, String hashKey, String value)
	 *     仅当hashKey的value不同时才设置散列hashKey的值。
	 */
	@Test
	public void testPutIfAbsent() {
		Boolean putIfAbsent = hashOps.putIfAbsent("hash01", "id", "100001");
		System.out.println(putIfAbsent);
	}
	
	/**
	 * hasKey(String key, Object hashKey)
	 *     确定哈希hashKey是否存在。
	 */
	@Test
	public void testHashKey() {
		Boolean hasKey = hashOps.hasKey("hash02", "name");
		System.out.println(hasKey);
	}
	
	/**
	 * get(String key, Object hashKey)
	 *     从键中的哈希获取给定hashKey的值。
	 */
	@Test
	public void testGet() {
		String id = hashOps.get("hash02", "id");
		String name = hashOps.get("hash02", "name");
		String age = hashOps.get("hash02", "age");
		System.out.println("id:" + id + ", name:" + name + ", age:" + age);
	}
	
	/**
	 * multiGet(String key, Collection<String> hashKeys)
	 *     从哈希中获取给定hashKey的值。
	 */
	@Test
	public void testMultiGet() {
		ArrayList<String> hashKeys = new ArrayList<String>();
		hashKeys.add("id");
		hashKeys.add("name");
		hashKeys.add("age");
		List<String> multiGet = hashOps.multiGet("hash02", hashKeys);
		System.out.println(multiGet);
	}
	
	/**
	 * increment(String key, String hashKey, long delta)
	 *     通过给定的delta增加散列hashKey的值（整型），返回增加后的值。
	 */
	@Test
	public void testIncrementLong() {
		Long increment = hashOps.increment("hash02", "age", 1);
		System.out.println(increment);
	}
	
	/**
	 * increment(String key, String hashKey, double delta)
	 *     通过给定的delta增加散列hashKey的值（浮点数），返回增加后的值。
	 */
	@Test
	public void testIncrementDouble() {
		hashOps.put("hash02", "weight", "68.2");
		Double increment = hashOps.increment("hash02", "weight", 0.5);
		System.out.println(increment);
	}
	
	/**
	 * keys(String key)
	 *     获取key所对应的散列表的key。
	 */
	@Test
	public void testKeys() {
		Set<String> keys = hashOps.keys("hash02");
		System.out.println(keys);
	}
	
	/**
	 * values(String key)
	 *     获取整个哈希存储的值根据密钥。
	 */
	@Test
	public void testValues() {
		List<String> values = hashOps.values("hash02");
		System.out.println(values);
	}
	
	/**
	 * entries(String key)
	 *     获取整个哈希存储根据密钥。
	 */
	@Test
	public void testEntries() {
		Map<String, String> entries = hashOps.entries("hash02");
		System.out.println(entries);
	}
	
	/**
	 * scan(String key, ScanOptions options)
	 *     使用Cursor在key的hash中迭代，相当于迭代器。
	 */
	@Test
	public void TestScan() {
		Cursor<Entry<String,String>> cursor = hashOps.scan("hash02", ScanOptions.NONE);
		while(cursor.hasNext()) {
			Map.Entry<String, String> next = cursor.next();
			System.out.println("key:" + next.getKey() + ", value:" + next.getValue());
		}
	}
	
	/**
	 * size(String key)
	 *     获取key所对应的散列表的大小个数。
	 */
	@Test
	public void testSize() {
		Long size = hashOps.size("hash02");
		System.out.println(size);
	}
	
	/**
	 * delete(String key, Object... hashKeys)
	 *     删除给定的哈希hashKeys。
	 */
	@Test
	public void testDelete() {
		Long deleteCount = hashOps.delete("hash02", "name", "age");
		System.out.println(deleteCount);
	}
	
}