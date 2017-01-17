package com.baozun.nebula.wormhole.mq.entity.pay;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 订单支付信息
 * 当订单有付款发生时，推送消息给到SCM
 * 每当有付款完成时，发送相应消息。如果在订单创建时对应支付方式已经完成，需在此时发送对应的付款消息，如外部积分支付和预付卡支付
 * 
 * @author Justin Hu
 *
 */

public class PaymentInfoV5 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -219443117348024676L;
	
	
	 /**
     * 商城订单编码
     */
    private String bsOrderCode;
    
    /**
     * 商城支付信息id
     */
    private String bsPayInfoId;
    
    /**
     * 支付方式:从SCM处获得列表
     */
    private String paymentType;
    
    /**
     * 支付相关银行：在线支付时的追加信息，可能为空
     */
    private String paymentBank;
    
    /**
     * 支付金额
     */
    private BigDecimal payTotal;
    
    
    
    /**
     * 支付流水
     * 可以让财务获取到对应支付记录的交易流水号，理论上就是官方商城的交易流水号，会送完支付网关产生交易
     */
    private String payNo;
    /**
     * 买家的支付账号( 第三方支付帐号)
     * 
     * @since 5.3.2.10
     */
    private String   paymentAccount;
    
    /**
     * 付款时间
     */
    private Date paymentTime;
    
   
    /**
     * 当整单都全部支付完成时，告知整单支付完成的一个标记，Y/N
     */
    private Boolean allComplete;
    
    /**
     * 备注
     */
    private String remark;


	public String getBsOrderCode() {
		return bsOrderCode;
	}


	public void setBsOrderCode(String bsOrderCode) {
		this.bsOrderCode = bsOrderCode;
	}


	public String getPaymentType() {
		return paymentType;
	}


	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}


	public String getPaymentBank() {
		return paymentBank;
	}


	public void setPaymentBank(String paymentBank) {
		this.paymentBank = paymentBank;
	}


	public BigDecimal getPayTotal() {
		return payTotal;
	}


	public void setPayTotal(BigDecimal payTotal) {
		this.payTotal = payTotal;
	}


	public String getPayNo() {
		return payNo;
	}


	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}


	public Date getPaymentTime() {
		return paymentTime;
	}


	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}


	public Boolean getAllComplete() {
		return allComplete;
	}


	public void setAllComplete(Boolean allComplete) {
		this.allComplete = allComplete;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getBsPayInfoId() {
		return bsPayInfoId;
	}


	public void setBsPayInfoId(String bsPayInfoId) {
		this.bsPayInfoId = bsPayInfoId;
	}


    
    /**
     * 买家的支付账号( 第三方支付帐号)
     * 
     * @since 5.3.2.10
     * @return the paymentAccount
     */
    public String getPaymentAccount(){
        return paymentAccount;
    }


    /**
     * 买家的支付账号( 第三方支付帐号)
     * 
     * @since 5.3.2.10
     * @param paymentAccount the paymentAccount to set
     */
    public void setPaymentAccount(String paymentAccount){
        this.paymentAccount = paymentAccount;
    }

   



}
