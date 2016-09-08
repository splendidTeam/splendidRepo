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
package com.baozun.nebula.sdk.command;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baozun.nebula.command.OnLinePaymentCancelCommand;
import com.baozun.nebula.command.OnLinePaymentCommand;
import com.baozun.nebula.model.BaseModel;
import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.utilities.common.command.WechatPayParamCommand;

/**
 * The Class SalesOrderCommand.
 * 
 * @see com.baozun.nebula.model.salesorder.SalesOrder
 */
//XXX feilong 目前这个类 把创建 和查询的command 都融合在一起, 不利于扩展
public class SalesOrderCommand extends BaseModel{

    /** The Constant serialVersionUID. */
    private static final long           serialVersionUID = 4539781027280506601L;

    /** PK. */
    private Long                        id;

    /** 订单类型 1-普通订单(默认), 2-预售订单. */
    private Integer                     orderType;

    /** 预计发货时间. */
    private Date                        appointShipDate;

    /** 支付类型 1-全额付款（默认）, 2-分阶段付款. */
    private Integer                     payType;

    /** 订单号. */
    private String                      code;

    /** OMS订单号. */
    private String                      omsCode;

    /** 会员id. */
    private Long                        memberId;

    /** 会员名称. */
    private String                      memberName;

    /** 订单行. */
    private List<OrderLineCommand>      orderLines;

    /** 商品信息描述. */
    private String                      describe;

    //********************************* 收货信息平铺. ********************************
    /** 姓名. */
    private String                      name;

    /** 购买人姓名. */
    private String                      buyerName;

    /** 购买人电话. */
    private String                      buyerTel;

    /** 国. */
    private String                      country;

    /** 省. */
    private String                      province;

    /** 市. */
    private String                      city;

    /** 区. */
    private String                      area;

    /** 镇. */
    private String                      town;

    /** 国. */
    private Long                        countryId;

    /** 省. */
    private Long                        provinceId;

    /** 市. */
    private Long                        cityId;

    /** 区. */
    private Long                        areaId;

    /** 镇id. */
    private Long                        townId;

    /** 地址. */
    private String                      address;

    /** 手机. */
    private String                      mobile;

    /** 固话. */
    private String                      tel;

    /** email. */
    private String                      email;

    /** 邮编. */
    private String                      postcode;
    
    //***********************************************************

    /** 指定时间段. */
    private String                      appointTimeQuantum;

    /** 指定日期. */
    private String                      appointTime;

    /** 指定类型. */
    private String                      appointType;
    
    //***********************************************************

    /** 收货信息修改时间. */
    private Date                        consigneeModifyTime;

    /** 订单促销信息. */
    private List<OrderPromotionCommand> orderPromotions;

    /** 该订单所使用优惠券 *. */
    private List<CouponCodeCommand>     couponCodes;

    /** 支付信息. */
    private List<PayInfoCommand>        payInfo;

    /** 游客标识. */
    private String                      guestIdentify;

    /** 商品总数量. */
    private Integer                     quantity;

    /** 总价. */
    private BigDecimal                  total;

    /** 折扣. */
    private BigDecimal                  discount;

    /** 物流状态. */
    private Integer                     logisticsStatus;

    /** 财务状态. */
    private Integer                     financialStatus;

    /** 支付方式. */
    private Integer                     payment;

    /** 支付方式字符串. */
    private String                      paymentStr;

    /** 订单来源. */
    private Integer                     source;

    /** 下单ip. */
    private String                      ip;

    /** 应付运费. */
    private BigDecimal                  payableFreight;

    /** 实付运费. */
    private BigDecimal                  actualFreight;

    /** 物流单号 快递单号：当出库时会提供. */
    private String                      transCode;

    /** 物流商编码. */
    private String                      logisticsProviderCode;

    /** 物流商名称. */
    private String                      logisticsProviderName;

    /** 发票号. */
    private String                      receiptCode;

    /** 发票类型. */
    private Integer                     receiptType;

    /** 发票抬头. */
    private String                      receiptTitle;

    /** 发票收货人. */
    private String                      receiptConsignee;

    /** 发票收货人联系方式. */
    private String                      receiptTelphone;

