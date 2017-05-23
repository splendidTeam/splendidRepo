/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
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
 * 商品标签
 * @author dongliang.ma
 *
 */
@Entity
@Table(name = "T_PD_ITEMTAG")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
@Deprecated
public class ItemTag extends BaseModel{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6301038427180710153L;
	
	/** PK. */
	private Long				id;

	/** 标签名称. */
	private String				name;

	/** 类型. */
	private Integer				type;

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
	@SequenceGenerator(name = "SEQ_T_PD_ITEMTAG",sequenceName = "S_T_PD_ITEMTAG",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PD_ITEMTAG")
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

	@Column(name = "NAME")
	public String getName(){
		return name;
	}

	
	public void setName(String name){
		this.name = name;
	}

	@Column(name = "TYPE")
    @Index(name = "IDX_ITEMTAG_TYPE")
	public Integer getType(){
		return type;
	}

	/**
	 * 
	 * @param type
	 */
	
	public void setType(Integer type){
		this.type = type;
	}

	/**
	 * Gets the 生命周期.
	 * 
	 * @return the 生命周期
	 */
	@Column(name = "LIFECYCLE")
    @Index(name = "IDX_ITEMTAG_LIFECYCLE")
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
