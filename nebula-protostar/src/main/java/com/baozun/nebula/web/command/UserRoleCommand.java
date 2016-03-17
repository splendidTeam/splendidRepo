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
package com.baozun.nebula.web.command;

import java.util.ArrayList;
import java.util.List;

import com.baozun.nebula.command.Command;

/**
 * 用户角色关系,用于编辑时传递给页面
 * 
 * @author Justin
 */
public class UserRoleCommand implements Command{

	private static final long	serialVersionUID	= -9071701751952208064L;

	/**
	 * 序号id,针对服务端无意义,不是UserRole表的id 只是用于客户端单个角色修改所用
	 */
	private Long				urId;

	/**
	 * 组织机构类型id
	 */
	private Long				orgTypeId;

	/**
	 * 角色id
	 */
	private Long				roleId;

	/**
	 * 组织机构id列表
	 */
	private List<Long>			orgIds				= new ArrayList<Long>();

	/**
	 * 组织机构类型名称
	 */
	private String				orgType;

	/**
	 * 角色名称
	 */
	private String				role;

	/**
	 * 组织机构名称集，以逗号分隔
	 */
	private String				orgs;

	public Long getUrId(){
		return urId;
	}

	public void setUrId(Long urId){
		this.urId = urId;
	}

	public Long getOrgTypeId(){
		return orgTypeId;
	}

	public void setOrgTypeId(Long orgTypeId){
		this.orgTypeId = orgTypeId;
	}

	public Long getRoleId(){
		return roleId;
	}

	public void setRoleId(Long roleId){
		this.roleId = roleId;
	}

	public List<Long> getOrgIds(){
		return orgIds;
	}

	public void setOrgIds(List<Long> orgIds){
		this.orgIds = orgIds;
	}

	public String getOrgType(){
		return orgType;
	}

	public void setOrgType(String orgType){
		this.orgType = orgType;
	}

	public String getRole(){
		return role;
	}

	public void setRole(String role){
		this.role = role;
	}

	public String getOrgs(){
		return orgs;
	}

	public void setOrgs(String orgs){
		this.orgs = orgs;
	}

}
