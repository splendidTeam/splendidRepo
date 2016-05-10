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
import java.util.List;

import com.baozun.nebula.command.OnLinePaymentCancelCommand;
import com.baozun.nebula.command.OnLinePaymentCommand;
import com.baozun.nebula.model.BaseModel;
import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.utilities.common.command.WechatPayParamCommand;

public class SalesOrderCommand extends BaseModel {

	private static final long serialVersionUID = 4539781027280506601L;

	/** PK. */
	private Long id;

	/**
	 * 订单类型 1-普通订单(默认), 2-预售订单
	 */
	private Integer orderType;

	/**
	 * 预计发货时间
	 */
	private Date appointShipDate;

	/**
	 * 支付类型 1-全额付款（默认）, 2-分阶段付款
	 */
	private Integer payType;

	/** 订单号 */
	private String code;

	/** OMS订单号 */
	private String omsCode;

	/** 会员id */
	private Long memberId;

	/** 会员名称 */
	private String memberName;

	/** 订单行 */
	private List<OrderLineCommand> orderLines;

	/** 商品信息描述 */
	private String describe;

	/** 收货信息平铺 */
	/** 姓名 */
	private String name;

	/** 购买人姓名 */
	private String buyerName;

	/** 购买人电话 */
	private String buyerTel;

	/** 国 */
	private String country;

	/** 省 */
	private String province;

	/** 市 */
	private String city;

	/** 区 */
	private String area;

	/** 镇 */
	private String town;

	/** 国 */
	private Long countryId;

	/** 省 */
	private Long provinceId;

	/** 市 */
	private Long cityId;

	/** 区 */
	private Long areaId;

	/** 镇id */
	private Long townId;

	/** 地址 */
	private String address;

	/** 手机 */
	private String mobile;

	/** 固话 */
	private String tel;

	/** email */
	private String email;

	/** 邮编 */
	private String postcode;

	/** 指定时间段 */
	private String appointTimeQuantum;

	/** 指定日期 */
	private String appointTime;

	/** 指定类型 */
	private String appointType;

	/** 收货信息修改时间 */
	private Date consigneeModifyTime;

	/** 订单促销信息 */
	private List<OrderPromotionCommand> orderPromotions;

	/** 该订单所使用优惠券 **/
	private List<CouponCodeCommand> couponCodes;

	/** 支付信息 */
	private List<PayInfoCommand> payInfo;

	/** 游客标识 */
	private String guestIdentify;

	/** 商品总数量 */
	private Integer quantity;

	/** 总价 */
	private BigDecimal total;

	/** 折扣 */
	private BigDecimal discount;

	/** 物流状态 */
	private Integer logisticsStatus;

	/** 财务状态 */
	private Integer financialStatus;

	/** 支付方式 */
	private Integer payment;

	/** 支付方式字符串 */
	private String paymentStr;

	/** 订单来源 */
	private Integer source;

	/** 下单ip */
	private String ip;

	/** 应付运费 */
	private BigDecimal payableFreight;

	/** 实付运费 */
	private BigDecimal actualFreight;

	/**
	 * 物流单号 快递单号：当出库时会提供
	 */
	private String transCode;

	/** 物流商编码 */
	private String logisticsProviderCode;

	/** 物流商名称 */
	private String logisticsProviderName;

	/** 发票号 */
	private String receiptCode;

	/** 发票类型 */
	private Integer receiptType;

	/** 发票抬头 */
	private String receiptTitle;

	/** 发票内容 */
	private String receiptContent;

	/** 备注 */
	private String remark;

	/** 修改时间 */
	private Date modifyTime;

	/** 创建时间 */
	private Date createTime;

	/** 是否是立即购买的订单 **/
	private Boolean isImmediatelyBuy;

	/** 是否是后台下单 **/
	private Boolean isBackCreateOrder = false;

	/** 邮件模板 **/
	private String emailTemplete;

	/** 在线支付信息 **/
	private OnLinePaymentCommand onLinePaymentCommand;

	/** 在线支付取消信息 **/
	private OnLinePaymentCancelCommand onLinePaymentCancelCommand;

	/** 微信支付相关 **/
	private WechatPayParamCommand wechatPayParamCommand;

	/** 计算运费需要的信息 **/
	private CalcFreightCommand calcFreightCommand;

