package com.baozun.nebula.web.controller.member.form;

import com.baozun.nebula.web.command.MemberFrontendCommand;

/**
 * 登录表单
 * @author jeally
 * @version 2016年3月17日
 */
public class LoginForm {

	/**
	 * 登录名
	 */
	private String				loginName;

	/**
	 * 登录邮箱
	 */
	private String				loginEmail;

	/**
	 * 登录手机
	 */
	private String				loginMobile;
	
	/**
	 * 密码
	 */
	private String 				password;

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
	
	/**
	 * 转换成server需要的command
	 * @return
	 */
	public MemberFrontendCommand toMemberFrontendCommand(){
		MemberFrontendCommand memberCommand=new MemberFrontendCommand();
		memberCommand.setLoginEmail(this.loginEmail);
		//.....
		return memberCommand;
	}
}
