package com.wez.mybatis.entity;

public class Goods {

	private Integer goodsId;
	
	private Integer shopId;
	
	private String goodsName;
	
	private double goodsPrice;
	
	private int goodsStock;

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public double getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public int getGoodsStock() {
		return goodsStock;
	}

	public void setGoodsStock(int goodsStock) {
		this.goodsStock = goodsStock;
	}

	@Override
	public String toString() {
		return "Goods [goodsId=" + goodsId + ", shopId=" + shopId + ", goodsName=" + goodsName + ", goodsPrice="
				+ goodsPrice + ", goodsStock=" + goodsStock + "]";
	}
	
}
