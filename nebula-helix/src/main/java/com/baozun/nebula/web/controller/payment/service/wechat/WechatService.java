package com.baozun.nebula.web.controller.payment.service.wechat;

import com.baozun.nebula.web.controller.payment.service.wechat.command.AuthAccessTokenCommand;


public interface WechatService {
	
	String genWechatAuthCodeUrl(String callbackUrl, String scope, String state);
	
	AuthAccessTokenCommand getAuthAccessToken(String code);
	
	String getAccessToken();
	
	String getJsapiTicket();
	
	String genGetOpenidUrlFromNbp(String callbackUrl);
	
	String getJsaipTicketFromNbp();
}
