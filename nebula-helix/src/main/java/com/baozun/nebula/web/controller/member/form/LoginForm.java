package com.baozun.nebula.web.controller.member.form;

import com.baozun.nebula.web.command.MemberFrontendCommand;
import com.baozun.nebula.web.controller.BaseForm;

/**
 * 登录表单
 * 
 * @author jeally
 * @version 2016年3月17日
 */
public class LoginForm extends BaseForm{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4546580076732683893L;

	/**
	 * 登录名
	 */
	private String				loginName;

	/**
	 * 密码
	 */
	private String				password;

	/**
	 * 是否记住用户名
	 */
	private Boolean				isRemberMeLoginName	= true;

	/**
	 * 是否记住密码
	 */
	private Boolean				isRemberMePwd		= false;

	public String getLoginName(){
		return loginName;
	}

	public void setLoginName(String loginName){
		this.loginName = loginName;
	}

	public String getPassword(){
		return password;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public Boolean getIsRemberMeLoginName(){
		return isRemberMeLoginName;
	}

	public void setIsRemberMeLoginName(Boolean isRemberMeLoginName){
		this.isRemberMeLoginName = isRemberMeLoginName;
	}

	public Boolean getIsRemberMePwd(){
		return isRemberMePwd;
	}

	public void setIsRemberMePwd(Boolean isRemberMePwd){
		this.isRemberMePwd = isRemberMePwd;
	}

	/**
	 * 转换成server需要的command
	 * 
	 * @return
	 */
	public MemberFrontendCommand toMemberFrontendCommand(){
		MemberFrontendCommand memberCommand = new MemberFrontendCommand();
		memberCommand.setLoginName(this.loginName);
		memberCommand.setPassword(this.password);
		// .....
		return memberCommand;
	}
}
