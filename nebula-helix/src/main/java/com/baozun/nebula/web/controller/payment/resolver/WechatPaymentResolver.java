package com.baozun.nebula.web.controller.payment.resolver;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.ui.Model;

import com.baozun.nebula.exception.IllegalPaymentStateException;
import com.baozun.nebula.exception.IllegalPaymentStateException.IllegalPaymentState;
import com.baozun.nebula.manager.system.MataInfoManager;
import com.baozun.nebula.model.salesorder.PayInfoLog;
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.payment.manager.PayManager;
import com.baozun.nebula.payment.manager.PaymentManager;
import com.baozun.nebula.payment.manager.ReservedPaymentType;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.SdkPaymentManager;
import com.baozun.nebula.utilities.common.command.WechatPayParamCommand;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.utilities.integration.payment.PaymentServiceStatus;
import com.baozun.nebula.utilities.integration.payment.wechat.WechatResponseKeyConstants;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.constants.SessionKeyConstants;
import com.feilong.servlet.http.RequestUtil;

public class WechatPaymentResolver implements PaymentResolver {
	
	/** The Constant LOGGER. */
    private static final Logger LOGGER          = LoggerFactory.getLogger(WechatPaymentResolver.class);

	@Autowired
	private SdkPaymentManager sdkPaymentManager;
	
	@Autowired
	private PaymentManager paymentManager;

	@Autowired
	private PayManager payManager;
	
	@Autowired
	private MataInfoManager mataInfoManager;
	
	@Override
	public String buildPayUrl(SalesOrderCommand originalSalesOrder, PayInfoLog payInfoLog, 
			MemberDetails memberDetails, Device device, Map<String,Object> extra, HttpServletRequest request, 
			HttpServletResponse response, Model model) throws IllegalPaymentStateException {
		
		String subOrdinate = payInfoLog.getSubOrdinate();
		
		Integer payType = payInfoLog.getPayType();
		
		//更新t_so_paycode
    	sdkPaymentManager.updatePayCodeBySubOrdinate(subOrdinate, payType);
    	
		// 加上showwxpaytitle=1 微信会显示“微信安全支付”
		String url = "/pay/wechatPay.htm?showwxpaytitle=1&soCode="+subOrdinate;
		
		//0.如果是公众号支付需要准备openid
		if(WechatResponseKeyConstants.TRADE_TYPE_JSAPI.equals(getWechatPayType(request)) 
				&& request.getSession().getAttribute(SessionKeyConstants.MEMBER_WECHAT_OPENID) == null) {
			//TODO 应该支持定制
			return "redirect:/payment/wechat/openid.htm";
		}
	
		//1. 统一下单
		WechatPayParamCommand wechatPayParamCommand = buildWechatPayParamCommand(originalSalesOrder, payInfoLog, memberDetails, device, extra, request);
		PaymentResult paymentResult = paymentManager.unifiedOrder(wechatPayParamCommand, String.valueOf(ReservedPaymentType.WECHAT));
		
		if(PaymentServiceStatus.SUCCESS.equals(paymentResult.getPaymentServiceSatus())) {
			//2.根据不同的交易类型跳转到不同的处理页面
			//2.1公众号支付, 构造页面所需参数
			if(WechatResponseKeyConstants.TRADE_TYPE_JSAPI.equals(getWechatPayType(request))) {
				
				//TODO 进入公众号支付页面，应该支持定制
				return "redirect:/payment/wechat/wxpay.htm";
			} 
			//2.2扫码支付，将二维码链接放入session
			else if(WechatResponseKeyConstants.TRADE_TYPE_NATIVE.equals(getWechatPayType(request))) {
				request.getSession().setAttribute(SessionKeyConstants.WECHAT_NATIVE_CODE_URL, paymentResult.getPaymentStatusInformation().getCodeUrl());
				//TODO 进入扫码页面，应该支持定制
				return "redirect:/payment/wechat/codepay.htm";
			}
			
		} else {
			LOGGER.error("[BUILD_PAY_URL] wechat unifie order error. subOrdinate:{}, message:[{}]", subOrdinate, paymentResult.getMessage());
			throw new IllegalPaymentStateException(IllegalPaymentState.PAYMENT_GETURL_ERROR, "获取跳转地址失败");
		}
		
		return null;
	}

