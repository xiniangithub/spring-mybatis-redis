package com.wez.springdataredis.example.stringredistemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * zset操作类
 * Redis的Zset和Set一样也是string类型元素的集合，且不允许重复的成员。
 * 不同的是每个元素都会关联一个double类型的分数。redis正是通过分数来为集合中的成员进行从小到大的排序，Zset成员是唯一的，但分数（score）却可以重复。
 * 形象的描述：Zset在set的基础上，加一个score值，之前set是k1 v1 v2 v3，现在zset是k1 score1 v1 score2 v2；
 * 注：
 *     TimeUnit是java.util.concurrent包下面的一个类，表示给定单元粒度的时间段。
 *     常用的颗粒度
 *         TimeUnit.DAYS          //天
 *         TimeUnit.HOURS         //小时
 *         TimeUnit.MINUTES       //分钟
 *         TimeUnit.SECONDS       //秒
 *         TimeUnit.MILLISECONDS  //毫秒
 * @author Administrator
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-resource.xml")
public class ZSetOperationsExample {
	
	@Autowired
	public StringRedisTemplate stringRedisTemplate;
	
	private ZSetOperations<String, String> zsetOps;

	@Before
	public void init() {
		zsetOps = stringRedisTemplate.opsForZSet();
	}
	
	/**
	 * add(String key, String value, double score)
	 *     新增一个有序集合，存在的话为false，不存在的话为true。
	 */
	@Test
	public void testAddOne() {
		Boolean isAdd1 = zsetOps.add("zset01", "one", 1.0);
		Boolean isAdd2 = zsetOps.add("zset01", "two", 2.0);
		System.out.println(isAdd1);
		System.out.println(isAdd2);
	}
	
	/**
	 * add(String key, Set<TypedTuple<String>> tuples)
	 *     新增一个有序集合。
	 */
	@Test
	public void testAddTwo() {
		ZSetOperations.TypedTuple<String> tuple1 = new DefaultTypedTuple<String>("three", 3.0);
		ZSetOperations.TypedTuple<String> tuple2 = new DefaultTypedTuple<String>("four", 4.0);
		Set<TypedTuple<String>> tuples = new HashSet<TypedTuple<String>>();
		tuples.add(tuple1);
		tuples.add(tuple2);
		Long addCount = zsetOps.add("zset01", tuples);
		System.out.println(addCount);
	}
	
	/**
	 * incrementScore(String key, String value, double delta)
	 *     增加元素的score值，并返回增加后的值。
	 */
	@Test
	public void testIncrementScore() {
		Double afterIncrementScore = zsetOps.incrementScore("zset01", "one", 0.1);
		System.out.println(afterIncrementScore);
	}
	
	/**
	 * rank(String key, Object o)
	 *     返回有序集中指定成员的排名，其中有序集成员按分数值递增(从小到大)顺序排列。
	 */
	@Test
	public void testRank() {
		System.out.println(zsetOps.range("zset01", 0, -1));
		Long rank = zsetOps.rank("zset01", "two");
		System.out.println(rank);
	}
	
	/**
	 * reverseRank(String key, Object o)
	 *     返回有序集中指定成员的排名，其中有序集成员按分数值递减(从大到小)顺序排列。
	 */
	@Test
	public void testReverseRank() {
		System.out.println(zsetOps.range("zset01", 0, -1));
		Long reverseRank = zsetOps.reverseRank("zset01", "two");
		System.out.println(reverseRank);
	}
	
	/**
	 * range(String key, long start, long end)
	 *     通过索引区间返回有序集合成指定区间内的成员，其中有序集成员按分数值递增(从小到大)顺序排列。
	 */
	@Test
	public void testRange() {
		Set<String> range = zsetOps.range("zset01", 0, -1);
		System.out.println(range);
	}
	
	/**
	 * rangeWithScores(String key, long start, long end)
	 *     通过索引区间返回有序集合成指定区间内的成员对象，其中有序集成员按分数值递增(从小到大)顺序排列。
	 */
	@Test
	public void testRangeWithScores() {
		Set<TypedTuple<String>> rangeWithScores = zsetOps.rangeWithScores("zset01", 0, -1);
		Iterator<TypedTuple<String>> iterator = rangeWithScores.iterator();
		while(iterator.hasNext()) {
			TypedTuple<String> next = iterator.next();
			System.out.println("value:" + next.getValue() + ", score:" + next.getScore());
		}
	}
	
