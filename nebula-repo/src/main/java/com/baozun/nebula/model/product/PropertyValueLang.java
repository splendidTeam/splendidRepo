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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.BaseModel;

/**
 * 商品属性的属性可选值 与商品属性多对一关联
 * @author 何波
 */
@Entity
@Table(name = "T_PD_PROPERTY_VALUE_LANG")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PropertyValueLang extends BaseModel implements Command {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5108799204445680324L;

	/** PK. */
	private Long id;

	/** 所属属性 */
	private Long propertyValueId;
	/**
	 * 属性值
	 */
	private String value;
	
	
	/**
	 * 语言标识
	 */
	private String lang;
	
	
	@Column(name = "LANG")
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
	

	/**
	 * @return the pK
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_PROPERTY_VALUE_LANG", sequenceName = "S_T_PD_PROPERTY_VALUE_LANG", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_PD_PROPERTY_VALUE_LANG")
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

	@Column(name = "PROPERTY_VALUE_ID")
	@Index(name = "IDX_PROPERTY_VALUE_LANG_PROPERTY_VALUE_ID")
	public Long getPropertyValueId() {
		return propertyValueId;
	}

	public void setPropertyValueId(Long propertyValueId) {
		this.propertyValueId = propertyValueId;
	}

	@Column(name = "VALUE")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