	/********************** 保存时 拆单需要保存每个店铺的快递和指定收货时间 **/
	/** 快递公司 String格式：shopid||logisticsProvidercode||logisticsProviderName */
	private List<String> logisticsProvider;

	/** 指定时间段 String格式：shopId||指定时间段 */
	private List<String> appointTimeQuantums;

	/** 指定日期 String格式：shopId||指定日期 */
	private List<String> appointTimes;

	/** 指定类型 String格式：shopId||指定类型 */
	private List<String> appointTypes;

	/** 给卖家留言 String格式：shopId||留言 */
	private List<String> remarks;

	/**
	 * 支付方式 String格式：shopId||payMentType||金额 可能涉及预付卡或者积分方式支付
	 * 
	 */
	private List<String> soPayMentDetails;
	/** 是否qs **/
	private Boolean isQs = false;

	/** 物流方式 **/
	private Long distributionModeId;

	/** 银行分期付款的期数 */
	private Integer periods;

	public Long getDistributionModeId() {
		return distributionModeId;
	}

	public void setDistributionModeId(Long distributionModeId) {
		this.distributionModeId = distributionModeId;
	}

	public List<String> getSoPayMentDetails() {
		return soPayMentDetails;
	}

	public void setSoPayMentDetails(List<String> soPayMentDetails) {
		this.soPayMentDetails = soPayMentDetails;
	}

	public String getTransCode() {
		return transCode;
	}

	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	public String getLogisticsProviderCode() {
		return logisticsProviderCode;
	}

	public void setLogisticsProviderCode(String logisticsProviderCode) {
		this.logisticsProviderCode = logisticsProviderCode;
	}

	public String getLogisticsProviderName() {
		return logisticsProviderName;
	}

	public void setLogisticsProviderName(String logisticsProviderName) {
		this.logisticsProviderName = logisticsProviderName;
	}

	public String getEmailTemplete() {
		return emailTemplete;
	}

	public void setEmailTemplete(String emailTemplete) {
		this.emailTemplete = emailTemplete;
	}

	public String getPaymentStr() {
		return paymentStr;
	}

	public void setPaymentStr(String paymentStr) {
		this.paymentStr = paymentStr;
	}

	public CalcFreightCommand getCalcFreightCommand() {
		return calcFreightCommand;
	}

	public void setCalcFreightCommand(CalcFreightCommand calcFreightCommand) {
		this.calcFreightCommand = calcFreightCommand;
	}

	public List<String> getRemarks() {
		return remarks;
	}

	public void setRemarks(List<String> remarks) {
		this.remarks = remarks;
	}

	public List<String> getLogisticsProvider() {
		return logisticsProvider;
	}

	public void setLogisticsProvider(List<String> logisticsProvider) {
		this.logisticsProvider = logisticsProvider;
	}

	public List<String> getAppointTimeQuantums() {
		return appointTimeQuantums;
	}

	public void setAppointTimeQuantums(List<String> appointTimeQuantums) {
		this.appointTimeQuantums = appointTimeQuantums;
	}

	public List<String> getAppointTimes() {
		return appointTimes;
	}

	public void setAppointTimes(List<String> appointTimes) {
		this.appointTimes = appointTimes;
	}

	public List<String> getAppointTypes() {
		return appointTypes;
	}

	public void setAppointTypes(List<String> appointTypes) {
		this.appointTypes = appointTypes;
	}

	public List<CouponCodeCommand> getCouponCodes() {
		return couponCodes;
	}