	/**
	 * rangeByScore(String key, double min, double max)
	 *     通过分数返回有序集合指定区间内的成员，其中有序集成员按分数值递增(从小到大)顺序排列。
	 */
	@Test
	public void testRangeByScoreOne() {
		Set<String> rangeByScore = zsetOps.rangeByScore("zset01", 0.0, 4.0);
		System.out.println(rangeByScore);
	}
	
	/**
	 * rangeByScore(String key, double min, double max, long offset, long count)
	 *     通过分数返回有序集合指定区间内的成员，并在索引范围内，其中有序集成员按分数值递增(从小到大)顺序排列。
	 *     参数：
	 *         offset：开始下标；
	 *         count：获取个数；
	 */
	@Test
	public void testRangeByScoreTwo() {
		Set<String> rangeByScore = zsetOps.rangeByScore("zset01", 0, 4, 0, 2);
		System.out.println(rangeByScore);
	}
	
	/**
	 * rangeByScoreWithScores(String key, double min, double max)
	 *     通过分数返回有序集合指定区间内的成员对象，其中有序集成员按分数值递增(从小到大)顺序排列。
	 */
	@Test
	public void testRangeByScoreWithScoresOne() {
		Set<TypedTuple<String>> rangeByScoreWithScores = zsetOps.rangeByScoreWithScores("zset01", 0.0, 3.0);
		Iterator<TypedTuple<String>> iterator = rangeByScoreWithScores.iterator();
		while(iterator.hasNext()) {
			TypedTuple<String> next = iterator.next();
			System.out.println("value:" + next.getValue() + ", score:" + next.getScore());
		}
	}
	
	/**
	 * rangeByScoreWithScores(String key, double min, double max, long offset, long count)
	 *     通过分数返回有序集合指定区间内的成员对象，并在索引范围内，其中有序集成员按分数值递增(从小到大)顺序排列。
	 */
	@Test
	public void testRangeByScoreWithScoresTwo() {
		Set<TypedTuple<String>> rangeByScoreWithScores = zsetOps.rangeByScoreWithScores("zset01", 0, 5, 0, 2);
		Iterator<TypedTuple<String>> iterator = rangeByScoreWithScores.iterator();
		while(iterator.hasNext()) {
			TypedTuple<String> next = iterator.next();
			System.out.println("value:" + next.getValue() + ", score:" + next.getScore());
		}
	}
	
	/**
	 * reverseRange(String key, long start, long end)
	 *     通过索引区间返回有序集合成指定区间内的成员，其中有序集成员按分数值递减(从大到小)顺序排列。
	 */
	@Test
	public void testReverseRange() {
		Set<String> reverseRange = zsetOps.reverseRange("zset01", 0, -1);
		System.out.println(reverseRange);
	}
	
	/**
	 * reverseRangeWithScores(String key, long start, long end)
	 *     通过索引区间返回有序集合成指定区间内的成员对象，其中有序集成员按分数值递减(从大到小)顺序排列。
	 */
	@Test
	public void testReverseRangeWithScore() {
		Set<TypedTuple<String>> reverseRangeWithScores = zsetOps.reverseRangeWithScores("zset01", 0, -1);
		Iterator<TypedTuple<String>> iterator = reverseRangeWithScores.iterator();
		while(iterator.hasNext()) {
			TypedTuple<String> next = iterator.next();
			System.out.println("value:" + next.getValue() + ", score:" + next.getScore());
		}
	}
	
	/**
	 * reverseRangeByScore(String key, double min, double max)
	 *     与rangeByScore调用方法一样，其中有序集成员按分数值递减(从大到小)顺序排列。
	 */
	@Test
	public void testReverseRangeByScoreOne() {
		Set<String> reverseRangeByScore = zsetOps.reverseRangeByScore("zset01", 0.0, 4.0);
		System.out.println(reverseRangeByScore);
	}
	
	/**
	 * reverseRangeByScore(String key, double min, double max, long offset, long count)
	 *     使用：与rangeByScore调用方法一样，其中有序集成员按分数值递减(从大到小)顺序排列。
	 */
	@Test
	public void testReverseRangeByScoreTwo() {
		Set<String> reverseRangeByScore = zsetOps.reverseRangeByScore("zset01", 0.0, 4.0, 0, 3);
		System.out.println(reverseRangeByScore);
	}
	
