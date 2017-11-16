package com.wez.springdataredis.example.stringredistemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * set操作类
 * Redis的Set是string类型的无序集合，集合成员是唯一的。它是通过HashTable实现的。
 * @author Administrator
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-resource.xml")
public class SetOperationsExample {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	private SetOperations<String, String> setOps;
	
	@Before
	public void init() {
		setOps = stringRedisTemplate.opsForSet();
	}
	
	/**
	 * add(String key, String... values)
	 *     往无序集合中添加一个或多个元素，返回添加的个数。
	 */
	@Test
	public void testAdd() {
		Long addCount = setOps.add("set", "one", "two", "three");
//		Long addCount = setOps.add("set", "four", "five");
		System.out.println(addCount);
	}
	
	/**
	 * remove(String key, Object... values)
	 *     移除无序集合中一个或多个元素，返回删除的个数。
	 */
	@Test
	public void testRemove() {
		Long removeCount = setOps.remove("set", "four", "five");
		System.out.println(removeCount);
	}
	
	/**
	 * pop(String key)
	 *     移除并返回集合中的一个随机元素。
	 */
	@Test
	public void testPop() {
		String popElem = setOps.pop("set");
		System.out.println(popElem);
	}
	
	/**
	 * move(String key, String value, String destKey)
	 *     将key中的value元素移动到destKey中。
	 *     注：移动后，key中就没有value元素了。
	 */
	@Test
	public void testMove() {
		Boolean isMove = setOps.move("set", "three", "set02");
		System.out.println(isMove);
	}
	
	/**
	 * size(String key)
	 *     获取无序集合中的大小。
	 */
	@Test
	public void testSize() {
		Long setSize = setOps.size("set");
		System.out.println(setSize);
	}
	
	/**
	 * isMember(String key, Object o)
	 *     判断o是否在key集合中。
	 */
	@Test
	public void testIsMember() {
		Boolean isMember = setOps.isMember("set02", "one");
		System.out.println(isMember);
	}
	
	/**
	 * intersect(String key, String otherKey)
	 *     求key集合与otherkey集合的交集。
	 */
	@Test
	public void testIntersectOne() {
		Set<String> intersectSet = setOps.intersect("set", "set02");
		for (String member : intersectSet) {
			System.out.println(member);
		}
	}
	
	/**
	 * intersect(String key, Collection<String> otherKeys)
	 *     key对应的无序集合与多个otherKey对应的无序集合求交集。
	 */
	@Test
	public void testIntersectTwo() {
		List<String> otherKeys = new ArrayList<String>();
		otherKeys.add("set02");
		Set<String> intersectSet = setOps.intersect("set", otherKeys);
		for (String member: intersectSet) {
			System.out.println(member);
		}
	}
	
	/**
	 * intersectAndStore(String key, String otherKey, String destKey)
	 *     key无序集合与otherkey无序集合的交集存储到destKey无序集合中，返回存入到destKey中的个数。
	 */
	@Test
	public void testIntersectAndStore() {
		Long intersectAndStoreCount = setOps.intersectAndStore("set", "set02", "set03");
		System.out.println(intersectAndStoreCount);
	}
	
	/**
	 * union(String key, String otherKey)
	 *     key无序集合与otherKey无序集合的并集。
	 */
	@Test
	public void testUnionOne() {
		Set<String> unionSet = setOps.union("set", "set02");
		for (String member : unionSet) {
			System.out.println(member);
		}
	}
	
	/**
	 * union(String key, Collection<String> otherKeys)
	 *     key无序集合与多个otherKey无序集合的并集。
	 */
	@Test
	public void testUnionTwo() {
		List<String> otherKeys = new ArrayList<String>();
		otherKeys.add("set02"); // 注意：这里集合中添加的无序集合的key
		Set<String> union = setOps.union("set", otherKeys);
		System.out.println(union);
	}
	
