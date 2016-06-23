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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.exception.BusinessException;
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
 * <p>Nebula预支持如下的支付方式，分期去实现（括号中为期次）：</br>
 * 
 * 一、PC网页
 * <ol>
 * <li>支付宝即时到账(I)</li>
 * <li>支付宝扫码(I)</li>
 * <li>微信扫码(I)</li>
 * <li>银联网关支付(II)</li>
 * <li>建行直连支付(III)</li>
 * <li>Asia Pay(III)</li>
 * <li>Paypal(III)</li>
 * <li>支付宝国际卡支付 (III)</li>
 * </ol>
 * 二、移动页面
 * <ol>
 * <li>支付宝手机网站支付(I)</li>
 * <li>银联手机支付(II)</li>
 * <li>微信H5支付(III)</li>
 * </ol>
 * 三、微信公众号
 * <ol>
 * <li>微信公众号支付 (I)</li>
 * </ol>
 * </p>
 *
 * <ul>支付相关的操作：
 * <li>发起支付: {@link #toPay(MemberDetails, String, HttpServletRequest, HttpServletResponse, Model)}}</br>
 * 根据不同的支付方式，构造进入第三方支付平台支付页面的url，微信支付和其他的支付方式不同，会进入商城自己实现的支付页面，在该页面完成扫码或者公众号支付。
 * {@link #buildPayUrl(String, MemberDetails, HttpServletRequest, HttpServletResponse, Model)}，该实现默认不支持合并付款，所以传过来的流水号理论上只能查询到一笔支付日志（PayInfoLog）。
 * 在发起支付的逻辑里，会对当前支付流水及订单做基本的校验，比如，订单是否存在，订单状态是否和规等，如果不满足需求可以重写{@link #NebulaBasePaymentController.validateSalesOrder(SalesOrderCommand, MemberDetails)}
 * </li>
 * <li>支付完成的前台通知:{@link #doPayReturn(String, HttpServletRequest, HttpServletResponse, Model)} </li>
 * <li>支付完成的后台通知:{@link #doPayNotify(String, HttpServletRequest, HttpServletResponse)}</li>
 * <li>支付成功页面:{@link #showPaySuccess(MemberDetails, String, HttpServletRequest, HttpServletResponse, Model)}</li>
 * <li>支付失败页面:{@link #showPayFailure(MemberDetails, String, HttpServletRequest, HttpServletResponse, Model)}</li>
 * <li>发起支付异常页面:{@link #showToPayException(MemberDetails, String, HttpServletRequest, HttpServletResponse, Model)}</li>
 * </ul>
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
    protected static final String URL_TOPAY_EXCEPTION_PAGE = "/pay/error.htm";
    
    /** 支付成功页面. */
    protected static final String URL_PAY_SUCCESS_PAGE     = "/pay/success.htm";
    
    /** 支付失败页面. */
    protected static final String URL_PAY_FAILURE_PAGE     = "/pay/failure.htm";

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
     * @RequestMapping(value = "/pay/return/{payType}.htm")
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
     * @RequestMapping(value = "/pay/notify/{payType}.htm")
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

            paymentResolver.doPayNotify(payType, request, response);

        }catch (IllegalPaymentStateException | IOException | DocumentException e){
            LOGGER.error(e.getMessage(), e);
        } 

    }

    /**
     * 支付成功页面
     * 
     * @RequestMapping(value = "/pay/success.htm")
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

        if(Validator.isNullOrEmpty(pc)){
        	throw new BusinessException("Show pay success error.");
        }
        
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("subOrdinate", subOrdinate);
        // 查询订单的需要支付的payInfolog
        List<PayInfoLog> payInfoLogs = sdkPaymentManager.findPayInfoLogListByQueryMap(paraMap);

        Set<Long> set = new HashSet<Long>();
        for (PayInfoLog payInfoLog : payInfoLogs){
            set.add(payInfoLog.getOrderId());
        }

        List<SalesOrderCommand> orders = new ArrayList<SalesOrderCommand>();
        
        for (Long oid : set){
            SalesOrderCommand so = orderManager.findOrderById(oid, null);
            orders.add(so);
        }

        model.addAttribute("orders", orders);

        model.addAttribute("totalFee", pc.getPayMoney());
        
        if(pc.getPaySuccessStatus()){
        	 return VIEW_PAY_SUCCESS;
        }else{
             return VIEW_PAY_FAILURE;
        }
        
    }

    /**
     * 支付失败页面
     * 
     * @RequestMapping(value = "/pay/failure.htm")
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

        if(Validator.isNullOrEmpty(pc)){
        	throw new BusinessException("Show pay failure error.");
        }
        
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("subOrdinate", subOrdinate);
        // 查询订单的需要支付的payInfolog
        List<PayInfoLog> payInfoLogs = sdkPaymentManager.findPayInfoLogListByQueryMap(paraMap);

        Set<Long> set = new HashSet<Long>();
        for (PayInfoLog payInfoLog : payInfoLogs){
            set.add(payInfoLog.getOrderId());
        }

        List<SalesOrderCommand> orders = new ArrayList<SalesOrderCommand>();
        
        for (Long oid : set){
            SalesOrderCommand so = orderManager.findOrderById(oid, null);
            orders.add(so);
        }

        model.addAttribute("orders", orders);

        model.addAttribute("totalFee", pc.getPayMoney());
        
        if(pc.getPaySuccessStatus()){
        	 return VIEW_PAY_SUCCESS;
        }else{
             return VIEW_PAY_FAILURE;
        }
        
    }

    /**
     * 发起支付异常页面
     * 
     * @RequestMapping(value = "/pay/error.htm")
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
