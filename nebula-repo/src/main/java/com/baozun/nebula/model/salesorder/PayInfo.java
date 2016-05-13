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

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 支付详细.
 *
 * @author chuanyang.zheng
 * @creattime 2013-11-20
 */
@Entity
@Table(name = "t_so_payinfo")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PayInfo extends BaseModel{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3350505308978769968L;

    /** The coupon pay. */
    public static Integer     COUPON_PAY       = 101;

    /** PK. */
    private Long              id;

    /** 有效支付（开始）实践. */
    private Date              payStartTime;

    /** 有效支付（结束）实践. */
    private Date              payEndTime;

    /** 订单id. */
    private Long              orderId;

    //XXX feilong 好奇怪的两个参数 PayMoney PayNumerical 好像一样的, 有区别吗

    /** 支付数值. */
    private BigDecimal        payNumerical;

    /** 支付金额. */
    private BigDecimal        payMoney;

    /** 支付类型. */
    private Integer           payType;

    /** 付款详情. */
    private String            payInfo;

    /** 第三方支付流水. */
    private String            thirdPayNo;

    /** 第三方支付帐号. */
    private String            thirdPayAccount;

    /** 修改时间. */
    private Date              modifyTime;

    /** version. */
    private Date              version;

    /** 是否支付成功状态 *. */
    private Boolean           paySuccessStatus;

    /** The create time. */
    private Date              createTime;

    /** The pic url. */
    private String            picUrl;

    /** The bank code. */
    private String            bankCode;

    /** 代表分期支付的期数 *. */
    private Integer           periods;

    /** 代表分期金额 *. */
    private BigDecimal        amount;

    /** 代表分期手续费 *. */
    private BigDecimal        poundage;

    /**
     * 获得 pK.
     *
     * @return the pK
     */
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_T_SAL_PAYINFO",sequenceName = "S_T_SO_PAYINFO",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SAL_PAYINFO")
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
     * 获得 订单id.
     *
     * @return the 订单id
     */
    @Column(name = "ORDER_ID")
    public Long getOrderId(){
        return orderId;
    }

    /**
     * 设置 订单id.
     *
     * @param orderId
     *            the new 订单id
     */
    public void setOrderId(Long orderId){
        this.orderId = orderId;
    }

    /**
     * 获得 支付数值.
     *
     * @return the 支付数值
     */
    @Column(name = "PAY_NUMERICAL")
    public BigDecimal getPayNumerical(){
        return payNumerical;
    }

    /**
     * 设置 支付数值.
     *
     * @param payNumerical
     *            the new 支付数值
     */
    public void setPayNumerical(BigDecimal payNumerical){
        this.payNumerical = payNumerical;
    }

    /**
     * 获得 支付金额.
     *
     * @return the 支付金额
     */
    @Column(name = "PAY_MONEY")
    public BigDecimal getPayMoney(){
        return payMoney;
    }

    /**
     * 设置 支付金额.
     *
     * @param payMoney
     *            the new 支付金额
     */
    public void setPayMoney(BigDecimal payMoney){
        this.payMoney = payMoney;
    }

    /**
     * 获得 支付类型.
     *
     * @return the 支付类型
     */
    @Column(name = "PAY_TYPE")
    public Integer getPayType(){
        return payType;
    }

    /**
     * 设置 支付类型.
     *
     * @param payType
     *            the new 支付类型
     */
    public void setPayType(Integer payType){
        this.payType = payType;
    }

    /**
     * 获得 付款详情.
     *
     * @return the 付款详情
     */
    @Column(name = "PAY_INFO")
    public String getPayInfo(){
        return payInfo;
    }

    /**
     * 设置 付款详情.
     *
     * @param payInfo
     *            the new 付款详情
     */
    public void setPayInfo(String payInfo){
        this.payInfo = payInfo;
    }

    /**
     * 获得 第三方支付流水.
     *
     * @return the 第三方支付流水
     */
    @Column(name = "THIRD_PAY_NO")
    public String getThirdPayNo(){
        return thirdPayNo;
    }

    /**
     * 设置 第三方支付流水.
     *
     * @param thirdPayNo
     *            the new 第三方支付流水
     */
    public void setThirdPayNo(String thirdPayNo){
        this.thirdPayNo = thirdPayNo;
    }

    /**
     * 获得 第三方支付帐号.
     *
     * @return the 第三方支付帐号
     */
    @Column(name = "THIRD_PAY_ACCOUNT")
    public String getThirdPayAccount(){
        return thirdPayAccount;
    }

    /**
     * 设置 第三方支付帐号.
     *
     * @param thirdPayAccount
     *            the new 第三方支付帐号
     */
    public void setThirdPayAccount(String thirdPayAccount){
        this.thirdPayAccount = thirdPayAccount;
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
     * 获得 version.
     *
     * @return the version
     */
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
     * 获得 是否支付成功状态 *.
     *
     * @return the 是否支付成功状态 *
     */
    @Column(name = "PAY_SUCCESS_STATUS")
    public Boolean getPaySuccessStatus(){
        return paySuccessStatus;
    }

    /**
     * 设置 是否支付成功状态 *.
     *
     * @param paySuccessStatus
     *            the new 是否支付成功状态 *
     */
    public void setPaySuccessStatus(Boolean paySuccessStatus){
        this.paySuccessStatus = paySuccessStatus;
    }

    /**
     * 获得 create time.
     *
     * @return the 创建 time
     */
    @Column(name = "CREATE_TIME")
    public Date getCreateTime(){
        return createTime;
    }

    /**
     * 设置 create time.
     *
     * @param createTime
     *            the 创建 time
     */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    /**
     * 获得 pic url.
     *
     * @return the pic url
     */
    public String getPicUrl(){
        return picUrl;
    }

    /**
     * 设置 pic url.
     *
     * @param picUrl
     *            the pic url
     */
    public void setPicUrl(String picUrl){
        this.picUrl = picUrl;
    }

    /**
     * 获得 bank code.
     *
     * @return the bank code
     */
    public String getBankCode(){
        return bankCode;
    }

    /**
     * 设置 bank code.
     *
     * @param bankCode
     *            the bank code
     */
    public void setBankCode(String bankCode){
        this.bankCode = bankCode;
    }

    /**
     * 获得 代表分期支付的期数 *.
     *
     * @return the 代表分期支付的期数 *
     */
    @Column(name = "PERIODS")
    public Integer getPeriods(){
        return periods;
    }

    /**
     * 设置 代表分期支付的期数 *.
     *
     * @param periods
     *            the new 代表分期支付的期数 *
     */
    public void setPeriods(Integer periods){
        this.periods = periods;
    }

    /**
     * 获得 有效支付（开始）实践.
     *
     * @return the 有效支付（开始）实践
     */
    @Column(name = "PAY_START_TIME")
    public Date getPayStartTime(){
        return payStartTime;
    }

    /**
     * 设置 有效支付（开始）实践.
     *
     * @param payStartTime
     *            the new 有效支付（开始）实践
     */
    public void setPayStartTime(Date payStartTime){
        this.payStartTime = payStartTime;
    }

    /**
     * 获得 有效支付（结束）实践.
     *
     * @return the 有效支付（结束）实践
     */
    @Column(name = "PAY_END_TIME")
    public Date getPayEndTime(){
        return payEndTime;
    }

    /**
     * 设置 有效支付（结束）实践.
     *
     * @param payEndTime
     *            the new 有效支付（结束）实践
     */
    public void setPayEndTime(Date payEndTime){
        this.payEndTime = payEndTime;
    }

    /**
     * @return amount
     * @date 2016年2月18日 下午3:10:36
     */
    @Column(name = "AMOUNT")
    public BigDecimal getAmount(){
        return amount;
    }

    /**
     * @param amount
     *            要设置的 amount
     * @date 2016年2月18日 下午3:10:36
     */
    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }

    /**
     * @return poundage
     * @date 2016年2月18日 下午3:10:36
     */
    @Column(name = "POUNDAGE")
    public BigDecimal getPoundage(){
        return poundage;
    }

    /**
     * @param poundage
     *            要设置的 poundage
     * @date 2016年2月18日 下午3:10:36
     */
    public void setPoundage(BigDecimal poundage){
        this.poundage = poundage;
    }
}
