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

/**
 * @author Justin
 */
public class UserCommand implements Command{

	private static final long	serialVersionUID	= -2425853109002374526L;

	/**
	 * PK
	 */
	private Long				id;

	/**
	 * 登录账号
	 */
	private String				userName;

	/**
	 * 密码
	 */
	private String				password;

	/**
	 * 生命周期
	 */
	private Integer				lifecycle;

	/**
	 * 创建时间
	 */
	private Date				createTime;

	/**
	 * 最后修改时间
	 */
	private Date				latestUpdateTime;

	/**
	 * 最后登录时间
	 */
	private Date				latestAccessTime;

	/**
	 * 真实姓名
	 */
	private String				realName;

	/**
	 * 邮箱
	 */
	private String				email;

	/**
	 * 手机
	 */
	private String				mobile;

	/**
	 * 备注
	 */
	private String				memo;

	/**
	 * 组织名称
	 */
	private String				orgName;

	/**
	 * 组织id
	 */
	private Long				orgId;

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public String getUserName(){
		return userName;
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

	public Integer getLifecycle(){
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle){
		this.lifecycle = lifecycle;
	}

	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public Date getLatestUpdateTime(){
		return latestUpdateTime;
	}

	public void setLatestUpdateTime(Date latestUpdateTime){
		this.latestUpdateTime = latestUpdateTime;
	}

	public Date getLatestAccessTime(){
		return latestAccessTime;
	}

	public void setLatestAccessTime(Date latestAccessTime){
		this.latestAccessTime = latestAccessTime;
	}

	public String getRealName(){
		return realName;
	}

	public void setRealName(String realName){
		this.realName = realName;
	}

	public String getEmail(){
		return email;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getMobile(){
		return mobile;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getMemo(){
		return memo;
	}

	public void setMemo(String memo){
		this.memo = memo;
	}

	public String getOrgName(){
		return orgName;
	}

	public void setOrgName(String orgName){
		this.orgName = orgName;
	}

	public Long getOrgId(){
		return orgId;
	}

	public void setOrgId(Long orgId){
		this.orgId = orgId;
	}

}
