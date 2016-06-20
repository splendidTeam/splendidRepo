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
package com.baozun.nebula.web.controller.payment;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.exception.IllegalPaymentStateException;
import com.baozun.nebula.exception.IllegalPaymentStateException.IllegalPaymentState;
import com.baozun.nebula.model.payment.PayCode;
import com.baozun.nebula.model.salesorder.PayInfoLog;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.SdkPaymentManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.payment.resolver.PaymentResolver;
import com.baozun.nebula.web.controller.payment.resolver.PaymentResolverType;
import com.feilong.core.Validator;
import com.feilong.servlet.http.RequestUtil;

/**
 * Neubla支付Controller
 *
 * @author yimin.qiao
 * @version 1.0
 * @time 2016年5月26日 上午11:33:30
 */
public class NebulaPaymentController extends NebulaBasePaymentController{

    /** The Constant LOGGER. */
    private static final Logger   LOGGER                   = LoggerFactory.getLogger(NebulaPaymentController.class);

    //view的常量定义
    /** 支付成功页面. */
    protected static final String VIEW_PAY_SUCCESS         = "payment.pay-success";

    /** 支付失败页面. */
    protected static final String VIEW_PAY_FAILURE         = "payment.pay-failure";

    /** 去支付的异常页面. */
    protected static final String VIEW_PAY_TOPAY_EXCEPTION = "payment.topay-exception";

    //默认url的定义
    /** 支付异常页的url */
    protected static final String URL_TOPAY_EXCEPTION_PAGE = "/payment/error.htm";
    
    /** 支付成功页面. */
    protected static final String URL_PAY_SUCCESS_PAGE     = "/payment/success.htm";
    
    /** 支付失败页面. */
    protected static final String URL_PAY_FAILURE_PAGE     = "/payment/failure.htm";

    @Autowired
    private SdkPaymentManager     sdkPaymentManager;

    @Autowired
    private OrderManager          orderManager;

    @Autowired
    private PaymentResolverType   paymentResolverType;

    /**
     * 去往支付页面
     * <p>
     * 一般会进入第三方的支付平台页面（如，支付宝），或商城定义的支付页面（如，微信扫码支付）
     * </p>
     * 
     * @RequestMapping(value = URL_TOPAY)
     * 
     * @param memberDetails
     * @param subOrdinate
     * @param request
     * @param response
     * @param model
     * @return
     */
    public String toPay(
                    @LoginMember MemberDetails memberDetails,
                    @RequestParam(value = "subOrdinate") String subOrdinate,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Model model){

        try {

            //根据不同的支付方式准备url
            return buildPayUrl(subOrdinate, memberDetails, request, response, model);

        } catch (IllegalPaymentStateException e){

            LOGGER.error("[TO_PAY] " + e.getMessage(), e);

            //去往支付异常页面
            return "redirect:" + getToPayExceptionPageRedirect() + "?subOrdinate=" + subOrdinate;
        }

    }

    /**
     * 支付完成的前台通知
     * 
     * @RequestMapping(value = "/payment/return/{payType}.htm")
     * 
     * @return
     */
    public String doPayReturn(@PathVariable("payType") String payType, HttpServletRequest request,HttpServletResponse response, Model model){
        try{

            if (LOGGER.isDebugEnabled()){
                LOGGER.debug("[PAY_NOTIFY] {}", RequestUtil.getRequestURL(request));
            }

            PaymentResolver paymentResolver = paymentResolverType.getInstance(payType);

            return paymentResolver.doPayReturn(payType, getDevice(request), getPaySuccessRedirect(), getPayFailureRedirect(), request, response);

        } catch (IllegalPaymentStateException e){
            LOGGER.error(e.getMessage(), e);
            //去往支付异常页面
            return "redirect:" + getToPayExceptionPageRedirect() + "?subOrdinate=" + e.getSubOrdinate();
        }
    }

    /**
     * 支付完成的后台通知
     * 
     * @RequestMapping(value = "/payment/notify/{payType}.htm")
     * 
     * @param payType
     * @param request
     * @param response
     */
    public void doPayNotify(@PathVariable("payType") String payType,HttpServletRequest request,HttpServletResponse response){

        try{

            if (LOGGER.isDebugEnabled()){
                LOGGER.debug("[PAY_NOTIFY] {}", RequestUtil.getRequestURL(request));
            }

            PaymentResolver paymentResolver = paymentResolverType.getInstance(payType);

            paymentResolver.doPayNotify(payType, getDevice(request), request, response);

        }catch (IllegalPaymentStateException | IOException e){
            LOGGER.error(e.getMessage(), e);
        }

    }

    /**
     * 支付成功页面
     * 
     * @RequestMapping(value = "/payment/success.htm")
     * 
     * @param memberDetails
     * @param subOrdinate
     * @param request
     * @param response
     * @param model
     * @return
     */
    public String showPaySuccess(
                    @LoginMember MemberDetails memberDetails,
                    @RequestParam(value = "subOrdinate") String subOrdinate,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Model model){

        PayCode pc = sdkPaymentManager.findPayCodeBySubOrdinate(subOrdinate);

        if (Validator.isNotNullOrEmpty(pc)){
            Map<String, Object> paraMap = new HashMap<String, Object>();
            paraMap.put("subOrdinate", subOrdinate);
            // 查询订单的需要支付的payInfolog
            List<PayInfoLog> payInfoLogs = sdkPaymentManager.findPayInfoLogListByQueryMap(paraMap);

            Set<Long> set = new HashSet<Long>();
            StringBuffer orderCode = new StringBuffer();
            for (PayInfoLog payInfoLog : payInfoLogs){
                set.add(payInfoLog.getOrderId());
            }

            for (Long oid : set){
                SalesOrderCommand so = orderManager.findOrderById(oid, null);
                orderCode.append(so.getCode()).append("、");
            }

            model.addAttribute("orderCode", orderCode.substring(0, orderCode.length() - 1));

            model.addAttribute("totalFee", pc.getPayMoney());
        }
        return VIEW_PAY_SUCCESS;
    }

