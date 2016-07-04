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
 */

package com.baozun.nebula.web.controller.payment.service.wechat;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baozun.nebula.manager.system.AbstractCacheBuilder;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.utilities.common.Md5Encrypt;
import com.baozun.nebula.utilities.common.WechatUtil;
import com.baozun.nebula.utilities.common.command.WechatPayParamCommand;
import com.baozun.nebula.utilities.integration.payment.PaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.PaymentFactory;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.utilities.integration.payment.PaymentServiceStatus;
import com.baozun.nebula.utilities.integration.payment.exception.PaymentParamErrorException;
import com.baozun.nebula.utilities.integration.payment.wechat.WechatConfig;
import com.baozun.nebula.utilities.integration.payment.wechat.WechatResponseKeyConstants;
import com.baozun.nebula.utilities.io.http.HttpClientUtil;
import com.baozun.nebula.utilities.io.http.HttpMethodType;
import com.baozun.nebula.web.controller.payment.service.common.CommonPayServiceImpl;
import com.baozun.nebula.web.controller.payment.service.wechat.command.AuthAccessTokenCommand;
import com.feilong.core.TimeInterval;
import com.feilong.core.Validator;
import com.feilong.core.net.ParamUtil;
import com.feilong.core.net.URIUtil;

/**
 * @author Jamel.li
 * @date 2016年1月8日 下午8:08:53
 */
@Service
public class WechatServiceImpl extends CommonPayServiceImpl implements WechatService {

	private static final Logger		LOGGER							= LoggerFactory.getLogger(WechatServiceImpl.class);
	
	/** 微信支付 用的AccessToken */
	private static final String	CACHE_KEY_WECHAT_ACCESSTOKEN		= "NEBULA_CACHE_KEY_WECHATPAY_ACCESSTOKEN";

	/** 微信支付 用的JsapiTicket */
	private static final String	CACHE_KEY_WECHAT_JSAPITICKET		= "NEBULA_CACHE_KEY_WECHATPAY_JSAPITICKET";
	
	/** 微信支付 用的AccessToken和JsapiTicket的缓存时间90分钟 */
	private static final Integer CACHE_EXPIRESECONDS_ACCESSTOKEN 	= 90 * TimeInterval.SECONDS_PER_MINUTE; 
	
	/** ## 商户app数据-appId */
	@Value("#{wechat['wechat.appId']}")
	private String					appId;
	
	/** ## 商户app数据-appSecret */
	@Value("#{wechat['wechat.appSecret']}")
	private String					appSecret;
	
	/** ##获取code的url */
	@Value("#{wechat['wechat.oAuth.authorize.url']}")
	private String					oAuthAuthorizeUrl;
	
	/** ##获取授权accessToken的url */
	@Value("#{wechat['wechat.oAuth.accessToken.url']}")
	private String					oAuthAccessTokenUrl;
	
	/** ##获取全局accessToken的url */
	@Value("#{wechat['wechat.global.accessToken.url']}")
	private String					globalAccessTokenUrl;
	
	/** ##获取ticket的url */
	@Value("#{wechat['wechat.ticket.get.url']}")
	private String					ticketUrl;
	
	/** ##获取授权code的回跳url */
	@Value("#{wechat['wechat.oAuth.redirect.uri']}")
	private String					authRedirectUri;
	
	/** ## NBP提供的appid */
	@Value("#{wechat['wechat.nbp.appId']}")
	private String					nbpAppId;
	
	/** ## NBP提供的key */
	@Value("#{wechat['wechat.nbp.key']}")
	private String					nbpKey;
	
	/** ## NBP提供的获取openId的url */
	@Value("#{wechat['wechat.nbp.openid.url']}")
	private String					nbpOpenidUrl;
	
	/** ## 从NBP获取openId后跳转的url */
	@Value("#{wechat['wechat.nbp.openid.redirect.url']}")
	private String					nbpOpenidRedirectUrl;
	
