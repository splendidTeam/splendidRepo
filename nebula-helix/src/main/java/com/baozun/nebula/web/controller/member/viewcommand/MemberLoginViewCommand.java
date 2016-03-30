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
package com.baozun.nebula.web.controller.member.viewcommand;

import com.baozun.nebula.web.controller.BaseViewCommand;

public class MemberLoginViewCommand extends BaseViewCommand{

	private static final long	serialVersionUID	= -5507901035774462172L;

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
	private Boolean				isRememberMe		= false;

	/**
	 * 是否自动登录(记住密码功能)
	 */
	private Boolean				isAutoLogin			= false;

	/**
	 * get loginName
	 * 
	 * @return loginName
	 */
	public String getLoginName(){
		return loginName;
	}

	/**
	 * set loginName
	 * 
	 * @param loginName
	 */
	public void setLoginName(String loginName){
		this.loginName = loginName;
	}

	/**
	 * get password
	 * 
	 * @return password
	 */
	public String getPassword(){
		return password;
	}

	/**
	 * set password
	 * 
	 * @param password
	 */
	public void setPassword(String password){
		this.password = password;
	}

	/**
	 * get isRememberMe
	 * 
	 * @return isRememberMe
	 */
	public Boolean getIsRememberMe(){
		return isRememberMe;
	}

	/**
	 * set isRememberMe
	 * 
	 * @param isRememberMe
	 */
	public void setIsRememberMe(Boolean isRememberMe){
		this.isRememberMe = isRememberMe;
	}

	/**
	 * get isAutoLogin
	 * 
	 * @return isAutoLogin
	 */
	public Boolean getIsAutoLogin(){
		return isAutoLogin;
	}

	/**
	 * set isAutoLogin
	 * 
	 * @param isAutoLogin
	 */
	public void setIsAutoLogin(Boolean isAutoLogin){
		this.isAutoLogin = isAutoLogin;
	}

}
