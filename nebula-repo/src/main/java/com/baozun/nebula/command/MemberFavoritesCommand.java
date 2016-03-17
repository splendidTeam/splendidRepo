package com.baozun.nebula.command;

import java.util.Date;
import java.util.List;

import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.sdk.command.SkuProperty;

public class MemberFavoritesCommand implements Command {

	private static final long	serialVersionUID	= -2680553231535957091L;

	/**
	 * 收藏Id
	 */
	private Long				id;

	/**
	 * 属性值，用于表征商品和SKU基于销售属性的划分对照，其标准格式目前定义为：   现在更改为([商品属性值id];)*([商品属性值id])  
	 * 这里的商品属性值id为ItemProperties实体的id
	 */
	private String				properties;

	/**
	 * sku所对应的销售属性的中文名字串，格式如：pid1:vid1:pid_name1:vid_name1;pid2:vid2:pid_name2: vid_name2……
	 */
	@Deprecated
	private String				propertiesName;

	/** 会员ID */
	private Long				memberId;

	/** 收藏时间 */
	private Date				createTime;

	/** 商品ID */
	private Long				itemId;

	private String				itemCode;

	/** 商品名称 */
	private String				itemName;

	/** 商品价格 */
	private Double				salePrice;

	/**
	 * 商品吊牌价
	 */
	private Double				listPrice;

	/**
	 * 生命周期
	 */
	private Integer				lifecycle;

	/**
	 * skuId
	 */
	private Long				skuId;

	private String				picUrl;

	/**
	 * 商品图片
	 */
	private List<ItemImage>		itemImageList;

	/** 销售属性 **/
	private List<SkuProperty>	skuPropertys;

	/** 和oms 沟通交互的 唯一编码,extension1. */
	private String				extentionCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getPropertiesName() {
		return propertiesName;
	}

	public void setPropertiesName(String propertiesName) {
		this.propertiesName = propertiesName;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	public Double getListPrice() {
		return listPrice;
	}

	public void setListPrice(Double listPrice) {
		this.listPrice = listPrice;
	}

	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public List<ItemImage> getItemImageList() {
		return itemImageList;
	}

	public void setItemImageList(List<ItemImage> itemImageList) {
		this.itemImageList = itemImageList;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public List<SkuProperty> getSkuPropertys() {
		return skuPropertys;
	}

	public void setSkuPropertys(List<SkuProperty> skuPropertys) {
		this.skuPropertys = skuPropertys;
	}

	public String getExtentionCode() {
		return extentionCode;
	}

	public void setExtentionCode(String extentionCode) {
		this.extentionCode = extentionCode;
	}
}