	/**
	 * reverseRangeByScoreWithScores(String key, double min, double max)
	 *     使用：与rangeByScoreWithScores调用方法一样，其中有序集成员按分数值递减(从大到小)顺序排列。
	 */
	@Test
	public void testReverseRangeByScoreWithScoresOne() {
		Set<TypedTuple<String>> reverseRangeByScoreWithScores = zsetOps.reverseRangeByScoreWithScores("zset01", 0.0, 4.0);
		System.out.println(reverseRangeByScoreWithScores);
	}
	
	/**
	 * reverseRangeByScoreWithScores(String key, double min, double max, long offset, long count)
	 *     使用：与rangeByScoreWithScores调用方法一样，其中有序集成员按分数值递减(从大到小)顺序排列。
	 */
	@Test
	public void testReverseRangeByScoreWithScoresTwo() {
		Set<TypedTuple<String>> reverseRangeByScoreWithScores = zsetOps.reverseRangeByScoreWithScores("zset01", 0.0, 4.0, 0, 3);
		System.out.println(reverseRangeByScoreWithScores);
	}
	
	/**
	 * count(String key, double min, double max)
	 *     通过分数返回有序集合指定区间内的成员个数。
	 */
	@Test
	public void testCount() {
		Long count = zsetOps.count("zset01", 0.0, 3.0);
		System.out.println(count);
	}
	
	/**
	 * Long size(K key)
	 *     获取有序集合的成员数，内部调用的就是zCard方法。
	 */
	@Test
	public void testSize() {
		Long size = zsetOps.size("zset01");
		System.out.println(size);
	}
	
	/**
	 * zCard(String key)
	 *     获取有序集合的成员数。
	 */
	@Test
	public void testZCard() {
		Long zCard = zsetOps.zCard("zset01");
		System.out.println(zCard);
	}
	
	/**
	 * score(String key, Object o)
	 *     获取指定成员的score值。
	 */
	@Test
	public void testScore() {
		Double score = zsetOps.score("zset01", "one");
		System.out.println(score);
	}
	
	/**
	 * remove(String key, Object... values)
	 *     从有序集合中移除一个或者多个元素。
	 */
	@Test
	public void testRemove() {
		Long removeCount = zsetOps.remove("zset01", "one", "two");
		System.out.println(removeCount);
	}
	
	/**
	 * removeRange(String key, long start, long end)
	 *     移除指定索引位置的成员，其中有序集成员按分数值递增(从小到大)顺序排列。
	 */
	@Test
	public void testRemoveRange() {
		Long removeRange = zsetOps.removeRange("zset01", 0, -1);
		System.out.println(removeRange);
	}
	
	/**
	 * removeRangeByScore(String key, double min, double max)
	 *     根据指定的score值得范围来移除成员。
	 */
	@Test
	public void testRemoveRangeByScore() {
		Long removeRangeByScore = zsetOps.removeRangeByScore("zset01", 0.0, 4.0);
		System.out.println(removeRangeByScore);
	}
	
	/**
	 * Long unionAndStore(K key, K otherKey, K destKey)
	 *     计算给定的一个有序集的并集，并存储在新的 destKey中，key相同的话会把score值相加。
	 */
	@Test
	public void testUnionAndStoreOne() {
		System.out.println(zsetOps.add("zzset1", "zset-1", 1.0));
		System.out.println(zsetOps.add("zzset1", "zset-2", 2.0));
		System.out.println(zsetOps.add("zzset1", "zset-3", 3.0));
		System.out.println(zsetOps.add("zzset1", "zset-4", 6.0));

		System.out.println(zsetOps.add("zzset2", "zset-1", 1.0));
		System.out.println(zsetOps.add("zzset2", "zset-2", 2.0));
		System.out.println(zsetOps.add("zzset2", "zset-3", 3.0));
		System.out.println(zsetOps.add("zzset2", "zset-4", 6.0));
		System.out.println(zsetOps.add("zzset2", "zset-5", 7.0));
		System.out.println(zsetOps.unionAndStore("zzset1", "zzset2", "destZset11"));

		Set<ZSetOperations.TypedTuple<String>> tuples = zsetOps.rangeWithScores("destZset11", 0, -1);
		Iterator<ZSetOperations.TypedTuple<String>> iterator = tuples.iterator();
		while (iterator.hasNext()) {
			ZSetOperations.TypedTuple<String> typedTuple = iterator.next();
			System.out.println("value:" + typedTuple.getValue() + "score:" + typedTuple.getScore());
		}
	}
	
