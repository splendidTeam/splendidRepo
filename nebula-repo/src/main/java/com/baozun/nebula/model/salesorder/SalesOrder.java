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
import javax.persistence.Version;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 订单概要
 * 
 * @author chuanyang.zheng
 * @creattime 2013-11-20
 */

@Entity
@Table(name = "t_so_salesorder")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class SalesOrder extends BaseModel{

	
	private static final long serialVersionUID = 8864498764122811848L;

	/** 1新建. */
	public static final Integer			SALES_ORDER_STATUS_NEW				= 1;

	/** 3 已同步oms. */
	public static final Integer			SALES_ORDER_STATUS_TOOMS			= 3;

	/** 4 库存已确认. */
	public static final Integer			SALES_ORDER_STATUS_CONFIRMED		= 4;

	/** 5 库房准备中. */
	public static final Integer			SALES_ORDER_STATUS_WH_HANDLING		= 5;

	/** 6 在途. */
	public static final Integer			SALES_ORDER_STATUS_DELIVERIED		= 6;
	
	/** 9 会员取消. */
	public static final Integer			SALES_ORDER_STATUS_CANCELED			= 9;
	
	/** 10 商城取消. */
	public static final Integer			SALES_ORDER_STATUS_SYS_CANCELED		= 10;

	/** 15 交易完成. */
	public static final Integer			SALES_ORDER_STATUS_FINISHED			= 15;

	// so finance status
	/** 财务状态:未收款 1. */
	public static final Integer			SALES_ORDER_FISTATUS_NO_PAYMENT		= 1;

	/** 财务状态:货到付款 2. */
	public static final Integer			SALES_ORDER_FISTATUS_COD	= 2;
	
	/** 财务状态:已全额收款 3. */
	public static final Integer			SALES_ORDER_FISTATUS_FULL_PAYMENT	= 3;

	/** 财务状态:已部分收款4. */
	public static final Integer			SALES_ORDER_FISTATUS_PART_PAYMENT	= 4;
	
	/** 1 货到付款. */
	public static final String			SO_PAYMENT_TYPE_COD					= "1";

	/** 2 银行电汇. */
	public static final String			SO_PAYMENT_TYPE_TELETRANSFER		= "2";

	/** 3 网银在线. */
	public static final String			SO_PAYMENT_TYPE_NETPAY				= "3";
	
	/** 4 微信支付*/
	public static final String			SO_PAYMENT_TYPE_WECHAT				= "4";

	/** 6 支付宝. */
	public static final String			SO_PAYMENT_TYPE_ALIPAY				= "6";

	/** 7 快钱. */
	public static final String			SO_PAYMENT_TYPE_99BILL				= "7";

	/** 10 预付卡 只有全额抵扣才会设置此种支付类型. */
	public static final String			SO_PAYMENT_TYPE_PREPAID_CARD		= "10";

	/** 11 财付通. */
	public static final String			SO_PAYMENT_TYPE_TENPAY				= "300";

	/** 12 外部积分兑换. */
	public static final String			SO_PAYMENT_TYPE_EXTERNAL_POINT		= "12";

	/** 新华一成卡 */
	public static final String			SO_PAYMENT_TYPE_XINHUA_CARD			= "104";

	/** LEVIS淘宝B2C－支付宝 */
	public static final String			SO_PAYMENT_TYPE_LEVIS_ALIPAY_B2C	= "108";

	/** 百付宝（汇付天下） */
	public static final String			SO_PAYMENT_TYPE_BAIFUBAO			= "200";

	/** 现金收款 */
	public static final String			SO_PAYMENT_TYPE_SASH				= "9";

	/**
	 * 14 信用卡-支付宝
	 */
	public static final String			SO_PAYMENT_TYPE_ALIPAY_CREDIT		= "14";
	
	/**
	 * 18支付宝-快捷支付网关接口
	 */
	public static final String			SO_PAYMENT_TYPE_ALIPAY_EXPRESS		= "18";
	
	/**
	 * 支付宝信用卡分期付款
	 */
	public static final String			SO_PAYMENT_TYPE_ALIPAY_GREDITCARDINSTALLMENT		= "19";
	
	/**
	 * 零元购
	 */
	public static final String			SO_PAYMENT_TYPE_ZERO_PURCHASE		= "20";
	
	/**
	 * 320 银联支付
	 */
	public static final String			SO_PAYMENT_TYPE_UNIONPAY		= "320";
	
	/**
	 * 21 分期支付
	 */
	public static final String			SO_PAYMENT_TYPE_PERIODS		= "21";
	
	/**
	 * 22 信用卡支付
	 */
	public static final String			SO_PAYMENT_TYPE_CREDIT_CARD	= "22";
	
	/**
	 * 商城正常下单
	 */
	public static final Integer			SO_SOURCE_NORMAL		= 1;
	
	/**
	 * 手机端正常下单
	 */
	public static final Integer			SO_SOURCE_MOBILE_NORMAL		= 3;
	
	/**
	 * 订单类型 1-普通订单
	 */
	public static final Integer NORMAL_ORDER = 1;
	/**
	 * 订单类型 2-预售订单
	 */
	public static final Integer PRESALE_ORDER = 2;

	/**
	 * 支付类型 1-全额付款
	 */
	public static final Integer Full_Payment = 1;
	/**
	 * 支付类型 2-分阶段付款
	 */
	public static final Integer Phased_Payment = 2;

	/** COD收款类型：现金 */
	public static final Integer COD_TYPE_CASH = 1;

	/** COD收款类型：刷卡 */
	public static final Integer COD_TYPE_CARD = 2;

	// -----------------------------------------------------------------

	/** PK. */
	private Long				id;

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
	private String				code;
	
	/** OMS订单号 */
	private String				omsCode;
	
	/** 会员id */
	private Long				memberId;
	
	/** 店铺id*/
	private Long                shopId;
	
	/** 会员名称*/
	private String				memberName;
	
	/** 游客标识 */
	private String				guestIdentify;
	
	/** 商品总数量 */
	private Integer				quantity;
	
	/** 
	 * 总价 不含运费的客户端显示最终金额 
	 * 总价+整单折扣=sum（行商品销售价X数量）
	 *  
	 */
	private BigDecimal			total;
	
	/** 
	 * 整单折扣
	 * 整单折扣-sum（行折扣）= 由于整单促销/商城积分形成的未分摊到行上的折扣总额 
	 */
	private BigDecimal			discount;
	
	/** 物流状态 */
	private Integer				logisticsStatus;
	
	/** 财务状态 */
	private Integer				financialStatus;
	
	/** 支付方式 */
	private Integer				payment;
	
	/** 订单来源 */
	private Integer				source;
	
	/** 下单ip */
	private String				ip;
	
	/** 应付运费 */
	private BigDecimal			payableFreight;
	
	/** 实付运费 */
	private BigDecimal			actualFreight;
	
    /**
     * 物流单号
     * 快递单号：当出库时会提供
     */
    private String transCode;

    /**
     * 物流商编码
     */
    private String logisticsProviderCode;
    
    
    /**
     * 物流商名称
     */
    private String logisticsProviderName;
	
	/** 发票号 */
	private String				receiptCode;
	
	/** 发票类型 */
	private Integer				receiptType;
	
	/** 发票抬头 */
	private String				receiptTitle;
	
	/** 发票内容 */
	private String				receiptContent;
	
	/** 备注 */
	private String				remark;
	
	/** 创建时间 */
	private Date				createTime;
	
	/** 修改时间 */
	private Date				modifyTime;
	
	/** 下单时的语言 由于用于wormhole发邮件时邮件模板的多语言，以及地址信息的多语言 */
	private String 				lang;

	/** version*/
	private Date				version;
	
	/** 是否是QS订单，默认false  */
	private Boolean				isQS									=  false;
	
	/** COD类型. */
	private Integer codPaymentType;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SAL_SALESORDER",sequenceName = "S_T_SAL_SALESORDER",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SAL_SALESORDER")
	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	@Column(name = "CODE", length = 50)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "OMS_CODE", length = 50)
	public String getOmsCode() {
		return omsCode;
	}

	public void setOmsCode(String omsCode) {
		this.omsCode = omsCode;
	}

	@Column(name = "MEMBER_ID")
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	@Column(name = "SHOP_ID")
	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	@Column(name = "GUEST_IDENTIFY", length = 200)
	public String getGuestIdentify() {
		return guestIdentify;
	}

	public void setGuestIdentify(String guestIdentify) {
		this.guestIdentify = guestIdentify;
	}

	@Column(name = "QUANTITY")
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	@Column(name = "TOTAL")
	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	@Column(name = "DISCOUNT")
	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	@Column(name = "LOGISTICS_STATUS")
	public Integer getLogisticsStatus() {
		return logisticsStatus;
	}

	public void setLogisticsStatus(Integer logisticsStatus) {
		this.logisticsStatus = logisticsStatus;
	}

	@Column(name = "FINANCIAL_STATUS")
	public Integer getFinancialStatus() {
		return financialStatus;
	}

	public void setFinancialStatus(Integer financialStatus) {
		this.financialStatus = financialStatus;
	}

	@Column(name = "PAYMENT")
	public Integer getPayment() {
		return payment;
	}

	public void setPayment(Integer payment) {
		this.payment = payment;
	}

	@Column(name = "SOURCE")
	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	@Column(name = "IP", length = 50)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@Column(name = "PAYABLE_FREIGHT")
	public BigDecimal getPayableFreight() {
		return payableFreight;
	}

	public void setPayableFreight(BigDecimal payableFreight) {
		this.payableFreight = payableFreight;
	}

	@Column(name = "ACTUAL_FREIGHT")
	public BigDecimal getActualFreight() {
		return actualFreight;
	}

	public void setActualFreight(BigDecimal actualFreight) {
		this.actualFreight = actualFreight;
	}

	@Column(name = "RECEIPT_CODE", length = 100)
	public String getReceiptCode() {
		return receiptCode;
	}

	public void setReceiptCode(String receiptCode) {
		this.receiptCode = receiptCode;
	}

	@Column(name = "RECEIPT_TYPE")
	public Integer getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(Integer receiptType) {
		this.receiptType = receiptType;
	}

	@Column(name = "RECEIPT_TITLE", length = 200)
	public String getReceiptTitle() {
		return receiptTitle;
	}

	public void setReceiptTitle(String receiptTitle) {
		this.receiptTitle = receiptTitle;
	}

	@Column(name = "RECEIPT_CONTENT", length = 500)
	public String getReceiptContent() {
		return receiptContent;
	}

	public void setReceiptContent(String receiptContent) {
		this.receiptContent = receiptContent;
	}

	@Column(name = "REMARK", length = 500)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
	
	@Column(name = "MEMBER_NAME", length = 100)
	public String getMemberName() {
		return memberName;
	}
	
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	@Column(name = "TRANS_CODE")
	public String getTransCode() {
		return transCode;
	}

	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	@Column(name = "LOGISTICS_PROVIDER_CODE")
	public String getLogisticsProviderCode() {
		return logisticsProviderCode;
	}

	public void setLogisticsProviderCode(String logisticsProviderCode) {
		this.logisticsProviderCode = logisticsProviderCode;
	}

	@Column(name = "LOGISTICS_PROVIDER_NAME")
	public String getLogisticsProviderName() {
		return logisticsProviderName;
	}

	public void setLogisticsProviderName(String logisticsProviderName) {
		this.logisticsProviderName = logisticsProviderName;
	}

	@Column(name = "IS_QS")
	@Index(name = "IDX_SALESORDER_IS_QS")
	public Boolean getIsQS() {
		return isQS;
	}

	public void setIsQS(Boolean isQS) {
		this.isQS = isQS;
	}

	@Column(name = "ORDER_TYPE")
	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	@Column(name = "APPOINT_SHIP_DATE")
	public Date getAppointShipDate() {
		return appointShipDate;
	}

	public void setAppointShipDate(Date appointShipDate) {
		this.appointShipDate = appointShipDate;
	}

	@Column(name = "PAY_TYPE")
	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	@Column(name = "COD_PAYMENT_TYPE")
	public Integer getCodPaymentType() {
		return codPaymentType;
	}

	public void setCodPaymentType(Integer codPaymentType) {
		this.codPaymentType = codPaymentType;
	}
	
	@Column(name = "LANG")
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

}