	@Override
	public String doPayReturn(String payType, Device device, String paySuccessRedirect, String payFailureRedirect, 
			HttpServletRequest request, HttpServletResponse response) throws IllegalPaymentStateException {
		throw new IllegalAccessError("wechat pay donot support return invoke.");
	}

	@Override
	public void doPayNotify(String payType, Device device, HttpServletRequest request,
			HttpServletResponse response) throws IllegalPaymentStateException, IOException {
		// TODO Auto-generated method stub
		
	}
	
	//openid的获取分三种情况：
	//1.用户在微信中打开网站时，商城已经自动完成了微信登录，此时从session中获取
	//2.通过NBP获取，获取后也放在session中
	//3.直接通过微信获取，获取后也放在session中
	public String getOpenid(HttpServletRequest request, HttpServletResponse response, Model model) {
		return (String)request.getSession().getAttribute(SessionKeyConstants.MEMBER_WECHAT_OPENID);
	}
	
	//获取统一下单的参数
	private WechatPayParamCommand buildWechatPayParamCommand(SalesOrderCommand originalSalesOrder, PayInfoLog payInfoLog, 
			MemberDetails memberDetails, Device device, Map<String,Object> extra, HttpServletRequest request) {
		
		WechatPayParamCommand wechatPayParamCommand = new WechatPayParamCommand();
		
		//支付流水号
		wechatPayParamCommand.setOut_trade_no(payInfoLog.getSubOrdinate());
		
		//支付金额
		Integer totalFee = payInfoLog.getPayMoney().multiply(new BigDecimal(100)).setScale(0, RoundingMode.HALF_UP).intValue();
        wechatPayParamCommand.setTotal_fee(totalFee);
        
        //客户端ip
        wechatPayParamCommand.setSpbill_create_ip(RequestUtil.getClientIp(request));
        
        //订单生成时间
        wechatPayParamCommand.setTime_start(DateFormatUtils.format(originalSalesOrder.getCreateTime(), "yyyyMMddHHmmss"));
        
        //订单过期时间
        String payExpireTime = getPayExpireTime(originalSalesOrder.getCreateTime());
        if(getPayExpireTime(originalSalesOrder.getCreateTime()) != null) {
        	wechatPayParamCommand.setTime_expire(payExpireTime);
        }
        
        //交易类型
        String tradeType = getWechatPayType(request);
        wechatPayParamCommand.setTrade_type(tradeType);
        
        //trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。
        if(WechatResponseKeyConstants.TRADE_TYPE_JSAPI.equals(tradeType)) {
        	String openid = (String)request.getSession().getAttribute(SessionKeyConstants.MEMBER_WECHAT_OPENID);
        	wechatPayParamCommand.setOpenid(openid);
        }
        
        //trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
        //官方商城使用扫码模式二，该参数值无关紧要，随便设置一个数字
        if(WechatResponseKeyConstants.TRADE_TYPE_NATIVE.equals(tradeType)) {
        	wechatPayParamCommand.setProduct_id("1");
        }
        
        //商品描述, 这里放订单编号
        //TODO 应该支持定制
        wechatPayParamCommand.setBody(originalSalesOrder.getCode());
        
        return wechatPayParamCommand;
	}
	
	private String getWechatPayType(HttpServletRequest request) {
		String userAgent = RequestUtil.getHeaderUserAgent(request);
		if(userAgent.contains("MicroMessenger")) {
			return WechatResponseKeyConstants.TRADE_TYPE_JSAPI;
		} else {
			return WechatResponseKeyConstants.TRADE_TYPE_NATIVE;
		}
	}
	
	/**
	 * 获取过期时间. 从系统配置中获取，如果没取到则不设置
	 * @param orderCreateDate
	 * @return
	 * @throws IllegalPaymentStateException
	 */
	private String getPayExpireTime(Date orderCreateDate) {
		String payExpiryMinute = mataInfoManager.findValue(MataInfo.PAYMENT_EXPIRY_TIME);
		
		if (payExpiryMinute != null) {
			Date payExpiryDate = DateUtils.addMinutes(orderCreateDate, Integer.parseInt(payExpiryMinute));
			return DateFormatUtils.format(payExpiryDate, "yyyyMMddHHmmss");
		}
		
		return null;
	}
	
}