    /** 发票收货地址. */
    private String                      receiptAddress;

    /** 发票内容. */
    private String                      receiptContent;

    /** 备注. */
    private String                      remark;

    /** 修改时间. */
    private Date                        modifyTime;

    /** 创建时间. */
    private Date                        createTime;

    /** 在线支付信息 *. */
    private OnLinePaymentCommand        onLinePaymentCommand;

    /** 在线支付取消信息 *. */
    private OnLinePaymentCancelCommand  onLinePaymentCancelCommand;

    /** 微信支付相关 *. */
    private WechatPayParamCommand       wechatPayParamCommand;

    /**
     * 计算运费需要的信息.
     */
    private CalcFreightCommand          calcFreightCommand;

    /** ******************** 保存时 拆单需要保存每个店铺的快递和指定收货时间 *. */
    //XXX feilong 批注 其实设置成map 会更合理  2016-05-13
    /** 快递公司 String格式：shopid||logisticsProvidercode||logisticsProviderName */
    private List<String>                logisticsProvider;

    /** 指定时间段 String格式：shopId||指定时间段. */
    private List<String>                appointTimeQuantums;

    /** 指定日期 String格式：shopId||指定日期. */
    private List<String>                appointTimes;

    /** 指定类型 String格式：shopId||指定类型. */
    private List<String>                appointTypes;

    /** 给卖家留言 String格式：shopId||留言. */
    private List<String>                remarks;

    /** 支付方式 String格式：shopId||payMentType||金额 可能涉及预付卡或者积分方式支付. */
    private List<String>                soPayMentDetails;

    //*********************************************************************************************

    /**
     * 邮件模板.
     * 
     * @deprecated 没有用到
     */
    private String                      emailTemplete;

    /** 物流方式 *. */
    private Long                        distributionModeId;

    /** 银行分期付款的期数. */
    private Integer                     periods;

    //*********************************************************************************************

    /** 多语言. */
    private String                      lang;

    /**
     * 获得 物流方式 *.
     *
     * @return the 物流方式 *
     */
    public Long getDistributionModeId(){
        return distributionModeId;
    }

    /**
     * 设置 物流方式 *.
     *
     * @param distributionModeId
     *            the new 物流方式 *
     */
    public void setDistributionModeId(Long distributionModeId){
        this.distributionModeId = distributionModeId;
    }

    /**
     * 获得 支付方式 String格式：shopId||payMentType||金额 可能涉及预付卡或者积分方式支付.
     *
     * @return the 支付方式 String格式：shopId||payMentType||金额 可能涉及预付卡或者积分方式支付
     */
    public List<String> getSoPayMentDetails(){
        return soPayMentDetails;
    }

    /**
     * 设置 支付方式 String格式：shopId||payMentType||金额 可能涉及预付卡或者积分方式支付.
     *
     * @param soPayMentDetails
     *            the new 支付方式 String格式：shopId||payMentType||金额 可能涉及预付卡或者积分方式支付
     */
    public void setSoPayMentDetails(List<String> soPayMentDetails){
        this.soPayMentDetails = soPayMentDetails;
    }

    /**
     * 获得 物流单号 快递单号：当出库时会提供.
     *
     * @return the 物流单号 快递单号：当出库时会提供
     */
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
     * 获得 邮件模板 *.
     *
     * @return the 邮件模板 *
     * @deprecated 没有用到
     */
    public String getEmailTemplete(){
        return emailTemplete;
    }

    /**
     * 设置 邮件模板 *.
     *
     * @param emailTemplete
     *            the new 邮件模板 *
     * @deprecated 没有用到
     */
    public void setEmailTemplete(String emailTemplete){
        this.emailTemplete = emailTemplete;
    }

    /**
     * 获得 支付方式字符串.
     *
     * @return the 支付方式字符串
     */
    public String getPaymentStr(){
        return paymentStr;
    }

    /**
     * 设置 支付方式字符串.
     *
     * @param paymentStr
     *            the new 支付方式字符串
     */
    public void setPaymentStr(String paymentStr){
        this.paymentStr = paymentStr;
    }

