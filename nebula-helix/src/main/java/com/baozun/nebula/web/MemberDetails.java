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
import java.util.List;
import java.util.Set;

import com.baozun.nebula.model.member.MemberGroup;

/**
 * 会员登录成功后，存储在session中的用户结构
 * 
 * @author D.C
 */
public class MemberDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6195912122597589053L;

	/**
	 * 登录名
	 */
	private String loginName;

	/**
	 * 登录邮箱
	 */
	private String loginEmail;

	/**
	 * 登录手机
	 */
	private String loginMobile;

	/**
	 * 昵称
	 */
	private String nickName;

	/**
	 * 真实姓名
	 */
	private String realName;

	/**
	 * 会员id
	 */
	private Long memberId;

	/**
	 * 会员所属分组
	 */
	private List<MemberGroup> groups;

	/**
	 * 分组标签
	 */
	private Set<String> memComboList;
	
	/**
	 * 状态
	 */
	private List<String> status;


	public MemberDetails() {
		super();
	}

	public MemberDetails(String loginName, String loginEmail, String loginMobile, Long memberId, String nickName,
			List<MemberGroup> groups, Set<String> memComboList) {
		super();
		this.loginName = loginName;
		this.loginEmail = loginEmail;
		this.loginMobile = loginMobile;
		this.memberId = memberId;
		this.nickName = nickName;
		this.groups = groups;
		this.memComboList = memComboList;
	}

	public MemberDetails(String loginName, String loginEmail, String loginMobile, Long memberId, String nickName,
			List<MemberGroup> groups, Set<String> memComboList, String realName) {
		super();
		this.loginName = loginName;
		this.loginEmail = loginEmail;
		this.loginMobile = loginMobile;
		this.memberId = memberId;
		this.nickName = nickName;
		this.groups = groups;
		this.memComboList = memComboList;
		this.realName = realName;
	}

	public MemberDetails(String loginName, String loginEmail, String loginMobile, Long memberId, String nickName) {
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

	public List<MemberGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<MemberGroup> groups) {
		this.groups = groups;
	}

	public Set<String> getMemComboList() {
		return memComboList;
	}

	public void setMemComboList(Set<String> memComboList) {
		this.memComboList = memComboList;
	}
	
	public List<String> getStatus() {
		return status;
	}

	public void setStatus(List<String> status) {
		this.status = status;
	}

	public enum Status {
		INVALIDATE("invalidate"), VALIDATE("validate"), EMAIL_ACTIVE(""), EMAIL_INACTIVE(""), MOBILE_INACTIVE(""), WAITING_BIND(""), WAITING_PROFILE("");
		
		private String value;
		
		Status(String value) {
			this.setValue(value);
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
}
