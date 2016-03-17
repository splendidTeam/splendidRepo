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
package com.baozun.nebula.command;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baozun.nebula.model.auth.Privilege;

/**
 * @author xianze.zhang
 * @creattime 2013-6-3
 */
public class RoleCommand implements Command{

	private static final long	serialVersionUID	= 1503674950266310420L;

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
	 * VERSION
	 */
	private Date				version;

	/**
	 * 机构类型id
	 */
	private Long				orgTypeId;

	/**
	 * 机构类型名称
	 */
	private String				orgTypeName;
	/**
	 * 权限的list集合
	 */
	private Map<Long,Privilege> privileges;
	
	
	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public Integer getLifecycle(){
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle){
		this.lifecycle = lifecycle;
	}

	public Date getVersion(){
		return version;
	}

	public void setVersion(Date version){
		this.version = version;
	}

	public Long getOrgTypeId(){
		return orgTypeId;
	}

	public void setOrgTypeId(Long orgTypeId){
		this.orgTypeId = orgTypeId;
	}

	public String getOrgTypeName(){
		return orgTypeName;
	}

	public void setOrgTypeName(String orgTypeName){
		this.orgTypeName = orgTypeName;
	}

	public String getDesc(){
		return desc;
	}

	public void setDesc(String desc){
		this.desc = desc;
	}

	public Map<Long, Privilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Map<Long, Privilege> privileges) {
		this.privileges = privileges;
	}


}
