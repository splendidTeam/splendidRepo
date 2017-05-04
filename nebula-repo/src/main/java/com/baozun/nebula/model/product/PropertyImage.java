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
 * property 图片
 * 
 * @author dianchao.song
 */
@Entity
@Table(name = "T_PD_PROPERTY_IMAGE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PropertyImage extends BaseModel {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1163510031352962054L;
	/** PK. */
	private Long id;
	/**
	 * 商品
	 */
	private Long itemId;
	
	/**
	 * 图片所对应的属性组合的字符串
	 */
	private String properties;
	/**
	 * 图片url
	 */
	private String picUrl;


	/**
	 * 图片放在第几张（多图时可设置 ，越小越靠前）
	 */
	private Integer position;

	/** 创建时间. */
	private Date createTime;

	/** 修改时间 */
	private Date modifyTime;

	/** version. */
	private Date version;
	/**
	 * Gets the pK.
	 * 
	 * @return the pK
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_ITEM_IMAGE", sequenceName = "S_T_PD_ITEM_IMAGE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_PD_ITEM_IMAGE")
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
	@Column(name = "ITEM_ID")
    @Index(name = "IDX_PROPERTY_IMAGE_ITEM_ID")
	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	@Column(name = "PIC_URL")
	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	@Column(name = "POSITION")
	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	/**
	 * @param properties the properties to set
	 */
	@Column(name = "PROPERTIES")
    @Index(name = "IDX_PROPERTY_IMAGE_PROPERTIES")
	public void setProperties(String properties) {
		this.properties = properties;
	}

	/**
	 * @return the properties
	 */
	public String getProperties() {
		return properties;
	}
}
