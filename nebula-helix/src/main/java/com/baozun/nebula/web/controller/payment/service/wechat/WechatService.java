package com.baozun.nebula.web.controller.payment.service.wechat;

import com.baozun.nebula.utilities.common.command.WechatPayParamCommand;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.web.controller.payment.service.common.CommonPayService;
import com.baozun.nebula.web.controller.payment.service.wechat.command.AuthAccessTokenCommand;


public interface WechatService extends CommonPayService {
	
	String genWechatAuthCodeUrl(String queryString, String scope, String state);
	
	AuthAccessTokenCommand getAuthAccessToken(String code);
	
	String getAccessToken();
	
	String getJsapiTicket();
	
	String genGetOpenidUrlFromNbp(String queryString);
	
	String getJsaipTicketFromNbp();
	
	PaymentResult unifiedOrder(WechatPayParamCommand wechatPayParamCommand,String paymentType);
	
}
