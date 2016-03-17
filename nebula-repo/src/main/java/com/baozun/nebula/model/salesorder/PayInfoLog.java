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
 * 支付详细的log
 * 因为支付流水每次都会重刷
 * 所以需要找到支付流水对应的支付详细，所以使用log表记录
 * 
 * @author 
 * @creattime 2013-11-20
 */
@Entity
@Table(name = "T_SO_PAYINFO_LOG")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PayInfoLog extends BaseModel{


	private static final long serialVersionUID = -3350505308978769968L;

	public static Integer COUPON_PAY = 101;
	
	/** PK. */
	private Long				id;
	
	/** 订单id */
	private Long				orderId;
	
	/**
	 * 支付详细id
	 */
	private Long				payInfoId;
	
	/** 支付数值 */
	private BigDecimal			payNumerical;
	
	/** 支付金额 */
	private BigDecimal			payMoney;
	
	/** 支付类型 */
	private Integer				payType;
	
	/** 付款详情 */
	private String				payInfo;
	
	/** 
	 * 第三方支付类型
	 * 如： 支付宝内部类型  用于支付宝内部使用
	 * 
	 */
	private Integer				thirdPayType ;
	
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
	
	/** 拆单号 **/
	private String 				subOrdinate;
	
	/** 付款人 **/
	private String 				paymentPeople;
	
	private Date 				createTime;
	
	/** 是否已经成功调用取消交易接口  true表示已经成功调用 false表示为调用或者未成功调用**/
	private Boolean 			callCloseStatus;
	
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
	@SequenceGenerator(name = "SEQ_T_SO_PAYINFO_LOG",sequenceName = "S_T_SO_PAYINFO_LOG",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SO_PAYINFO_LOG")
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}
	
	
	@Column(name = "PAY_INFO_ID")
	public Long getPayInfoId() {
		return payInfoId;
	}


	public void setPayInfoId(Long payInfoId) {
		this.payInfoId = payInfoId;
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

	@Column(name = "SUB_ORDINATE")
	public String getSubOrdinate() {
		return subOrdinate;
	}

	public void setSubOrdinate(String subOrdinate) {
		this.subOrdinate = subOrdinate;
	}
	
	@Column(name="PAYMENT_PEOPLE")
	public String getPaymentPeople() {
		return paymentPeople;
	}
	
	public void setPaymentPeople(String paymentPeople) {
		this.paymentPeople = paymentPeople;
	}

	@Column(name="CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	@Column(name="CALL_CLOSE_STATUS")
	public Boolean getCallCloseStatus() {
		return callCloseStatus;
	}


	public void setCallCloseStatus(Boolean callCloseStatus) {
		this.callCloseStatus = callCloseStatus;
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


	public Integer getThirdPayType() {
		return thirdPayType;
	}


	public void setThirdPayType(Integer thirdPayType) {
		this.thirdPayType = thirdPayType;
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
	
	@Column(name = "PERIODS")
	public Integer getPeriods(){
		return periods;
	}
	
	public void setPeriods(Integer periods){
		this.periods = periods;
	}
}
