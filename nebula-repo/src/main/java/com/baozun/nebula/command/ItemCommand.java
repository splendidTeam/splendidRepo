/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.command;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baozun.nebula.model.product.ItemImage;

/**
 * @author lin.liu
 */
public class ItemCommand implements Command{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2544112989421233359L;

	/** PK. */
	private Long				id;

	private Long				shopId;

	/**
	 * 商品描述
	 */
	private String				description;

	/**
	 * 商品概述
	 */
	private String				sketch;

	/**
	 * 行业Id
	 */
	private String				industryId;

	/**
	 * 商品编码
	 */
	private String				code;

	/**
	 * 商品名称
	 */
	private String				title;

	/**
	 * 商品副标题
	 */
	private String				subTitle;

	/** 行业名称 */
	private String				industryName;

	/**
	 * 销售价格
	 */
	private BigDecimal			salePrice;

	/**
	 * 吊牌价
	 */
	private BigDecimal			listPrice;

	/** 分类名称 */
	private List<String>		categoryNames;

	/** 分类编码 */
	private List<String>		categoryCodes;

	/** 标签名称 */
	private List<String>		tagNames;

	/** 创建时间 */
	private Date				createTime;

	/** 修改时间 */
	private Date				modifyTime;

	/** version. */
	private Date				version;

	/** 闄勫姞灞炴� */
	private Map<String, String>	additionalAttributes;

	/**
	 * 涓婃灦鏃堕棿
	 */
	private Date				listTime;

	/**
	 * 生命周期
	 */
	private Integer				lifecycle;

	/**
	 * 灞炴�鍊糺son
	 */
	private String				jsonSku;

	/**
	 * 鍟嗗搧寮�娲昏穬鏃堕棿
	 * 
	 * @return
	 */
	private Date				activeBeginTime;

	/**
	 * 鍟嗗搧缁堟娲昏穬鏃堕棿
	 * 
	 * @return
	 */
	private Date				activeEndTime;

	/**
	 * 娓犻亾Id
	 * 
	 * @return channels
	 */
	private List<String>		channels;

	/**
	 * 销量
	 * 
	 * @return
	 */
	private Integer				salesCount;

	/**
	 * 分类是否是商品的默认分类
	 */
	private Boolean				isDefault;

	/**
	 * seo搜索描述
	 */
	private String				seoDescription;

	/**
	 * seo title
	 */
	private String				seoTitle;

	private String				style;

	/**
	 * seo搜索关键字
	 */
	private String				seoKeywords;

	private String				picUrl;

	private String				itemUrl;

	private String				suitItem;

	private List<ItemImage>		itemImageList;

	/**
	 * 0非卖品，赠品 1或者null为，正常商品
	 */
	private Integer				type;

	/**
	 * 商品图片个数
	 */
	private Integer				imageCount;

	/**
	 * 商品评分
	 */
	private Float				rankavg;

	private String				defCategory;

	private Long				defCategroyId;

	/**
	 * 商品库存（该商品下所有UPC的累加可用库存）
	 */
	private Integer				inventory;

	/**
	 * 商品类型
	 * 
	 * @see com.baozun.nebula.model.product.Item#type
	 */
	private Integer				itemType;

	/** 被用户收藏的次数 */
	private Integer				wishedNum;

	/** 如果会员登录了，判断是否收藏了此商品 */
	private boolean				wished;

	public Integer getInventory(){
		return inventory;
	}

	public void setInventory(Integer inventory){
		this.inventory = inventory;
	}

	public Integer getItemType(){
		return itemType;
	}

	public void setItemType(Integer itemType){
		this.itemType = itemType;
	}

	public BigDecimal getSalePrice(){
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice){
		this.salePrice = salePrice;
	}

	public BigDecimal getListPrice(){
		return listPrice;
	}

	public void setListPrice(BigDecimal listPrice){
		this.listPrice = listPrice;
	}

	public String getJsonSku(){
		return jsonSku;
	}

	public void setJsonSku(String jsonSku){
		this.jsonSku = jsonSku;
	}

	public List<String> getCategoryNames(){
		return categoryNames;
	}

	public void setCategoryNames(List<String> categoryNames){
		this.categoryNames = categoryNames;
	}

	public List<String> getCategoryCodes(){
		return categoryCodes;
	}

	public void setCategoryCodes(List<String> categoryCodes){
		this.categoryCodes = categoryCodes;
	}

	public Integer getLifecycle(){
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle){
		this.lifecycle = lifecycle;
	}

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public String getCode(){
		return code;
	}

