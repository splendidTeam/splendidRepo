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
package com.baozun.nebula.model.salesorder;

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
 * 申请取消订单
 * 
 * @author chuanyang.zheng
 * @creattime 2013-11-20
 */
@Entity
@Table(name = "t_so_cancelorderapp")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class CancelOrderApp extends BaseModel{


	private static final long serialVersionUID = 3242142572107310224L;

	/** oms反馈结果 **/
	public static final Integer APPLYCANCEL_NEW = 1;
	public static final Integer FEEDBACK_AGREE = 2;
	public static final Integer FEEDBACK_NOT_AGREE = 3;
	
	/** PK. */
	private Long				id;
	
	/** 订单号 */
	private String				orderCode;
	
	/** 会员id */
	private Long				memberId;
	
	/** 会员姓名 */
	private String				memberName;
	
	/** 取消原因*/
	private String				reason;
	
	/** 留言*/
	private String				message;
	
	/** 联系人手机号*/
	private String				mobile;
	
	/** 状态*/
	private Integer				status;
	
	/** 处理人id*/
	private Long				handleId;
	
	/** 处理人姓名*/
	private String				handleName;
	
	/** 反馈信息*/
	private String				feedback;
	
	/** 创建时间 */
	private Date				createTime;
	
	/** 修改时间 */
	private Date				modifyTime;
	
	/** version*/
	private Date				version;
	
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SAL_CANCELORDERAPP",sequenceName = "S_T_SAL_CANCELORDERAPP",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SAL_CANCELORDERAPP")
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "ORDER_CODE")
	public String getOrderCode() {
		return orderCode;
	}


	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}


	@Column(name = "MEMBER_ID")
	public Long getMemberId() {
		return memberId;
	}


	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	
	
	@Column(name = "MEMBER_NAME")
	public String getMemberName() {
		return memberName;
	}


	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}


	@Column(name = "REASON",length=200)
	public String getReason() {
		return reason;
	}


	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "MESSAGE",length=200)
	public String getMessage() {
		return message;
	}

	@Column(name = "FEEDBACK",length=500)
	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}


	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name = "MOBILE",length=200)
	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "HANDLE_ID")
	public Long getHandleId() {
		return handleId;
	}


	public void setHandleId(Long handleId) {
		this.handleId = handleId;
	}

	
	@Column(name = "HANDLE_NAME",length=100)
	public String getHandleName() {
		return handleName;
	}


	public void setHandleName(String handleName) {
		this.handleName = handleName;
	}


	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	
	@Column(name = "MODIFY_TIME")
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}


	public void setVersion(Date version) {
		this.version = version;
	}

	


	
}
