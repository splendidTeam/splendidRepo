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
 * 商品分类表.
 * 
 * @author dianchao.song
 */
@Entity
@Table(name = "T_PD_CATEGORY")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class Category extends BaseModel{

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= -7817797309630793957L;

	/** PK. */
	private Long				id;

	/** 商品分类编码. */
	private String				code;

	/** 分类名称. */
	private String				name;

	/** 顺序. */
	private Integer				sortNo;

	/** 父级分类.父节点 */
	private Long				parentId;

	// ************************************************************************************

	/** 生命周期. */
	private Integer				lifecycle;

	/** 创建时间. */
	private Date				createTime			= new Date();

	/** 修改时间. */
	private Date				modifyTime;

	/** version. */
	private Date				version				= new Date();

	/**
	 * Gets the pK.
	 * 
	 * @return the pK
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_CATEGORY",sequenceName = "S_T_PD_CATEGORY",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PD_CATEGORY")
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
	 * Gets the 商品分类编码.
	 * 
	 * @return the 商品分类编码 <br>
	 */
	@Column(name = "CODE",length = 200)
	@Index(name = "FINX_PD_CATEGORY_CODE")
	public String getCode(){
		return code;
	}

	/**
	 * Sets the 商品分类编码 <br>
	 * 
	 * @param code
	 *            the new 商品分类编码
	 */
	public void setCode(String code){
		this.code = code;
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

	/**
	 * Gets the 顺序.
	 * 
	 * @return the 顺序
	 */
	@Column(name = "SORT_NO")
	public Integer getSortNo(){
		return sortNo;
	}

	/**
	 * Sets the 顺序.
	 * 
	 * @param sortNo
	 *            the new 顺序
	 */
	public void setSortNo(Integer sortNo){
		this.sortNo = sortNo;
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
	 * Gets the 修改时间.
	 * 
	 * @return the 修改时间
	 */
	@Column(name = "MODIFY_TIME")
	public Date getModifyTime(){
		return modifyTime;
	}

	/**
	 * Sets the 修改时间.
	 * 
	 * @param modifyTime
	 *            the new 修改时间
	 */
	public void setModifyTime(Date modifyTime){
		this.modifyTime = modifyTime;
	}

	/**
	 * Gets the 生命周期.
	 * 
	 * @return the 生命周期
	 */
	@Column(name = "LIFECYCLE")
    @Index(name = "IDX_CATEGORY_LIFECYCLE")
	public Integer getLifecycle(){
		return lifecycle;
	}

	/**
	 * Sets the 生命周期.
	 * 
	 * @param lifecycle
	 *            the new 生命周期
	 */
	public void setLifecycle(Integer lifecycle){
		this.lifecycle = lifecycle;
	}

	/**
	 * Gets the 父级分类.
	 * 
	 * @return the parentId
	 */
	@Index(name = "IDX_CATEGORY_PARENTID")
	@Column(name = "PARENT_ID")
	public Long getParentId(){
		return parentId;
	}

	/**
	 * Sets the 父级分类.
	 * 
	 * @param parentId
	 *            the parentId to set
	 */
	public void setParentId(Long parentId){
		this.parentId = parentId;
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
}
