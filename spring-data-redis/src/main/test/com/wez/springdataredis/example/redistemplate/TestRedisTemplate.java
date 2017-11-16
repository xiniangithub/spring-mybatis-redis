package com.wez.springdataredis.example.redistemplate;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-resource.xml")
public class TestRedisTemplate {
	
	@Resource(name="redisTemplate")
	private ValueOperations<String, Object> valueOps;
	
	@Resource(name="redisTemplate")
	private ListOperations<String, Object> listOps;
	
	@Resource(name="redisTemplate")
	private HashOperations<String, Object, Object> hashOps;

	@Resource(name="redisTemplate")
	private SetOperations<String, Object> setOps;
	
	@Resource(name="redisTemplate")
	private ZSetOperations<String, Object> zsetOps;

	@Test
	public void testRedisTemplate() {
		Long size = valueOps.size("one");
		System.out.println(size);
	}
	
}
