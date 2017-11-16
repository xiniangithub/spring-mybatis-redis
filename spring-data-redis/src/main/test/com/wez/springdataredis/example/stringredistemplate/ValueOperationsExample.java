package com.wez.springdataredis.example.stringredistemplate;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * string操作类
 * string是Redis中最基本的数据类型，一个key对应一个value；
 * string类型是二进制安全的，即redis的string可以包含任何类型的数据，例如图片，序列化的对象；
 * string类型的value最多可以存储512M；
 * @author Administrator
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-resource.xml")
public class ValueOperationsExample {
	
	/**
	 * Redis字符串操作模板对象
	 */
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	/**
	 * string操作对象
	 */
	private ValueOperations<String, String> valueOps;
	
	/**
	 * 初始化ValueOperations操作对象
	 */
	@Before
	public void initOpsForValue() {
		valueOps = stringRedisTemplate.opsForValue();
	}
	
	/**
	 * set(String key, String value)
	 *     保存key和value。
	 */
	@Test
	public void testSetOne() {
		// 保存key-value
		valueOps.set("valueOps", "valueOps");
		// 根据key获取value
		String result = valueOps.get("valueOps");
		System.out.println(result);
	}
	
	/**
	 * set(String key, String value, long offset)
	 *     用 value 参数覆写(overwrite)给定 key 所储存的字符串值，从偏移量 offset 开始。
	 */
	@Test
	public void testSetTwo() {
		valueOps.set("valueOps", "valueOpsTwo", 3);
		String result = valueOps.get("valueOps");
		System.out.println(result);
	}
	
	/**
	 * set(String key, String value, long timeout, TimeUnit unit)
	 *     设置保存key-value保存的时间。
	 *     参数：
	 *         timeout: 保存时间
	 *         unit: 时间单位
	 */
	@Test
	public void testSetThree() {
		valueOps.set("valueOps", "valueOps", 20, TimeUnit.SECONDS);
		System.out.println(valueOps.get("valueOps"));
	}
	
	/**
	 * get(Object key)
	 *     获取指定key的value。
	 */
	@Test
	public void testGetOne() {
		String result = valueOps.get("valueOps");
		System.out.println(result);
	}
	
	/**
	 * get(String key, long start, long end)
	 *     获取指定key的value，从开始位置start，到结束为止end。
	 */
	@Test
	public void testGetTwo() {
		String result = valueOps.get("valueOps", 0, 1);
		System.out.println(result);
	}
	
	/**
	 * append(String key, String value)
	 *     在指定key的value值后面追加字符串，如果该key不存在，则新建一个key，将该value设置该key。
	 */
	@Test
	public void testAppend() {
		valueOps.append("valueOps", " append str");
		System.out.println(valueOps.get("valuesOps"));
	}
	
}
