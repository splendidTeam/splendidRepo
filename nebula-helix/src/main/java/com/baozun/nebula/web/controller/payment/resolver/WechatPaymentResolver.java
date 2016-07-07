package com.baozun.nebula.web.controller.payment.resolver;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.ui.Model;

import com.baozun.nebula.event.EventPublisher;
import com.baozun.nebula.event.PayWarnningEvent;
import com.baozun.nebula.exception.IllegalPaymentStateException;
import com.baozun.nebula.exception.IllegalPaymentStateException.IllegalPaymentState;
import com.baozun.nebula.manager.system.MataInfoManager;
import com.baozun.nebula.model.payment.PayCode;
import com.baozun.nebula.model.salesorder.PayInfoLog;
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.payment.manager.PayManager;
import com.baozun.nebula.payment.manager.PaymentManager;
import com.baozun.nebula.payment.manager.ReservedPaymentType;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.SdkPaymentManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.sdk.utils.MapConvertUtils;
import com.baozun.nebula.utilities.common.command.WechatPayParamCommand;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.utilities.integration.payment.PaymentServiceStatus;
import com.baozun.nebula.utilities.integration.payment.wechat.WechatResponseKeyConstants;
import com.baozun.nebula.utils.convert.PayTypeConvertUtil;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.constants.Constants;
import com.baozun.nebula.web.constants.SessionKeyConstants;
import com.baozun.nebula.web.controller.payment.service.wechat.WechatService;
import com.feilong.core.Validator;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.tools.jsonlib.JsonUtil;

public class WechatPaymentResolver extends BasePaymentResolver implements PaymentResolver {
	
	/** The Constant LOGGER. */
    private static final Logger LOGGER          = LoggerFactory.getLogger(WechatPaymentResolver.class);

	@Autowired
	private SdkPaymentManager sdkPaymentManager;

	@Autowired
	private PayManager payManager;
	
	@Autowired
	private MataInfoManager mataInfoManager;
	
	@Autowired
	private OrderManager sdkOrderManager;
	
	@Autowired
	private EventPublisher eventPublisher;
	
	@Autowired
	private WechatService  wechatService;
	
