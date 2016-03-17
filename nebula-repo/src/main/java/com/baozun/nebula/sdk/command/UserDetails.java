package com.baozun.nebula.sdk.command;

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

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 会员登录后，存储在session中的用户结构
 * 登录后，在session中存储此对象，退出后，则删除此对象
 * 
 * @author songdianchao
 */
public class UserDetails implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6195912122597589053L;

	/**
	 * 登录名
	 */
	private String							loginName;
	
	/**
	 * 登录邮箱
	 */
	private String 							loginEmail;
	
	/**
	 * 登录手机
	 */
	private String 							loginMobile;
	
	/**
	 * 用户昵称
	 */
	private String							nickName;

	/**
	 * 用的真实名
	 */
	private String							realName;

	/**
	 * 当前会员id
	 */
	private Long							memberId;

	/**
	 * 会员拥有的人群标签列表(用户登录时将用户满足的人群标签写在这里）
	 */
	private List<Long> audienceTagIdList;
	
	/** 
	 * 会员所属分组
	 */
	private List<Long>						groupIds;

	/**
	 * 分组标签
	 */
	private Set<String>						memComboList;
	
	
	public UserDetails() {
		super();
	}

	public UserDetails(String loginName, String loginEmail, String loginMobile, Long memberId, String nickName,
			List<Long> groupIds,Set<String> memComboList){
		super();
		this.loginName = loginName;
		this.loginEmail = loginEmail;
		this.loginMobile = loginMobile;
		this.memberId = memberId;
		this.nickName = nickName;
		this.groupIds = groupIds;
		this.memComboList = memComboList;
	}

	public UserDetails(String loginName, String loginEmail, String loginMobile,
			String nickName, String realName, Long memberId,
			List<Long> groupIds) {
		super();
		this.loginName = loginName;
		this.loginEmail = loginEmail;
		this.loginMobile = loginMobile;
		this.nickName = nickName;
		this.realName = realName;
		this.memberId = memberId;
		this.groupIds = groupIds;
	}


	public UserDetails(String loginName, String loginEmail, String loginMobile, Long memberId, String nickName){
		super();
		this.loginName = loginName;
		this.loginEmail = loginEmail;
		this.loginMobile = loginMobile;
		this.memberId = memberId;
		this.nickName = nickName;
	}


	public String getLoginName() {
		return loginName;
	}


	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}


	public String getLoginEmail() {
		return loginEmail;
	}


	public void setLoginEmail(String loginEmail) {
		this.loginEmail = loginEmail;
	}


	public String getLoginMobile() {
		return loginMobile;
	}


	public void setLoginMobile(String loginMobile) {
		this.loginMobile = loginMobile;
	}


	public String getNickName() {
		return nickName;
	}


	public void setNickName(String nickName) {
		this.nickName = nickName;
	}


	public String getRealName() {
		return realName;
	}


	public void setRealName(String realName) {
		this.realName = realName;
	}


	public Long getMemberId() {
		return memberId;
	}


	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}


	public List<Long> getAudienceTagIdList() {
		return audienceTagIdList;
	}


	public void setAudienceTagIdList(List<Long> audienceTagIdList) {
		this.audienceTagIdList = audienceTagIdList;
	}

	public List<Long> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(List<Long> groupIds) {
		this.groupIds = groupIds;
	}

	public Set<String> getMemComboList() {
		return memComboList;
	}

	public void setMemComboList(Set<String> memComboList) {
		this.memComboList = memComboList;
	}
	
	
}
