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
package com.baozun.nebula.model.auth;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 后台操作用户<br/>
 * 与组织机构多对一关联
 * @author Justin
 *
 */
@Entity
@Table(name = "T_AU_USER")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class User extends BaseModel {


	private static final long serialVersionUID = -3754550257027114027L;

	/**
	 * PK
	 */
	private Long 				id;


	/**
	 * 登录账号
	 */
	private String 				userName;

	/**
	 * 密码
	 */
	private String				password;


	/**
	 * 生命周期
	 */
	private Integer 			lifecycle ;

	/**
	 * 创建时间
	 */
	private Date 				createTime;

	/**
	 * 最后修改时间
	 */
	private Date 				latestUpdateTime;

	/**
	 * 最后登录时间
	 */
	private Date 				latestAccessTime;

	/**
	 * VERSION
	 */
	private Date 				version;

	/**
	 * 真实姓名
	 */
	private String 				realName;
	
	/**
	 * 邮箱
	 */
	private String 				email;

	/**
	 * 手机
	 */
	private String 				mobile;


	/**
	 * 备注
	 */
	private String 				memo;
	
	
	/**
	 * 组织机构
	 */
	private Long 				orgId;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_AU_USER", sequenceName = "S_T_AU_USER", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_AU_USER")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	@Column(name = "USER_NAME", length = 100)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "PASSWORD", length = 255)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}



	@Column(name = "LIFECYCLE")
	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "LATEST_UPDATE_TIME")
	public Date getLatestUpdateTime() {
		return latestUpdateTime;
	}

	public void setLatestUpdateTime(Date latestUpdateTime) {
		this.latestUpdateTime = latestUpdateTime;
	}

	@Column(name = "LATEST_ACCESS_TIME")
	public Date getLatestAccessTime() {
		return latestAccessTime;
	}

	public void setLatestAccessTime(Date latestAccessTime) {
		this.latestAccessTime = latestAccessTime;
	}
	

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	@Column(name = "REALNAME", length = 20)
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	
	@Column(name = "MOBILE", length = 50)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "MEMO", length = 255)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	@Column(name = "ORG_ID")
	@Index(name = "FIDX_USR_ORGA")
	public Long getOrgId() {
		return orgId;
	}
	
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getEmail() {
		return email;
	}
	
	
	@Column(name = "EMAIL", length = 100)
	public void setEmail(String email) {
		this.email = email;
	}



	
	
	
}