	@Override
	public String buildPayUrl(SalesOrderCommand originalSalesOrder, PayInfoLog payInfoLog, 
			MemberDetails memberDetails, Device device, Map<String,String> extra, HttpServletRequest request, 
			HttpServletResponse response, Model model) throws IllegalPaymentStateException {
		
		//0.如果是公众号支付需要准备openid
		if(WechatResponseKeyConstants.TRADE_TYPE_JSAPI.equals(getWechatPayType(request)) 
				&& request.getSession().getAttribute(SessionKeyConstants.MEMBER_WECHAT_OPENID) == null) {
			return "redirect:/payment/wechat/openid.htm?subOrdinate=" + payInfoLog.getSubOrdinate();
		}
	
		//1. 统一下单
		WechatPayParamCommand wechatPayParamCommand = buildWechatPayParamCommand(originalSalesOrder, payInfoLog, memberDetails, device, extra, request);
		PaymentResult paymentResult = wechatService.unifiedOrder(wechatPayParamCommand, payInfoLog.getPayType().toString());
		
		if(PaymentServiceStatus.SUCCESS.equals(paymentResult.getPaymentServiceSatus())) {
			//2.根据不同的交易类型跳转到不同的处理页面
			//2.1公众号支付, 将预支付id放入session
			if(WechatResponseKeyConstants.TRADE_TYPE_JSAPI.equals(getWechatPayType(request))) {
				request.getSession().setAttribute(SessionKeyConstants.WECHATPAY_JSAPI_PREPAY_ID, paymentResult.getPaymentStatusInformation().getPrepayId());
				return "redirect:/payment/wechat/jsapipay.htm?showwxpaytitle=1&subOrdinate=" + payInfoLog.getSubOrdinate();
			} 
			//2.2扫码支付，将二维码链接放入session
			else if(WechatResponseKeyConstants.TRADE_TYPE_NATIVE.equals(getWechatPayType(request))) {
				request.getSession().setAttribute(SessionKeyConstants.WECHATPAY_NATIVE_CODE_URL, paymentResult.getPaymentStatusInformation().getCodeUrl());
				return "redirect:/payment/wechat/codepay.htm?subOrdinate=" + payInfoLog.getSubOrdinate();
			}
			
		} else {
			LOGGER.error("[BUILD_PAY_URL] wechat unifie order error. subOrdinate:{}, message:[{}]", payInfoLog.getSubOrdinate(), paymentResult.getMessage());
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
	public void doPayNotify(String payType, HttpServletRequest request,
			HttpServletResponse response) throws IllegalPaymentStateException, IOException, DocumentException{
		    // 获取异步通知
	        PaymentResult paymentResult = getPaymentResult(request);
	        
	        String subOrdinate = getSubOrdinate(paymentResult);
	        
	        // 判断交易是否已有成功 防止重复调用 。
	        PayCode payCode = sdkPaymentManager.findPayCodeByCodeAndPayTypeAndPayStatus(subOrdinate, Integer.valueOf(payType), true);

	        if (Validator.isNotNullOrEmpty(payCode)){
	        	// 向微信返回SUCCESS
	        	responseNotifyOrder(response);
	        	
	        }else{
	           
	            if (null == paymentResult){
	                //log
	                sdkPaymentManager.savePaymentLog(
	                                new Date(),
	                                com.baozun.nebula.sdk.constants.Constants.PAY_LOG_NOTIFY_AFTER_MESSAGE,
	                                null,
	                                request.getRequestURL().toString());
	            }else{
	                // 获取支付状态
	                String responseStatus = paymentResult.getPaymentServiceSatus().toString();

	                SalesOrderCommand salesOrderCommand = getSalesOrderCommand(subOrdinate);

	                if (canUpdatePayInfos(responseStatus, salesOrderCommand)){
	                    // 获取通知成功，修改支付及订单信息
	                    payManager.updatePayInfos(paymentResult, null, Integer.valueOf(payType), false, request);
	                }else{
	                    if (Validator.isNotNullOrEmpty(salesOrderCommand)){
	                        //log
	                        String result = "FinancialStatus：" + salesOrderCommand.getFinancialStatus() + " LogisticsStatus:"
	                                        + salesOrderCommand.getLogisticsStatus();

	                        PayWarnningEvent payWarnningEvent = new PayWarnningEvent(
	                                        this,
	                                        salesOrderCommand.getCode(),
	                                        null,
	                                        new Date(),
	                                        null,
	                                        responseStatus,
	                                        null,
	                                        result);
	                        eventPublisher.publish(payWarnningEvent);
	                    }

	                    // 返回失败记录日志
	                    payManager.savePaymentResultPaymentLog(paymentResult, null, Constants.DO_NOTIFY_AFTER_TYPE);
	                }

	                // 向微信返回SUCCESS
		        	responseNotifyOrder(response);
	            }
	        }
		
	}

	private SalesOrderCommand getSalesOrderCommand(String subOrdinate) {
		//添加订单查询：支付状态为1 物流状态为1||3
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("subOrdinate", subOrdinate);
		List<PayInfoLog> payInfoLogs = sdkPaymentManager.findPayInfoLogListByQueryMap(paraMap);

		SalesOrderCommand salesOrderCommand = null;
		if (Validator.isNotNullOrEmpty(payInfoLogs)){
		    salesOrderCommand = sdkOrderManager.findOrderById(payInfoLogs.get(0).getOrderId(), 1);
		}
		return salesOrderCommand;
	}

	private PaymentResult getPaymentResult(HttpServletRequest request) {
		
		LOGGER.debug("[DO_PAY_NOTIFY] RequestInfoMapForLog:{}", JsonUtil.format(RequestUtil.getRequestInfoMapForLog(request)));
		
		PaymentResult paymentResult = wechatService.getPaymentResultForAsy(request, PayTypeConvertUtil.getPayType(ReservedPaymentType.WECHAT));
		
		LOGGER.info("[DO_PAY_NOTIFY] async notifications return value: " + MapConvertUtils.transPaymentResultToString(paymentResult));
		
		return paymentResult;
	}

	private String getSubOrdinate(PaymentResult paymentResult) {
		String subOrdinate = null;
		
		if(paymentResult!=null &&  paymentResult.getPaymentStatusInformation()!=null){
			subOrdinate = paymentResult.getPaymentStatusInformation().getOrderNo();
		}
		
	    LOGGER.info("[DO_PAY_NOTIFY] get sync notifications before , subOrdinate: {}", subOrdinate);
		
		return subOrdinate;
	}
	/**
	 * 微信支付异步通知处理成功时发送此response给微信，否则无需发送，微信会以一定的频率继续通知到商城
	 * @param response
	 */
	public void responseNotifyOrder(HttpServletResponse response) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		sb.append("<return_code><![CDATA[SUCCESS]]></return_code>");
		sb.append("<return_msg><![CDATA[OK]]></return_msg>");
		sb.append("</xml>");
		try {
			response.setHeader("Cache-Control", "no-cache");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml"); 
			response.getWriter().write(sb.toString());
		} catch (Exception e) {
			LOGGER.error("responseNotifyOrder error.", e);
		}
	}
	
	//获取统一下单的参数
	private WechatPayParamCommand buildWechatPayParamCommand(SalesOrderCommand originalSalesOrder, PayInfoLog payInfoLog, 
			MemberDetails memberDetails, Device device, Map<String,String> extra, HttpServletRequest request) {
		
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
