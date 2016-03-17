package com.baozun.nebula.utilities.integration.payment.wechat;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.baozun.nebula.utilities.common.ProfileConfigUtil;

public class WechatConfig {

	// @Value("#{configProperties['wechat.appId']}")
	public static final String	APP_ID;

	// @Value("#{configProperties['wechat.appSecret']}")
	public static final String	APP_SECRET;

	// @Value("#{configProperties['wechat.partnerId']}")
	public static final String	PARTNER_ID;

	// @Value("#{configProperties['wechat.paySignKey']}")
	public static final String	PAY_SIGN_KEY;

	// @Value("#{configProperties['wechat.payment.js.notify.url']}")
	public static final String	JS_API_PAYMENT_CALLBACK_URL;

	// @Value("#{configProperties['wechat.api.url.orderquery']}")
	public static final String	ORDER_QUERY_URL;
	
	// @Value("#{configProperties['wechat.pay.unified.order']}")
	public static final String	PAY_UNIFIED_ORDER_URL;
	
	// @Value("#{configProperties['wechat.api.url.closeorder']}")
	public static final String	CLOSE_ORDER_URL;

	static {
		Properties pro = ProfileConfigUtil.findPro("config/wechat.properties");

		APP_ID = pro.getProperty("wechat.appId").trim();

		APP_SECRET = pro.getProperty("wechat.appSecret").trim();

		PARTNER_ID = pro.getProperty("wechat.partnerId").trim();

		PAY_SIGN_KEY = pro.getProperty("wechat.paySignKey").trim();

		JS_API_PAYMENT_CALLBACK_URL = pro.getProperty("wechat.payment.js.notify.url").trim();

		ORDER_QUERY_URL = pro.getProperty("wechat.api.url.orderquery").trim();

		CLOSE_ORDER_URL = StringUtils.trim(pro.getProperty("wechat.api.url.closeorder"));

		PAY_UNIFIED_ORDER_URL = pro.getProperty("wechat.pay.unified.order").trim();
	}
}
