/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.model.product;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 扩展商品信息
 * 
 * @author dianchao.song
 */
@Entity
@Table(name = "T_PD_ITEMINFO",uniqueConstraints = { @UniqueConstraint(columnNames = { "ITEM_ID" }) })
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ItemInfo extends BaseModel{

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= 882446624240577496L;
	
	public static final Integer TYPE_GIFT =0;
	public static final Integer TYPE_MAIN =1;

	/** PK. */
	private Long				id;

	/**
	 * 商品
	 */
	private Long				itemId;

	/**
	 * 语言
	 */
	private String				language;

	/**
	 * 商品名称
	 */
	private String				title;

	/**
	 * 副标题
	 */
	private String				subTitle;

	/** 商品概述 . */
	private String				sketch;

	/** 商品详细描述 */
	private String				description;

	/**
	 * 销售价
	 */
	private BigDecimal			salePrice;

	/**
	 * 吊牌价(原单价)
	 */
	private BigDecimal			listPrice;

	/** 创建时间. */
	private Date				createTime;
	
	/** 上架时间 */
	private Date				activeBeginTime;

	/**
	 * 修改时间
	 */
	private Date				modifyTime;

	/**
	 * version
	 */
	private Date				version;

	/**
	 * 0代表赠品 1代表主卖品
	 */
	private Integer				type;

	/**
	 * seo搜索描述
	 */
	private String				seoDescription;

	/**
	 * seo搜索关键字
	 */
	private String				seoKeywords;

	/**
	 * seoTitle
	 */
	private String				seoTitle;
	
	/**
	 * 分组style
	 */
	private String				style;

	/**
	 * 最后选择的属性Id
	 */
	@Deprecated
	private Long				lastSelectPropertyId;

	/**
	 * 最后选择的属性值Id
	 */
	@Deprecated
	private Long				lastSelectPropertyValueId;

	/**
	 * Gets the pK.
	 * 
	 * @return the pK
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_ITEMINFO",sequenceName = "S_T_PD_ITEMINFO",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PD_ITEMINFO")
	public Long getId(){
		return id;
	}

	/**
	 * Sets the pK.
	 * 
	 * @param id
	 *            the new pK
	 */
	public void setId(Long id){
		this.id = id;
	}

	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	@Version
	@Column(name = "VERSION")
	public Date getVersion(){
		return version;
	}

	/**
	 * Sets the version.
	 * 
	 * @param version
	 */
	public void setVersion(Date version){
		this.version = version;
	}

	/**
	 * Gets the 创建时间.
	 * 
	 * @return the createTime
	 */
	@Column(name = "CREATE_TIME")
	public Date getCreateTime(){
		return createTime;
	}

	/**
	 * Sets the 创建时间.
	 * 
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	@Column(name = "MODIFY_TIME")
	public Date getModifyTime(){
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime){
		this.modifyTime = modifyTime;
	}

	@Column(name = "ITEM_ID")
	@Index(name = "IDX_ITEM_ID")
	public Long getItemId(){
		return itemId;
	}

	public void setItemId(Long itemId){
		this.itemId = itemId;
	}

	@Column(name = "TITLE")
	public String getTitle(){
		return title;
	}

	public void setTitle(String title){
		this.title = title;
	}

	@Column(name = "SUB_TITLE")
	public String getSubTitle(){
		return subTitle;
	}

	public void setSubTitle(String subTitle){
		this.subTitle = subTitle;
	}

	@Column(name = "SKETCH", length=2000)
	public String getSketch(){
		return sketch;
	}

	public void setSketch(String sketch){
		this.sketch = sketch;
	}

	@Column(name = "DESCRIPTION")
	@Lob
	public String getDescription(){
		return description;
	}

	public void setDescription(String description){
		this.description = description;
	}

	@Column(name = "LANGUAGE")
	public String getLanguage(){
		return language;
	}

	public void setLanguage(String language){
		this.language = language;
	}

	@Column(name = "TYPE")
    @Index(name = "IDX_ITEMINFO_TYPE")
	public Integer getType(){
		return type;
	}

	public void setType(Integer type){
		this.type = type;
	}

	@Column(name = "SALE_PRICE")
	public BigDecimal getSalePrice(){
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice){
		this.salePrice = salePrice;
	}

	@Column(name = "LIST_PRICE")
	public BigDecimal getListPrice(){
		return listPrice;
	}

	public void setListPrice(BigDecimal listPrice){
		this.listPrice = listPrice;
	}

	@Column(name = "LAST_SELECT_PROPERTY_ID")
	public Long getLastSelectPropertyId(){
		return lastSelectPropertyId;
	}

	public void setLastSelectPropertyId(Long lastSelectPropertyId){
		this.lastSelectPropertyId = lastSelectPropertyId;
	}

	@Column(name = "LAST_SELECT_PROPERTY_VALUE_ID")
	public Long getLastSelectPropertyValueId(){
		return lastSelectPropertyValueId;
	}

	public void setLastSelectPropertyValueId(Long lastSelectPropertyValueId){
		this.lastSelectPropertyValueId = lastSelectPropertyValueId;
	}

	@Column(name = "SEODESCRIPTION", length=2000)
	public String getSeoDescription(){
		return seoDescription;
	}

	
	public void setSeoDescription(String seoDescription){
		this.seoDescription = seoDescription;
	}

	@Column(name = "SEOKEYWORDS")
	public String getSeoKeywords(){
		return seoKeywords;
	}

	
	public void setSeoKeywords(String seoKeywords){
		this.seoKeywords = seoKeywords;
	}

	@Column(name = "SEOTITLE")
	public String getSeoTitle(){
		return seoTitle;
	}
	
	public void setSeoTitle(String seoTitle){
		this.seoTitle = seoTitle;
	}

	public void setStyle(String sytle) {
		this.style = sytle;
	}

	@Column(name = "STYLE")
    @Index(name = "IDX_ITEMINFO_STYLE")
	public String getStyle() {
		return style;
	}

	public void setActiveBeginTime(Date activeBeginTime) {
		this.activeBeginTime = activeBeginTime;
	}

	@Column(name = "ACTIVE_BEGIN_TIME")
	public Date getActiveBeginTime() {
		return activeBeginTime;
	}

}
