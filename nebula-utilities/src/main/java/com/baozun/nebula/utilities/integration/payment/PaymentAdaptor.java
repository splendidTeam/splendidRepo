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
package com.baozun.nebula.utilities.integration.payment;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.baozun.nebula.utilities.integration.payment.exception.PaymentException;

public interface PaymentAdaptor{

    public static final String PAYMENT_GATEWAY = "payment_gateway";

    //--------------------------------------------------------------------------------------------------------------
    /**
     * 获取服务提供方编码
     * 
     * @return
     */
    String getServiceProvider();

    /**
     * 获取服务提供方提供的该类服务的编码或者类型标识，如Alipay提供的移动支付
     * 
     * @return
     */
    String getServiceType();

    /**
     * 获取服务版本
     * 
     * @return
     */
    String getServiceVersion();

    //--------------------------------------------------------------------------------------------------------------

    /**
     * 创建一个新的PC端支付请求
     * 
     * @param httpType
     * @param orderNo
     * @param amt
     * @param addition
     * @return
     * @throws PaymentException
     */
    PaymentRequest newPaymentRequest(String httpType,Map<String, String> addition);

    /**
     * 创建一个新的MOBILE端授权请求
     * 
     * @param httpType
     * @param orderNo
     * @param amt
     * @param addition
     * @return
     * @throws PaymentException
     */
    PaymentRequest newPaymentRequestForMobileCreateDirect(Map<String, String> addition);

    //--------------------------------------------------------------------------------------------------------------

    /**
     * 返回支付结果(正常页面跳转返回的结果)
     * 
     * @param request
     * @return
     */
    PaymentResult getPaymentResult(HttpServletRequest request);

    /**
     * 解析通知返回支付结果
     * 
     * @param request
     * @return
     */
    PaymentResult getPaymentResultFromNotification(HttpServletRequest request);

    /**
     * 手机WAP端同步交易结果
     * 
     * @param request
     * @return
     */
    PaymentResult getPaymentResultForMobileAuthAndExecuteSYN(HttpServletRequest request);

    /**
     * 手机WAP端异步交易结果
     * 
     * @param request
     * @return
     */
    PaymentResult getPaymentResultForMobileAuthAndExecuteASY(HttpServletRequest request);

    //--------------------------------------------------------------------------------------------------------------

    /**
     * 关闭交易
     * 
     * @param amt
     *            退款金额 ,暂时这个参数没有用, 以后可能出现 预付款交易退款的情况, 需要先验证 支付金额和退款金额是否一致,如果不一致 返回false
     * @return
     */
    PaymentResult closePaymentRequest(Map<String, String> parm);

    /**
     * 是否支持关闭交易
     * 
     * @return
     */
    boolean isSupportClosePaymentRequest();

    //--------------------------------------------------------------------------------------------------------------

    /**
     * 交易查询
     * 
     * @return
     */
    PaymentResult getOrderInfo(Map<String, String> addition);

    //--------------------------------------------------------------------------------------------------------------

    /**
     * <p>微信下单创建微信支付链接方法<p>
     * 
     * <p>该方法原定义存在歧义，返回值不应该定义为PaymentResult（支付结果），而应该为PaymentRequest<br>
     * 且接口中已定义newPaymentRequest方法（应该在此方法中实现创建微信支付逻辑），与该方法用途定义重复，让外部调用者会产生疑问。
     * <p>
     * 
     * @param addition
     * @return
     */
    @Deprecated
    PaymentResult unifiedOrder(Map<String, String> addition);

    /**
     * 
     * @Description Alipay WAP 支付需要拼接完参数之后，再获取一次token
     * @param paymentRequest
     * @return
     * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
     * @version 2016-11-17
     */
    PaymentRequest getCreateResponseToken(PaymentRequest paymentRequest);

}
