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
package com.baozun.nebula.model.payment;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;
/**
 * 支付详细
 * 
 * @author chuanyang.zheng
 * @creattime 2013-11-20
 */
@Entity
@Table(name = "T_SO_PAYWARNNINGLOG")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PayWarnningLog extends BaseModel{


	private static final long serialVersionUID = -3350505308978769968L;
	
	/**
	 * 从支付平台同步信息
	 */
	public static final String RESULT_PAYMENTPLATFORM = "FromPaymentPlatform";
	
	/**
	 * 丢弃支付平台同步信息(在未申请订单查询功能时使用)
	 */
	public static final String RESULT_DISCARD = "Discard the information";
	
	/**
	 * 信息一致
	 */
	public static final String RESULT_SAME = "SAME";

	/** PK. */
	private Long				id;
	
	/**
	 * 订单号
	 */
	private String				orderCode;
	
	/**
	 * 支付流水号
	 */
	private String 				thirPayNo;
	
	/** version*/
	private Date				version;
	
	/** 创建时间  **/
	private Date 				createTime;
	
	/**
	 * 商城支付状态
	 */
	private String 				paystate_shop;
	
	/**
	 * 支付平台返回支付状态
	 */
	private String 				paystate_payment;
	
	/**
	 * 是否支持订单状态查询
	 * @return
	 */
	private Boolean				isSupportQuery;
	
	/**
	 * 处理结果
	 * @return
	 */
	private String 				result;
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SO_PAYWARNNINGLOG",sequenceName = "S_T_SO_PAYWARNNINGLOG",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SO_PAYWARNNINGLOG")
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}


	public void setVersion(Date version) {
		this.version = version;
	}

	@Column(name="CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name="PAYSTATE_SHOP")
    @Index(name = "IDX_PAYWARNNINGLOG_PAYSTATE_SHOP")
	public String getPaystate_shop() {
		return paystate_shop;
	}

	public void setPaystate_shop(String paystate_shop) {
		this.paystate_shop = paystate_shop;
	}

	@Column(name="PAYSTATE_PAYMENT")
    @Index(name = "IDX_PAYWARNNINGLOG_PAYSTATE_PAYMENT")
	public String getPaystate_payment() {
		return paystate_payment;
	}

	public void setPaystate_payment(String paystate_payment) {
		this.paystate_payment = paystate_payment;
	}

	@Column(name="ISSUPPORTQUERY")
    @Index(name = "IDX_PAYWARNNINGLOG_ISSUPPORTQUERY")
	public Boolean getIsSupportQuery() {
		return isSupportQuery;
	}


	public void setIsSupportQuery(Boolean isSupportQuery) {
		this.isSupportQuery = isSupportQuery;
	}

	@Column(name="RESULT")
	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}

	@Column(name="ORDERCODE")
    @Index(name = "IDX_PAYWARNNINGLOG_ORDERCODE")
	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	@Column(name="THIRPAYNO")
	public String getThirPayNo() {
		return thirPayNo;
	}


	public void setThirPayNo(String thirPayNo) {
		this.thirPayNo = thirPayNo;
	}


	public PayWarnningLog(String orderCode, String thirPayNo, Date createTime,
			String paystate_shop, String paystate_payment,
			Boolean isSupportQuery, String result) {
		super();
		this.orderCode = orderCode;
		this.thirPayNo = thirPayNo;
		this.createTime = createTime;
		this.paystate_shop = paystate_shop;
		this.paystate_payment = paystate_payment;
		this.isSupportQuery = isSupportQuery;
		this.result = result;
	}
	
	
	
}
