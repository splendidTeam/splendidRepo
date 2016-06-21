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
import com.baozun.nebula.web.controller.payment.service.wechat.WechatService;
import com.baozun.nebula.web.controller.payment.service.wechat.command.AuthAccessTokenCommand;
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
    
    //微信支付的openid和jsapiticket的来源
    /** 微信支付的openid和jsapiticket的来源: 从nebulaplus获取 */
    protected static final Integer WECAT_PAYSOURCEMODE_NBP = 1;
    
    /** 微信支付的openid和jsapiticket的来源: 从微信获取 */
    protected static final Integer WECAT_PAYSOURCEMODE_WECHAT = 2;
    
    /** 微信支付的openid和jsapiticket的来源: 默认从微信获取 */
    protected static final Integer WECAT_PAYSOURCEMODE_DEFAULT = WECAT_PAYSOURCEMODE_WECHAT;
    
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
    private OrderManager orderManager;
    
    @Autowired
    private WechatService wechatService;
    
    /**
     * 
     * <ul>openid的获取分三种情况：
  	 * <li>用户在微信中打开网站时，商城已经自动完成了微信登录，此时从session中获取，不会进入该页面</li>
  	 * <li>通过NBP获取，获取后也放在session中</li>
  	 * <li>直接通过微信获取，获取后也放在session中</li>
  	 * <ul>
  	 * 
     * @RequestMapping(value = "/payment/wechat/openid.htm")
     * 
     * @param memberDetails
     * @param subOrdinate
     * @param request
     * @param response
     * @param model
     * @return 获取openid的url
     */
  	public String showWechatOpenidPage(
    		@LoginMember MemberDetails memberDetails,
    		@RequestParam(value = "subOrdinate") String subOrdinate, 
			HttpServletRequest request, HttpServletResponse response, Model model) {
  		
  		if(WECAT_PAYSOURCEMODE_NBP.equals(getWecatPaySourceMode())) {
    		return getOpenidFromNbp(subOrdinate);
    	}
    	return getOpenidFromWechat(subOrdinate);
    }
    
    //从微信中获取openid
    protected String getOpenidFromWechat(String subOrdinate) {
    	return "redirect:" + wechatService.genWechatAuthCodeUrl("?subOrdinate=" + subOrdinate, "snsapi_base", null);
    }
    
    //从nbp中获取openid
    protected String getOpenidFromNbp(String subOrdinate) {
    	return "redirect:" + wechatService.genGetOpenidUrlFromNbp("?subOrdinate=" + subOrdinate);
    }
    
    /**
     * @RequestMapping(value = "/wechat/auth/callback.htm")
     * @param code
     * @param subOrdinate
     * @param request
     * @param response
     * @param model
     * @return
     */
    public String getOpenidFromWechatCallback(
    		@RequestParam(value = "code") String code, 
    		@RequestParam(value = "subOrdinate") String subOrdinate, 
			HttpServletRequest request, HttpServletResponse response, Model model) {
    	AuthAccessTokenCommand authAccessTokenCommand = wechatService.getAuthAccessToken(code);
    	request.getSession().setAttribute(SessionKeyConstants.MEMBER_WECHAT_OPENID, authAccessTokenCommand.getOpenid());
    	return "redirect:" + URL_TOPAY + "?subOrdinate=" + subOrdinate;
    }
    
    /**
     * 
     * @RequestMapping(value = "/wechat/nbpopenid/callback.htm")
     * 
     * @param subOrdinate
     * @param openid
     * @param request
     * @param response
     * @param model
     * @return
     */
    public String getOpenidFromNbpCallback(
    		@RequestParam(value = "subOrdinate") String subOrdinate, 
    		@RequestParam(value = "openid") String openid, 
			HttpServletRequest request, HttpServletResponse response, Model model) {
    	request.getSession().setAttribute(SessionKeyConstants.MEMBER_WECHAT_OPENID, openid);
    	return "redirect:" + URL_TOPAY + "?subOrdinate=" + subOrdinate;
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
        
        if (unpaidPayInfoLogs.size() > 1){
            //支付信息不存在或已支付
            throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_ILLEGAL_SUBORDINATE_MULTI_ORDERS,
            		"交易流水对应多个未支付订单, subOrdinate:" + subOrdinate);
        }

        //校验订单是否存在、取消、已支付
        validateSalesOrder(salesOrder, memberDetails);
        
    	WechatJsApiPayCommand wechatJsApiPayCommand = payManager.getWechatJsApiPayCommand(prepayId);
    	
    	String url = RequestUtil.getRequestFullURL(request, CharsetType.UTF8);
    	String jsapiTicket = getJsapiTicket();
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
        
        model.addAttribute(MODEL_KEY_CODE_URL, codeUrl);
        model.addAttribute(MODEL_KEY_SALES_ORDER, salesOrder);
        
    	return VIEW_CODE_PAY;
    }
    
    protected Integer getWecatPaySourceMode() {
    	return WECAT_PAYSOURCEMODE_DEFAULT;
    }
    
    //获取jsapi ticket
    private String getJsapiTicket() {
    	if(WECAT_PAYSOURCEMODE_NBP.equals(getWecatPaySourceMode())) {
    		return wechatService.getJsaipTicketFromNbp();
    	}
    	
    	return wechatService.getJsapiTicket();
    }
    
}
