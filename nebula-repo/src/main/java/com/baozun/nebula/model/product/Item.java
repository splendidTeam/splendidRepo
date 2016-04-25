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
 * Item
 * 
 * @author dianchao.song
 */
@Entity
@Table(name = "t_pd_item")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class Item extends BaseModel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 882446624240577496L;
	
	/** PK. */
	private Long id;

	/**
	 * 商品编码
	 */
	private String code;

	/**
	 * 所属店铺
	 */
	private Long shopId;

	/**
	 * 所属行业
	 */
	private Long industryId;

	/**
	 * 生命周期
	 */
	private Integer lifecycle;

	/** 创建时间. */
	private Date createTime;

	/** 修改时间 */
	private Date modifyTime;

	/** version. */
	private Date version;

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
	 * 商品主图 此字段已失效，请使用ItemImage中对应的图片
	 */
	@Deprecated
	private String picUrl;

	/**
	 * 是否已绑定分类:1表示已加入 否则未加入
	 */
	@Deprecated
	private Integer isaddcategory;

	/**
	 * 是否已绑定标签:1表示已加入 0表示未加入
	 */
	@Deprecated
	private Integer isAddTag;

	/**
	 * Gets the pK.
	 * 
	 * @return the pK
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_ITEM", sequenceName = "S_T_PD_ITEM", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_PD_ITEM")
	public Long getId() {
		return id;
	}

	/**
	 * Sets the pK.
	 * 
	 * @param id
	 *            the new pK
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	/**
	 * Sets the version.
	 * 
	 * @param version
	 */
	public void setVersion(Date version) {
		this.version = version;
	}

	/**
	 * Gets the 创建时间.
	 * 
	 * @return the createTime
	 */
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * Sets the 创建时间.
	 * 
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_TIME")
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	@Column(name = "LIFECYCLE")
	public Integer getLifecycle() {
		return lifecycle;
	}

	/**
	 * @param shop
	 *            the shop to set
	 */
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	/**
	 * @return the shop
	 */
	@Column(name = "SHOP_ID")
	@Index(name = "IDX_SHOP_ID")
	public Long getShopId() {
		return shopId;
	}

	@Column(name = "CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "LIST_TIME")
	public Date getListTime() {
		return listTime;
	}

	public void setListTime(Date listTime) {
		this.listTime = listTime;
	}

	@Column(name = "DELIST_TIME")
	public Date getDelistTime() {
		return delistTime;
	}

	public void setDelistTime(Date delistTime) {
		this.delistTime = delistTime;
	}

	@Column(name = "TEMPLATE_ID")
	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	@Column(name = "PIC_URL", length = 150)
	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	/**
	 * @param industryId
	 *            the industryId to set
	 */
	public void setIndustryId(Long industryId) {
		this.industryId = industryId;
	}

	/**
	 * @return the industryId
	 */
	@Column(name = "INDUSTRY_ID")
	public Long getIndustryId() {
		return industryId;
	}

	/**
	 * 
	 * @return the isaddcategory
	 */
	@Column(name = "ISADDCATEGORY")
	public Integer getIsaddcategory() {
		return isaddcategory;
	}

	public void setIsaddcategory(Integer isaddcategory) {
		this.isaddcategory = isaddcategory;
	}

	/**
	 * 
	 * @return the isaddtag
	 */
	@Column(name = "ISADDTAG")
	public Integer getIsAddTag() {
		return isAddTag;
	}

	public void setIsAddTag(Integer isAddTag) {
		this.isAddTag = isAddTag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
