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
package com.baozun.nebula.model.logs;

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
 * 用户绑定日志
 * 
 * @author D.C
 * 
 */
@Entity
@Table(name = "T_AU_USER_BINDING_LOG")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class MemberBindingLog extends BaseModel {
	private static final long serialVersionUID = 1624030444406073873L;
	
	private Long id;
	/**
	 * IP
	 */
	private String ip;
	/**
	 * 绑定ID1
	 */
	private Long userId1;
	/**
	 * 绑定ID2
	 */
	private Long userId2;
	/**
	 * 绑定类型，1：绑定 0：解绑
	 */
	private Integer bindingType;
	/**
	 * 时间
	 */
	private Date createTime;
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_AU_USER_BINDING_LOG", sequenceName = "S_T_AU_USER_BINDING_LOG", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_AU_USER_BINDING_LOG")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "IP", length = 50)
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	

	@Column(name = "USER_ID_1")
	public Long getUserId1() {
		return userId1;
	}
	public void setUserId1(Long userId1) {
		this.userId1 = userId1;
	}
	@Column(name = "USER_ID_2")
	public Long getUserId2() {
		return userId2;
	}
	public void setUserId2(Long userId2) {
		this.userId2 = userId2;
	}
	
	@Column(name = "BINDING_TYPE")
	public Integer getBindingType() {
		return bindingType;
	}
	public void setBindingType(Integer bindingType) {
		this.bindingType = bindingType;
	}
	
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
