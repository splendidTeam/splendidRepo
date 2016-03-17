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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 权限功能 与机构类型多对一关联 与菜单一对一关联(通过外键)
 * 
 * @author Justin
 */
@Entity
@Table(name = "T_AU_PRIVILEGE",uniqueConstraints = { @UniqueConstraint(columnNames = { "ACL" }) })
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class Privilege extends BaseModel{

	private static final long	serialVersionUID	= 5059515852159799713L;

	/**
	 * ID
	 */
	private Long				id;

	/**
	 * controller ID
	 */
	private String				acl;

	/**
	 * 权限名称
	 */
	private String				name;

	/**
	 * 描述
	 */
	private String				description;
	
	/**
	 * 权限分组
	 */
	private String				groupName;

	/**
	 * 生命周期
	 */
	private Integer				lifecycle;

	/**
	 * 机构类型(主要是有系统管理员，店铺管理员)
	 */
	private OrgType				orgType;

	/**
	 * VERSION
	 */
	private Date				version;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_AU_PRIVILEGE",sequenceName = "S_T_AU_PRIVILEGE",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_AU_PRIVILEGE")
	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	@Column(name = "ACL",length = 255)
	public String getAcl(){
		return acl;
	}

	public void setAcl(String acl){
		this.acl = acl;
	}

	@Column(name = "NAME",length = 300)
	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_TYPE_ID")
	@Index(name = "FIDX_AU_PRI_OT_ID")
	public OrgType getOrgType(){
		return orgType;
	}

	public void setOrgType(OrgType orgaType){
		this.orgType = orgaType;
	}

	@Column(name = "LIFECYCLE")
	public Integer getLifecycle(){
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle){
		this.lifecycle = lifecycle;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion(){
		return version;
	}

	public void setVersion(Date version){
		this.version = version;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription(){
		return description;
	}

	public void setDescription(String description){
		this.description = description;
	}

	@Column(name = "GROUP_NAME",length = 128)
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	

	
}
