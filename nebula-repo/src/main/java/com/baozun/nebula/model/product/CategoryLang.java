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

import com.baozun.nebula.model.BaseModel;

/**
 * 商品分类表.
 * 
 * @author 何波
 */
@Entity
@Table(name = "T_PD_CATEGORY_LANG")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class CategoryLang extends BaseModel{

	private static final long serialVersionUID = -4541659006314334242L;

	/** PK. */
	private Long	id;

	private  Long   categoryid;

	/** 分类名称. */
	private String	name;
	
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
	 * Gets the pK.
	 * 
	 * @return the pK
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_CATEGORY_LANG",sequenceName = "S_T_PD_CATEGORY_LANG",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PD_CATEGORY_LANG")
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

	
	@Column(name = "CATEGORY_ID")
    @Index(name = "IDX_CATEGORY_LANG_CATEGORY_ID")
	public Long getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(Long categoryid) {
		this.categoryid = categoryid;
	}

	/**
	 * Gets the 分类名称.
	 * 
	 * @return the 分类名称
	 */
	@Column(name = "NAME",length = 100)
	public String getName(){
		return name;
	}

	/**
	 * Sets the 分类名称.
	 * 
	 * @param name
	 *            the new 分类名称
	 */
	public void setName(String name){
		this.name = name;
	}
	
	

	
}
