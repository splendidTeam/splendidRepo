/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
package com.baozun.nebula.model.email;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 用户咨询实体类
 * 
 * @author Tianlong.Zhang
 * 
 */
@Entity
@Table(name = "t_pd_account_emailcheck")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class EmailCheck extends BaseModel {

	/** 链接有效时间*/
	public static final long EFFECT_TIME = 2*60*60*1000;
	/** 当日请求最多次数*/
	public static final long MAX_NUM = 5;
	
	/** 标记该条记录(1有效未点击)*/
	public static final Integer STATUS_VALID_NOTCLICK = 1;
	/** 标记该条记录(2有效已点击)*/
	public static final Integer STATUS_VALID_CLICKED = 2;
	/** 标记该条记录(3失效/已修改)*/
	public static final Integer STATUS_INVALID_USED = 3;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3004779359580068602L;

	/**
	 * Id
	 */
	private Long id;
	
	/**
	 * 用户id
	 */
	private Long memberId;

	/**
	 * 邮件创建时间
	 */
	private Date createTime;
	
	/**
	 * 加密字段
	 */
	private String Encrypted_S;
	
	/**
	 * 标记该条记录(1有效未点击，2有效已点击，3失效/已修改)
	 */
	private Integer status;
	
	/**
	 * 邮件地址
	 */
	private String emailAddress;
	
	/**
	 * 触发事件的action名
	 */
	private String action;
	
	/**
	 * 记录请求条目
	 * @return
	 */
	private Integer count;
	
	/**
	 * token
	 * @return
	 */
	private String token;
	
	/**
	 * ModifyTime
	 * @return
	 */
	private Date ModifyTime;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_EMAILCHECK", sequenceName = "S_T_PD_EMAILCHECK", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_PD_EMAILCHECK")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "CREATIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "ENCRYPTED_S")
	public String getEncrypted_S() {
		return Encrypted_S;
	}

	public void setEncrypted_S(String encrypted_S) {
		Encrypted_S = encrypted_S;
	}

	@Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	

	@Column(name = "EMAILADDRESS")
	public String getEmailAddress() {
		return emailAddress;
	}


	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@Column(name = "ACTION")
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	@Column(name = "MEMBER_ID")
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	@Column(name = "COUNT")
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Column(name = "TOKEN")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Column(name = "MODIFYTIME")
	public Date getModifyTime() {
		return ModifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		ModifyTime = modifyTime;
	}
	
	
}
