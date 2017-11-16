package com.wez.mybatis.dao;

import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import com.wez.mybatis.entity.Goods;

/**
 * 测试mybatis一级缓存 - SqlSession
 * 说明：这个测试需要配置log4j日志配置，通过mybatis打印的sql查询日志，才能判断测试的效果。
 * 
 * @author Administrator
 * 2017年11月16日
 *
 */
public class TestGoodsDao {
	
	private static SqlSessionFactory ssf;
	
	@BeforeClass
	public static void init() {
		InputStream inputStream = TestGoodsDao.class.getResourceAsStream("/mybatis-config.xml");
		ssf = new SqlSessionFactoryBuilder().build(inputStream);
	}

	/**
	 * 测试一级缓存
	 */
	@Test
	public void testSqlSession() {
		// 获取SQLSession对象
		SqlSession sqlSession = ssf.openSession();
		// 获取GoodsDao对象
		GoodsDao goodsDao = sqlSession.getMapper(GoodsDao.class);
		
		/*
		 * 第一次调用时，从数据库中查询，并将数据缓存在一级缓存中
		 * 这次查询时，有查询的日志信息打印出来。
		 */
		// 查询所有记录
		List<Goods> queryForList = goodsDao.queryForList();
		System.out.println(queryForList);
		
		/*
		 * 第二次调用时，直接从缓存中获取
		 * 我们可以在控制台中看到，这次执行查询时，没有查询的日志信息打印出来。
		 */
		// 查询所有记录
		List<Goods> queryForList2 = goodsDao.queryForList();
		System.out.println(queryForList2);
	}
	
	/**
	 * 测试一级缓存的生命周期(一)
	 * a. MyBatis在开启一个数据库会话时，会创建一个新的SqlSession对象，
	 * SqlSession对象中会有一个新的Executor对象，Executor对象中持有一个新的PerpetualCache对象；
	 * 当会话结束时，SqlSession对象及其内部的Executor对象还有PerpetualCache对象也一并释放掉。
	 */
	@Test
	public void testLifeCycleOne() {
		/*
		 * 这个其实就是SqlSession的一个正常的生命周期结束，当testLifeCycleOne()方法调用完，该方法中的局部变量就会被释放，
		 * 那么SqlSession中的成员变量Executor就会被释放，Executor类中的PerpetualCache那也就被释放了。
		 */
		// 获取SQLSession对象
		SqlSession sqlSession = ssf.openSession();
		// 获取GoodsDao对象
		GoodsDao goodsDao = sqlSession.getMapper(GoodsDao.class);
		// 查询所有记录
		List<Goods> queryForList = goodsDao.queryForList();
		System.out.println(queryForList);
	}
	
	/**
	 * 测试一级缓存的生命周期(二)
	 * b. 如果SqlSession调用了close()方法，会释放掉一级缓存PerpetualCache对象，一级缓存将不可用；
	 */
	@Test
	public void testLifeCycleTwo() {
		// 获取SQLSession对象
		SqlSession sqlSession = ssf.openSession();
		// 获取GoodsDao对象
		GoodsDao goodsDao = sqlSession.getMapper(GoodsDao.class);
		// 查询所有记录
		List<Goods> queryForList = goodsDao.queryForList();
		System.out.println(queryForList);
		
		/*
		 * 调用close()
		 * 这里sqlSession对象调用close()方法，在SqlSession接口的实现类DefaultSqlSession源码中，实际是调用的Executor的close()方法，
		 * 而Executor接口的实现类BaseExecutor的close()是将BaseExcutor类中维护一级缓存的PerpetualCache对象设置为了null，从而一级缓存就不能再使用了。
		 */
		// 关闭sqlSession
		sqlSession.close();
		
		/*
		 * 当我们再次查询时，日志信息会提示JDBC连接关闭了，具体提示信息如下：
		 *     [DEBUG] -2017-11-16 -org.apache.ibatis.transaction.jdbc.JdbcTransaction -Closing JDBC Connection [com.mysql.jdbc.JDBC4Connection@58c1670b]
		 */
		List<Goods> queryForList2 = goodsDao.queryForList();
		System.out.println(queryForList2);
	}
	
