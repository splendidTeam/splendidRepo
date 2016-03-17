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
 * INDUSTRY 属性分组  行业
 * @author dianchao.song
 */
@Entity
@Table(name = "t_pd_industry")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class Industry extends BaseModel{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4055605390668192816L;

	/** PK. */
	private Long				id;

	/** 名称*/
	private String				name;

	/** 顺序. */
	private Integer				sortNo;

	/** 父级分类.父节点 */
	private Long			    parentId;

	/** 创建时间. */
	private Date				createTime;
	
	/**修改时间*/
	private Date                modifyTime;

	/** version. */
	private Date				version;
	
	/**
	 * 生命周期
	 */
	private Integer 			lifecycle ;

	/**
	 * Gets the pK.
	 * 
	 * @return the pK
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_INDUSTRY",sequenceName = "S_T_PD_INDUSTRY",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PD_INDUSTRY")
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
	 * Gets the 名称.
	 * 
	 * @return the 名称
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
	 * @param priority
	 *            the new 顺序
	 */
	public void setSortNo(Integer sortNo){
		this.sortNo = sortNo;
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
	@Index(name = "IDX_INDUSTRY_PARENT_ID")
	@Column(name = "PARENT_ID")
	public Long getParentId(){
		return parentId;
	}

	/**
	 * Sets the 父级分类.
	 * 
	 * @param parent
	 *            the parent to set
	 */
	public void setParentId(Long parentId){
		this.parentId = parentId;
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

}
