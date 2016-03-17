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

import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.BaseModel;

/**
 * 商品属性的属性可选值
 * 与商品属性多对一关联
 * @author dianchao.song
 */
@Entity
@Table(name = "t_pd_property_value")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PropertyValue extends BaseModel implements Command{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5108799204445680324L;

	/** PK. */
	private Long				id;

	/** 所属属性 */
	private Long			    propertyId;
	
	/**
	 * 行业属性（扩展用）
	 */
	private Long				commonPropertyId;
	
	/**
	 * 属性值
	 */
	private String              value;
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
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_PROPERTY_VALUE",sequenceName = "S_T_PD_PROPERTY_VALUE",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PD_PROPERTY_VALUE")
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

	/**
	 * Gets the 父级分类.
	 * 
	 * @return the parent
	 */
	@Index(name = "IDX_PROPERTY_ID")
	@Column(name = "PROPERTY_ID")
	public Long getPropertyId(){
		return propertyId;
	}
	public void setPropertyId(Long propertyId) {
		this.propertyId = propertyId;
	}
	
	@Index(name = "IDX_COMMON_PROPERTY_ID")
	@Column(name = "COMMON_PROPERTY_ID")
	public Long getCommonPropertyId(){
		return commonPropertyId;
	}
	
	public void setCommonPropertyId(Long commonPropertyId){
		this.commonPropertyId = commonPropertyId;
	}
	
	@Column(name = "VALUE")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Column(name = "THUMB")
	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	@Column(name = "MODIFY_TIME")
	public Date getModifyTime() {
		return modifyTime;
	}

	@Column(name = "SORT_NO")
	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}
	
	
}