	public void setCouponCodes(List<CouponCodeCommand> couponCodes) {
		this.couponCodes = couponCodes;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getOmsCode() {
		return omsCode;
	}

	public void setOmsCode(String omsCode) {
		this.omsCode = omsCode;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public List<OrderLineCommand> getOrderLines() {
		return orderLines;
	}

	public void setOrderLines(List<OrderLineCommand> orderLines) {
		this.orderLines = orderLines;
	}

	public String getGuestIdentify() {
		return guestIdentify;
	}

	public void setGuestIdentify(String guestIdentify) {
		this.guestIdentify = guestIdentify;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public Integer getLogisticsStatus() {
		return logisticsStatus;
	}

	public void setLogisticsStatus(Integer logisticsStatus) {
		this.logisticsStatus = logisticsStatus;
	}

	public Integer getFinancialStatus() {
		return financialStatus;
	}

	public void setFinancialStatus(Integer financialStatus) {
		this.financialStatus = financialStatus;
	}

	public Integer getPayment() {
		return payment;
	}

	public void setPayment(Integer payment) {
		this.payment = payment;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public BigDecimal getPayableFreight() {
		return payableFreight;
	}

	public void setPayableFreight(BigDecimal payableFreight) {
		this.payableFreight = payableFreight;
	}

	public BigDecimal getActualFreight() {
		return actualFreight;
	}

	public void setActualFreight(BigDecimal actualFreight) {
		this.actualFreight = actualFreight;
	}

	public String getReceiptCode() {
		return receiptCode;
	}

	public void setReceiptCode(String receiptCode) {
		this.receiptCode = receiptCode;
	}

	public Integer getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(Integer receiptType) {
		this.receiptType = receiptType;
	}

	public String getReceiptTitle() {
		return receiptTitle;
	}

	public void setReceiptTitle(String receiptTitle) {
		this.receiptTitle = receiptTitle;
	}

	public String getReceiptContent() {
		return receiptContent;
	}

	public void setReceiptContent(String receiptContent) {
		this.receiptContent = receiptContent;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public List<PayInfoCommand> getPayInfo() {
		return payInfo;
	}

	public void setPayInfo(List<PayInfoCommand> payInfo) {
		this.payInfo = payInfo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getAppointTimeQuantum() {
		return appointTimeQuantum;
	}

	public void setAppointTimeQuantum(String appointTimeQuantum) {
		this.appointTimeQuantum = appointTimeQuantum;
	}

	public String getAppointTime() {
		return appointTime;
	}

	public void setAppointTime(String appointTime) {
		this.appointTime = appointTime;
	}

	public String getAppointType() {
		return appointType;
	}

	public void setAppointType(String appointType) {
		this.appointType = appointType;
	}

	public Date getConsigneeModifyTime() {
		return consigneeModifyTime;
	}

	public void setConsigneeModifyTime(Date consigneeModifyTime) {
		this.consigneeModifyTime = consigneeModifyTime;
	}

	public List<OrderPromotionCommand> getOrderPromotions() {
		return orderPromotions;
	}

	public void setOrderPromotions(List<OrderPromotionCommand> orderPromotions) {
		this.orderPromotions = orderPromotions;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberName() {
		return memberName;
	}

	public Boolean getIsImmediatelyBuy() {
		return isImmediatelyBuy;
	}

	public void setIsImmediatelyBuy(Boolean isImmediatelyBuy) {
		this.isImmediatelyBuy = isImmediatelyBuy;
	}

	public Boolean getIsBackCreateOrder() {
		return isBackCreateOrder;
	}

	public void setIsBackCreateOrder(Boolean isBackCreateOrder) {
		this.isBackCreateOrder = isBackCreateOrder;
	}

	public OnLinePaymentCommand getOnLinePaymentCommand() {
		return onLinePaymentCommand;
	}

	public void setOnLinePaymentCommand(OnLinePaymentCommand onLinePaymentCommand) {
		this.onLinePaymentCommand = onLinePaymentCommand;
	}

	public OnLinePaymentCancelCommand getOnLinePaymentCancelCommand() {
		return onLinePaymentCancelCommand;
	}

	public void setOnLinePaymentCancelCommand(OnLinePaymentCancelCommand onLinePaymentCancelCommand) {
		this.onLinePaymentCancelCommand = onLinePaymentCancelCommand;
	}

	public WechatPayParamCommand getWechatPayParamCommand() {
		return wechatPayParamCommand;
	}

	public void setWechatPayParamCommand(WechatPayParamCommand wechatPayParamCommand) {
		this.wechatPayParamCommand = wechatPayParamCommand;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public Long getTownId() {
		return townId;
	}

	public void setTownId(Long townId) {
		this.townId = townId;
	}

	public Boolean getIsQs() {
		return isQs;
	}

	public void setIsQs(Boolean isQs) {
		this.isQs = isQs;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Date getAppointShipDate() {
		return appointShipDate;
	}

	public void setAppointShipDate(Date appointShipDate) {
		this.appointShipDate = appointShipDate;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getPeriods() {
		return periods;
	}

	public void setPeriods(Integer periods) {
		this.periods = periods;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getBuyerTel() {
		return buyerTel;
	}

	public void setBuyerTel(String buyerTel) {
		this.buyerTel = buyerTel;
	}

}
