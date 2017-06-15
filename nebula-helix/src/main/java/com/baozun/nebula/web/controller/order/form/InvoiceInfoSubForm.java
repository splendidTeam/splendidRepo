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
package com.baozun.nebula.web.controller.order.form;

import java.io.Serializable;


/**
 * 发票信息.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年4月28日 下午1:17:04
 * @since 5.3.1
 */
public class InvoiceInfoSubForm implements Serializable {

	public final static String INVOICE_TYPE_PRIVATE = "1";

	public final static String INVOICE_TYPE_COMPANY = "2";

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7282842164155121566L;

	/** 是否需要发票. */
	private boolean isNeedInvoice;

	/****** 1:个人 2:公司 ****/
	private Integer invoiceType;

	/** 发票抬头,比如 个人 还是 上海宝尊电子商务有限公司. */
	private String invoiceTitle;

	/** * 收取人姓名** */
	private String consignee;

	/** * 联系方式** */
	private String telphone;

	/** * 收取地址** */
	private String address;

	/** 发票内容,比如 明细 办公用品 电脑配件 耗材. */
	private String invoiceContent;
	
	/** 纳税人识别码 . 
     * @since 5.3.2.18
     * */
    private String taxPayerId;
    
    /** 公司地址.
     * @since 5.3.2.18
     *  */
    private String             companyAddress;
    
    /** 公司电话.
     * @since 5.3.2.18
     *  */
    private String             companyPhone;
    
    /** 开户银行名称.
     * @since 5.3.2.18
     *  */
    private String             accountBankName;
    
    /** 开户银行账号. 
     * @since 5.3.2.18
     * */
    private String             accountBankNumber;

	// XXX feilong 将来扩展发票类型， 比如 普通发票 增值税发票 电子发票等等

	/**
	 * 获得 是否需要发票.
	 *
	 * @return the isNeedInvoice
	 */
	public boolean getIsNeedInvoice() {
		return isNeedInvoice;
	}

	/**
	 * 设置 是否需要发票.
	 *
	 * @param isNeedInvoice
	 *            the isNeedInvoice to set
	 */
	public void setIsNeedInvoice(boolean isNeedInvoice) {
		this.isNeedInvoice = isNeedInvoice;
	}

	/**
	 * 获得 发票抬头,比如 个人 还是 上海宝尊电子商务有限公司.
	 *
	 * @return the invoiceTitle
	 */
	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	/**
	 * 设置 发票抬头,比如 个人 还是 上海宝尊电子商务有限公司.
	 *
	 * @param invoiceTitle
	 *            the invoiceTitle to set
	 */
	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	/**
	 * 获得 发票内容,比如 明细 办公用品 电脑配件 耗材.
	 *
	 * @return the invoiceContent
	 */
	public String getInvoiceContent() {
		return invoiceContent;
	}

	/**
	 * 设置 发票内容,比如 明细 办公用品 电脑配件 耗材.
	 *
	 * @param invoiceContent
	 *            the invoiceContent to set
	 */
	public void setInvoiceContent(String invoiceContent) {
		this.invoiceContent = invoiceContent;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public Integer getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setNeedInvoice(boolean isNeedInvoice) {
		this.isNeedInvoice = isNeedInvoice;
	}
	
	/**
     * 获得 纳税人识别码
     * @return the 获取纳税人识别码
     * @since 5.3.2.18
     */
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
     * 获得 公司地址
     * @return the 公司地址
     * @since 5.3.2.18
     */
    public String getCompanyAddress(){
        return companyAddress;
    }

    /**
     * 设置 公司地址
     * @param companyAddress
     *              the new 公司地址
     * @since 5.3.2.18
     */
    public void setCompanyAddress(String companyAddress){
        this.companyAddress = companyAddress;
    }

    /**
     * 获得 公司电话
     * @return the 公司电话
     * @since 5.3.2.18
     */
    public String getCompanyPhone(){
        return companyPhone;
    }

    /**
     * 设置 公司电话
     * @param companyPhone
     *              the new 公司电话
     * @since 5.3.2.18
     */
    public void setCompanyPhone(String companyPhone){
        this.companyPhone = companyPhone;
    }

    /**
     * 获得 开户银行名称
     * @return the 开户银行名称
     * @since 5.3.2.18
     */
    public String getAccountBankName(){
        return accountBankName;
    }

    /**
     * 设置 开户银行名称
     * @param accountBankName
     *              the new 开户银行名称
     * @since 5.3.2.18
     */
    public void setAccountBankName(String accountBankName){
        this.accountBankName = accountBankName;
    }

    /**
     * 获得 开户银行账号
     * @return the 开户银行账号
     * @since 5.3.2.18
     */
    public String getAccountBankNumber(){
        return accountBankNumber;
    }

    /**
     * 设置 开户银行账号
     * @param accountBankNumber
     *              the new 开户银行账号
     * @since 5.3.2.18
     */
    public void setAccountBankNumber(String accountBankNumber){
        this.accountBankNumber = accountBankNumber;
    }


}
