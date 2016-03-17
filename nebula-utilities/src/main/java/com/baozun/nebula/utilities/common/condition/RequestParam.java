package com.baozun.nebula.utilities.common.condition;


public class RequestParam {
	
	public static final String HTTP_TYPE_GET = "get";
	public static final String HTTP_TYPE_POST = "post";
	
	//****************************************支付宝*********************************//
	/**
	 * 余额支付
	 */
	public static final String PAYMETHOE_D = "directPay";
	
	/**
	 * 信用支付
	 */
	public static final String PAYMETHOE_C = "creditPay";
	
	/**
	 * 银行支付
	 */
	public static final String PAYMETHOE_BANK = "bankPay";
	
	/**
	 * 国际卡支付需要短信验证
	 */
	public static final String PAYMETHOE_3D = "jvm-3d";
	
	/**
	 * 国际卡支付无需短信验证
	 */
	public static final String PAYMETHOE_MOTO = "jvm-moto";
	
	public static final int ALIPAY = 1;//支付宝
	public static final int ALIPAYBANK = 1;//支付宝转银行
	public static final int CREDITCARD = 14;//国内信用卡
	public static final int VISA = 131;//VISA
	public static final int INTCREDITCARD = 141;//国际信用卡
	
	public static final String ALIPAYSUCCESS = "success";
	
	public static final String ALIPAYFAIL = "fail";
	
	//**********************************************网银**************************************//
	
	/**
	 * 支付类型：消费
	 */
	public static final String PAYMETHOE_U_PAY = "01";
	
	/**
	 * 支付类型：消费撤销
	 */
	public static final String PAYMETHOE_U_CANCEL = "31";
	
	/**
	 * 返回服务器信息
	 */
	public static final String UNIONSUCCESS = "200";
	
	public static final String UNIONFAIL = "000";
}
