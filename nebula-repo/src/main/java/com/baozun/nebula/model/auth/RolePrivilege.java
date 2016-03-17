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
 * 角色与权限的关联表 有两个外键指向后台权限表及角色表
 * 
 * @author Justin
 */
@Entity
@Table(name = "T_AU_ROLE_PRIVILEGE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class RolePrivilege extends BaseModel{

	private static final long	serialVersionUID	= 8435357315810171845L;

	/**
	 * ID
	 */
	private Long				id;

	/**
	 * VERSION
	 */
	private Date				version;

	/**
	 * 角色Id
	 */
	private Long				roleId;

	/**
	 * 权限Id
	 */
	private Long				privilegeId;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_AU_ROLE_PRIVILEGE",sequenceName = "S_T_AU_ROLE_PRIVILEGE",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_AU_ROLE_PRIVILEGE")
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

	@Column(name = "ROLE_ID")
	public Long getRoleId(){
		return roleId;
	}

	public void setRoleId(Long roleId){
		this.roleId = roleId;
	}

	@Column(name = "PRI_ID")
	public Long getPrivilegeId(){
		return privilegeId;
	}

	public void setPrivilegeId(Long privilegeId){
		this.privilegeId = privilegeId;
	}

}