	/**
	 * unionAndStore(String key, String otherKey, String destKey)
	 *     key无序集合与otherkey无序集合的并集存储到destKey无序集合中。
	 */
	@Test
	public void testUnionAndStoreOne() {
		Long unionAndStoreCount = setOps.unionAndStore("set", "set02", "set03");
		System.out.println(unionAndStoreCount);
	}
	
	/**
	 * unionAndStore(String key, Collection<String> otherKeys, String destKey)
	 *     key无序集合与多个otherkey无序集合的并集存储到destKey无序集合中。
	 */
	@Test
	public void testUnionAndStoreTwo() {
		List<String> otherKeys = new ArrayList<String>();
		otherKeys.add("set02");
		Long unionAndStoreCount = setOps.unionAndStore("set", otherKeys, "set03");
		System.out.println(unionAndStoreCount);
	}
	
	/**
	 * difference(String key, String otherKey)
	 *     key无序集合与otherKey无序集合的差集（ 即key中的元素在otherKey没有的元素）。
	 */
	@Test
	public void testDifferenceOne() {
		Set<String> differenceSet = setOps.difference("set", "set02");
		for (String member : differenceSet) {
			System.out.println(member);
		}
	}
	
	/**
	 * difference(String key, Collection<String> otherKeys)
	 *     key无序集合与多个otherKey无序集合的差集。
	 */
	@Test
	public void testDifferenceTwo() {
		List<String> otherKeys = new ArrayList<String>();
		otherKeys.add("set02");
		Set<String> differenceSet = setOps.difference("set", otherKeys);
		for (String member : differenceSet) {
			System.out.println(member);
		}
	}
	
	/**
	 * differenceAndStore(String key, String otherKey, String destKey)
	 *     key无序集合与otherkey无序集合的差集存储到destKey无序集合中。
	 */
	@Test
	public void testDifferenceAndStoreOne() {
		Long differenceAndStoreCount = setOps.differenceAndStore("set", "set02", "set03");
		System.out.println(differenceAndStoreCount);
	}
	
	/**
	 * differenceAndStore(String key, Collection<String> otherKeys, String destKey)
	 *     key无序集合与多个otherkey无序集合的差集存储到destKey无序集合中。
	 */
	@Test
	public void testDifferenceAndStoreTwo() {
		ArrayList<String> otherKeys = new ArrayList<String>();
		otherKeys.add("set02");
		Long differenceAndStoreCount = setOps.differenceAndStore("set", otherKeys, "set03");
		System.out.println(differenceAndStoreCount);
	}
	
	/**
	 * members(String key)
	 *     返回集合中的所有成员。
	 */
	@Test
	public void testMembers() {
		Set<String> members = setOps.members("set");
		for (String member : members) {
			System.out.println(member);
		}
	}
	
	/**
	 * randomMember(String key)
	 *    随机获取key无序集合中的一个元素。 
	 */
	@Test
	public void testRandomMember() {
		String randomMember = setOps.randomMember("set");
		System.out.println(randomMember);
	}
	
	/**
	 * randomMembers(String key, long count)
	 *     获取多个key无序集合中的元素，count表示个数。
	 */
	@Test
	public void testRandomMembers() {
		List<String> randomMembers = setOps.randomMembers("set", 2);
		for (String member: randomMembers) {
			System.out.println(member);
		}
	}
	
	/**
	 * distinctRandomMembers(String key, long count)
	 *     获取多个key无序集合中的元素（去重），count表示个数。
	 */
	@Test
	public void testDistinctRandomMembers() {
		Set<String> distinctRandomMembers = setOps.distinctRandomMembers("set", 2);
		for (String member : distinctRandomMembers) {
			System.out.println(member);
		}
	}
	
	/**
	 * scan(String key, ScanOptions options)
	 *     遍历set。
	 */
	@Test
	public void test() {
		Cursor<String> cursor = setOps.scan("set", ScanOptions.NONE);
		while(cursor.hasNext()) {
			String next = cursor.next();
			System.out.println(next);
		}
	}
	
}
