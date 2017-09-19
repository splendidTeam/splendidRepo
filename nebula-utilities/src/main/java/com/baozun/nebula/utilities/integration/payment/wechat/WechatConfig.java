package com.baozun.nebula.utilities.integration.payment.wechat;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.baozun.nebula.utilities.common.ProfileConfigUtil;

public class WechatConfig{

    public static final String APP_ID;

    public static final String APP_SECRET;

    public static final String PARTNER_ID;

    public static final String PAY_SIGN_KEY;

    public static final String JS_API_PAYMENT_CALLBACK_URL;

    public static final String ORDER_QUERY_URL;

    public static final String PAY_UNIFIED_ORDER_URL;

    public static final String CLOSE_ORDER_URL;

    static{
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
