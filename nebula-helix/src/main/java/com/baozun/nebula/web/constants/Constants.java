package com.baozun.nebula.web.constants;

import com.baozun.nebula.utilities.common.ConfigurationUtil;

public class Constants {
	public static final String BAOZUN_SEESSION_MEMBER = "baozunMember";
	
	public static final String RESULTCODE = "resultCode";
	
	public static final String RESULTREASON = "resultReason";
	
	public static final String SUCCESS = "1";
	
	public static final String FAILURE = "0";
	
	public static final String CHECK_NOT_VALID = "10";
	
	public static final String CHECK_LIMIT_FAILURE = "11";
	
	public static final String GUESTINDENTIFY = "guestIndentifyCart";
	
	/** 支付feedback 支付服务器主动通知 <code>{@value}</code>. */
	public static final String	paymentFeedback_doNotify		= "/pay/doNotify/{payType}.htm";

	/** 浏览器直接跳转返回通知<code>{@value}</code>. */
	public static final String	paymentFeedback_doReturn		= "/pay/doReturn/{payType}.htm";

	public static final int GET_URL_AFTER_TYPE = 1;
	
	public static final int DO_RETURN_AFTER_TYPE = 2;
	
	public static final int DO_NOTIFY_AFTER_TYPE = 3;
	
	public static final int CLOSE_AFTER_TYPE = 4;

	public static final Integer FINDBACKSTEP_1 = 1;

	public static final Integer FINDBACKSTEP_2 = 2;

	public static final Integer FINDBACKSTEP_3 = 3;
	
	/** ase加密key **/
	public static final String 	ENCODE_I_V = ConfigurationUtil.getInstance().getNebulaUtilityConfiguration("aes.key.iv");
	
	public static final String 	ENCODE_SALT = ConfigurationUtil.getInstance().getNebulaUtilityConfiguration("aes.key.salt");
	
	public static final String 	MASTER_KEY = ConfigurationUtil.getInstance().getNebulaUtilityConfiguration("aes.masterkey");
	
	public static final Integer CHECKED_CHOOSE_STATE = 1;
	
	public static final String  COLON = ":";
	
	public static final String UTF_8 = "UTF-8";
	
	public static final String REFERER = "referer";
	
	public static final String failure = "failure";
	
	public static final String code = "code";
	
	public static final String url = "url";
	
	/** 购物车中商品最大行数
	 * 
	 * @deprecated  不应该常量写死
	 * */
	public static final int SHOPPING_CART_SKU_MAX_COUNT	=	30;
	
	/** 购物车中单商品最大数量
	 * @deprecated   不应该常量写死
	 * */
	public static final int SHOPPING_CART_SKU_ONE_LINE_COUNT	=	8;
	
	/** 新增商品*/
	public static final String SHOPPING_CART_ACT_ADD	=	"add";
	/** 修改数量*/
	public static final String SHOPPING_CART_ACT_UPDATE	=	"update";
	/** 替换商品*/
	public static final String SHOPPING_CART_ACT_REPLACE	=	"replace";
	/** cookie中存放的商品總數key*/
	public static final String 	SHOPPING_CART_SESSION_SKU_COUNT	=	"cart_sku_count";
}
