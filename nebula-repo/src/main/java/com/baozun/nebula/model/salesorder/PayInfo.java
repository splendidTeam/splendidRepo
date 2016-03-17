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

import java.math.BigDecimal;
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
 * 支付详细
 * 
 * @author chuanyang.zheng
 * @creattime 2013-11-20
 */
@Entity
@Table(name = "t_so_payinfo")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PayInfo extends BaseModel{


	private static final long serialVersionUID = -3350505308978769968L;

	public static Integer COUPON_PAY = 101;
	
	/** PK. */
	private Long				id;
	
	/** 有效支付（开始）实践 */
	private Date payStartTime;

	/** 有效支付（结束）实践 */
	private Date payEndTime;

	/** 订单id */
	private Long				orderId;
	
	/** 支付数值 */
	private BigDecimal			payNumerical;
	
	/** 支付金额 */
	private BigDecimal			payMoney;
	
	/** 支付类型 */
	private Integer				payType;
	
	/** 付款详情 */
	private String				payInfo;
	
	/** 第三方支付流水 */
	private String				thirdPayNo;
	
	/** 第三方支付帐号 */
	private String				thirdPayAccount ;
	
	/** 修改时间 */
	private Date				modifyTime;
	
	/** version*/
	private Date				version;
	
	/** 是否支付成功状态 **/
	private Boolean 			paySuccessStatus;
	
	
	private Date 				createTime;
	
	
	
	private String 				picUrl;
	
	private String 				bankCode;
	
	/** 代表分期支付的期数 **/
	private Integer             periods; 
	
	/** 代表分期金额 **/
	private BigDecimal			amount;
	
	/** 代表分期手续费 **/
	private BigDecimal			poundage;
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SAL_PAYINFO",sequenceName = "S_T_SO_PAYINFO",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SAL_PAYINFO")
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "ORDER_ID")
	public Long getOrderId() {
		return orderId;
	}


	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	@Column(name = "PAY_NUMERICAL")
	public BigDecimal getPayNumerical() {
		return payNumerical;
	}


	public void setPayNumerical(BigDecimal payNumerical) {
		this.payNumerical = payNumerical;
	}

	@Column(name = "PAY_MONEY")
	public BigDecimal getPayMoney() {
		return payMoney;
	}


	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}

	@Column(name = "PAY_TYPE")
	public Integer getPayType() {
		return payType;
	}


	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	@Column(name = "PAY_INFO")
	public String getPayInfo() {
		return payInfo;
	}


	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}
	
	@Column(name = "THIRD_PAY_NO")
	public String getThirdPayNo() {
		return thirdPayNo;
	}

	public void setThirdPayNo(String thirdPayNo) {
		this.thirdPayNo = thirdPayNo;
	}

	@Column(name = "THIRD_PAY_ACCOUNT")
	public String getThirdPayAccount() {
		return thirdPayAccount;
	}


	public void setThirdPayAccount(String thirdPayAccount) {
		this.thirdPayAccount = thirdPayAccount;
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

	@Column(name = "PAY_SUCCESS_STATUS")
	public Boolean getPaySuccessStatus() {
		return paySuccessStatus;
	}

	public void setPaySuccessStatus(Boolean paySuccessStatus) {
		this.paySuccessStatus = paySuccessStatus;
	}


	@Column(name="CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}



	public String getPicUrl() {
		return picUrl;
	}


	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	
	@Column(name = "PERIODS")
	public Integer getPeriods(){
		return periods;
	}
	
	public void setPeriods(Integer periods){
		this.periods = periods;
	}

	@Column(name = "PAY_START_TIME")
	public Date getPayStartTime() {
		return payStartTime;
	}

	public void setPayStartTime(Date payStartTime) {
		this.payStartTime = payStartTime;
	}

	@Column(name = "PAY_END_TIME")
	public Date getPayEndTime() {
		return payEndTime;
	}

	public void setPayEndTime(Date payEndTime) {
		this.payEndTime = payEndTime;
	}
	

	/** 
	 * @return amount 
	 * @date 2016年2月18日 下午3:10:36 
	 */
	@Column(name = "AMOUNT")
	public BigDecimal getAmount() {
		return amount;
	}


	/** 
	 * @param amount 要设置的 amount 
	 * @date 2016年2月18日 下午3:10:36 
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	/** 
	 * @return poundage 
	 * @date 2016年2月18日 下午3:10:36 
	 */
	@Column(name = "POUNDAGE")
	public BigDecimal getPoundage() {
		return poundage;
	}


	/** 
	 * @param poundage 要设置的 poundage 
	 * @date 2016年2月18日 下午3:10:36 
	 */
	public void setPoundage(BigDecimal poundage) {
		this.poundage = poundage;
	}
}
