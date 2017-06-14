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
package com.baozun.nebula.web.controller.order.viewcommand;

import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * 订单里面的收获地址信息.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月12日 下午3:16:25
 * @see com.baozun.nebula.web.controller.order.form.InvoiceInfoSubForm
 * @see com.baozun.nebula.model.salesorder.SalesOrder
 * @since 5.3.1
 */
public class InvoiceInfoSubViewCommand extends BaseViewCommand {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6400008677521495969L;

    /** 发票类型. */
    private Integer receiptType;

    /** 发票抬头 发票抬头,比如 个人 还是 上海宝尊电子商务有限公司. */
    private String receiptTitle;

    /** 发票内容 发票内容,比如 明细 办公用品 电脑配件 耗材. */
    private String receiptContent;

    /** 发票号. */
    private String receiptCode;

    /** 发票收货人. */
    private String receiptConsignee;

    /** 发票收货人联系方式. */
    private String receiptTelphone;

    /** 发票收货地址. */
    private String receiptAddress;
    
    /** 纳税人识别码 . 
     * @since 5.3.2.18
     * */
    private String taxPayerId;

    // XXX feilong 缺少属性

    /**
     * 获得 发票类型.
     *
     * @return the receiptType
     */
    public Integer getReceiptType() {
        return receiptType;
    }

    /**
     * 设置 发票类型.
     *
     * @param receiptType the receiptType to set
     */
    public void setReceiptType(Integer receiptType) {
        this.receiptType = receiptType;
    }

    /**
     * 获得 发票抬头 发票抬头,比如 个人 还是 上海宝尊电子商务有限公司.
     *
     * @return the receiptTitle
     */
    public String getReceiptTitle() {
        return receiptTitle;
    }

    /**
     * 设置 发票抬头 发票抬头,比如 个人 还是 上海宝尊电子商务有限公司.
     *
     * @param receiptTitle the receiptTitle to set
     */
    public void setReceiptTitle(String receiptTitle) {
        this.receiptTitle = receiptTitle;
    }

    /**
     * 获得 发票内容 发票内容,比如 明细 办公用品 电脑配件 耗材.
     *
     * @return the receiptContent
     */
    public String getReceiptContent() {
        return receiptContent;
    }

    /**
     * 设置 发票内容 发票内容,比如 明细 办公用品 电脑配件 耗材.
     *
     * @param receiptContent the receiptContent to set
     */
    public void setReceiptContent(String receiptContent) {
        this.receiptContent = receiptContent;
    }

    /**
     * 获得 发票号.
     *
     * @return the receiptCode
     */
    public String getReceiptCode() {
        return receiptCode;
    }

    /**
     * 设置 发票号.
     *
     * @param receiptCode the receiptCode to set
     */
    public void setReceiptCode(String receiptCode) {
        this.receiptCode = receiptCode;
    }

    
    public String getReceiptConsignee() {
        return receiptConsignee;
    }

    public void setReceiptConsignee(String receiptConsignee) {
        this.receiptConsignee = receiptConsignee;
    }

    public String getReceiptTelphone() {
        return receiptTelphone;
    }

    public void setReceiptTelphone(String receiptTelphone) {
        this.receiptTelphone = receiptTelphone;
    }

    public String getReceiptAddress() {
        return receiptAddress;
    }

    public void setReceiptAddress(String receiptAddress) {
        this.receiptAddress = receiptAddress;
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
}
