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
package com.baozun.nebula.sdk.command;

import java.math.BigDecimal;
import java.util.Date;

import com.baozun.nebula.model.BaseModel;


public class PayInfoCommand extends BaseModel{

	private static final long serialVersionUID = 5654249365915785947L;

	/** PK. */
	private Long				id;
	
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
	/** 支付流水号 **/
	private String 				subOrdinate;
	
	private Date 				createTime;
	
	private String 				picUrl;
	
	private String 				bankCode;
	/**订单code**/
	private String              orderCode;
	/**主支付方式**/
	private Integer             mainPayType;
	/** 代表分期支付的期数 **/
	private Integer             periods; 
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public BigDecimal getPayNumerical() {
		return payNumerical;
	}
	public void setPayNumerical(BigDecimal payNumerical) {
		this.payNumerical = payNumerical;
	}
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	public Integer getPayType() {
		return payType;
	}
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	public String getPayInfo() {
		return payInfo;
	}
	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}
	public String getThirdPayNo() {
		return thirdPayNo;
	}
	public void setThirdPayNo(String thirdPayNo) {
		this.thirdPayNo = thirdPayNo;
	}
	public String getThirdPayAccount() {
		return thirdPayAccount;
	}
	public void setThirdPayAccount(String thirdPayAccount) {
		this.thirdPayAccount = thirdPayAccount;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public Date getVersion() {
		return version;
	}
	public void setVersion(Date version) {
		this.version = version;
	}
	public Boolean getPaySuccessStatus() {
		return paySuccessStatus;
	}
	public void setPaySuccessStatus(Boolean paySuccessStatus) {
		this.paySuccessStatus = paySuccessStatus;
	}
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
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public Integer getMainPayType() {
		return mainPayType;
	}
	public void setMainPayType(Integer mainPayType) {
		this.mainPayType = mainPayType;
	}
	public String getSubOrdinate() {
		return subOrdinate;
	}
	public void setSubOrdinate(String subOrdinate) {
		this.subOrdinate = subOrdinate;
	}
	
	public Integer getPeriods(){
		return periods;
	}
	
	public void setPeriods(Integer periods){
		this.periods = periods;
	}

	
}