    /**
     * 支付失败页面
     * 
     * @RequestMapping(value = "/payment/failure.htm")
     * 
     * @param memberDetails
     * @param subOrdinate
     * @param request
     * @param response
     * @param model
     * @return
     */
    public String showPayFailure(
                    @LoginMember MemberDetails memberDetails,
                    @RequestParam(value = "subOrdinate") String subOrdinate,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Model model){
    	PayCode pc = sdkPaymentManager.findPayCodeBySubOrdinate(subOrdinate);

        if (Validator.isNotNullOrEmpty(pc)){
            Map<String, Object> paraMap = new HashMap<String, Object>();
            paraMap.put("subOrdinate", subOrdinate);
            // 查询订单的需要支付的payInfolog
            List<PayInfoLog> payInfoLogs = sdkPaymentManager.findPayInfoLogListByQueryMap(paraMap);

            Set<Long> set = new HashSet<Long>();
            StringBuffer orderCode = new StringBuffer();
            for (PayInfoLog payInfoLog : payInfoLogs){
                set.add(payInfoLog.getOrderId());
            }

            for (Long oid : set){
                SalesOrderCommand so = orderManager.findOrderById(oid, null);
                orderCode.append(so.getCode()).append("、");
            }

            model.addAttribute("orderCode", orderCode.substring(0, orderCode.length() - 1));

            model.addAttribute("totalFee", pc.getPayMoney());
        }
        return VIEW_PAY_FAILURE;
    }

    /**
     * 发起支付异常页面
     * 
     * @RequestMapping(value = "/payment/error.htm")
     * 
     * @param memberDetails
     * @param subOrdinate
     * @param request
     * @param response
     * @param model
     * @return
     */
    public String showToPayException(
                    @LoginMember MemberDetails memberDetails,
                    @RequestParam(value = "subOrdinate") String subOrdinate,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Model model){

        return VIEW_PAY_TOPAY_EXCEPTION;
    }

    protected String buildPayUrl(
                    String subOrdinate,
                    MemberDetails memberDetails,
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Model model) throws IllegalPaymentStateException{
        //根据流水号查询未支付订单信息
        List<PayInfoLog> unpaidPayInfoLogs = getUnpaidPayInfoLogsBySubOrdinate(subOrdinate);

        if (Validator.isNullOrEmpty(unpaidPayInfoLogs)){
            //支付信息不存在或已支付
            throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_ILLEGAL_SUBORDINATE_NOT_EXISTS_OR_PAID,
            		"支付信息不存在或已支付, subOrdinate:" + subOrdinate);
        }
        
        if (unpaidPayInfoLogs.size() > 1){
            //支付信息不存在或已支付
            throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_ILLEGAL_SUBORDINATE_MULTI_ORDERS,
            		"交易流水对应多个未支付订单, subOrdinate:" + subOrdinate);
        }

        //如果找到多条，只取第一条
        PayInfoLog payInfoLog = unpaidPayInfoLogs.get(0);
        SalesOrderCommand salesOrder = orderManager.findOrderById(payInfoLog.getOrderId(), 1);

        //校验订单是否存在、取消、已支付
        validateSalesOrder(salesOrder, memberDetails);
        
        PaymentResolver paymentResolver = paymentResolverType.getInstance(payInfoLog.getPayType().toString());
        Map<String, Object> extraPayParams = getExtraPayParams(salesOrder, payInfoLog, memberDetails, getDevice(request), request);
        return paymentResolver.buildPayUrl(salesOrder, payInfoLog, memberDetails, getDevice(request), extraPayParams, request, response, model);
    }
    
    /**
     * <pre>
     *  设置支付所需要的额外参数，设置初始化参数 如果有需要需要重写该方法
     * 
     *  1： 当设置支付宝扫码支付模式qrPayMode为0或1或3（扫码支付方式为订单码-简约前置模式或订单码-前置模式或订单码-迷你前置模式）的情况下，
     *  同步通知地址return_url需要传入商户中间跳转页面，即该页面需要实现让父页面自行跳转的功能，
     *  中间页面javascript代码：<script>window.parent.location.href='父页面调整的URL';</script>
     *   
     *  2：目前仅仅需要用到上面这个参数……
     * 
     * </pre>
     * 
     * @return
     */
    protected Map<String, Object> getExtraPayParams(SalesOrderCommand originalSalesOrder, PayInfoLog payInfoLog, 
			MemberDetails memberDetails, Device device, HttpServletRequest request) {

        Map<String, Object> extra = new HashMap<String, Object>();

        //默认即时到账，非扫码
        extra.put("qrPayMode", null);

        return extra;
    }

    protected String getToPayExceptionPageRedirect(){
        return URL_TOPAY_EXCEPTION_PAGE;
    }
    
	protected String getPaySuccessRedirect() {
		return URL_PAY_SUCCESS_PAGE;
	}
	
	protected String getPayFailureRedirect() {
		return URL_PAY_FAILURE_PAGE;
	}
}
