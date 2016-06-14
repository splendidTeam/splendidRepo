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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.baozun.nebula.command.wechat.WechatJsApiConfigCommand;
import com.baozun.nebula.command.wechat.WechatJsApiPayCommand;
import com.baozun.nebula.exception.IllegalPaymentStateException;
import com.baozun.nebula.exception.IllegalPaymentStateException.IllegalPaymentState;
import com.baozun.nebula.model.salesorder.PayInfoLog;
import com.baozun.nebula.payment.manager.PayManager;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.constants.SessionKeyConstants;
import com.feilong.core.CharsetType;
import com.feilong.core.Validator;
import com.feilong.servlet.http.RequestUtil;

/**
 * Neubla微信支付Controller
 *
 * @author yimin.qiao
 * @version 1.0
 * @time 2016年6月6日  下午18:33:30
 */
public class NebulaWechatPaymentController extends NebulaBasePaymentController {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(NebulaWechatPaymentController.class);
    
    //model key的常量定义
    /** 扫码支付页支付url */
  	public static final String		MODEL_KEY_JSAPI_PAY_PARAM		= "jsapiPayParam";
  	
  	/** 扫码支付页支付url */
  	public static final String		MODEL_KEY_JSAPI_CONFIG_PARAM	= "jsapiConfigParam";
  	
  	/** 扫码支付页支付url */
  	public static final String		MODEL_KEY_CODE_URL				= "codeUrl";
  	
  	/** 扫码支付页订单信息 */
  	public static final String		MODEL_KEY_SALES_ORDER			= "salesOrder";
    
    //view的常量定义
  	/** 公众号支付页 的默认定义 */
  	public static final String		VIEW_JSAPI_PAY					= "payment.wechat.jsapipay";
  	
  	/** 扫码支付页 的默认定义 */
  	public static final String		VIEW_CODE_PAY					= "payment.wechat.codepay";
    
    @Autowired
    private PayManager payManager;
    
    @Autowired
    private OrderManager          orderManager;
    
    //进入微信支付页面
    //如果是pc端，则显示二维码及其他订单信息，供用户扫码支付，二维码从session中获取
    //如果是mobile端，则进入mobile端的支付页面，调用jsapi发起支付，需要封装额外的信息
    public String showWechatPaymentPage(
    		@LoginMember MemberDetails memberDetails,
    		@RequestParam(value = "subOrdinate") String subOrdinate, 
			HttpServletRequest request, HttpServletResponse response, Model model) {
    	
    	return null;
    }
    
    /**
     * 
     * @RequestMapping(value = "/payment/wechat/openid.htm")
     * 
     * @param memberDetails
     * @param subOrdinate
     * @param request
     * @param response
     * @param model
     * @return
     */
    public String getWechatOpenidPage(
    		@LoginMember MemberDetails memberDetails,
    		@RequestParam(value = "subOrdinate") String subOrdinate, 
			HttpServletRequest request, HttpServletResponse response, Model model) {
    	return null;
    }
    
    /**
     * 
     * @RequestMapping(value = "/payment/wechat/jsapipay.htm")
     * 
     * @param subOrdinate
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IllegalPaymentStateException 
     */
    public String showWechatJsapiPayPage(
    		@LoginMember MemberDetails memberDetails,
    		@RequestParam(value = "subOrdinate") String subOrdinate, 
			HttpServletRequest request, HttpServletResponse response, Model model) throws IllegalPaymentStateException {
    	String prepayId = (String)request.getSession().getAttribute(SessionKeyConstants.WECHATPAY_JSAPI_PREPAY_ID);
    	
    	if (Validator.isNullOrEmpty(prepayId)){
            //扫码支付链接不存在
    		LOGGER.error("[SHOW_WECHAT_JSAPIPAY_PAGE] wechat jsapi pay prepayId not exists. subOrdinate:{}", subOrdinate);
            throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_WECHATPAY_PREPAYID_NOT_EXISTS);
        }
    	
