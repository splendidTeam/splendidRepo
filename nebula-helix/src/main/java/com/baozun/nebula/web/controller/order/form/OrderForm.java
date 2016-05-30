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

import com.baozun.nebula.web.controller.BaseForm;

/**
 * 创建订单提交的form.
 * 
 * 
 * <h3>表单提交需要以下信息:</h3>
 * <blockquote>
 * 
 * <table border="1" cellspacing="0" cellpadding="4">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * <tr valign="top">
 * <td>会员信息</td>
 * <td>这个从session获取,游客另算</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>购买的东西</td>
 * <td>这个代码中获取</td>
 * </tr>
 * <tr valign="top" >
 * <td>收货地址是什么</td>
 * <td>使用封装的 {@link #shippingInfoSubForm}</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>支付方式是什么</td>
 * <td>使用封装的 {@link #paymentInfoSubForm}</td>
 * </tr>
 * <tr valign="top" >
 * <td>优惠券信息</td>
 * <td>使用封装的 {@link #couponInfoSubForm}</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>要不要发票啊</td>
 * <td>使用封装的 {@link #invoiceInfoSubForm}</td>
 * </tr>
 * </table>
 * </blockquote>
 *
 * @author feilong
 * @version 5.3.1 2016年4月28日 下午1:14:19
 * @since 5.3.1
 */
public class OrderForm extends BaseForm{

    /** The Constant serialVersionUID. */
    private static final long   serialVersionUID = -2466326238655996293L;

    /** 收获地址信息. */
    private ShippingInfoSubForm shippingInfoSubForm;

    /** 支付相关信息. */
    private PaymentInfoSubForm  paymentInfoSubForm;

    /** 优惠券相关信息. */
    private CouponInfoSubForm   couponInfoSubForm;

    /** 发票信息. */
    private InvoiceInfoSubForm  invoiceInfoSubForm;

    //XXX feilong 留言信息 comments
    //XXX feilong 配送信息  目前我们都不支持用户在前端商城选择配送方式 ，以后这是趋势，可以在这里扩展

    //**********************************************************************************

    /**
     * 获得 收获地址信息.
     *
     * @return the shippingInfoSubForm
     */
    public ShippingInfoSubForm getShippingInfoSubForm(){
        return shippingInfoSubForm;
    }

    /**
     * 设置 收获地址信息.
     *
     * @param shippingInfoSubForm
     *            the shippingInfoSubForm to set
     */
    public void setShippingInfoSubForm(ShippingInfoSubForm shippingInfoSubForm){
        this.shippingInfoSubForm = shippingInfoSubForm;
    }

    /**
     * 获得 支付相关信息.
     *
     * @return the paymentInfoSubForm
     */
    public PaymentInfoSubForm getPaymentInfoSubForm(){
        return paymentInfoSubForm;
    }

    /**
     * 设置 支付相关信息.
     *
     * @param paymentInfoSubForm
     *            the paymentInfoSubForm to set
     */
    public void setPaymentInfoSubForm(PaymentInfoSubForm paymentInfoSubForm){
        this.paymentInfoSubForm = paymentInfoSubForm;
    }

    /**
     * 获得 优惠券相关信息.
     *
     * @return the couponInfoSubForm
     */
    public CouponInfoSubForm getCouponInfoSubForm(){
        return couponInfoSubForm;
    }

    /**
     * 设置 优惠券相关信息.
     *
     * @param couponInfoSubForm
     *            the couponInfoSubForm to set
     */
    public void setCouponInfoSubForm(CouponInfoSubForm couponInfoSubForm){
        this.couponInfoSubForm = couponInfoSubForm;
    }

    /**
     * 获得 发票信息.
     *
     * @return the invoiceInfoSubForm
     */
    public InvoiceInfoSubForm getInvoiceInfoSubForm(){
        return invoiceInfoSubForm;
    }

    /**
     * 设置 发票信息.
     *
     * @param invoiceInfoSubForm
     *            the invoiceInfoSubForm to set
     */
    public void setInvoiceInfoSubForm(InvoiceInfoSubForm invoiceInfoSubForm){
        this.invoiceInfoSubForm = invoiceInfoSubForm;
    }

}