	public String getDescription(){
		return description;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getIndustryId(){
		return industryId;
	}

	public void setIndustryId(String industryId){
		this.industryId = industryId;
	}

	public void setCode(String code){
		this.code = code;
	}

	public String getTitle(){
		return title;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getIndustryName(){
		return industryName;
	}

	public void setIndustryName(String industryName){
		this.industryName = industryName;
	}

	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public Date getModifyTime(){
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime){
		this.modifyTime = modifyTime;
	}

	public Date getVersion(){
		return version;
	}

	public void setVersion(Date version){
		this.version = version;
	}

	public Date getListTime(){
		return listTime;
	}

	public void setListTime(Date listTime){
		this.listTime = listTime;
	}

	public List<String> getTagNames(){
		return tagNames;
	}

	public void setTagNames(List<String> tagNames){
		this.tagNames = tagNames;
	}

	public Date getActiveBeginTime(){
		return activeBeginTime;
	}

	public void setActiveBeginTime(Date activeBeginTime){
		this.activeBeginTime = activeBeginTime;
	}

	public Date getActiveEndTime(){
		return activeEndTime;
	}

	public void setActiveEndTime(Date activeEndTime){
		this.activeEndTime = activeEndTime;
	}

	public List<String> getChannels(){
		return channels;
	}

	public void setChannels(List<String> channels){
		this.channels = channels;
	}

	public Map<String, String> getAdditionalAttributes(){
		return additionalAttributes;
	}

	public void setAdditionalAttributes(Map<String, String> additionalAttributes){
		this.additionalAttributes = additionalAttributes;
	}

	public String getSketch(){
		return sketch;
	}

	public void setSketch(String sketch){
		this.sketch = sketch;
	}

	public Boolean getIsDefault(){
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault){
		this.isDefault = isDefault;
	}

	public String getSeoDescription(){
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription){
		this.seoDescription = seoDescription;
	}

	public String getSeoTitle(){
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle){
		this.seoTitle = seoTitle;
	}

	public String getSeoKeywords(){
		return seoKeywords;
	}

	public void setSeoKeywords(String seoKeywords){
		this.seoKeywords = seoKeywords;
	}

	public void setShopId(Long shopId){
		this.shopId = shopId;
	}

	public Long getShopId(){
		return shopId;
	}

	public String getPicUrl(){
		return picUrl;
	}

	public void setPicUrl(String picUrl){
		this.picUrl = picUrl;
	}

	public void setItemUrl(String itemUrl){
		this.itemUrl = itemUrl;
	}

	public String getItemUrl(){
		return itemUrl;
	}

	public void setRankavg(Float rankavg){
		this.rankavg = rankavg;
	}

	public Float getRankavg(){
		return rankavg;
	}

	public Integer getSalesCount(){
		return salesCount;
	}

	public void setSalesCount(Integer salesCount){
		this.salesCount = salesCount;
	}

	public List<ItemImage> getItemImageList(){
		return itemImageList;
	}

	public void setItemImageList(List<ItemImage> itemImageList){
		this.itemImageList = itemImageList;
	}

	public void setStyle(String style){
		this.style = style;
	}

	public String getStyle(){
		return style;
	}

	public Integer getImageCount(){
		return imageCount;
	}

	public void setImageCount(Integer imageCount){
		this.imageCount = imageCount;
	}

	public String getSuitItem(){
		return suitItem;
	}

	public void setSuitItem(String suitItem){
		this.suitItem = suitItem;
	}

	public String getSubTitle(){
		return subTitle;
	}

	public void setSubTitle(String subTitle){
		this.subTitle = subTitle;
	}

	public Integer getType(){
		return type;
	}

	public void setType(Integer type){
		this.type = type;
	}

	public String getDefCategory(){
		return defCategory;
	}

	public void setDefCategory(String defCategory){
		this.defCategory = defCategory;
	}

	public Long getDefCategroyId(){
		return defCategroyId;
	}

	public void setDefCategroyId(Long defCategroyId){
		this.defCategroyId = defCategroyId;
	}

	/**
	 * @return the wishedNum
	 */
	public Integer getWishedNum(){
		return wishedNum;
	}

	/**
	 * @param wishedNum
	 *            the wishedNum to set
	 */
	public void setWishedNum(Integer wishedNum){
		this.wishedNum = wishedNum;
	}

	
	/**
	 * @return the wished
	 */
	public boolean getWished(){
		return wished;
	}

	
	/**
	 * @param wished the wished to set
	 */
	public void setWished(boolean wished){
		this.wished = wished;
	}


}
