package com.baozun.nebula.web.constants;

/**
 * SESSION key相关的常量
 * 
 * @author Justin Hu
 *
 */
public class CookieKeyConstants {

	/** cookie失效时间 **/
	public static final Integer COOKIE_EXPIRE_TIME = 30 * 24 * 60 * 60;

	/** 在浏览器关闭时删除该cookie **/
	public static final Integer COOKIE_LOGIN_STATUS_EXPIRE_TIME = -1;

	/** 持久级别的cookie保存的时间 **/
	public static final Integer PERSIST_COOKIE_EXPIRE_TIME = 365 * 24 * 60 * 60;

	/** 会话级别cookie中的用户名 **/
	public static final String MEMBER_COOKIE_LOGIN_NAME = "u";

	/** 最后一次登录名称 **/
	public static final String MEMBER_LAST_LOGIN_NAME = "l_n";

	/** 1为游客登录 **/
	public static final String GUEST_COOKIE_LOGIN_STATUS = "1";

	/** cookie中的登录状态 **/
	public static final String MEMBER_COOKIE_LOGIN_STATUS = "u_l";

	/** 2为会员登录 **/
	public static final String MEMBER_LOGIN_STATUS = "2";

	/** cookie中的手机注册returnUrl **/
	public static final String MOBILE_REG_RETURN_URL = "r_u";

	/** 持久级别cookie中的用户名 **/
	public static final String PERSIST_COOKIE_LOGIN_NAME = "l";

	public static final String GUESTINDENTIFY = "g_c";

	/** 最近浏览的商品 cookie key */
	public static final String HOSTORY_BROWSE_ITEM = "h_b_i";

	/** 游客保存在cookie当中的购物车 **/
	public static final String GUEST_COOKIE_GC = "g_c_s";

	/** 购物车数量 add by feilong 2016年4月27日22:19:43 **/
	public static final String SHOPPING_CART_COUNT = "s_c_c";

	/** 游客保存在cookie当中的购物车商品总数量 .
	 * @deprecated 通常用不到,直接使用  {@link #SHOPPING_CART_COUNT}
	 * **/
	@Deprecated
    public static final String GUEST_COOKIE_GC_CNT = "g_c_s_cnt";

	/** 用户性别 */
	public static final String MEMBER_SEX = "u_s";

	/**
	 * 用于https登录相关的签名
	 */
	public static final String HTTPS_SIGN = "s_ck";
}