	/** ## 从NBP获取jsapi_ticket的url */
	@Value("#{wechat['wechat.nbp.jsapi_ticket.url']}")
	private String					nbpJsapiTicketUrl;
	
	@Override
	public String genWechatAuthCodeUrl(String queryString, String scope, String state) {
		Map<String,String> connectionParam=new LinkedHashMap<String, String>();
		connectionParam.put("appid", appId);
		connectionParam.put("redirect_uri", URIUtil.encode(authRedirectUri + queryString, "UTF-8"));
		connectionParam.put("response_type", "code");
		connectionParam.put("scope", scope);
		if(Validator.isNotNullOrEmpty(state)) {
			connectionParam.put("state", state);
		}
			
		String getCodeUrl = ParamUtil.addParameterSingleValueMap(oAuthAuthorizeUrl, connectionParam, null) + "#wechat_redirect";
		LOGGER.info("[GEN_WECHAT_AUTHCODE_URL] url:[{}]", getCodeUrl);
		
		return getCodeUrl;
	}

	@Override
	public AuthAccessTokenCommand getAuthAccessToken(String code) {
		String authAccessTokenUrl = genAuthAccessTokenUrl(code);
		String tokenResult = HttpClientUtil.getHttpMethodResponseBodyAsString(authAccessTokenUrl, HttpMethodType.GET);
		
		if(tokenResult.indexOf("errcode") >= 0) {
			LOGGER.error("[GET_AUTH_ACCESSTOKEN] get auth access token error. {}", tokenResult);
			return null;
		}
		
		AuthAccessTokenCommand authAccessTokenCommand = JSON.parseObject(tokenResult, AuthAccessTokenCommand.class);
		
		return authAccessTokenCommand;
	}
	
	@Override
	public String getAccessToken() {
		return new AbstractCacheBuilder<String, RuntimeException>(CACHE_KEY_WECHAT_ACCESSTOKEN, CACHE_EXPIRESECONDS_ACCESSTOKEN) {
			@Override
			protected String buildCachedObject() {
				Map<String,String> connectionParam=new LinkedHashMap<String, String>();
				connectionParam.put("appid", appId);
				connectionParam.put("secret", appSecret);
				connectionParam.put("grant_type", "client_credential");
				
				String accessTokenUrl = ParamUtil.addParameterSingleValueMap(globalAccessTokenUrl, connectionParam, null);
				String tokenResult = HttpClientUtil.getHttpMethodResponseBodyAsString(accessTokenUrl, HttpMethodType.GET);
				
				if(tokenResult.indexOf("errcode") >= 0) {
					LOGGER.error("[GET_ACCESSTOKEN] get access token error. {}", tokenResult);
					return null;
				}
				
				return JSON.parseObject(tokenResult).getString("access_token");
			}
		}.getCachedObject();
	}
	
	@Override
	public String getJsapiTicket() {
		return new AbstractCacheBuilder<String, RuntimeException>(CACHE_KEY_WECHAT_JSAPITICKET, CACHE_EXPIRESECONDS_ACCESSTOKEN) {
			@Override
			protected String buildCachedObject() {
				String jsapiTicketUrl = ticketUrl.concat("?access_token=").concat(getAccessToken()).concat("&type=jsapi");
				String ticketResult = HttpClientUtil.getHttpMethodResponseBodyAsString(jsapiTicketUrl, HttpMethodType.GET);
				
				JSONObject o = JSON.parseObject(ticketResult);
				if(!"0".equals(o.getString("errcode"))) {//0表示成功
					LOGGER.error("[GET_JSAPITICKET] get jsapi ticket error. {}", ticketResult);
					return null;
				}
				
				return o.getString("ticket");
			}
			
		}.getCachedObject();
	}
	