	/**
	 * 测试一级缓存的生命周期(三)
	 * c. 如果SqlSession调用了clearCache()，会清空PerpetualCache对象中的数据，但是该对象仍可使用；
	 */
	@Test
	public void testLifeCycleThree() {
		// 获取SQLSession对象
		SqlSession sqlSession = ssf.openSession();
		// 获取GoodsDao对象
		GoodsDao goodsDao = sqlSession.getMapper(GoodsDao.class);
		// 查询所有记录
		List<Goods> queryForList = goodsDao.queryForList();
		System.out.println(queryForList);
		
		/*
		 * 调用clearCache()
		 * 这里sqlSession对象调用clearCache()方法，在SqlSession接口的实现类DefaultSqlSession源码中，实际是调用的Executor的clearLocalCache()方法，
		 * 而Executor接口的实现类BaseExecutor的clearLocalCache()是将BaseExcutor类中维护一级缓存的PerpetualCache对象中保存缓存数据的Map对象clear()了。
		 */
		sqlSession.clearCache();
		
		// 让当前线程睡眠3秒。这里是为了将清空缓存操作和再次查询操作中间有个时间的间隔，让实验效果更明显。
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		 * 这时，我们通过控制台可以发现，这次掉用查询方法会重新执行查询语句，从数据库中查询记录。
		 */
		List<Goods> queryForList2 = goodsDao.queryForList();
		System.out.println(queryForList2);
	}
	
	/**
	 * 测试一级缓存的生命周期(四)
	 * d.SqlSession中执行了任何一个update操作(update()、delete()、insert())，都会清空PerpetualCache对象的数据，但是该对象可以继续使用；
	 */
	@Test
	public void testLifeCycleFour() {
		// 获取SQLSession对象
		SqlSession sqlSession = ssf.openSession();
		// 获取GoodsDao对象
		GoodsDao goodsDao = sqlSession.getMapper(GoodsDao.class);
		// 查询所有记录
		List<Goods> queryForList = goodsDao.queryForList();
		System.out.println(queryForList);
		
		/*
		 * 这里指定了update操作，会情况上面执行查询记录时缓存数据。
		 */
		// 执行update操作
		testModifyGoods();
		
		/*
		 * 这里再次执行查询语句，因为上面执行了update操作，清空了一级缓存，所有这次执行操作的时候，会重新执行查询语句，访问数据库，然后在将数据缓存起来。
		 */
		List<Goods> queryForList2 = goodsDao.queryForList();
		System.out.println(queryForList2);
		
	}
	
	/**
	 * 保存
	 */
	public void testSaveGoods() {
		Goods goods = new Goods();
		goods.setShopId(202003);
		goods.setGoodsName("苹果");
		goods.setGoodsPrice(8.9);
		goods.setGoodsStock(100);
		
		SqlSession sqlSession = ssf.openSession();
		
		GoodsDao goodsDao = sqlSession.getMapper(GoodsDao.class);
		int affectRow = goodsDao.saveGoods(goods);
		
		sqlSession.commit();
		
		System.out.println(affectRow);
	}
	
	/**
	 * 修改
	 */
	public void testModifyGoods() {
		Goods goods = new Goods();
		goods.setGoodsId(303011);
		goods.setShopId(202003);
		goods.setGoodsName("苹果");
		goods.setGoodsPrice(8.9);
		goods.setGoodsStock(50);
		
		SqlSession sqlSession = ssf.openSession();
		
		GoodsDao goodsDao = sqlSession.getMapper(GoodsDao.class);
		int affectRow = goodsDao.modifyGoods(goods);
		
		sqlSession.commit();
		
		System.out.println(affectRow);
	}
	
}
