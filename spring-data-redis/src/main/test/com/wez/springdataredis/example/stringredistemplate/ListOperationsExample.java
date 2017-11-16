package com.wez.springdataredis.example.stringredistemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * list操作类
 * Redis的List是简单的字符串列表，按照插入顺序排序，你可以添加一个元素到列表的头部（左边）或者尾部（右边）。
 * List的底层实际是个链表。
 * @author Administrator
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-resource.xml")
public class ListOperationsExample {

	/**
	 * Redis字符串操作模板对象
	 */
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	/**
	 * list操作对象
	 */
	private ListOperations<String, String> listOps;
	
	/**
	 * 初始化ListOperations操作对象
	 */
	@Before
	public void init() {
		listOps = stringRedisTemplate.opsForList();
	}
	
	/**
	 * leftPush(String key, String value)
	 *     从头部添加一个元素，返回添加后list中的元素个数。
	 */
	@Test
	public void testLeftPushOne() {
		Long currentListSize1 = listOps.leftPush("nums", "one");
		System.out.println(currentListSize1);
		Long currentListSize2 = listOps.leftPush("nums", "two");
		System.out.println(currentListSize2);
		Long currentListSize3 = listOps.leftPush("nums", "three");
		System.out.println(currentListSize3);
	}
	
	/**
	 * leftPushAll(String key, String... values)
	 *     从头部添加多个元素，返回添加后list中的元素个数。
	 */
	@Test
	public void testLeftPushTwo() {
		Long currentListSize = listOps.leftPushAll("nums", "four", "five");
		System.out.println(currentListSize);
	}
	
	/**
	 * leftPushAll(String key, Collection<String> values)
	 *     从头部添加一个集合，返回添加后list中的元素个数。
	 */
	@Test
	public void testLeftPushThree() {
		List<String> list = new ArrayList<String>();
		list.add("six");
		list.add("seven");
		list.add("eight");
		Long currentListSize = listOps.leftPushAll("nums", list);
		System.out.println(currentListSize);
	}
	
	/**
	 * index(String key, long index)
	 *     获取key列表中指定下标位置上的元素。
	 */
	@Test
	public void testIndex() {
		String indexResult = listOps.index("nums", 2);
		System.out.println(indexResult);
	}
	
	/**
	 * range(String key, long start, long end)
	 *     返回存储在键中的列表的指定元素。偏移开始和停止是基于零的索引，其中0是列表的第一个元素（列表的头部），1是下一个元素。
	 */
	@Test
	public void testRange() {
		List<String> listResult = listOps.range("nums", 0, -1);
		System.out.println(listResult);
	}
	
	/**
	 * size(String key)
	 *     获取list的元素个数。
	 */
	@Test
	public void testSize() {
		Long listSize = listOps.size("nums");
		System.out.println(listSize);
	}
	
	/**
	 * set(String key, long index, String value)
	 *     在list的指定位置插入元素
	 */
	@Test
	public void testSet() {
		listOps.set("nums", 2, "insert");
		System.out.println(listOps.range("nums", 0, -1));
	}
	
	/**
	 * remove(String key, long i, Object value)
	 *     从存储在键中的列表中删除等于值的元素的第一个计数事件。
	 *         计数参数以下列方式影响操作：
	 *             count> 0：删除等于从头到尾移动的值的元素。
	 *             count <0：删除等于从尾到头移动的值的元素。
	 *             count = 0：删除等于value的所有元素。
	 *     返回删除的个数。
	 */
	@Test
	public void testRemove() {
		Long removeCount = listOps.remove("nums", 1, "five");
		System.out.println(removeCount);
	}
	
	/**
	 * leftPop(String key)
	 *     弹出最左边的元素，弹出之后该值在列表中将不复存在。
	 */
	@Test
	public void testLeftPopOne() {
		String popResult = listOps.leftPop("nums");
		System.out.println(popResult);
	}
	
	/**
	 * leftPop(String key, long timeout, TimeUnit unit)
	 *     移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
	 */
	@Test
	public void testLeftPoPTwo() {
		String popResult = listOps.leftPop("nums", 2000, TimeUnit.SECONDS);
		System.out.println(popResult);
	}
	
}
