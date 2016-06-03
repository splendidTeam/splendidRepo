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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.exception.IllegalPaymentStateException;
import com.baozun.nebula.exception.IllegalPaymentStateException.IllegalPaymentState;
import com.baozun.nebula.model.payment.PayCode;
import com.baozun.nebula.model.salesorder.PayInfoLog;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.SdkPaymentManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.payment.resolver.PaymentResolver;
import com.baozun.nebula.web.controller.payment.resolver.PaymentResolverType;
import com.feilong.core.Validator;
import com.feilong.servlet.http.RequestUtil;

/**
 * Neubla支付Controller
 *
 * @author yimin.qiao
 * @version 1.0
 * @time 2016年5月26日  上午11:33:30
 */
public class NebulaPaymentController extends BaseController {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(NebulaPaymentController.class);
    
    //view的常量定义
    /** 支付成功页面. */
    protected static final String VIEW_PAY_SUCCESS = "payment.pay-success";
    
    /** 支付失败页面. */
    protected static final String VIEW_PAY_FAILURE = "payment.pay-failure";
    
    /** 去支付的异常页面. */
    protected static final String VIEW_PAY_TOPAY_EXCEPTION = "payment.topay-exception";
    
    //默认url的定义
    /** 支付异常页的url */
    protected static String URL_TOPAY_EXCEPTION_PAGE = "/payment/error.htm";
    
    @Autowired
	private SdkPaymentManager sdkPaymentManager;
	
	@Autowired
	private OrderManager orderManager;
	
	@Autowired
	private PaymentResolverType paymentResolverType;
	
	/**
	 * 去往支付页面
	 * <p>一般会进入第三方的支付平台页面（如，支付宝），或商城定义的支付页面（如，微信扫码支付）</p>
	 * 
	 * @RequestMapping(value = "/payment/toPay.htm")
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
			HttpServletRequest request, HttpServletResponse response, Model model) {
    	
    	try {
			//根据不同的支付方式准备url
			return buildPayUrl(subOrdinate, memberDetails, request, response, model);
			
		} catch (IllegalPaymentStateException e) {
			
			LOGGER.error(e.getMessage(), e);
			
			//去往支付异常页面
			return "redirect:" + getToPayExceptionPageRedirect(subOrdinate);
		}
    	
    }
    
    /**
     * 支付完成的前台通知
     * 
     * @RequestMapping(value = "/payment/return/{payType}.htm")
     * 
     * @return
     */
    public String doPayReturn(@PathVariable("payType") String payType, 
    		HttpServletRequest request, HttpServletResponse response, Model model) {
	   try {
			
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("[PAY_NOTIFY] {}",RequestUtil.getRequestURL(request));
		    }
			
			PaymentResolver paymentResolver = paymentResolverType.getInstance(payType);
			
			return paymentResolver.doPayReturn(request, response, payType);
			
		} catch (IllegalPaymentStateException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
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
    public void doPayNotify(@PathVariable("payType") String payType,
    		HttpServletRequest request, HttpServletResponse response) {
    	
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
			HttpServletRequest request, HttpServletResponse response, Model model) {
    	
	    PayCode pc = sdkPaymentManager.findPayCodeBySubOrdinate(subOrdinate);
    	
		if (Validator.isNotNullOrEmpty(pc)) {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("subOrdinate", subOrdinate);
			// 查询订单的需要支付的payInfolog
			List<PayInfoLog> payInfoLogs = sdkPaymentManager.findPayInfoLogListByQueryMap(paraMap);

			Set<Long> set = new HashSet<Long>();
			StringBuffer orderCode = new StringBuffer();
			for (PayInfoLog payInfoLog : payInfoLogs) {
				set.add(payInfoLog.getOrderId());
			}

			for (Long oid : set) {
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
			HttpServletRequest request, HttpServletResponse response, Model model) {
    	
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
			HttpServletRequest request, HttpServletResponse response, Model model) {
    	
    	return VIEW_PAY_TOPAY_EXCEPTION;
    }
    
    protected String buildPayUrl(String subOrdinate, MemberDetails memberDetails, HttpServletRequest request, HttpServletResponse response, Model model) 
			throws IllegalPaymentStateException {
		//根据流水号查询未支付订单信息
		List<PayInfoLog> unpaidPayInfoLogs = getUnpaidPayInfoLogsBySubOrdinate(subOrdinate);
		
		if(Validator.isNullOrEmpty(unpaidPayInfoLogs)) {
			//支付信息不存在或已支付
			throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_ILLEGAL_SUBORDINATE_NOT_EXISTS_OR_PAID, "支付信息不存在或已支付");
		}
		
		//如果找到多条，只取第一条
		PayInfoLog payInfoLog = unpaidPayInfoLogs.get(0);
		SalesOrderCommand salesOrder = orderManager.findOrderById(payInfoLog.getOrderId(), 1);
		
		//校验订单是否存在、取消、已支付
		if(validateSalesOrderStatus(salesOrder)) {
			PaymentResolver paymentResolver = paymentResolverType.getInstance(payInfoLog.getPayType().toString());
			return paymentResolver.buildPayUrl(salesOrder, payInfoLog, memberDetails, getDevice(request), request, response, model);
		}
		
		return null;
	}
	
	/**
	 * 验证订单的状态
	 * 
	 */
	protected boolean validateSalesOrderStatus(SalesOrderCommand salesOrder) throws IllegalPaymentStateException {
		//订单不存在
		if (Validator.isNullOrEmpty(salesOrder) || Validator.isNullOrEmpty(salesOrder.getId())
				|| Validator.isNullOrEmpty(salesOrder.getCode())) {
			throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_ILLEGAL_ORDER_NOT_EXISTS);
		} else {
			//订单已取消
			if (SalesOrder.SALES_ORDER_STATUS_CANCELED.equals(salesOrder.getLogisticsStatus())
					|| SalesOrder.SALES_ORDER_STATUS_SYS_CANCELED.equals(salesOrder.getLogisticsStatus())) {
				throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_ILLEGAL_ORDER_CANCLED);
			}
			//订单已支付
			if (SalesOrder.SALES_ORDER_FISTATUS_FULL_PAYMENT.equals(salesOrder.getFinancialStatus())) {
				throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_ILLEGAL_ORDER_PAID);
			}
		}
		
