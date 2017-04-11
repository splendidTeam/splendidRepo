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
 * 商品分类关系
 * 
 * @author dianchao.song
 */
@Entity
@Table(name = "T_PD_ITEM_CATEGORY")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ItemCategory extends BaseModel{

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= -7817797309630793957L;

	/** PK. */
	private Long				id;

	/**
	 * 商品
	 */
	private Long				itemId;

	/** 分类 */
	private Long				categoryId;

	/** 顺序. */
	private Integer				sortNo;

	/** 创建时间. */
	private Date				createTime;

	/** 修改时间 */
	private Date				modifyTime;
	
	/**
	 * 是否默认分类
	 */
	private Boolean				isDefault;

	/** version. */
	private Date				version;

	/**
	 * Gets the pK.
	 * 
	 * @return the pK
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_t_pd_item_category",sequenceName = "S_t_pd_item_category",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_t_pd_item_category")
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
	 *            the new version
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

	/**
	 * @param itemId
	 *            the itemId to set
	 */
	public void setItemId(Long itemId){
		this.itemId = itemId;
	}

	/**
	 * @return the itemId
	 */
	@Index(name = "IDX_ITEMCATEGORY_ITEMID")
	@Column(name = "ITEM_ID")
	public Long getItemId(){
		return itemId;
	}

	/**
	 * @return the categoryId
	 */
	@Index(name = "IDX_ITEMCATEGORY_CATEGORYID")
	@Column(name = "CATEGORY_ID")
	public Long getCategoryId(){
		return categoryId;
	}

	/**
	 * @param categoryId
	 *            the categoryId to set
	 */
	public void setCategoryId(Long categoryId){
		this.categoryId = categoryId;
	}

	@Column(name = "SORT_NO")
	public Integer getSortNo(){
		return sortNo;
	}

	public void setSortNo(Integer sortNo){
		this.sortNo = sortNo;
	}

	@Column(name = "IS_DEFAULT")
	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	

}
