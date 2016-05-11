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
 * @author feilong
 * @version 5.3.1 2016年4月28日 下午1:17:04
 * @since 5.3.1
 */
public class InvoiceInfoSubForm implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7282842164155121566L;

    /** 是否需要发票. */
    private boolean           isNeedInvoice;

    /** 发票抬头,比如 个人 还是 上海宝尊电子商务有限公司. */
    private String            invoiceTitle;

    /** 发票内容,比如 明细 办公用品 电脑配件 耗材. */
    private String            invoiceContent;

    //TODO feilong 将来扩展发票类型， 比如 普通发票 增值税发票 电子发票等等
    /**
     * 获得 是否需要发票.
     *
     * @return the isNeedInvoice
     */
    public boolean getIsNeedInvoice(){
        return isNeedInvoice;
    }

    /**
     * 设置 是否需要发票.
     *
     * @param isNeedInvoice
     *            the isNeedInvoice to set
     */
    public void setIsNeedInvoice(boolean isNeedInvoice){
        this.isNeedInvoice = isNeedInvoice;
    }

    /**
     * 获得 发票抬头,比如 个人 还是 上海宝尊电子商务有限公司.
     *
     * @return the invoiceTitle
     */
    public String getInvoiceTitle(){
        return invoiceTitle;
    }

    /**
     * 设置 发票抬头,比如 个人 还是 上海宝尊电子商务有限公司.
     *
     * @param invoiceTitle
     *            the invoiceTitle to set
     */
    public void setInvoiceTitle(String invoiceTitle){
        this.invoiceTitle = invoiceTitle;
    }

    /**
     * 获得 发票内容,比如 明细 办公用品 电脑配件 耗材.
     *
     * @return the invoiceContent
     */
    public String getInvoiceContent(){
        return invoiceContent;
    }

    /**
     * 设置 发票内容,比如 明细 办公用品 电脑配件 耗材.
     *
     * @param invoiceContent
     *            the invoiceContent to set
     */
    public void setInvoiceContent(String invoiceContent){
        this.invoiceContent = invoiceContent;
    }

}
