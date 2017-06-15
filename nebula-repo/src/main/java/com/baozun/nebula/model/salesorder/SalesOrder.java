/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
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
 * 订单概要.
 *
 * @author chuanyang.zheng
 * @creattime 2013-11-20
 */

@Entity
@Table(name = "T_SO_SALESORDER")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class SalesOrder extends BaseModel{

    /** The Constant serialVersionUID. */
    private static final long  serialVersionUID                             = 8864498764122811848L;

    /** 1新建. */
    public static final int    SALES_ORDER_STATUS_NEW                       = 1;

    /** 3 已同步oms. */
    public static final int    SALES_ORDER_STATUS_TOOMS                     = 3;

    /** 4 库存已确认. */
    public static final int    SALES_ORDER_STATUS_CONFIRMED                 = 4;

    /** 5 库房准备中. */
    public static final int    SALES_ORDER_STATUS_WH_HANDLING               = 5;

    /** 6 在途. */
    public static final int    SALES_ORDER_STATUS_DELIVERIED                = 6;

    /** 9 会员取消. */
    public static final int    SALES_ORDER_STATUS_CANCELED                  = 9;

    /** 10 商城取消. */
    public static final int    SALES_ORDER_STATUS_SYS_CANCELED              = 10;

    /** 15 交易完成. */
    public static final int    SALES_ORDER_STATUS_FINISHED                  = 15;
    
  //---------------------------------------------------------------------------------------

    // so finance status
    /** 财务状态:未收款 1. */
    public static final int    SALES_ORDER_FISTATUS_NO_PAYMENT              = 1;

    /** 财务状态:货到付款 2. */
    public static final int    SALES_ORDER_FISTATUS_COD                     = 2;

    /** 财务状态:已全额收款 3. */
    public static final int    SALES_ORDER_FISTATUS_FULL_PAYMENT            = 3;

    /** 财务状态:已部分收款4. */
    public static final int    SALES_ORDER_FISTATUS_PART_PAYMENT            = 4;
    
    //---------------------------------------------------------------------------------------

    /** 1 货到付款. */
    public static final String SO_PAYMENT_TYPE_COD                          = "1";

    /** 2 银行电汇. */
    public static final String SO_PAYMENT_TYPE_TELETRANSFER                 = "2";

    /** 3 网银在线. */
    public static final String SO_PAYMENT_TYPE_NETPAY                       = "3";

    /** 4 微信支付. */
    public static final String SO_PAYMENT_TYPE_WECHAT                       = "4";

    /** 6 支付宝. */
    public static final String SO_PAYMENT_TYPE_ALIPAY                       = "6";

    /** 7 快钱. */
    public static final String SO_PAYMENT_TYPE_99BILL                       = "7";

    /** 10 预付卡 只有全额抵扣才会设置此种支付类型. */
    public static final String SO_PAYMENT_TYPE_PREPAID_CARD                 = "10";

    /** 11 财付通. */
    public static final String SO_PAYMENT_TYPE_TENPAY                       = "300";

    /** 12 外部积分兑换. */
    public static final String SO_PAYMENT_TYPE_EXTERNAL_POINT               = "12";

    /** 新华一成卡. */
    public static final String SO_PAYMENT_TYPE_XINHUA_CARD                  = "104";

    /** LEVIS淘宝B2C－支付宝. */
    public static final String SO_PAYMENT_TYPE_LEVIS_ALIPAY_B2C             = "108";

    /** 百付宝（汇付天下）. */
    public static final String SO_PAYMENT_TYPE_BAIFUBAO                     = "200";

    /** 现金收款. */
    public static final String SO_PAYMENT_TYPE_SASH                         = "9";

    /** 14 信用卡-支付宝. */
    public static final String SO_PAYMENT_TYPE_ALIPAY_CREDIT                = "14";

    /** 18支付宝-快捷支付网关接口. */
    public static final String SO_PAYMENT_TYPE_ALIPAY_EXPRESS               = "18";

    /** 支付宝信用卡分期付款. */
    public static final String SO_PAYMENT_TYPE_ALIPAY_GREDITCARDINSTALLMENT = "19";

    /** 零元购. */
    public static final String SO_PAYMENT_TYPE_ZERO_PURCHASE                = "20";

    /** 320 银联支付. */
    public static final String SO_PAYMENT_TYPE_UNIONPAY                     = "320";

    /** 21 分期支付. */
    public static final String SO_PAYMENT_TYPE_PERIODS                      = "21";

    /** 22 信用卡支付. */
    public static final String SO_PAYMENT_TYPE_CREDIT_CARD                  = "22";
    
    /** 710 Paypal */
	public static final String	SO_PAYMENT_TYPE_PAYPAL						= "710";
    
    /** 720 PayDoolar */
	public static final String	SO_PAYMENT_TYPE_PAYDOOLAR					= "720";
	
	/** 131 支付宝-国际卡 */
	public static final String	SO_PAYMENT_TYPE_INTERNATIONALCARD		    = "131";
	
	   // -----------------------------------------------------------------

    /** 商城正常下单. */
    public static final int    SO_SOURCE_NORMAL                             = 1;

    /** Shopdog正常下单. */
    public static final int    SO_SOURCE_SHOPDOG_NORMAL                     = 2;

    /** 手机端正常下单. */
    public static final int    SO_SOURCE_MOBILE_NORMAL                      = 3;
    
    // -----------------------------------------------------------------

    /** 订单类型 1-普通订单. */
    public static final int    NORMAL_ORDER                                 = 1;

    /** 订单类型 2-预售订单. */
    public static final int    PRESALE_ORDER                                = 2;

    /** 支付类型 1-全额付款. */
    public static final int    Full_Payment                                 = 1;

    /** 支付类型 2-分阶段付款. */
    public static final int    Phased_Payment                               = 2;
    
    // -----------------------------------------------------------------

    /** COD收款类型：现金. */
    public static final int    COD_TYPE_CASH                                = 1;

    /** COD收款类型：刷卡. */
    public static final int    COD_TYPE_CARD                                = 2;

    // -----------------------------------------------------------------

    /** PK. */
    private Long               id;

    /** 订单号. */
    private String             code;

    /** OMS订单号. */
    private String             omsCode;

    /** 店铺id. */
    private Long               shopId;

    /** 会员id. */
    private Long               memberId;

    /** 会员名称. */
    private String             memberName;

    /** 游客标识. */
    private String             guestIdentify;

    /** 订单类型 1-普通订单(默认), 2-预售订单. */
    private Integer            orderType;

    /** 预计发货时间. */
    private Date               appointShipDate;

    /** 支付类型 1-全额付款（默认）, 2-分阶段付款. */
    private Integer            payType;
    
    //**********************************************************************

    /** 商品总数量. */
    private Integer            quantity;

    /** 总价 不含运费的客户端显示最终金额 总价+整单折扣=sum（行商品销售价X数量）. */
    private BigDecimal         total;

    /** 整单折扣 整单折扣-sum（行折扣）= 由于整单促销/商城积分形成的未分摊到行上的折扣总额. */
    private BigDecimal         discount;
    
    //------------------------------------------------------------------------------------------

    /** 物流状态. */
    private Integer            logisticsStatus;

    /** 财务状态. */
    private Integer            financialStatus;

    /** 支付方式. */
    private Integer            payment;
    
  //------------------------------------------------------------------------------------------


    /** 应付运费. */
    private BigDecimal         payableFreight;

    /** 实付运费. */
    private BigDecimal         actualFreight;

    /** 物流单号 快递单号：当出库时会提供. */
    private String             transCode;

    /** 物流商编码. */
    private String             logisticsProviderCode;

    /** 物流商名称. */
    private String             logisticsProviderName;

    //**********************************************************************
    /** 发票号. */
    private String             receiptCode;

    /** 发票类型. */
    private Integer            receiptType;

    /** 发票抬头. */
    private String             receiptTitle;

    /** 发票收货人. */
    private String             receiptConsignee;

    /** 发票收货人联系方式. */
    private String             receiptTelphone;

    /** 发票收货地址. */
    private String             receiptAddress;

    /** 发票内容. */
    private String             receiptContent;
    
    /** 纳税人识别码 . 
     * 纳税人识别码就是税务登记证上的号，每个企业的识别号都是唯一的
     * @since 5.3.2.18
     * */
    private String             taxPayerId;

    //**********************************************************************
    /** 下单时的语言 由于用于wormhole发邮件时邮件模板的多语言，以及地址信息的多语言. */
    private String             lang;

    /** 备注. */
    private String             remark;

    /** 是否是QS订单，默认false. */
    private Boolean            isQS                                         = false;

    /** COD类型. */
    private Integer            codPaymentType;
    
    //**********************************************************************
    /** 订单来源. */
    private Integer            source;

    /** 下单ip. */
    private String             ip;

    //**********************************************************************

    /** 创建时间. */
    private Date               createTime;

    /** 修改时间. */
    private Date               modifyTime;

    /** version. */
    private Date               version;

    //**********************************************************************

    /**
     * 获得 pK.
     *
     * @return the pK
     */
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_T_SAL_SALESORDER",sequenceName = "S_T_SAL_SALESORDER",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SAL_SALESORDER")
    public Long getId(){
        return id;
    }

    /**
     * 设置 pK.
     *
     * @param id
     *            the new pK
     */
    public void setId(Long id){
        this.id = id;
    }

    /**
     * 获得 订单号.
     *
     * @return the 订单号
     */
    @Column(name = "CODE",length = 50)
    @Index(name = "IDX_SALESORDER_CODE")
    public String getCode(){
        return code;
    }

    /**
     * 设置 订单号.
     *
     * @param code
     *            the new 订单号
     */
    public void setCode(String code){
        this.code = code;
    }

    /**
     * 获得 oMS订单号.
     *
     * @return the oMS订单号
     */
    @Column(name = "OMS_CODE",length = 50)
    public String getOmsCode(){
        return omsCode;
    }

    /**
     * 设置 oMS订单号.
     *
     * @param omsCode
     *            the new oMS订单号
     */
    public void setOmsCode(String omsCode){
        this.omsCode = omsCode;
    }

    /**
     * 获得 会员id.
     *
     * @return the 会员id
     */
    @Column(name = "MEMBER_ID")
    @Index(name = "IDX_SALESORDER_MEMBER_ID")
    public Long getMemberId(){
        return memberId;
    }

    /**
     * 设置 会员id.
     *
     * @param memberId
     *            the new 会员id
     */
    public void setMemberId(Long memberId){
        this.memberId = memberId;
    }

    /**
     * 获得 店铺id.
     *
     * @return the 店铺id
     */
    @Column(name = "SHOP_ID")
    @Index(name = "IDX_SALESORDER_SHOP_ID")
    public Long getShopId(){
        return shopId;
    }

    /**
     * 设置 店铺id.
     *
     * @param shopId
     *            the new 店铺id
     */
    public void setShopId(Long shopId){
        this.shopId = shopId;
    }

    /**
     * 获得 游客标识.
     *
     * @return the 游客标识
     */
    @Column(name = "GUEST_IDENTIFY",length = 200)
    public String getGuestIdentify(){
        return guestIdentify;
    }

    /**
     * 设置 游客标识.
     *
     * @param guestIdentify
     *            the new 游客标识
     */
    public void setGuestIdentify(String guestIdentify){
        this.guestIdentify = guestIdentify;
    }

    /**
     * 获得 商品总数量.
     *
     * @return the 商品总数量
     */
    @Column(name = "QUANTITY")
    public Integer getQuantity(){
        return quantity;
    }

    /**
     * 设置 商品总数量.
     *
     * @param quantity
     *            the new 商品总数量
     */
    public void setQuantity(Integer quantity){
        this.quantity = quantity;
    }

    /**
     * 获得 总价 不含运费的客户端显示最终金额 总价+整单折扣=sum（行商品销售价X数量）.
     *
     * @return the 总价 不含运费的客户端显示最终金额 总价+整单折扣=sum（行商品销售价X数量）
     */
    @Column(name = "TOTAL")
    public BigDecimal getTotal(){
        return total;
    }

    /**
     * 设置 总价 不含运费的客户端显示最终金额 总价+整单折扣=sum（行商品销售价X数量）.
     *
     * @param total
     *            the new 总价 不含运费的客户端显示最终金额 总价+整单折扣=sum（行商品销售价X数量）
     */
    public void setTotal(BigDecimal total){
        this.total = total;
    }

    /**
     * 获得 整单折扣 整单折扣-sum（行折扣）= 由于整单促销/商城积分形成的未分摊到行上的折扣总额.
     *
     * @return the 整单折扣 整单折扣-sum（行折扣）= 由于整单促销/商城积分形成的未分摊到行上的折扣总额
     */
    @Column(name = "DISCOUNT")
    public BigDecimal getDiscount(){
        return discount;
    }

    /**
     * 设置 整单折扣 整单折扣-sum（行折扣）= 由于整单促销/商城积分形成的未分摊到行上的折扣总额.
     *
     * @param discount
     *            the new 整单折扣 整单折扣-sum（行折扣）= 由于整单促销/商城积分形成的未分摊到行上的折扣总额
     */
    public void setDiscount(BigDecimal discount){
        this.discount = discount;
    }

    /**
     * 获得 物流状态.
     *
     * @return the 物流状态
     */
    @Column(name = "LOGISTICS_STATUS")
    @Index(name = "IDX_SALESORDER_LOGISTICS_STATUS")
    public Integer getLogisticsStatus(){
        return logisticsStatus;
    }

    /**
     * 设置 物流状态.
     *
     * @param logisticsStatus
     *            the new 物流状态
     */
    public void setLogisticsStatus(Integer logisticsStatus){
        this.logisticsStatus = logisticsStatus;
    }

    /**
     * 获得 财务状态.
     *
     * @return the 财务状态
     */
    @Column(name = "FINANCIAL_STATUS")
    @Index(name = "IDX_SALESORDER_FINANCIAL_STATUS")
    public Integer getFinancialStatus(){
        return financialStatus;
    }

    /**
     * 设置 财务状态.
     *
     * @param financialStatus
     *            the new 财务状态
     */
    public void setFinancialStatus(Integer financialStatus){
        this.financialStatus = financialStatus;
    }

    /**
     * 获得 支付方式.
     *
     * @return the 支付方式
     */
    @Column(name = "PAYMENT")
    @Index(name = "IDX_SALESORDER_PAYMENT")
    public Integer getPayment(){
        return payment;
    }

    /**
     * 设置 支付方式.
     *
     * @param payment
     *            the new 支付方式
     */
    public void setPayment(Integer payment){
        this.payment = payment;
    }

    /**
     * 获得 订单来源.
     *
     * @return the 订单来源
     */
    @Column(name = "SOURCE")
    public Integer getSource(){
        return source;
    }

    /**
     * 设置 订单来源.
     *
     * @param source
     *            the new 订单来源
     */
    public void setSource(Integer source){
        this.source = source;
    }

    /**
     * 获得 下单ip.
     *
     * @return the 下单ip
     */
    @Column(name = "IP",length = 50)
    public String getIp(){
        return ip;
    }

    /**
     * 设置 下单ip.
     *
     * @param ip
     *            the new 下单ip
     */
    public void setIp(String ip){
        this.ip = ip;
    }

    /**
     * 获得 应付运费.
     *
     * @return the 应付运费
     */
    @Column(name = "PAYABLE_FREIGHT")
    public BigDecimal getPayableFreight(){
        return payableFreight;
    }

    /**
     * 设置 应付运费.
     *
     * @param payableFreight
     *            the new 应付运费
     */
    public void setPayableFreight(BigDecimal payableFreight){
        this.payableFreight = payableFreight;
    }

    /**
     * 获得 实付运费.
     *
     * @return the 实付运费
     */
    @Column(name = "ACTUAL_FREIGHT")
    public BigDecimal getActualFreight(){
        return actualFreight;
    }

    /**
     * 设置 实付运费.
     *
     * @param actualFreight
     *            the new 实付运费
     */
    public void setActualFreight(BigDecimal actualFreight){
        this.actualFreight = actualFreight;
    }

    /**
     * 获得 发票号.
     *
     * @return the 发票号
     */
    @Column(name = "RECEIPT_CODE",length = 100)
    public String getReceiptCode(){
        return receiptCode;
    }

    /**
     * 设置 发票号.
     *
     * @param receiptCode
     *            the new 发票号
     */
    public void setReceiptCode(String receiptCode){
        this.receiptCode = receiptCode;
    }

    /**
     * 获得 发票类型.
     *
     * @return the 发票类型
     */
    @Column(name = "RECEIPT_TYPE")
    public Integer getReceiptType(){
        return receiptType;
    }

    /**
     * 设置 发票类型.
     *
     * @param receiptType
     *            the new 发票类型
     */
    public void setReceiptType(Integer receiptType){
        this.receiptType = receiptType;
    }

    /**
     * 获得 发票抬头.
     *
     * @return the 发票抬头
     */
    @Column(name = "RECEIPT_TITLE",length = 200)
    public String getReceiptTitle(){
        return receiptTitle;
    }

    /**
     * 设置 发票抬头.
     *
     * @param receiptTitle
     *            the new 发票抬头
     */
    public void setReceiptTitle(String receiptTitle){
        this.receiptTitle = receiptTitle;
    }

    /**
     * 获得 发票内容.
     *
     * @return the 发票内容
     */
    @Column(name = "RECEIPT_CONTENT",length = 500)
    public String getReceiptContent(){
        return receiptContent;
    }

    /**
     * 设置 发票内容.
     *
     * @param receiptContent
     *            the new 发票内容
     */
    public void setReceiptContent(String receiptContent){
        this.receiptContent = receiptContent;
    }
    
    /**
     * 获得 纳税人识别码
     * @return the 获取纳税人识别码
     * @since 5.3.2.18
     */
    @Column(name = "TAXPAYER_ID",length = 40,nullable = true,unique = false)
    public String getTaxPayerId(){
        return taxPayerId;
    }

    /**
     * 设置 纳税人识别码
     * @param taxPayerId
     *              the new 纳税人识别码
     * @since 5.3.2.18
     */
    public void setTaxPayerId(String taxPayerId){
        this.taxPayerId = taxPayerId;
    }

    /**
     * 获得 备注.
     *
     * @return the 备注
     */
    @Column(name = "REMARK",length = 500)
    public String getRemark(){
        return remark;
    }

    /**
     * 设置 备注.
     *
     * @param remark
     *            the new 备注
     */
    public void setRemark(String remark){
        this.remark = remark;
    }

    /**
     * 获得 创建时间.
     *
     * @return the 创建时间
     */
    @Column(name = "CREATE_TIME")
    public Date getCreateTime(){
        return createTime;
    }

    /**
     * 设置 创建时间.
     *
     * @param createTime
     *            the new 创建时间
     */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    /**
     * 获得 修改时间.
     *
     * @return the 修改时间
     */
    @Column(name = "MODIFY_TIME")
    public Date getModifyTime(){
        return modifyTime;
    }

    /**
     * 设置 修改时间.
     *
     * @param modifyTime
     *            the new 修改时间
     */
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }

    /**
     * 获得 会员名称.
     *
     * @return the 会员名称
     */
    @Column(name = "MEMBER_NAME",length = 100)
    public String getMemberName(){
        return memberName;
    }

    /**
     * 设置 会员名称.
     *
     * @param memberName
     *            the new 会员名称
     */
    public void setMemberName(String memberName){
        this.memberName = memberName;
    }

    /**
     * 获得 version.
     *
     * @return the version
     */
    @Version
    @Column(name = "VERSION")
    public Date getVersion(){
        return version;
    }

    /**
     * 设置 version.
     *
     * @param version
     *            the new version
     */
    public void setVersion(Date version){
        this.version = version;
    }

    /**
     * 获得 物流单号 快递单号：当出库时会提供.
     *
     * @return the 物流单号 快递单号：当出库时会提供
     */
    @Column(name = "TRANS_CODE")
    public String getTransCode(){
        return transCode;
    }

    /**
     * 设置 物流单号 快递单号：当出库时会提供.
     *
     * @param transCode
     *            the new 物流单号 快递单号：当出库时会提供
     */
    public void setTransCode(String transCode){
        this.transCode = transCode;
    }

    /**
     * 获得 物流商编码.
     *
     * @return the 物流商编码
     */
    @Column(name = "LOGISTICS_PROVIDER_CODE")
    public String getLogisticsProviderCode(){
        return logisticsProviderCode;
    }

    /**
     * 设置 物流商编码.
     *
     * @param logisticsProviderCode
     *            the new 物流商编码
     */
    public void setLogisticsProviderCode(String logisticsProviderCode){
        this.logisticsProviderCode = logisticsProviderCode;
    }

    /**
     * 获得 物流商名称.
     *
     * @return the 物流商名称
     */
    @Column(name = "LOGISTICS_PROVIDER_NAME")
    public String getLogisticsProviderName(){
        return logisticsProviderName;
    }

    /**
     * 设置 物流商名称.
     *
     * @param logisticsProviderName
     *            the new 物流商名称
     */
    public void setLogisticsProviderName(String logisticsProviderName){
        this.logisticsProviderName = logisticsProviderName;
    }

    /**
     * 获得 是否是QS订单，默认false.
     *
     * @return the 是否是QS订单，默认false
     */
    @Column(name = "IS_QS")
    @Index(name = "IDX_SALESORDER_IS_QS")
    public Boolean getIsQS(){
        return isQS;
    }

    /**
     * 设置 是否是QS订单，默认false.
     *
     * @param isQS
     *            the new 是否是QS订单，默认false
     */
    public void setIsQS(Boolean isQS){
        this.isQS = isQS;
    }

    /**
     * 获得 订单类型 1-普通订单(默认), 2-预售订单.
     *
     * @return the 订单类型 1-普通订单(默认), 2-预售订单
     */
    @Column(name = "ORDER_TYPE")
    public Integer getOrderType(){
        return orderType;
    }

    /**
     * 设置 订单类型 1-普通订单(默认), 2-预售订单.
     *
     * @param orderType
     *            the new 订单类型 1-普通订单(默认), 2-预售订单
     */
    public void setOrderType(Integer orderType){
        this.orderType = orderType;
    }

    /**
     * 获得 预计发货时间.
     *
     * @return the 预计发货时间
     */
    @Column(name = "APPOINT_SHIP_DATE")
    public Date getAppointShipDate(){
        return appointShipDate;
    }

    /**
     * 设置 预计发货时间.
     *
     * @param appointShipDate
     *            the new 预计发货时间
     */
    public void setAppointShipDate(Date appointShipDate){
        this.appointShipDate = appointShipDate;
    }

    /**
     * 获得 支付类型 1-全额付款（默认）, 2-分阶段付款.
     *
     * @return the 支付类型 1-全额付款（默认）, 2-分阶段付款
     */
    @Column(name = "PAY_TYPE")
    public Integer getPayType(){
        return payType;
    }

    /**
     * 设置 支付类型 1-全额付款（默认）, 2-分阶段付款.
     *
     * @param payType
     *            the new 支付类型 1-全额付款（默认）, 2-分阶段付款
     */
    public void setPayType(Integer payType){
        this.payType = payType;
    }

    /**
     * 获得 cOD类型.
     *
     * @return the cOD类型
     */
    @Column(name = "COD_PAYMENT_TYPE")
    public Integer getCodPaymentType(){
        return codPaymentType;
    }

    /**
     * 设置 cOD类型.
     *
     * @param codPaymentType
     *            the new cOD类型
     */
    public void setCodPaymentType(Integer codPaymentType){
        this.codPaymentType = codPaymentType;
    }

    /**
     * 获得 下单时的语言 由于用于wormhole发邮件时邮件模板的多语言，以及地址信息的多语言.
     *
     * @return the 下单时的语言 由于用于wormhole发邮件时邮件模板的多语言，以及地址信息的多语言
     */
    @Column(name = "LANG")
    public String getLang(){
        return lang;
    }

    /**
     * 设置 下单时的语言 由于用于wormhole发邮件时邮件模板的多语言，以及地址信息的多语言.
     *
     * @param lang
     *            the new 下单时的语言 由于用于wormhole发邮件时邮件模板的多语言，以及地址信息的多语言
     */
    public void setLang(String lang){
        this.lang = lang;
    }

    @Column(name = "RECEIPT_CONSIGNEE")
    public String getReceiptConsignee(){
        return receiptConsignee;
    }

    public void setReceiptConsignee(String receiptConsignee){
        this.receiptConsignee = receiptConsignee;
    }

    @Column(name = "RECEIPT_TELPHONE")
    public String getReceiptTelphone(){
        return receiptTelphone;
    }

    public void setReceiptTelphone(String receiptTelphone){
        this.receiptTelphone = receiptTelphone;
    }

    @Column(name = "RECEIPT_ADDRESS")
    public String getReceiptAddress(){
        return receiptAddress;
    }

    public void setReceiptAddress(String receiptAddress){
        this.receiptAddress = receiptAddress;
    }

}