	/**
	 * Long unionAndStore(K key, Collection<K> otherKeys, K destKey)
	 *     计算给定的多个有序集的并集，并存储在新的 destKey中。
	 */
	@Test
	public void testUnionAndStoreTwo() {
		// System.out.println(zsetOps.add("zzset1","zset-1",1.0));
		// System.out.println(zsetOps.add("zzset1","zset-2",2.0));
		// System.out.println(zsetOps.add("zzset1","zset-3",3.0));
		// System.out.println(zsetOps.add("zzset1","zset-4",6.0));
		//
		// System.out.println(zsetOps.add("zzset2","zset-1",1.0));
		// System.out.println(zsetOps.add("zzset2","zset-2",2.0));
		// System.out.println(zsetOps.add("zzset2","zset-3",3.0));
		// System.out.println(zsetOps.add("zzset2","zset-4",6.0));
		// System.out.println(zsetOps.add("zzset2","zset-5",7.0));

		System.out.println(zsetOps.add("zzset3", "zset-1", 1.0));
		System.out.println(zsetOps.add("zzset3", "zset-2", 2.0));
		System.out.println(zsetOps.add("zzset3", "zset-3", 3.0));
		System.out.println(zsetOps.add("zzset3", "zset-4", 6.0));
		System.out.println(zsetOps.add("zzset3", "zset-5", 7.0));

		List<String> stringList = new ArrayList<String>();
		stringList.add("zzset2");
		stringList.add("zzset3");
		System.out.println(zsetOps.unionAndStore("zzset1", stringList, "destZset22"));

		Set<ZSetOperations.TypedTuple<String>> tuples = zsetOps.rangeWithScores("destZset22", 0, -1);
		Iterator<ZSetOperations.TypedTuple<String>> iterator = tuples.iterator();
		while (iterator.hasNext()) {
			ZSetOperations.TypedTuple<String> typedTuple = iterator.next();
			System.out.println("value:" + typedTuple.getValue() + "score:" + typedTuple.getScore());
		}
	}
	
	/**
	 * Long intersectAndStore(K key, K otherKey, K destKey)
	 *     计算给定的一个或多个有序集的交集并将结果集存储在新的有序集合 key 中。
	 */
	@Test
	public void testIntersectAndStoreOne() {
		System.out.println(zsetOps.intersectAndStore("zzset1", "zzset2", "destZset33"));

		Set<ZSetOperations.TypedTuple<String>> tuples = zsetOps.rangeWithScores("destZset33", 0, -1);
		Iterator<ZSetOperations.TypedTuple<String>> iterator = tuples.iterator();
		while (iterator.hasNext()) {
			ZSetOperations.TypedTuple<String> typedTuple = iterator.next();
			System.out.println("value:" + typedTuple.getValue() + "score:" + typedTuple.getScore());
		}
	}
	
	/**
	 * Long intersectAndStore(K key, Collection<K> otherKeys, K destKey)
	 *     计算给定的一个或多个有序集的交集并将结果集存储在新的有序集合 key 中
	 */
	@Test
	public void testIntersectAndStoreTwo() {
		List<String> stringList = new ArrayList<String>();
		stringList.add("zzset2");
		stringList.add("zzset3");
		System.out.println(zsetOps.intersectAndStore("zzset1", stringList, "destZset44"));

		Set<ZSetOperations.TypedTuple<String>> tuples = zsetOps.rangeWithScores("destZset44", 0, -1);
		Iterator<ZSetOperations.TypedTuple<String>> iterator = tuples.iterator();
		while (iterator.hasNext()) {
			ZSetOperations.TypedTuple<String> typedTuple = iterator.next();
			System.out.println("value:" + typedTuple.getValue() + "score:" + typedTuple.getScore());
		}
	}
	
	/**
	 * Cursor<TypedTuple<V>> scan(K key, ScanOptions options)
	 *     遍历zset
	 */
	@Test
	public void testScan() {
		Cursor<TypedTuple<String>> cursor = zsetOps.scan("zzset1", ScanOptions.NONE);
		while (cursor.hasNext()) {
			ZSetOperations.TypedTuple<String> item = cursor.next();
			System.out.println(item.getValue() + ":" + item.getScore());
		}
	}
	
}
