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
 * 商品属性的属性可选值
 * 与商品属性多对一关联
 */
public class PropertyValueCommand implements Command{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5108799204445680324L;

	/** PK. */
	private Long				id;

	/** 所属属性 */
	private Long			    propertyId;
	/**
	 * 属性值
	 */
	private LangProperty       value;
	/**
	 * 配图地址，绝对地址
	 */
	private String              thumb;

	/** 创建时间. */
	private Date				createTime;
	
	/**修改时间*/
	private Date                modifyTime;

	/** version. */
	private Date				version;
	
	/**
	 * 排序，用于确定哪个可选项排在前面
	 */
	private Integer				sortNo;
	/**
	 * Gets the pK.
	 * 
	 * @return the pK
	 */
	@Column("ID")
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
	@Column("VERSION")
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
	@Column("CREATE_TIME")
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

	/**
	 * Gets the 父级分类.
	 * 
	 * @return the parent
	 */
	@Column("PROPERTY_ID")
	public Long getPropertyId(){
		return propertyId;
	}
	public void setPropertyId(Long propertyId) {
		this.propertyId = propertyId;
	}
	
	
	public LangProperty getValue() {
		return value;
	}

	public void setValue(LangProperty value) {
		this.value = value;
	}
	
	@Column("THUMB")
	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	@Column("MODIFY_TIME")
	public Date getModifyTime() {
		return modifyTime;
	}

	@Column("SORT_NO")
	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}
	
	
}