	@Override
	public String genGetOpenidUrlFromNbp(String queryString) {
		Map<String,String> connectionParam=new LinkedHashMap<String, String>();
		connectionParam.put("appid", nbpAppId);
		connectionParam.put("redirect_uri", URIUtil.encode(nbpOpenidRedirectUrl + queryString, "UTF-8"));
		connectionParam.put("sign", Md5Encrypt.md5(ParamUtil.toNaturalOrderingQueryString(connectionParam) + "&key=" + nbpKey).toUpperCase());
		
		String getOpenidUrl = ParamUtil.addParameterSingleValueMap(nbpOpenidUrl, connectionParam, null);
		LOGGER.info("[BUILD_GETOPENIDURL_FROMNBP] url:[{}], queryString:[{}]", getOpenidUrl, queryString);
		
		return getOpenidUrl;
	}
	
	@Override
	public String getJsaipTicketFromNbp() {
		Map<String,String> connectionParam=new LinkedHashMap<String, String>();
		
		connectionParam.put("appid", nbpAppId);
		connectionParam.put("sign", Md5Encrypt.md5(ParamUtil.toNaturalOrderingQueryString(connectionParam) + "&key=" + nbpKey).toUpperCase());
		
		String jsapiTicketUrl = ParamUtil.addParameterSingleValueMap(nbpJsapiTicketUrl, connectionParam, null);
		LOGGER.info("[GET_JSAIP_TICKET_FROMNBP] nbp jsapiTicketUrl url:[{}]", jsapiTicketUrl);
		
		String jsapiTicketResult = HttpClientUtil.getHttpMethodResponseBodyAsString(jsapiTicketUrl, HttpMethodType.GET);
		LOGGER.info("[GET_JSAIP_TICKET_FROMNBP] jsapi ticket result:[{}]", jsapiTicketResult);
		
		return JSON.parseObject(jsapiTicketResult).getString("jsapiTicket");
	}
	
	
	@Override
	public PaymentResult unifiedOrder(WechatPayParamCommand wechatPayParamCommand,String paymentType) {
		PaymentResult paymentResult = new PaymentResult();
		try {
			PaymentFactory paymentFactory = PaymentFactory.getInstance();
			PaymentAdaptor paymentAdaptor = paymentFactory.getPaymentAdaptor(getPayType(Integer.valueOf(paymentType)));//获得支付适配器
			paymentResult = paymentAdaptor.unifiedOrder(getUnifiedOrderParaMap(wechatPayParamCommand));
		} catch (PaymentParamErrorException e) {
			LOGGER.error("[WECHATPAY_UNIFIEDORDER] error desc : "+e.toString());
			paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
			paymentResult.setMessage(e.toString());
		}catch(Exception ex){
			LOGGER.error("[WECHATPAY_UNIFIEDORDER] error desc : "+ex.toString());
			paymentResult.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
			paymentResult.setMessage(ex.toString());
		}
		return paymentResult;
	}
	
	public String getPayType(Integer paymentType) {
		if(SalesOrder.SO_PAYMENT_TYPE_ALIPAY.equals(paymentType)) {
    		return PaymentFactory.PAY_TYPE_ALIPAY;
    	} else if(SalesOrder.SO_PAYMENT_TYPE_NETPAY.equals(paymentType)) {
    		return PaymentFactory.PAY_TYPE_ALIPAY_BANK;
    	} else if(SalesOrder.SO_PAYMENT_TYPE_ALIPAY_CREDIT.equals(paymentType)) {
    		return PaymentFactory.PAY_TYPE_ALIPAY_CREDIT;
    	} else if(SalesOrder.SO_PAYMENT_TYPE_INTERNATIONALCARD.equals(paymentType)) {
    		return PaymentFactory.PAY_TYPE_ALIPAY_CREDIT_INT;
    	} else {
    		throw new IllegalArgumentException("not an valide alipay type. payType:[" + paymentType + "]");
    	}
	}
	