    /**
     * 获得 计算运费需要的信息 *.
     *
     * @return the 计算运费需要的信息 *
     */
    public CalcFreightCommand getCalcFreightCommand(){
        return calcFreightCommand;
    }

    /**
     * 设置 计算运费需要的信息 *.
     *
     * @param calcFreightCommand
     *            the new 计算运费需要的信息 *
     */
    public void setCalcFreightCommand(CalcFreightCommand calcFreightCommand){
        this.calcFreightCommand = calcFreightCommand;
    }

    /**
     * 获得 给卖家留言 String格式：shopId||留言.
     *
     * @return the 给卖家留言 String格式：shopId||留言
     */
    public List<String> getRemarks(){
        return remarks;
    }

    /**
     * 设置 给卖家留言 String格式：shopId||留言.
     *
     * @param remarks
     *            the new 给卖家留言 String格式：shopId||留言
     */
    public void setRemarks(List<String> remarks){
        this.remarks = remarks;
    }

    /**
     * 获得 ******************** 保存时 拆单需要保存每个店铺的快递和指定收货时间 *.
     *
     * @return the ******************** 保存时 拆单需要保存每个店铺的快递和指定收货时间 *
     */
    public List<String> getLogisticsProvider(){
        return logisticsProvider;
    }

    /**
     * 设置 ******************** 保存时 拆单需要保存每个店铺的快递和指定收货时间 *.
     *
     * @param logisticsProvider
     *            the new ******************** 保存时 拆单需要保存每个店铺的快递和指定收货时间 *
     */
    public void setLogisticsProvider(List<String> logisticsProvider){
        this.logisticsProvider = logisticsProvider;
    }

    /**
     * 获得 指定时间段 String格式：shopId||指定时间段.
     *
     * @return the 指定时间段 String格式：shopId||指定时间段
     */
    public List<String> getAppointTimeQuantums(){
        return appointTimeQuantums;
    }

    /**
     * 设置 指定时间段 String格式：shopId||指定时间段.
     *
     * @param appointTimeQuantums
     *            the new 指定时间段 String格式：shopId||指定时间段
     */
    public void setAppointTimeQuantums(List<String> appointTimeQuantums){
        this.appointTimeQuantums = appointTimeQuantums;
    }

    /**
     * 获得 指定日期 String格式：shopId||指定日期.
     *
     * @return the 指定日期 String格式：shopId||指定日期
     */
    public List<String> getAppointTimes(){
        return appointTimes;
    }

    /**
     * 设置 指定日期 String格式：shopId||指定日期.
     *
     * @param appointTimes
     *            the new 指定日期 String格式：shopId||指定日期
     */
    public void setAppointTimes(List<String> appointTimes){
        this.appointTimes = appointTimes;
    }

    /**
     * 获得 指定类型 String格式：shopId||指定类型.
     *
     * @return the 指定类型 String格式：shopId||指定类型
     */
    public List<String> getAppointTypes(){
        return appointTypes;
    }

    /**
     * 设置 指定类型 String格式：shopId||指定类型.
     *
     * @param appointTypes
     *            the new 指定类型 String格式：shopId||指定类型
     */
    public void setAppointTypes(List<String> appointTypes){
        this.appointTypes = appointTypes;
    }

    /**
     * 获得 该订单所使用优惠券 *.
     *
     * @return the 该订单所使用优惠券 *
     */
    public List<CouponCodeCommand> getCouponCodes(){
        return couponCodes;
    }

    /**
     * 设置 该订单所使用优惠券 *.
     *
     * @param couponCodes
     *            the new 该订单所使用优惠券 *
     */
    public void setCouponCodes(List<CouponCodeCommand> couponCodes){
        this.couponCodes = couponCodes;
    }

    /**
     * 获得 商品信息描述.
     *
     * @return the 商品信息描述
     */
    public String getDescribe(){
        return describe;
    }

    /**
     * 设置 商品信息描述.
     *
     * @param describe
     *            the new 商品信息描述
     */
    public void setDescribe(String describe){
        this.describe = describe;
    }

