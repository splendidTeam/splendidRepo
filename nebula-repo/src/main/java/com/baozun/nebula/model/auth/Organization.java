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
package com.baozun.nebula.model.auth;

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
 * 组织机构 类型表示店铺或公司等机构 与机构类型多对一关联 与本身多对一关联(parent)
 * 
 * @author Justin
 */
@Entity
@Table(name = "T_AU_ORGANIZATION")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class Organization extends BaseModel{

	private static final long	serialVersionUID	= 3708806165474796746L;

	/**
	 * ID
	 */
	private Long				id;

	/**
	 * 类型名称
	 */
	private String				name;

	/**
	 * 描述
	 */
	private String				description;

	/**
	 * 机构编码(考虑与oms联动)
	 */
	private String				code;

	/**
	 * 机构类型(主要是有系统管理员，店铺管理员)
	 */
	private Long				orgTypeId;

	/**
	 * 父级
	 */
	private Long				parentId;

	/**
	 * VERSION
	 */
	private Date				version;

	/**
	 * 生命周期
	 */
	private Integer				lifecycle;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_AU_ORGANIZATION",sequenceName = "S_T_AU_ORGANIZATION",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_AU_ORGANIZATION")
	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion(){
		return version;
	}

	public void setVersion(Date version){
		this.version = version;
	}

	@Column(name = "NAME",length = 100)
	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	@Column(name = "DESCRIPTION",length = 300)
	public String getDescription(){
		return description;
	}

	public void setDescription(String description){
		this.description = description;
	}

	@Column(name = "ORG_TYPE_ID")
	@Index(name = "FIDX_AU_ORG_OT_ID")
	public Long getOrgTypeId(){
		return orgTypeId;
	}

	public void setOrgTypeId(Long orgTypeId){
		this.orgTypeId = orgTypeId;
	}

	@Column(name = "PARENT_ID")
	@Index(name = "FIDX_AU_ORA_PARENT_ID")
	public Long getParentId(){
		return parentId;
	}

	public void setParentId(Long parentId){
		this.parentId = parentId;
	}

	@Column(name = "LIFECYCLE")
	public Integer getLifecycle(){
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle){
		this.lifecycle = lifecycle;
	}

	@Column(name = "CODE")
	public String getCode(){
		return code;
	}

	public void setCode(String code){
		this.code = code;
	}
	
}
