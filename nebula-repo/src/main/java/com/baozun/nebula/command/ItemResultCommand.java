package com.baozun.nebula.command;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ItemResultCommand implements Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2538381536159681763L;
	
	private List<Long> id;

	/**
	 * 商品编码
	 */
	private List<String> code;
	
	/**
	 * 商品名称
	 */
	private List<String> title;
	
	/**
	 * 副标题
	 */
	private List<String> subTitle;
	
	/**
	 * 商品概述
	 */
	private List<String> sketch;
	
	/**
	 * 商品详细描述
	 */
	private List<String> description;
	

	/**
	 * 商店或供货商名称
	 */
	private List<String> shopName;

	/**
	 * 商店或供货商id
	 */
	private List<Long> shopId;

	/**
	 * 行业
	 */
	private List<Long> industryId;
	
	/**
	 * 行业排序
	 */
	private List<Integer> industrySortNo;

	/**
	 * 行业
	 */
	private List<String> industryName;

	/**
	 * 生命周期
	 */
	private List<Integer> lifecycle;

	/** 创建时间. */
	private List<Date> createTime;
	
	/** 吊牌价 */
	private List<Double> list_price;
	
	/** 销售价 */
	private List<Double> sale_price;

	/**
	 * 修改时间
	 */
	private List<Date> modifyTime;

	/**
	 * 上架时间
	 */
	private List<Date> listTime;

	/**
	 * 下架时间
	 */
	private List<Date> delistTime;
	
	/**
	 * 活跃开始时间
	 */
	private List<Date> activeBeginTime;
	
	/**
	 * 活跃结束时间
	 */
	private List<Date> activeEndTime;
	
	/**
	 * 是否显示
	 */
	private List<Boolean> itemIsDisplay;
	
	/**
	 * 图片
	 */
	private List<List<String>> img;
	
	private List<Float> rankavg;
	
	private List<Integer> salesCount;
	
	private List<String> style;
	
	private List<Integer> favouriteCount;
	
	/**
	 * 商品对应分类
	 */
	private List<Map<String,String>> categoryCodeMap;

	public List<Long> getId() {
		return id;
	}

	public void setId(List<Long> id) {
		this.id = id;
	}

	public List<String> getCode() {
		return code;
	}

	public void setCode(List<String> code) {
		this.code = code;
	}

	public List<String> getTitle() {
		return title;
	}

	public void setTitle(List<String> title) {
		this.title = title;
	}

	public List<String> getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(List<String> subTitle) {
		this.subTitle = subTitle;
	}

	public List<String> getSketch() {
		return sketch;
	}

	public void setSketch(List<String> sketch) {
		this.sketch = sketch;
	}

	public List<String> getDescription() {
		return description;
	}

	public void setDescription(List<String> description) {
		this.description = description;
	}

	public List<String> getShopName() {
		return shopName;
	}

	public void setShopName(List<String> shopName) {
		this.shopName = shopName;
	}

	public List<Long> getShopId() {
		return shopId;
	}

	public void setShopId(List<Long> shopId) {
		this.shopId = shopId;
	}

	public List<Long> getIndustryId() {
		return industryId;
	}

	public void setIndustryId(List<Long> industryId) {
		this.industryId = industryId;
	}

	public List<Integer> getIndustrySortNo() {
		return industrySortNo;
	}

	public void setIndustrySortNo(List<Integer> industrySortNo) {
		this.industrySortNo = industrySortNo;
	}

	public List<String> getIndustryName() {
		return industryName;
	}

	public void setIndustryName(List<String> industryName) {
		this.industryName = industryName;
	}

	public List<Integer> getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(List<Integer> lifecycle) {
		this.lifecycle = lifecycle;
	}

	public List<Date> getCreateTime() {
		return createTime;
	}

	public void setCreateTime(List<Date> createTime) {
		this.createTime = createTime;
	}

	public List<Double> getList_price() {
		return list_price;
	}

	public void setList_price(List<Double> list_price) {
		this.list_price = list_price;
	}

	public List<Double> getSale_price() {
		return sale_price;
	}

	public void setSale_price(List<Double> sale_price) {
		this.sale_price = sale_price;
	}

	public List<Date> getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(List<Date> modifyTime) {
		this.modifyTime = modifyTime;
	}

	public List<Date> getListTime() {
		return listTime;
	}

	public void setListTime(List<Date> listTime) {
		this.listTime = listTime;
	}

	public List<Date> getDelistTime() {
		return delistTime;
	}

	public void setDelistTime(List<Date> delistTime) {
		this.delistTime = delistTime;
	}

	public List<Date> getActiveBeginTime() {
		return activeBeginTime;
	}

	public void setActiveBeginTime(List<Date> activeBeginTime) {
		this.activeBeginTime = activeBeginTime;
	}

	public List<Date> getActiveEndTime() {
		return activeEndTime;
	}

	public void setActiveEndTime(List<Date> activeEndTime) {
		this.activeEndTime = activeEndTime;
	}

	public List<List<String>> getImg() {
		return img;
	}

	public void setImg(List<List<String>> img) {
		this.img = img;
	}

	public List<Boolean> getItemIsDisplay() {
		return itemIsDisplay;
	}

	public void setItemIsDisplay(List<Boolean> itemIsDisplay) {
		this.itemIsDisplay = itemIsDisplay;
	}

	public List<Float> getRankavg() {
		return rankavg;
	}

	public void setRankavg(List<Float> rankavg) {
		this.rankavg = rankavg;
	}

	public List<Integer> getSalesCount() {
		return salesCount;
	}

	public void setSalesCount(List<Integer> salesCount) {
		this.salesCount = salesCount;
	}

	public List<String> getStyle() {
		return style;
	}

	public void setStyle(List<String> style) {
		this.style = style;
	}

	public void setFavouriteCount(List<Integer> favouriteCount) {
		this.favouriteCount = favouriteCount;
	}

	public List<Integer> getFavouriteCount() {
		return favouriteCount;
	}

	public List<Map<String, String>> getCategoryCodeMap() {
		return categoryCodeMap;
	}

	public void setCategoryCodeMap(List<Map<String, String>> categoryCodeMap) {
		this.categoryCodeMap = categoryCodeMap;
	}
	
}
