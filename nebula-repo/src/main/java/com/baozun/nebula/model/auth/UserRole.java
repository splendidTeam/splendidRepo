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
import javax.persistence.Version;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 用户、机构、角色的关联表<br/>
 * 有三个外键<br/>
 * <li>用户表</li>
 * <li>机构表</li>
 * <li>角色表</li>
 * @author Justin
 *
 */
@Entity
@Table(name = "T_AU_USER_ROLE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class UserRole extends BaseModel {

	private static final long serialVersionUID = 788879001926200535L;

	/**
	 * ID
	 */
	private Long 				id;

	/**
	 * VERSION
	 */
	private Date 				version;

	/**
	 * 用户
	 */
	private Long 				userId;

	/**
	 * 组织机构
	 */
	private Long 				orgId;
	
	/**
	 * 角色
	 */
	private Long 				roleId;
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_AU_USER_ROLE", sequenceName = "S_T_AU_USER_ROLE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_AU_USER_ROLE")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	
	@Column(name = "USER_ID")
	@Index(name = "FIDX_UOR_USER")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}



	@Column(name = "ORG_ID")
	@Index(name = "FIDX_UOR_ORGA")
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	@Column(name = "ROLE_ID")
	@Index(name = "FIDX_UOR_ROLE")
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	

	
}
