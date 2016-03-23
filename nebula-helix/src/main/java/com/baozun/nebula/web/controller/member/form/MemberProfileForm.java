/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.member.form;

import java.util.Date;

import com.baozun.nebula.model.member.MemberPersonalData;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.web.controller.BaseForm;

public class MemberProfileForm extends BaseForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 869373758998326938L;

	// 抄袭MemberPersonalData到这里，并删除保留字段
	// 注意密码字段需要两个

	/**
	 * id
	 */
	private Long id;

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
	 * 密码
	 */
	private String password;
	/**
	 * BrandStore迁移的密码
	 */
	private String oldPassword;
	/**
	 * 第三方标识
	 */
	private String thirdPartyIdentify;

	/**
	 * 来源： 自注册 微博登录 QQ登录 等。 value在ChooseOption中配置
	 */
	private Integer source;

	/**
	 * 类型: 自有会员 自注册会员 第三方会员 等。 value在ChooseOption中配置
	 */
	private Integer type;

	/**
	 * 是否已绑定分类:1表示已加入 0表示未加入
	 */
	private Integer isaddgroup;

	/**
	 * 性别 ：0表示男 1表示女
	 */
	private Integer sex;

	/**
	 * 生日
	 */
	private String birthday;

	private String repassword;

	/**
	 * 生命周期
	 */
	private Integer lifecycle;

	/**
	 * 1 接收 0 不接收
	 */
	private Integer receiveMail;

	private String newPassword;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getThirdPartyIdentify() {
		return thirdPartyIdentify;
	}

	public void setThirdPartyIdentify(String thirdPartyIdentify) {
		this.thirdPartyIdentify = thirdPartyIdentify;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getIsaddgroup() {
		return isaddgroup;
	}

	public void setIsaddgroup(Integer isaddgroup) {
		this.isaddgroup = isaddgroup;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getRepassword() {
		return repassword;
	}

	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}

	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	public Integer getReceiveMail() {
		return receiveMail;
	}

	public void setReceiveMail(Integer receiveMail) {
		this.receiveMail = receiveMail;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public MemberPersonalData convertMemberProfileFormToMemberPersonalData(
			MemberPersonalData memberPersonalData) {
		memberPersonalData.setVersion(new Date());
		memberPersonalData.setBirthday(new Date(this.birthday));
		memberPersonalData.setEmail(this.loginEmail);
		memberPersonalData.setMobile(this.loginMobile);
		memberPersonalData.setNickname(this.loginName);
		memberPersonalData.setSex(this.sex);
		return memberPersonalData;
	}

	public MemberCommand setMemberLoginEmail(MemberCommand memberCommand) {
		memberCommand.setLoginEmail(this.loginEmail);
		return memberCommand;
	}

	public MemberCommand setMemberLoginMobile(MemberCommand memberCommand) {
		memberCommand.setLoginMobile(this.loginMobile);
		return memberCommand;
	}

	public MemberCommand setMemberLoginName(MemberCommand memberCommand,
			String loginName) {
		memberCommand.setLoginName(loginName);
		return memberCommand;
	}
}
