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
package com.baozun.nebula.web;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;

import com.baozun.nebula.model.BaseModel;

/**
 * 用户登录后，存储在session中的用户结构，必须遵循spring security接口规范，但可以自行扩展字段，供特殊业务使用
 * 
 * @author songdianchao
 */
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails,Serializable{

	private static final long				serialVersionUID	= 9154433970518929187L;

	/**
	 * 
	 * 这里的不应该获取用户所有的角色，而是权限列表
	 * 
	 */
	private Collection<GrantedAuthority>	authorities;

	/**
	 * 用户当前所在的组织编号
	 */
	private Long							currentOrganizationId;

	/**
	 * 用户可以管辖的机构列表
	 */
	private List<Map<String, String>>		grantedOrgnizations;

	/**
	 * 用户密码
	 */
	private String							password;

	/**
	 * 系统中实际采用email作为登录用户名，这里的值为email
	 */
	private String							username;

	/**
	 * 用的显示名称
	 */
	private String							realName;

	/**
	 * 当前用户编号
	 */
	private Long							userId;

	/**
	 * 用户状态
	 */
	private Integer							lifecycle;

	public UserDetails(String password, String username, String realName, Long userId, Integer lifecycle){
		super();
		this.password = password;
		this.username = username;
		this.realName = realName;
		this.userId = userId;
		this.lifecycle = lifecycle;
	}

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId = userId;
	}

	public Integer getLifecycle(){
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle){
		this.lifecycle = lifecycle;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public Collection<GrantedAuthority> getAuthorities(){
		return this.authorities;
	}

	public void setAuthorities(Collection<GrantedAuthority> authorities){
		this.authorities = authorities;
	}

	public String getPassword(){
		return this.password;
	}

	public boolean isAccountNonExpired(){
		return true;
	}

	public boolean isAccountNonLocked(){
		return true;
	}

	public boolean isCredentialsNonExpired(){
		return true;
	}

	@Override
	public boolean isEnabled(){
		return lifecycle.intValue() == BaseModel.LIFECYCLE_ENABLE.intValue();
	}

	@Override
	public String getUsername(){
		return username;
	}

	/*
	 * equals 和 hashCode 方法必须重写，spring security在限制同一用户的登录次数时，判断是否为同一用户的关键在这里
	 */
	public boolean equals(Object object){
		if (object instanceof UserDetails){
			if (this.userId.longValue() == ((UserDetails) object).getUserId().longValue()){
				return true;
			}
		}
		return false;
	}

	public int hashCode(){
		return this.userId.intValue();
	}

	public void setCurrentOrganizationId(Long currentOrganizationId){
		this.currentOrganizationId = currentOrganizationId;
	}

	public Long getCurrentOrganizationId(){
		return currentOrganizationId;
	}

	public void setRealName(String realName){
		this.realName = realName;
	}

	public String getRealName(){
		return realName;
	}

	public void setGrantedOrgnizations(List<Map<String, String>> grantedOrgnizations){
		this.grantedOrgnizations = grantedOrgnizations;
	}

	public List<Map<String, String>> getGrantedOrgnizations(){
		return grantedOrgnizations;
	}

	/**
	 * 判断某个用户是否可以访问某个机构
	 * 
	 * @param orgId
	 *            机构id
	 * @return
	 */
	public boolean isGrantedOrgnization(String orgId){
		for (Map<String, String> orgnization : grantedOrgnizations){
			if (orgId.equals(orgnization.get("id"))){
				return true;
			}
		}
		return false;
	}

}