	private String genAuthAccessTokenUrl(String code) {
		Map<String,String> connectionParam=new LinkedHashMap<String, String>();
		connectionParam.put("appid", appId);
		connectionParam.put("secret", appSecret);
		connectionParam.put("code", code);
		connectionParam.put("grant_type", "authorization_code");
		
		String	getAccessTokenUrl = ParamUtil.addParameterSingleValueMap(oAuthAccessTokenUrl, connectionParam, null);
		LOGGER.info("[GEN_AUTH_ACCESSTOKEN_URL] url:[{}]", getAccessTokenUrl);
		return getAccessTokenUrl;
	}
	
	private Map<String, String> getUnifiedOrderParaMap(WechatPayParamCommand wechatPayParamCommand)
			throws PaymentParamErrorException {	
		Map<String, String> addition = new HashMap<String, String>();
		
		addition.put("appid", WechatConfig.APP_ID);
		addition.put("mch_id", WechatConfig.PARTNER_ID);
		addition.put("nonce_str", WechatUtil.generateRandomString());
		addition.put("notify_url", WechatConfig.JS_API_PAYMENT_CALLBACK_URL);
		
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getDevice_info())){
			addition.put("device_info", wechatPayParamCommand.getDevice_info());
 		}
		
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getBody())){
			addition.put("body", wechatPayParamCommand.getBody());
 		}else{
 			throw new PaymentParamErrorException("unifiedOrder parameter error: body can't be null/empty!");
 		}
		
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getDetail())){
			addition.put("detail", wechatPayParamCommand.getDetail());
 		}
		
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getOut_trade_no())){
			addition.put("out_trade_no", wechatPayParamCommand.getOut_trade_no());
 		}else{
 			throw new PaymentParamErrorException("unifiedOrder parameter error: out_trade_no can't be null/empty!");
 		}
		
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getFee_type())){
			addition.put("fee_type", wechatPayParamCommand.getFee_type());
 		}
		
		
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getAttach())){
			addition.put("attach", wechatPayParamCommand.getAttach());
 		}
		
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getTotal_fee())){
			addition.put("total_fee", wechatPayParamCommand.getTotal_fee().toString());
 		}else{
 			throw new PaymentParamErrorException("unifiedOrder parameter error: total_fee can't be null/empty!");
 		}
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getSpbill_create_ip())){
			addition.put("spbill_create_ip", wechatPayParamCommand.getSpbill_create_ip());
 		}else{
 			throw new PaymentParamErrorException("unifiedOrder parameter error: spbill_create_ip can't be null/empty!");
 		}
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getTime_start())){
			addition.put("time_start", wechatPayParamCommand.getTime_start());
 		} 
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getTime_expire())){
			addition.put("time_expire", wechatPayParamCommand.getTime_expire());
 		} 
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getTrade_type())){
			addition.put("trade_type", wechatPayParamCommand.getTrade_type());
 		}else{
 			throw new PaymentParamErrorException("unifiedOrder parameter error: trade_type can't be null/empty!");
 		}
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getGoods_tag())){
			addition.put("goods_tag", wechatPayParamCommand.getGoods_tag());
 		} 
		
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getProduct_id())){
			addition.put("product_id", wechatPayParamCommand.getProduct_id());
 		}else{
 			if(wechatPayParamCommand.getTrade_type().equals(WechatResponseKeyConstants.TRADE_TYPE_NATIVE)){
 				throw new PaymentParamErrorException("unifiedOrder parameter error: when the trade_type value is 'NATIVE' product_id can't be null/empty!");
 			}
 		}
		
		if(Validator.isNotNullOrEmpty(wechatPayParamCommand.getOpenid())){
			addition.put("openid", wechatPayParamCommand.getOpenid());
 		}else{
 			if(wechatPayParamCommand.getTrade_type().equals(WechatResponseKeyConstants.TRADE_TYPE_JSAPI)){
 				throw new PaymentParamErrorException("unifiedOrder parameter error: when the trade_type value is 'JSAPI' openid can't be null/empty!");
 			}
 		}
		
		return addition;
		
	}
	
}
