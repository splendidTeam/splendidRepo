package com.baozun.nebula.command;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;

import com.baozun.nebula.utilities.common.Validator;

public class BaseItemForSolrCommand implements Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8426112092301036502L;

	/** PK. */
	private Long id;

	/**
	 * 商品编码
	 */
	private String code;
	
	/**
	 * 商品名称
	 */
	private String title;
	
	/**
	 * 副标题
	 */
	private String subTitle;
	
	/**
	 * 商品概述
	 */
	private String sketch;
	
	/**
	 * 商品详细描述
	 */
	private String description;
	

	/**
	 * 商店或供货商名称
	 */
	private String shopName;

	/**
	 * 商店或供货商id
	 */
	private Long shopId;

	/**
	 * 行业
	 */
	private Long industryId;
	
	/**
	 * 行业排序
	 */
	private Integer industrySortNo;

	/**
	 * 行业
	 */
	private String industryName;

	/**
	 * 生命周期
	 */
	private Integer lifecycle;

	/** 创建时间. */
	private Date createTime;
	
	/** 吊牌价 */
	private Double list_price;
	
	/** 销售价 */
	private Double sale_price;

	/**
	 * 修改时间
	 */
	private Date modifyTime;

	/**
	 * 上架时间
	 */
	private Date listTime;

	/**
	 * 下架时间
	 */
	private Date delistTime;

	/**
	 * 页面模版
	 */
	private Long templateId;

	/**
	 * 是否已绑定分类:1表示已加入 否则未加入
	 */
	private Integer isaddcategory;

	/**
	 * 是否已绑定标签:1表示已加入 0表示未加入
	 */
	private Integer isAddTag;
	
	/**
	 * 活跃开始时间
	 */
	private Date activeBeginTime;
	
	/**
	 * 活跃结束时间
	 */
	private Date activeEndTime;
	
	/**
	 * 是否显示
	 */
	private Boolean itemIsDisplay;
	
	/**
	 * seo搜索关键字
	 */
	private String seoKeywords;
	
	/**
	 * seo搜索关键字
	 */
	private String seoDescription;
	
	/**
	 * seoTitle
	 */
	private String seoTitle;
	
	/**
	 * style
	 * @return
	 */
	private String style;
	
	/**
	 * 商品类型
	 * @see com.baozun.nebula.model.product.Item#type
	 */
	private Long type;

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getSketch() {
		return sketch;
	}

	public void setSketch(String sketch) {
		this.sketch = sketch;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getIndustryId() {
		return industryId;
	}

	public void setIndustryId(Long industryId) {
		this.industryId = industryId;
	}

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Double getList_price() {
		return list_price;
	}

	public void setList_price(Double list_price) {
		this.list_price = list_price;
	}

	public Double getSale_price() {
		return sale_price;
	}

	public void setSale_price(Double sale_price) {
		this.sale_price = sale_price;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Date getListTime() {
		return listTime;
	}

	public void setListTime(Date listTime) {
		this.listTime = listTime;
	}

	public Date getDelistTime() {
		return delistTime;
	}

	public void setDelistTime(Date delistTime) {
		this.delistTime = delistTime;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public Integer getIsaddcategory() {
		return isaddcategory;
	}

	public void setIsaddcategory(Integer isaddcategory) {
		this.isaddcategory = isaddcategory;
	}

	public Integer getIsAddTag() {
		return isAddTag;
	}

	public void setIsAddTag(Integer isAddTag) {
		this.isAddTag = isAddTag;
	}

	public Integer getIndustrySortNo() {
		return industrySortNo;
	}

	public void setIndustrySortNo(Integer industrySortNo) {
		this.industrySortNo = industrySortNo;
	}

	public Boolean getItemIsDisplay() {
		return itemIsDisplay;
	}

	public void setItemIsDisplay(Boolean itemIsDisplay) {
		this.itemIsDisplay = itemIsDisplay;
	}

	public Date getActiveBeginTime() {
		return activeBeginTime;
	}

	public void setActiveBeginTime(Date activeBeginTime) {
		this.activeBeginTime = activeBeginTime;
	}

	public Date getActiveEndTime() {
		return activeEndTime;
	}

	public void setActiveEndTime(Date activeEndTime) {
		this.activeEndTime = activeEndTime;
	}

	public String getSeoKeywords() {
		return seoKeywords;
	}

	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}

	public String getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
	
	
	
}