    /**
     * 获得 pK.
     *
     * @return the pK
     */
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
     * 获得 订单行.
     *
     * @return the 订单行
     */
    public List<OrderLineCommand> getOrderLines(){
        return orderLines;
    }

    /**
     * 设置 订单行.
     *
     * @param orderLines
     *            the new 订单行
     */
    public void setOrderLines(List<OrderLineCommand> orderLines){
        this.orderLines = orderLines;
    }

    /**
     * 获得 游客标识.
     *
     * @return the 游客标识
     */
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
     * 获得 总价.
     *
     * @return the 总价
     */
    public BigDecimal getTotal(){
        return total;
    }

    /**
     * 设置 总价.
     *
     * @param total
     *            the new 总价
     */
    public void setTotal(BigDecimal total){
        this.total = total;
    }

    /**
     * 获得 折扣.
     *
     * @return the 折扣
     */
    public BigDecimal getDiscount(){
        return discount;
    }

    /**
     * 设置 折扣.
     *
     * @param discount
     *            the new 折扣
     */
    public void setDiscount(BigDecimal discount){
        this.discount = discount;
    }

    /**
     * 获得 物流状态.
     *
     * @return the 物流状态
     */
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
     * 获得 备注.
     *
     * @return the 备注
     */
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
     * 获得 支付信息.
     *
     * @return the 支付信息
     */
    public List<PayInfoCommand> getPayInfo(){
        return payInfo;
    }

    /**
     * 设置 支付信息.
     *
     * @param payInfo
     *            the new 支付信息
     */
    public void setPayInfo(List<PayInfoCommand> payInfo){
        this.payInfo = payInfo;
    }

    /**
     * 获得 姓名.
     *
     * @return the 姓名
     */
    public String getName(){
        return name;
    }

    /**
     * 设置 姓名.
     *
     * @param name
     *            the new 姓名
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * 获得 国.
     *
     * @return the 国
     */
    public String getCountry(){
        return country;
    }

    /**
     * 设置 国.
     *
     * @param country
     *            the new 国
     */
    public void setCountry(String country){
        this.country = country;
    }

    /**
     * 获得 省.
     *
     * @return the 省
     */
    public String getProvince(){
        return province;
    }

    /**
     * 设置 省.
     *
     * @param province
     *            the new 省
     */
    public void setProvince(String province){
        this.province = province;
    }

    /**
     * 获得 市.
     *
     * @return the 市
     */
    public String getCity(){
        return city;
    }

    /**
     * 设置 市.
     *
     * @param city
     *            the new 市
     */
    public void setCity(String city){
        this.city = city;
    }

    /**
     * 获得 区.
     *
     * @return the 区
     */
    public String getArea(){
        return area;
    }

    /**
     * 设置 区.
     *
     * @param area
     *            the new 区
     */
    public void setArea(String area){
        this.area = area;
    }

    /**
     * 获得 镇.
     *
     * @return the 镇
     */
    public String getTown(){
        return town;
    }

    /**
     * 设置 镇.
     *
     * @param town
     *            the new 镇
     */
    public void setTown(String town){
        this.town = town;
    }

    /**
     * 获得 地址.
     *
     * @return the 地址
     */
    public String getAddress(){
        return address;
    }

    /**
     * 设置 地址.
     *
     * @param address
     *            the new 地址
     */
    public void setAddress(String address){
        this.address = address;
    }

    /**
     * 获得 手机.
     *
     * @return the 手机
     */
    public String getMobile(){
        return mobile;
    }

    /**
     * 设置 手机.
     *
     * @param mobile
     *            the new 手机
     */
    public void setMobile(String mobile){
        this.mobile = mobile;
    }

    /**
     * 获得 固话.
     *
     * @return the 固话
     */
    public String getTel(){
        return tel;
    }

    /**
     * 设置 固话.
     *
     * @param tel
     *            the new 固话
     */
    public void setTel(String tel){
        this.tel = tel;
    }

    /**
     * 获得 email.
     *
     * @return the email
     */
    public String getEmail(){
        return email;
    }

    /**
     * 设置 email.
     *
     * @param email
     *            the new email
     */
    public void setEmail(String email){
        this.email = email;
    }

