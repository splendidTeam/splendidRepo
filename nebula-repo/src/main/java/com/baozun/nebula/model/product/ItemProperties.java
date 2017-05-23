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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 商品属性值
 * 
 * @author dianchao.song
 */
@Entity
@Table(name = "T_PD_ITEM_PROPERTIES")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ItemProperties extends BaseModel{

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= 5156981146856978609L;

	/** PK. */
	private Long				id;

	/**
	 * 商品属性值
	 */
	private String				propertyValue;

	/**
	 * 商品属性显示值
	 */
//	private String				propertyDisplayValue;

	/**
	 * 配图URL
	 */
	private String				picUrl;

	/** 创建时间. */
	private Date				createTime;

	/** 修改时间 */
	private Date				modifyTime;

	/** version. */
	private Date				version;

	/**
	 * 所属商品
	 */
	private Long				itemId;

	/**
	 * 所属属性
	 */
	private Long				propertyId;

	/**
	 * 关联商品可选值
	 */
	private Long				propertyValueId;

	/**
	 * Gets the pK.
	 * 
	 * @return the pK
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_ITEM_PROPERTIES",sequenceName = "SEQ_T_PD_ITEM_PROPERTIES",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PD_ITEM_PROPERTIES")
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

	public void setModifyTime(Date modifyTime){
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_TIME")
	public Date getModifyTime(){
		return modifyTime;
	}

	@Column(name = "picUrl",length = 150)
	public String getPicUrl(){
		return picUrl;
	}

	public void setPicUrl(String picUrl){
		this.picUrl = picUrl;
	}

	@Column(name = "PROPERTY_VALUE",length=1024)
	public String getPropertyValue(){
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue){
		this.propertyValue = propertyValue;
	}

//	@Column(name = "PROPERTY_DISPLAYVALUE")
//	public String getPropertyDisplayValue(){
//		return propertyDisplayValue;
//	}
//
//	public void setPropertyDisplayValue(String propertyDisplayValue){
//		this.propertyDisplayValue = propertyDisplayValue;
//	}

	@Column(name = "ITEM_ID")
    @Index(name = "IDX_ITEM_PROPERTIES_ITEM_ID")
	public Long getItemId(){
		return itemId;
	}

	public void setItemId(Long itemId){
		this.itemId = itemId;
	}

	@Column(name = "PROPERTY_ID")
    @Index(name = "IDX_ITEM_PROPERTIES_PROPERTY_ID")
	public Long getPropertyId(){
		return propertyId;
	}

	public void setPropertyId(Long propertyId){
		this.propertyId = propertyId;
	}

	@Column(name = "PROPERTYVALUE_ID")
    @Index(name = "IDX_ITEM_PROPERTIES_PROPERTYVALUE_ID")
	public Long getPropertyValueId(){
		return propertyValueId;
	}

	public void setPropertyValueId(Long propertyValueId){
		this.propertyValueId = propertyValueId;
	}

}
