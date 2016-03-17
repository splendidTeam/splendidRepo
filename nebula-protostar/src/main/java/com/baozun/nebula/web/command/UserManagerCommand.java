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

import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.auth.User;

/**
 * 用于用户管理中页面与controller传递数据
 * 
 * @author Justin
 */
public class UserManagerCommand implements Command{

	private static final long	serialVersionUID	= -304229857665974631L;

	/**
	 * 用户id
	 */
	private Long				userId;

	/**
	 * 密码
	 */
	private String				password;

	/**
	 * 确认密码
	 */
	private String				passwordAgain;

	/**
	 * 用户名
	 */
	private String				userName;

	/**
	 * 邮箱
	 */
	private String				email;

	/**
	 * 开始日期
	 */
	private String				startDate;

	/**
	 * 结束日期
	 */
	private String				endDate;

	/**
	 * 真实名称
	 */
	private String				realName;

	/**
	 * 手机号
	 */
	private String				mobile;

	/**
	 * 所属组织id
	 */
	private Long				orgId;

	/**
	 * 搜索类型
	 */
	private Integer				searchType;

	/**
	 * 搜索关键字
	 */
	private String				searchKey;
	
	/**
	 * 是否有效
	 */
	private Integer				available;
	
	

	public Integer getAvailable() {
		return available;
	}

	public void setAvailable(Integer available) {
		this.available = available;
	}

	public String getSearchKey(){
		return searchKey;
	}

	public void setSearchKey(String searchKey){
		this.searchKey = searchKey;
	}

	public Integer getSearchType(){
		return searchType;
	}

	public void setSearchType(Integer searchType){
		this.searchType = searchType;
	}

	public Long getOrgId(){
		return orgId;
	}

	public void setOrgId(Long orgId){
		this.orgId = orgId;
	}

	public String getRealName(){
		return realName;
	}

	public void setRealName(String realName){
		this.realName = realName;
	}

	public String getMobile(){
		return mobile;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getStartDate(){
		return startDate;
	}

	public void setStartDate(String startDate){
		this.startDate = startDate;
	}

	public String getEndDate(){
		return endDate;
	}

	public void setEndDate(String endDate){
		this.endDate = endDate;
	}

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId = userId;
	}

	public String getUserName(){
		if (userName == null){
			return null;
		}else{
			return userName.replace("<", "&lt;").replace(">", "&gt;").replace("'", "&quot;").replace("\"", "&quot;").replace("&", "&amp;");
		}
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getPassword(){
		return password;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPasswordAgain(){
		return passwordAgain;
	}

	public void setPasswordAgain(String passwordAgain){
		this.passwordAgain = passwordAgain;
	}

	public User getUser(){
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setUserName(userName);
		return user;
	}

	public String getEmail(){
		return this.email;
	}

	public void setEmail(String email){
		this.email = email;
	}

}
