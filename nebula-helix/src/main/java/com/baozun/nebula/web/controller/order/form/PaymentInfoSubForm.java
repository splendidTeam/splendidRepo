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
 * 支付信息.
 *
 * @author feilong
 * @version 5.3.1 2016年4月28日 下午1:18:13
 * @since 5.3.1
 */
public class PaymentInfoSubForm implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7282842164155121566L;

    /** 支付类型. */
    private String            paymentType;

    /** 银行编码,通常不需要设置，目前有 支付宝网银支付方式 需要额外设置 银行编码. */
    private String            bankcode;

    /**
     * 获得 支付类型.
     *
     * @return the paymentType
     */
    public String getPaymentType(){
        return paymentType;
    }

    /**
     * 设置 支付类型.
     *
     * @param paymentType
     *            the paymentType to set
     */
    public void setPaymentType(String paymentType){
        this.paymentType = paymentType;
    }

    /**
     * 获得 银行编码,通常不需要设置，目前有 支付宝网银支付方式 需要额外设置 银行编码.
     *
     * @return the bankcode
     */
    public String getBankcode(){
        return bankcode;
    }

    /**
     * 设置 银行编码,通常不需要设置，目前有 支付宝网银支付方式 需要额外设置 银行编码.
     *
     * @param bankcode
     *            the bankcode to set
     */
    public void setBankcode(String bankcode){
        this.bankcode = bankcode;
    }

}