    /**
     * 获得 邮编.
     *
     * @return the 邮编
     */
    public String getPostcode(){
        return postcode;
    }

    /**
     * 设置 邮编.
     *
     * @param postcode
     *            the new 邮编
     */
    public void setPostcode(String postcode){
        this.postcode = postcode;
    }

    /**
     * 获得 指定时间段.
     *
     * @return the 指定时间段
     */
    public String getAppointTimeQuantum(){
        return appointTimeQuantum;
    }

    /**
     * 设置 指定时间段.
     *
     * @param appointTimeQuantum
     *            the new 指定时间段
     */
    public void setAppointTimeQuantum(String appointTimeQuantum){
        this.appointTimeQuantum = appointTimeQuantum;
    }

    /**
     * 获得 指定日期.
     *
     * @return the 指定日期
     */
    public String getAppointTime(){
        return appointTime;
    }

    /**
     * 设置 指定日期.
     *
     * @param appointTime
     *            the new 指定日期
     */
    public void setAppointTime(String appointTime){
        this.appointTime = appointTime;
    }

    /**
     * 获得 指定类型.
     *
     * @return the 指定类型
     */
    public String getAppointType(){
        return appointType;
    }

    /**
     * 设置 指定类型.
     *
     * @param appointType
     *            the new 指定类型
     */
    public void setAppointType(String appointType){
        this.appointType = appointType;
    }

    /**
     * 获得 收货信息修改时间.
     *
     * @return the 收货信息修改时间
     */
    public Date getConsigneeModifyTime(){
        return consigneeModifyTime;
    }

    /**
     * 设置 收货信息修改时间.
     *
     * @param consigneeModifyTime
     *            the new 收货信息修改时间
     */
    public void setConsigneeModifyTime(Date consigneeModifyTime){
        this.consigneeModifyTime = consigneeModifyTime;
    }

    /**
     * 获得 订单促销信息.
     *
     * @return the 订单促销信息
     */
    public List<OrderPromotionCommand> getOrderPromotions(){
        return orderPromotions;
    }