    	//根据流水号查询未支付订单信息
        List<PayInfoLog> unpaidPayInfoLogs = getUnpaidPayInfoLogsBySubOrdinate(subOrdinate);

        if (Validator.isNullOrEmpty(unpaidPayInfoLogs)){
            //支付信息不存在或已支付
        	LOGGER.error("[SHOW_WECHAT_JSAPIPAY_PAGE] subordinate not exists or paid. subOrdinate:{}", subOrdinate);
            throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_ILLEGAL_SUBORDINATE_NOT_EXISTS_OR_PAID);
        }

        //如果找到多条，只取第一条
        PayInfoLog payInfoLog = unpaidPayInfoLogs.get(0);
        SalesOrderCommand salesOrder = orderManager.findOrderById(payInfoLog.getOrderId(), 1);

        //校验订单是否存在、取消、已支付
        validateSalesOrder(salesOrder, memberDetails);
        
    	WechatJsApiPayCommand wechatJsApiPayCommand = payManager.getWechatJsApiPayCommand(prepayId);
    	
    	String url = RequestUtil.getRequestFullURL(request, CharsetType.UTF8);
    	//TODO
    	String jsapiTicket = "";
    	WechatJsApiConfigCommand wechatJsApiConfigCommand = payManager.getWechatJsApiConfigCommand(url, jsapiTicket);
    	
    	model.addAttribute(MODEL_KEY_JSAPI_PAY_PARAM, wechatJsApiPayCommand);
    	model.addAttribute(MODEL_KEY_JSAPI_CONFIG_PARAM, wechatJsApiConfigCommand);
    	model.addAttribute(MODEL_KEY_SALES_ORDER, salesOrder);
    	
    	return VIEW_JSAPI_PAY;
    }
    
    /**
     * @RequestMapping(value = "/payment/wechat/codepay.htm")
     * 
     * @param subOrdinate
     * @param request
     * @param response
     * @param model
     * @return
     * @throws IllegalPaymentStateException 
     */
    public String showWechatCodePayPage(
    		@LoginMember MemberDetails memberDetails,
    		@RequestParam(value = "subOrdinate") String subOrdinate, 
			HttpServletRequest request, HttpServletResponse response, Model model) throws IllegalPaymentStateException {
    	
    	String codeUrl = (String)request.getSession().getAttribute(SessionKeyConstants.WECHATPAY_NATIVE_CODE_URL);
    	if (Validator.isNullOrEmpty(codeUrl)){
            //扫码支付链接不存在
    		LOGGER.error("[SHOW_WECHAT_CODEPAY_PAGE] wechat code pay url not exists. subOrdinate:{}", subOrdinate);
            throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_WECHATPAY_CODEURL_NOT_EXISTS);
        }
    	
    	//根据流水号查询未支付订单信息
        List<PayInfoLog> unpaidPayInfoLogs = getUnpaidPayInfoLogsBySubOrdinate(subOrdinate);

        if (Validator.isNullOrEmpty(unpaidPayInfoLogs)){
            //支付信息不存在或已支付
        	LOGGER.error("[SHOW_WECHAT_CODEPAY_PAGE] subordinate not exists or paid. subOrdinate:{}", subOrdinate);
            throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_ILLEGAL_SUBORDINATE_NOT_EXISTS_OR_PAID);
        }

        //如果找到多条，只取第一条
        PayInfoLog payInfoLog = unpaidPayInfoLogs.get(0);
        SalesOrderCommand salesOrder = orderManager.findOrderById(payInfoLog.getOrderId(), 1);

        //校验订单是否存在、取消、已支付
        validateSalesOrder(salesOrder, memberDetails);
        
        model.addAttribute(MODEL_KEY_CODE_URL, codeUrl);
        model.addAttribute(MODEL_KEY_SALES_ORDER, salesOrder);
        
    	return VIEW_CODE_PAY;
    }
    
}
