package com.wez.mybatis.dao;

import java.util.List;

import com.wez.mybatis.entity.Goods;

public interface GoodsDao {

	public int saveGoods(Goods goods);
	
	public int removeGoods(int goodsId);
	
	public int modifyGoods(Goods goods);
	
	public Goods queryForGoods(int goodsId);
	
	public List<Goods> queryForList();
	
}