    /**
     * 设置 订单促销信息.
     *
     * @param orderPromotions
     *            the new 订单促销信息
     */
    public void setOrderPromotions(List<OrderPromotionCommand> orderPromotions){
        this.orderPromotions = orderPromotions;
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
     * 获得 会员名称.
     *
     * @return the 会员名称
     */
    public String getMemberName(){
        return memberName;
    }

    /**
     * 获得 在线支付信息 *.
     *
     * @return the 在线支付信息 *
     */
    public OnLinePaymentCommand getOnLinePaymentCommand(){
        return onLinePaymentCommand;
    }

    /**
     * 设置 在线支付信息 *.
     *
     * @param onLinePaymentCommand
     *            the new 在线支付信息 *
     */
    public void setOnLinePaymentCommand(OnLinePaymentCommand onLinePaymentCommand){
        this.onLinePaymentCommand = onLinePaymentCommand;
    }

    /**
     * 获得 在线支付取消信息 *.
     *
     * @return the 在线支付取消信息 *
     */
    public OnLinePaymentCancelCommand getOnLinePaymentCancelCommand(){
        return onLinePaymentCancelCommand;
    }

    /**
     * 设置 在线支付取消信息 *.
     *
     * @param onLinePaymentCancelCommand
     *            the new 在线支付取消信息 *
     */
    public void setOnLinePaymentCancelCommand(OnLinePaymentCancelCommand onLinePaymentCancelCommand){
        this.onLinePaymentCancelCommand = onLinePaymentCancelCommand;
    }

    /**
     * 获得 微信支付相关 *.
     *
     * @return the 微信支付相关 *
     */
    public WechatPayParamCommand getWechatPayParamCommand(){
        return wechatPayParamCommand;
    }

    /**
     * 设置 微信支付相关 *.
     *
     * @param wechatPayParamCommand
     *            the new 微信支付相关 *
     */
    public void setWechatPayParamCommand(WechatPayParamCommand wechatPayParamCommand){
        this.wechatPayParamCommand = wechatPayParamCommand;
    }

    /**
     * 获得 国.
     *
     * @return the 国
     */
    public Long getCountryId(){
        return countryId;
    }

    /**
     * 设置 国.
     *
     * @param countryId
     *            the new 国
     */
    public void setCountryId(Long countryId){
        this.countryId = countryId;
    }

    /**
     * 获得 省.
     *
     * @return the 省
     */
    public Long getProvinceId(){
        return provinceId;
    }

    /**
     * 设置 省.
     *
     * @param provinceId
     *            the new 省
     */
    public void setProvinceId(Long provinceId){
        this.provinceId = provinceId;
    }

    /**
     * 获得 市.
     *
     * @return the 市
     */
    public Long getCityId(){
        return cityId;
    }

    /**
     * 设置 市.
     *
     * @param cityId
     *            the new 市
     */
    public void setCityId(Long cityId){
        this.cityId = cityId;
    }

    /**
     * 获得 区.
     *
     * @return the 区
     */
    public Long getAreaId(){
        return areaId;
    }

    /**
     * 设置 区.
     *
     * @param areaId
     *            the new 区
     */
    public void setAreaId(Long areaId){
        this.areaId = areaId;
    }

    /**
     * 获得 镇id.
     *
     * @return the 镇id
     */
    public Long getTownId(){
        return townId;
    }

    /**
     * 设置 镇id.
     *
     * @param townId
     *            the new 镇id
     */
    public void setTownId(Long townId){
        this.townId = townId;
    }

    /**
     * 获得 订单类型 1-普通订单(默认), 2-预售订单.
     *
     * @return the 订单类型 1-普通订单(默认), 2-预售订单
     */
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
     * 获得 银行分期付款的期数.
     *
     * @return the 银行分期付款的期数
     */
    public Integer getPeriods(){
        return periods;
    }

    /**
     * 设置 银行分期付款的期数.
     *
     * @param periods
     *            the new 银行分期付款的期数
     */
    public void setPeriods(Integer periods){
        this.periods = periods;
    }

    /**
     * 获得 购买人姓名.
     *
     * @return the 购买人姓名
     */
    public String getBuyerName(){
        return buyerName;
    }

    /**
     * 设置 购买人姓名.
     *
     * @param buyerName
     *            the new 购买人姓名
     */
    public void setBuyerName(String buyerName){
        this.buyerName = buyerName;
    }

    /**
     * 获得 购买人电话.
     *
     * @return the 购买人电话
     */
    public String getBuyerTel(){
        return buyerTel;
    }

    /**
     * 设置 购买人电话.
     *
     * @param buyerTel
     *            the new 购买人电话
     */
    public void setBuyerTel(String buyerTel){
        this.buyerTel = buyerTel;
    }

    /**
     * 获得 发票收货人.
     *
     * @return the 发票收货人
     */
    public String getReceiptConsignee(){
        return receiptConsignee;
    }

    /**
     * 设置 发票收货人.
     *
     * @param receiptConsignee
     *            the new 发票收货人
     */
    public void setReceiptConsignee(String receiptConsignee){
        this.receiptConsignee = receiptConsignee;
    }

    /**
     * 获得 发票收货人联系方式.
     *
     * @return the 发票收货人联系方式
     */
    public String getReceiptTelphone(){
        return receiptTelphone;
    }

    /**
     * 设置 发票收货人联系方式.
     *
     * @param receiptTelphone
     *            the new 发票收货人联系方式
     */
    public void setReceiptTelphone(String receiptTelphone){
        this.receiptTelphone = receiptTelphone;
    }

    /**
     * 获得 发票收货地址.
     *
     * @return the 发票收货地址
     */
    public String getReceiptAddress(){
        return receiptAddress;
    }

    /**
     * 设置 发票收货地址.
     *
     * @param receiptAddress
     *            the new 发票收货地址
     */
    public void setReceiptAddress(String receiptAddress){
        this.receiptAddress = receiptAddress;
    }

    /**
     * 获得 多语言.
     *
     * @return the 多语言
     */
    public String getLang(){
        return lang;
    }

    /**
     * 设置 多语言.
     *
     * @param lang
     *            the new 多语言
     */
    public void setLang(String lang){
        this.lang = lang;
    }

}