		return true;
	}
	
	protected String getToPayExceptionPageRedirect(String subOrdinate) {
		return URL_TOPAY_EXCEPTION_PAGE + "?subOrdinate=" + subOrdinate;
	}
	
	/**
	 * 根据流水号获取订单信息
	 * 
	 * @param subOrdinate 支付流水号
	 * @param paySuccessStatus 支付状态，1表示支付成功，2表示未支付
	 * @return
	 * @throws IllegalPaymentStateException 
	 */
	protected SalesOrderCommand getSalesOrderBySubOrdinate(String subOrdinate, Integer paySuccessStatus) throws IllegalPaymentStateException {
        List<PayInfoLog> payInfoLogList = getPayInfoLogListBySubOrdinate(subOrdinate, paySuccessStatus);
        
        if (Validator.isNullOrEmpty(payInfoLogList)){
            LOGGER.error("can not get payInfo_log by subOrdinate:[{}] and paySuccessStatus:[{}]", subOrdinate, true);
            throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_ILLEGAL_SUBORDINATE_NOT_EXISTS_OR_UNPAID, "支付信息不存在或尚未支付");
        }
        
        // 根据支付流水号，去取结果页面上要显示的订单信息
        PayInfoLog payInfoLog = payInfoLogList.get(0);
		SalesOrderCommand salesOrder = orderManager.findOrderById(payInfoLog.getOrderId(), 2);
		
        return salesOrder;
    }
	
	/**
	 * 根据流水号取未付款支付信息日志
	 * @param subOrdinate
	 * @return
	 */
	private List<PayInfoLog> getUnpaidPayInfoLogsBySubOrdinate(String subOrdinate) {
		//2代表支付没有成功，pay_success_status为false
		return getPayInfoLogListBySubOrdinate(subOrdinate, 2);
	}
	
	/**
	 * 根据流水号取支付信息日志
	 * @param subOrdinate 支付流水号
	 * @param paySuccessStatus 支付状态，1表示支付成功，2表示未支付
	 * @return
	 */
	private List<PayInfoLog> getPayInfoLogListBySubOrdinate(String subOrdinate, Integer paySuccessStatus) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("subOrdinate", subOrdinate);
        paraMap.put("paySuccessStatusStr", paySuccessStatus);
        List<PayInfoLog> payInfoLogList = sdkPaymentManager.findPayInfoLogListByQueryMap(paraMap);
        return payInfoLogList;
    }
    
}
