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
package com.baozun.nebula.command.product;

import java.util.Date;

import loxia.annotation.Column;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.command.i18n.LangProperty;

/**
 * Item 图片
 * 
 * @author dianchao.song
 */
public class ItemImageLangCommand implements Command {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4390002680870621396L;

	/** PK. */
	private Long id;
	/**
	 * 商品
	 */
	private Long itemId;
	/**
	 * 图片url
	 */
	private LangProperty picUrl;

	/**
	 * 属性值id ItemPropertiesId;ItemPropertiesId 但第一期只需要传一个id就行,因为只有一个颜色属性
	 */
	private Long itemProperties;

	/**
	 * @deprecated 属性id
	 */
	private Long propertyId;

	/**
	 * @deprecated 属性可选值id
	 */
	private Long propertyValueId;

	/**
	 * 描述
	 */
	private LangProperty description;

	/**
	 * 图片类型 1为列表页 2为内容页
	 * 
	 */
	private String type;
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
	 * @return the pK
	 */
	@Column("ID")
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
	@Column("VERSION")
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
	@Column("CREATE_TIME")
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

	@Column("MODIFY_TIME")
	public Date getModifyTime() {
		return modifyTime;
	}

	@Column("ITEM_ID")
	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public LangProperty getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(LangProperty picUrl) {
		this.picUrl = picUrl;
	}

	@Column("POSITION")
	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	@Column("PROPERTY_ID")
	public Long getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(Long propertyId) {
		this.propertyId = propertyId;
	}

	public LangProperty getDescription() {
		return description;
	}

	public void setDescription(LangProperty description) {
		this.description = description;
	}

	@Column("PROPERTY_VALUE_ID")
	public Long getPropertyValueId() {
		return propertyValueId;
	}

	public void setPropertyValueId(Long propertyValueId) {
		this.propertyValueId = propertyValueId;
	}

	@Column("ITEM_PROPERTIES")
	public Long getItemProperties() {
		return itemProperties;
	}

	public void setItemProperties(Long itemProperties) {
		this.itemProperties = itemProperties;
	}

	@Column("TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
