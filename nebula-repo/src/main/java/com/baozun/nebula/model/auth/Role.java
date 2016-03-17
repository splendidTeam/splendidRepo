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

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 用户所属角色 与机构类型多对一关联
 * 
 * @author Justin
 */
@Entity
@Table(name = "T_AU_ROLE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class Role extends BaseModel{

	private static final long	serialVersionUID	= 833233337104934569L;

	/**
	 * ID
	 */
	private Long				id;

	/**
	 * 角色名称
	 */
	private String				name;

	/**
	 * 角色描述
	 */
	private String				desc;

	/**
	 * 生命周期
	 */
	private Integer				lifecycle;

	/**
	 * 机构类型Id(主要是有系统管理员，店铺管理员)
	 */
	private Long				orgTypeId;

	/**
	 * VERSION
	 */
	private Date				version;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_AU_ROLE",sequenceName = "S_T_AU_ROLE",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_AU_ROLE")
	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	@Column(name = "NAME",length = 100)
	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	@Column(name = "DESCRIPTION",length = 300)
	public String getDesc(){
		return desc;
	}

	public void setDesc(String desc){
		this.desc = desc;
	}

	@Column(name = "LIFECYCLE")
	public Integer getLifecycle(){
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle){
		this.lifecycle = lifecycle;
	}

	@Column(name = "ORG_TYPE_ID")
	public Long getOrgTypeId(){
		return orgTypeId;
	}

	public void setOrgTypeId(Long orgTypeId){
		this.orgTypeId = orgTypeId;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion(){
		return version;
	}

	public void setVersion(Date version){
		this.version = version;
	}

}
